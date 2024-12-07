(ns gzmask.game1.main
  (:require [nrepl.server :refer [start-server stop-server]])
  (:import [com.badlogic.gdx ApplicationAdapter Gdx]
           [com.badlogic.gdx.utils ScreenUtils]
           [com.badlogic.gdx.graphics GL20 Texture PerspectiveCamera VertexAttributes$Usage]
           [com.badlogic.gdx.graphics.g3d ModelBatch Model ModelInstance]
           [com.badlogic.gdx.graphics.g3d.utils ModelBuilder]
           [com.badlogic.gdx.graphics.g3d.attributes ColorAttribute]
           [com.badlogic.gdx.graphics.g3d Attribute Material]
           [com.badlogic.gdx.graphics.g3d Environment]
           [com.badlogic.gdx.math Vector3]
           [com.badlogic.gdx.graphics.g2d SpriteBatch]
           [com.badlogic.gdx.graphics Color])
  (:gen-class :name gzmask.game1.main
              :extends com.badlogic.gdx.ApplicationAdapter))

(defonce nrepl-server (start-server :port 7888))

(def sprite-batch (atom nil))
(def image-texture (atom nil))
(def camera (atom nil))
(def model-batch (atom nil))
(def ground-model (atom nil))
(def ground-instance (atom nil))

(defn -create [this]
  (reset! sprite-batch (SpriteBatch.))
  (reset! image-texture (Texture. "libgdx.png"))
  (reset! model-batch (ModelBatch.))
  (reset! camera (PerspectiveCamera.
                  67
                  (float (.getWidth Gdx/graphics))
                  (float (.getHeight Gdx/graphics))))
  (.set (.position @camera) 0 10 10)
  (.lookAt @camera 0 0 0)
  (set! (.-near @camera) 1)
  (set! (.-far @camera) 300)
  (.update @camera)

  (let [builder (ModelBuilder.)
        material (Material. (into-array Attribute [(ColorAttribute/createDiffuse Color/BROWN)]))
        usage (bit-or VertexAttributes$Usage/Position VertexAttributes$Usage/Normal VertexAttributes$Usage/TextureCoordinates)]
    (reset! ground-model
            (.createBox builder
               10 0.5 10
               material
               usage))
    (reset! ground-instance (ModelInstance. @ground-model))))


(defn -render [this]
  (ScreenUtils/clear 0.15 0.15 0.2 1)

  ;; Render 3D models
  (.begin @model-batch @camera)
  (.render @model-batch @ground-instance)
  (.end @model-batch)

  (.begin @sprite-batch)
  (.draw @sprite-batch @image-texture (float 140) (float 210))
  (.end @sprite-batch))

(defn -dispose [this]
  (stop-server nrepl-server)
  (.dispose @sprite-batch)
  (.dispose @image-texture)
  (.dispose @model-batch)
  (.dispose @ground-model)
  )
