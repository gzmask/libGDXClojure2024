(ns gzmask.game1.main
  (:require [nrepl.server :refer [start-server stop-server]])
  (:import [com.badlogic.gdx ApplicationAdapter Gdx]
           [com.badlogic.gdx.utils ScreenUtils]
           [com.badlogic.gdx.graphics GL20 Texture]
           [com.badlogic.gdx.graphics.g2d SpriteBatch]))

(gen-class
    :name gzmask.game1.main
    :extends com.badlogic.gdx.ApplicationAdapter)

(defonce nrepl-server (start-server :port 7888))

(def batch (atom nil))
(def image (atom nil))

(defn -create [this]
  (reset! batch (SpriteBatch.))
  (reset! image (Texture. "libgdx.png")))

(defn -render [this]
  (ScreenUtils/clear 0.15 0.15 0.2 1)
  (.begin @batch)
  (.draw @batch @image (float 140) (float 210))
  (.end @batch))

(defn -dispose [this]
  (stop-server nrepl-server)
  (.dispose @batch)
  (.dispose @image))
