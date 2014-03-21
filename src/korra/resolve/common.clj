(ns korra.resolve.common
  (:require [clojure.string :as string]))

(def ^:dynamic *sep* (System/getProperty "file.separator"))

(def ^:dynamic *current-cl* (.getContextClassLoader (Thread/currentThread)))

(def ^:dynamic *lein-jar*
  (->> (string/split (System/getProperty "java.class.path") #":")
     (filter (comp not empty?))
     first))

(def ^:dynamic *local-repo*
  (string/join *sep* [(System/getProperty "user.home") ".m2" "repository"]))

(defn file-by-coordinates [[name version] suffix]
  (let [[group artifact] (string/split (str name) #"/")
        artifact (or artifact
                     group)]
    (string/join *sep*
                 [*local-repo* (.replaceAll group "\\." *sep*)
                  artifact version (str artifact "-" version suffix)] )))