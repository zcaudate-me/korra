(ns korra.common
  (:require [clojure.string :as string]
            [clojure.java.io :as io])
  (:import [clojure.lang Symbol]))

(def ^:dynamic *sep* (System/getProperty "file.separator"))

(def ^:dynamic *clojure-loader* (.getClassLoader clojure.lang.RT))

(def ^:dynamic *java-class-path*
  (->> (string/split (System/getProperty "java.class.path") #":")
       (filter (fn [x] (.endsWith x ".jar")))))

(def ^:dynamic *java-home* (System/getProperty "java.home"))

(def ^:dynamic *java-runtime-jar* (str *java-home* "/lib/rt.jar"))

(def ^:dynamic *local-repo*
  (string/join *sep* [(System/getProperty "user.home") ".m2" "repository"]))

(defn maven-file [[name version] & [suffix local-repo]]
  (let [[group artifact] (string/split (str name) #"/")
        artifact (or artifact
                     group)]
    (string/join *sep*
                 [(or local-repo *local-repo*) (.replaceAll group "\\." *sep*)
                  artifact version (str artifact "-" version (or suffix ".jar"))] )))

(defn maven-coordinate [path & [suffix local-repo]]
  (if (and (.startsWith path (or local-repo *local-repo*))
           (.endsWith   path (or suffix ".jar")))
    (let [[_ version artifact & group]
          (-> (subs path (count (or local-repo *local-repo*)))
              (clojure.string/split (re-pattern *sep*))
              (->> (filter (comp not empty?)))
              (reverse))]
      (-> (clojure.string/join  "." (reverse group))
          (str *sep* artifact)
          symbol
          (vector version)))))

(defn resource-path [x]
  (condp = (type x)
    String x
    Symbol (-> (str x)
               (munge)
               (.replaceAll "\\." *sep*)
               (str ".clj"))

    Class (-> (.getName x)
              (.replaceAll "\\." *sep*)
              (str  ".class"))))

(defn jar-entry [jar-path entry]
  (let [resource-name (resource-path entry)
        jar    (java.util.jar.JarFile. jar-path)]
    (.getEntry jar resource-name)))

(defn jar-contents [jar-path]
  (with-open [zip (java.util.zip.ZipInputStream.
                   (io/input-stream jar-path))]
    (loop [entries []]
      (if-let [e (.getNextEntry zip)]
        (recur (conj entries (.getName e)))
        entries))))

(defn path->classname [path]
  (let [path (if (.endsWith path".class")
               (subs path 0 (- (count path) 6))
               path)]
    (.replaceAll path *sep* ".")))

(defmulti to-bytes (fn [x] (type x)))

(defmethod to-bytes java.io.InputStream [stream]
  (let [o (java.io.ByteArrayOutputStream.)]
         (io/copy stream o)
         (.toByteArray o)))

(defmethod to-bytes String [path]
  (to-bytes (io/input-stream path)))

(comment
  (->> (jar-contents *java-runtime-jar*)
     (map path->classname)
     (filter identity)
     (map (fn [x] (try (Class/forName x)
                      (catch Throwable t)))))


  (.loadClass (clojure.lang.DynamicClassLoader.) "java.lang.String")

  (import '[org.reflections ReflectionUtils Reflections]
          [org.reflections.util ClasspathHelper ]
          [org.reflections.scanners SubTypesScanner Scanner])

  (.? ClasspathHelper #"for")

  (.? Reflections "new")

  (.$ storeMap (-> (Reflections.
           *java-runtime-jar*
           (let [a (make-array Scanner 1)]
             (aset a 0 (SubTypesScanner. false))
             a)
           )
          (.getStore))
      )
  (.getSubTypesOf "java.lang.Object")
  )
