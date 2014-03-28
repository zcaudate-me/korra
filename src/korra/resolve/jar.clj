(ns korra.resolve.jar
  (:require [korra.common :refer :all]
            [clojure.java.io :as io]
            [version-clj.core :refer [version-compare]])
  (:import [clojure.lang Symbol]))

(defmulti resolve-jar (fn [t & [k v]] k))

(defmethod resolve-jar nil
  [x]
  (resolve-jar x :classloader *clojure-loader*))

(defmethod resolve-jar :classloader
  [x _ loader]
  (if-let [res (-> (resource-path x)
                   (io/resource loader))]
    (-> (re-find #"file:(.*)" (.getPath res))
        second
        (clojure.string/split #"!/"))))

(defmethod resolve-jar :jar-path
  [x _ jar-file]
  (let [res-path (resource-path x)
        contents (filter #(= % res-path)
                         (jar-contents jar-file))]
    (if-not (empty? contents)
      [(str jar-file) res-path])))

(defmethod resolve-jar :jar-paths
  [x _ [jar-path & more]]
  (if-not (nil? jar-path)
    (if-let [res (resolve-jar x :jar-path jar-path)]
      res
      (recur x :jar-path more))))

(defmethod resolve-jar :coordinate
  [x _ coordinate]
  (resolve-jar x :jar-path (maven-file coordinate)))

(defmethod resolve-jar :coordinates
  [x _ [coordinate & more]]
  (if-not (nil? coordinate)
    (if-let [res (resolve-jar x :coordinate coordinate)]
      res
      (recur x :coordinates more))))

(defn find-all-jars [repo]
  (->> (file-seq (io/as-file repo))
       (filter (fn [f] (-> f (.getName) (.endsWith ".jar"))))
       (reduce (fn [out jar]
                 (let [parent-dir (.getParentFile jar)]
                   (assoc-in out [(.getParent parent-dir) (.getName parent-dir)]
                             (.getPath jar)))) {})))

(defn find-latest-jars [repo]
  (->> (find-all-jars repo)
       (map (fn [[_ entries]]
              (let [versions (keys entries)]
                (get entries (last (sort version-compare versions))))))))

(defmethod resolve-jar :repository
  [x _ & [repo]]
  (resolve-jar x :jar-paths (find-latest-jars (or repo *local-repo*))))
