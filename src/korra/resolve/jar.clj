(ns korra.resolve.jar
  (:require [korra.protocols :refer [resolve-jar]]
            [korra.resolve.common :refer :all]))

(defmethod resolve-jar nil
  [x]
  (resolve-jar x :classloader *current-cl*))


(defmethod resolve-jar :classloader
  [x _ loader])
