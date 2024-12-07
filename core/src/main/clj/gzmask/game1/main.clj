(ns gzmask.game1.main
  (:require [nrepl.server :refer [start-server stop-server]])
  (:import [com.badlogic.gdx ApplicationAdapter Gdx]
           [com.badlogic.gdx.utils ScreenUtils]
           [com.badlogic.gdx.graphics GL20 Texture]
           [com.badlogic.gdx.graphics.g2d SpriteBatch])
  (:gen-class :name gzmask.game1.main
              :extends com.badlogic.gdx.ApplicationAdapter))

(defonce nrepl-server (start-server :port 7888))

(def sprite-batch (atom nil))
(def image-texture (atom nil))

(defn -create [this]
  (reset! sprite-batch (SpriteBatch.))
  (reset! image-texture (Texture. "libgdx.png")))

(defn -render [this]
  (ScreenUtils/clear 0.15 0.15 0.2 1)
  (.begin @sprite-batch)
  (.draw @sprite-batch @image-texture (float 140) (float 210))
  (.end @sprite-batch))

(defn -dispose [this]
  (stop-server nrepl-server)
  (.dispose @sprite-batch)
  (.dispose @image-texture))
