# libgdx clj

libgdx in clojure in 2024.

## build/dev environment
macOS Sonoma 14.6.1

## dependencies
OpenJDK 23, Clojure 1.12.0, LibGDX 1.12.1

    $./gradlew --refresh-dependencies

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

## Package game

make jar

    $ ./gradlew lwjgl3:dist

run jar

    $java -jar lwjgl3/build/libs/libGDX-1-1.0.0.jar

make app

    $./gradlew lwjgl3:packageMacM1

app will be exported to `lwjgl3/build/construo/dist/libGDX-1-macM1.zip`

### TODO voxel render

- see https://stackoverflow.com/questions/62161618/low-fps-in-voxel-land-generation-with-opengl-libgdx
