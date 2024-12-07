# libgdx clj

libgdx in clojure in 2024.

## build/dev environment
macOS Sonoma 14.6.1

## dependencies
OpenJDK 23, Clojure 1.12.0, LibGDX 1.12.1

## project generation

project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff)

parameters:
  platforms: desktop core
  template: classic
  libGDX version 1.12.1
  Java version: OpenJDK 23

## Clojure compilation

    $ mkdir classes
    $ clj -M:compile

## Run game

compile clojure and run the game directly

    $ ./gradlew lwjgl3:run

## TODO

 - use [gradle-clojure](https://github.com/clojurephant/clojurephant) to convert deps.edn into gradle to unify dependencies
 - add android distribution ablity once gradle is fully working with clojure code
