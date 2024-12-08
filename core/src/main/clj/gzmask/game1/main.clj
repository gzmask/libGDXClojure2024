(ns gzmask.game1.main
  (:gen-class
    :name gzmask.game1.main
    :extends com.badlogic.gdx.ApplicationAdapter)
  (:require
    [nrepl.server :refer [start-server stop-server]])
  (:import
    (com.badlogic.gdx
      Gdx)
    (com.badlogic.gdx.graphics
      Color
      GL20
      PerspectiveCamera
      Texture
      VertexAttributes$Usage)
    (com.badlogic.gdx.graphics.g2d
      SpriteBatch)
    (com.badlogic.gdx.graphics.g3d
      Attribute
      Environment
      Material
      Model
      ModelBatch
      ModelInstance)
    (com.badlogic.gdx.graphics.g3d.attributes
      ColorAttribute)
    (com.badlogic.gdx.graphics.g3d.environment
      DirectionalLight)
    (com.badlogic.gdx.graphics.g3d.utils
      ModelBuilder)
    (com.badlogic.gdx.math
      Vector3)
    (com.badlogic.gdx.utils
      ScreenUtils)))

(defonce nrepl-server (start-server :port 7888))

(def sprite-batch (delay (SpriteBatch.)))
(def image-texture (delay (Texture. "libgdx.png")))
(def camera
  (delay (PerspectiveCamera.
           67
           (float (.getWidth Gdx/graphics))
           (float (.getHeight Gdx/graphics)))))

(def model-batch (delay (ModelBatch.)))
(def ground-model (atom nil))
(def ground-instance (atom nil))
(def environment (delay (Environment.)))

(defn- create-camera
  []
  @camera
  (.set (.position @camera) 0 10 10)
  (.lookAt @camera 0 0 0)
  (set! (.-near @camera) 1)
  (set! (.-far @camera) 300)
  (.update @camera))

(defn- create-environment
  []
  @environment
  (.set @environment (ColorAttribute. ColorAttribute/AmbientLight Color/DARK_GRAY))
  (.add @environment
        (.set (DirectionalLight.) Color/WHITE (Vector3. 1 -3 1)))
  (.add @environment
        (.set (DirectionalLight.) Color/WHITE (Vector3. 1 1 1))))

(defn- create-floor
  [model-builder]
  (let [material (Material. (into-array Attribute [(ColorAttribute/createDiffuse Color/BROWN)]))
        usage (bit-or VertexAttributes$Usage/Position VertexAttributes$Usage/Normal VertexAttributes$Usage/TextureCoordinates)]
    (reset! ground-model
            (.createBox model-builder
                        10 0.5 10
                        material
                        usage))
    (reset! ground-instance (ModelInstance. @ground-model))))

(defn -create
  [this]
  @sprite-batch
  @image-texture
  @model-batch
  (create-camera)
  (create-environment)

  (let [builder (ModelBuilder.)]
    (create-floor builder)))

(defn -render
  [this]
  (ScreenUtils/clear 0.15 0.15 0.2 1)

  (.set (.position @camera) 1 10 10)
  (.update @camera)
  ;; Render 3D models
  (.begin @model-batch @camera)
  (.render @model-batch @ground-instance @environment)
  (.end @model-batch)

  (.begin @sprite-batch)
  (.draw @sprite-batch @image-texture (float 140) (float 210))
  (.end @sprite-batch))

(defn -dispose
  [this]
  (stop-server nrepl-server)
  (.dispose @sprite-batch)
  (.dispose @image-texture)
  (.dispose @model-batch)
  (.dispose @ground-model))
