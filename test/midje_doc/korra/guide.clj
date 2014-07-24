(ns midje-doc.korra.guide
  (:use midje.sweet)
  (:require [korra.common :as k]
            [korra.resolve :as res]))

"Korra is a library for introspection of maven packages. The trick is to provide mappings between different representations of the same thing"


"There is a reversible mapping between the maven jar file and the coordinate. We use `maven-file` and `maven-coordinate` to transition from one to the other:"

```clojure
(k/maven-file '[org.clojure/clojure "1.6.0"])
;; => "/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar"

(k/maven-coordinate "/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar")
;; => [org.clojure/clojure "1.6.0"]
```

"As well as a reversible mapping between a class, the name of the class and it location in the jar"

```clojure
(k/jar-entry "/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar"
             'clojure.core)
;; => #<JarFileEntry clojure/core.clj>
```

"The main work-horse is for korra is `resolve-jar`. This function resolves a namespace based upon the context"


```clojure
(res/resolve-jar 'clojure.core)
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

"It will also resolve classes"

```clojure
(res/resolve-jar java.lang.Object)
;;=> ["/Library/Java/JavaVirtualMachines/jdk1.7.0_60.jdk/Contents/Home/jre/lib/rt.jar" "java/lang/Object.class"]
```

"It will resolve strings"

```clojure
(res/resolve-jar "clojure/core.clj")
;;=> ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

"Symbols with the last section Capitalized will default to a class"

```clojure
(res/resolve-jar 'clojure.lang.IProxy)
;;=> ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/lang/IProxy.class"]
```

"It will return nil if the library cannot be found"

```clojure
(res/resolve-jar 'does.not.exist)
;; => nil
```

##Search Contexts

"Apart from searching  default option is the current jvm load-path"

```clojure
"It takes a path to a jar-file"
(res/resolve-jar 'clojure.core "/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar")
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

"Or a vector of jar-files"

```clojure
(res/resolve-jar 'clojure.core ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar"])
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

"Or a coordinate"

```clojure
(res/resolve-jar 'clojure.core '[org.clojure/clojure "1.6.0"])
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

"Or a vector of coordinates"

```clojure
(res/resolve-jar 'clojure.core '[[org.clojure/clojure "1.6.0"]])
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

"Or the entire maven local repository"

```clojure
(res/resolve-jar 'clojure.core :repository)
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```
