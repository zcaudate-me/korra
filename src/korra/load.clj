(ns korra.load
  (:require [korra.common :refer :all]
            [clojure.java.io :as io]
            [iroh.core :as iroh]))

(def ^:dynamic *class-cache*
  (iroh/.> clojure.lang.DynamicClassLoader (.classCache)))

(def ^:dynamic *rq*
  (iroh/.> clojure.lang.DynamicClassLoader (.rq)))

(def class-0 (iroh/.? ClassLoader "defineClass0" :#))

(defn dynamic-loader []
  (clojure.lang.DynamicClassLoader. *clojure-loader*))

(defn unload-class [name]
  (clojure.lang.Util/clearCache *rq* *class-cache*)
  (.remove *class-cache* name))

(defmulti load-class (fn [x & args] (type x)))

(defmethod load-class Class [cls]
  (.put *class-cache*
        name (java.lang.ref.SoftReference. cls *rq*))
  cls)

(defmethod load-class (Class/forName "[B") [bytes]
  (clojure.lang.Util/clearCache *rq* *class-cache*)
  (let [name (-> bytes
                 (clojure.asm.ClassReader.)
                 (.getClassName)
                 (path->classname))
        cls (class-0
             (cast ClassLoader (dynamic-loader)) name
             bytes (int 0) (int (count bytes)) nil)]
    (load-class cls)))

(defmethod load-class String [path & more]
  (cond (.endsWith path ".class")
        (-> path
            (to-bytes)
            (load-class))

        (or (.endsWith path ".war")
            (.endsWith path ".jar"))
        (let [resource-name (resource-path (first more))
              rt    (java.util.jar.JarFile. path)
              entry (.getEntry rt resource-name)
              stream (.getInputStream rt entry)]
          (-> stream
              (to-bytes)
              (load-class)))))


(comment
  (.? (proxy [clojure.asm.ClassVisitor] []
        (visit [version access name sygnature suprname interfaces]
          (println name ))

        ) :name)
  (def cr (new clojure.asm.ClassReader "java.lang.Runnable"))
  (def cr (clojure.asm.ClassReader. "java.lang.String"))
  (.> cr .getSuperName)
  "java/lang/Object"
  (seq (.> cr .getInterfaces))
  nil

  (.? cr :name #"get")
  ("getAccess" "getAttributes" "getClassName" "getImplicitFrame" "getInterfaces" "getItem" "getItemCount" "getMaxStringLength" "getSuperName")

  (def cp
    (proxy [java.util.AbstractMap clojure.asm.ClassVisitor] []
      (visit [version access name signature suprname interfaces]
        (println name))
      (visitSource [source debug])
      (visitOuterClass [owner name debug])
      (visitMethod [access name desc signature exceptions]
        (println " " name))
      (visitAttribute [attr])
      (visitInnerClass [name outerName innerName access])
      (visitField [access name desc signature value])
      (visitEnd [])
      (visitAnnotation [owner debug desc])))

  (>source proxy)
  (.%> (type cp))
  (.? cp :name)
  [korra.load.proxy$java.lang.Object$ClassVisitor$d0504653 [java.lang.Object #{clojure.lang.IProxy clojure.asm.ClassVisitor}]]
  (.? clojure.asm.ClassVisitor :abstract :public :instance)
  (defn class-visitor
    )
  ;;
  (comment
    (.accept cr cp 0)

    (.getClassName (clojure.asm.ClassReader.
                    (to-bytes "/Users/zhengc/dev/chit/iroh/target/classes/test/A.class")))

    (def a (load-class "/Users/zhengc/dev/chit/iroh/target/classes/test/A.class"))
    (.> org.apache.bcel.classfile.ClassParser)
    (.? )
    ("EXPAND_FRAMES" "SKIP_CODE" "SKIP_DEBUG" "SKIP_FRAMES" "accept" "b" "copyPool" "getAccess" "getClassName" "getInterfaces" "getItem" "getSuperName" "header" "items" "maxStringLength" "new" "readAnnotationValue" "readAnnotationValues" "readAttribute" "readByte" "readClass" "readConst" "readFrameType" "readInt" "readLong" "readParameterAnnotations" "readShort" "readUTF" "readUTF8" "readUnsignedShort" "strings")

    )

  (comment

    (>refresh)
))
