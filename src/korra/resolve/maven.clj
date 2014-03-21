(ns korra.resolve.maven
  (:require [clojure.string :as string]
            [korra.protocols :refer [resolve-coordinates]]
            [korra.resolve.common :refer :all]
            [korra.resolve.jar]))

(defn jar-by-coordinates [coordinate]
  (file-by-coordinates coordinate ".jar"))

(defn pom-by-coordinates [coordinate]
  (file-by-coordinates coordinate ".pom"))

(defmethod resolve-coordinates nil
  [x]
  (resolve-coordinates x :classloader *current-cl*))

(defmethod resolve-coordinates :classloader
  [x _ loader])

(defmethod resolve-coordinates :jar
  [x _ loader])

(defmethod resolve-coordinates :maven-path
  [x _ loader])

(defmethod resolve-coordinates :jar-paths
  [x _ loader])





(comment
  (resolve-coordinates String)
  (resolve-coordinates String :maven-path *local-repo*)
  (resolve-coordinates String :jar-paths ["lib/stuff.jar"])
  (resolve-coordinates String :classloader (Current))

  [:maven-path "oeuoeu"]
  [:library-path "oeuoeu"]
  [:resource-paths []]
  [:classloader "oeuoeu"]


  (resolve-coordinates String library-path)

  (resolve-coordinates String []))
