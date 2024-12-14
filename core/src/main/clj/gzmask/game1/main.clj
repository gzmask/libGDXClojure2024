(ns gzmask.game1.main
  (:gen-class
    :name gzmask.game1.main
    :extends com.badlogic.gdx.ApplicationAdapter)
  (:require
    [nrepl.server :refer [start-server stop-server]])
  (:import
    (com.badlogic.gdx
      Gdx)
    (com.badlogic.gdx.assets.loaders
      ModelLoader)
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
    (com.badlogic.gdx.graphics.g3d.loader
      ObjLoader)
    (com.badlogic.gdx.graphics.g3d.utils
      CameraInputController
      ModelBuilder)
    (com.badlogic.gdx.math
      Vector3)
    (com.badlogic.gdx.utils
      ScreenUtils)
    (com.badlogic.gdx.utils.viewport
      FitViewport)))



(defonce nrepl-server (start-server :port 7888))

(def sprite-batch (delay (SpriteBatch.)))
(def image-texture (delay (Texture. "libgdx.png")))
(def camera
  (delay
    (doto
      (PerspectiveCamera.
        67
        (float (.getWidth Gdx/graphics))
        (float (.getHeight Gdx/graphics)))
      (-> (.position)
          (.set 0 10 5))
      (.lookAt 0 0 0)
      (-> (.-near)
          (set! 0.1))
      (-> (.-far)
          (set! 300))
      (.update))))

(def camera-controller
  (delay (CameraInputController. @camera)))

(def model-batch (delay (ModelBatch.)))
(def ground-model (atom nil))
(def ground-instance (atom nil))
(def box-model (atom nil))
(def box-instance (atom nil))
(def obj-model (atom nil))
(def obj-instance (atom nil))
(def environment (delay (Environment.)))
(def directional-light (delay (DirectionalLight.)))

(defn- create-environment
  []
  @environment
  (.set @environment (ColorAttribute. ColorAttribute/AmbientLight Color/DARK_GRAY))
  (.add @environment
        (.set @directional-light Color/WHITE (Vector3. -1 -0.8 -0.2))))

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

(defn- create-box
  [model-builder]
  (let [material (Material. (into-array Attribute [(ColorAttribute/createDiffuse Color/RED)]))
        usage (bit-or VertexAttributes$Usage/Position VertexAttributes$Usage/Normal VertexAttributes$Usage/TextureCoordinates)]
    (reset! box-model
            (.createBox model-builder
                        1 1 1
                        material
                        usage))
    (.set (.translation (first  (.-nodes @box-model))) 0 0.5 0)
    (reset! box-instance (ModelInstance. @box-model))))

(defn -create
  [this]
  @sprite-batch
  @image-texture
  @model-batch
  @camera
  @camera-controller
  (.setInputProcessor Gdx/input @camera-controller)
  (create-environment)
  (let [builder (ModelBuilder.)]
    (create-floor builder)
    (create-box builder))
  (let [^ModelLoader loader (ObjLoader.)
        model (.loadModel loader (.internal Gdx/files "a_vintage_motorcycle_that_is_chopper_style_with_rusted_metal.vox.obj"))]
    (reset! obj-model model)
    (reset! obj-instance (ModelInstance. model))))

(defn -render
  [this]
  (.update @camera-controller)

  (.glViewport Gdx/gl
               0 0
               (float (.getWidth Gdx/graphics))
               (float (.getHeight Gdx/graphics)))
  (.glClear Gdx/gl
            (bit-or GL20/GL_COLOR_BUFFER_BIT
                    GL20/GL_DEPTH_BUFFER_BIT))

  ;; Render 3D models
  (.begin @model-batch @camera)
  (.render @model-batch @ground-instance @environment)
  (.render @model-batch @box-instance @environment)
  (.render @model-batch @obj-instance @environment)
  (.end @model-batch)

  (.begin @sprite-batch)
  (.draw @sprite-batch @image-texture (float 140) (float 310))
  (.end @sprite-batch))

(defn -dispose
  [this]
  (stop-server nrepl-server)
  (.dispose @sprite-batch)
  (.dispose @image-texture)
  (.dispose @model-batch)
  (.dispose @ground-model))

(comment

  (.setDirection @directional-light 3 -3 0)
  (.set (.position @camera) 2 10 10)
  (.lookAt @camera 0 1 0)
  (.update @camera)
  (.set (.translation (first  (.-nodes @box-model))) 10 2.5 0)
  )
