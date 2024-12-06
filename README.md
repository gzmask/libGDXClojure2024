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
    $ clj -e "(compile 'gzmask.game1.main)"

## Run game

Run the project directly after clojure code is compiled and built to classes

    $ ./gradlew lwjgl3:run
