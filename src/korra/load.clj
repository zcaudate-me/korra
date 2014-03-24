(ns korra.load
  (:require [korra.common :refer :all]
            [clojure.java.io :as io]
            [iroh.core :as iroh]))

(def ^:dynamic *class-cache*
  (iroh/.> clojure.lang.DynamicClassLoader .classCache))

(def ^:dynamic *rq*
  (iroh/.> clojure.lang.DynamicClassLoader .rq))

(def class-0 (iroh/.? ClassLoader "defineClass0" :#))

(defn dynamic-loader []
  (clojure.lang.DynamicClassLoader. *clojure-loader*))

(defn to-java-class [bytes]
  (-> bytes
      (java.io.ByteArrayInputStream.)
      (org.apache.bcel.classfile.ClassParser. "")
      (.parse)))

(defn unload-class [name]
  (clojure.lang.Util/clearCache *rq* *class-cache*)
  (.remove *class-cache* name))

(defmulti load-class (fn [x] (type x)))

(defmethod load-class Class [cls]
  (.put *class-cache*
        name (java.lang.ref.SoftReference. cls *rq*))
  cls)

(defmethod load-class (Class/forName "[B") [bytes]
  (clojure.lang.Util/clearCache *rq* *class-cache*)
  (let [name (-> (to-java-class bytes)
                 (.getClassName))
        cls (class-0
             (cast ClassLoader (dynamic-loader)) name
             bytes (int 0) (int (count bytes)) nil)]
    (load-class cls)))

(defn to-bytes [path]
  (let [o (java.io.ByteArrayOutputStream.)]
         (io/copy (io/input-stream path) o)
         (.toByteArray o)))

(defmethod load-class String [path]
  (-> path
      (to-bytes)
      (load-class)))

(comment
  (def a (load-class "/Users/zhengc/dev/chit/iroh/target/classes/test/A.class"))
  (.> org.apache.bcel.classfile.ClassParser))

(comment

  (>refresh)
  (def stuff (let [barr ()]
               (.write)
               barr
               (.toByteArray barr)))
  (def out (java.io.ByteArrayOutputStream.))
  (def in (java.io.FileInputStream.
           (clojure.java.io/as-file "")))





  (def l (im.chit.korra.Loader.))


  (use 'no.disassemble)

  (.> l)
  (.getParent (.getParent l))
  (.resolveClass l "test.A" )



  (binding [*use-context-classloader* true]
    (let [cl (.getContextClassLoader (Thread/currentThread))]
      (try (.setContextClassLoader (Thread/currentThread) l)
           (Class/forName "test.A")
           (finally
             (.setContextClassLoader (Thread/currentThread) cl)))))

  (with-bindings {clojure.lang.Compiler/LOADER l}
    (eval ['test.A '(Class/forName "test.A")
           '(import [test A])]))

  (def class-cache (.* clojure.lang.DynamicClassLoader "classCache" :#))

  (def rq (.* clojure.lang.DynamicClassLoader "rq" :#))

  (take 10 (keys (class-cache clojure.lang.DynamicClassLoader)))

  ("korra.load$eval15479" "korra.load$eval4641$fn__4644" "korra.load$eval7627$fn__7628" "korra.load$eval6333$iter__6336__6342$fn__6343$iter__6338__6344$fn__6345$fn__6346" "korra.load$eval4406" "io.aviso.columns$fixed_column$fn__3409" "korra.load$eval15510$iter__15513__15519$fn__15520$iter__15515__15521$fn__15522" "korra.load$eval16840" "korra.load$eval11938$iter__11941__11947$fn__11948$iter__11943__11949$fn__11950$fn__11951" "")

  (.put (class-cache clojure.lang.DynamicClassLoader)
        "test.A" (java.lang.ref.SoftReference. (.loadClass l "test.A")
                                               (rq clojure.lang.DynamicClassLoader)))

  (.length (io/as-file "project.clj"))


  (io/copy (io/reader )
           out)

  (count (.toByteArray out))

  (io/copy in out)

  (seq (.toByteArray out))
  (@#'io/byte-array-type)
)
