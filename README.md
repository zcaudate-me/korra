# korra

[![Build Status](https://travis-ci.org/zcaudate/korra.png?branch=master)](https://travis-ci.org/zcaudate/korra)

![Aang and Korra](http://media.tumblr.com/tumblr_maorg9PDAt1ro0fob.bmp)

>  When we hit our lowest point, we are open to the greatest change.
>
>  Avatar Aang, Legend of Korra

## Installation

Add to project.clj dependencies:

```clojure
[im.chit/korra "0.1.2"]
```

## Overview

Korra is a library for introspection of maven packages. The trick is to provide mappings between different representations of the same thing. There is a reversible mapping between the maven jar file and the coordinate. We use `maven-file` and `maven-coordinate` to transition from one to the other:

```clojure
(use 'korra.common)

(maven-file '[org.clojure/clojure "1.6.0"])
;; => "/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar"

(maven-coordinate "/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar")
;; => [org.clojure/clojure "1.6.0"]
```

There is also a mapping between a clojure namespace, a java class and the their location in a jar.

```clojure
(jar-entry "/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar"
             'clojure.core)
;; => #<JarFileEntry clojure/core.clj>
```

### Resolve

The main work-horse is for korra is `resolve-jar`. The most basic functionality is to resolves a namespace symbol with the current jvm classpath:

```clojure
(use 'korra.resolve)
(resolve-jar 'clojure.core)
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

"It will also resolve classes"

```clojure
(resolve-jar java.lang.Object)
;;=> ["/Library/Java/JavaVirtualMachines/jdk1.7.0_60.jdk/Contents/Home/jre/lib/rt.jar" "java/lang/Object.class"]
```

It will resolve paths as a string

```clojure
(resolve-jar "clojure/core.clj")
;;=> ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

Symbols with the last section capitalized will default to a search of class

```clojure
(resolve-jar 'clojure.lang.IProxy)
;;=> ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/lang/IProxy.class"]
```

It will return nil if the library cannot be found

```clojure
(resolve-jar 'does.not.exist)
;; => nil
```

### Contexts

Apart from searching  default option is the current jvm load-path

```clojure
"It takes a path to a jar-file"
(resolve-jar 'clojure.core "/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar")
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

Or a vector of jar-files

```clojure
(resolve-jar 'clojure.core ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar"])
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

Or a coordinate

```clojure
(resolve-jar 'clojure.core '[org.clojure/clojure "1.6.0"])
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

Or a vector of coordinates

```clojure
(resolve-jar 'clojure.core '[[org.clojure/clojure "1.6.0"]])
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

Or the entire maven local repository

```clojure
(resolve-jar 'clojure.core :repository)
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

### Coordinates and Dependencies

Once a mapping between the object (resource path, class or namespace) and the actual jar and jar-entry on the file system, other very helpful functions can be built to make use of this: 

`resolve-coordinates` works similarly to `resolve-jar` but will return the actual maven-style coordinates

```clojure 
(resolve-coordinates 'iroh.core)
;; => '[im.chit/iroh "0.1.11"]

(resolve-coordinates 'clojure.core :repository)
;; => '[org.clojure/clojure "1.6.0"]
```

`resolve-with-deps` will recursively search all child dependencies until it finds

```clojure
(resolve-with-deps
       'clojure.core
       '[im.chit/korra "0.1.2"])
;;=> ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

## License

Copyright © 2014 Chris Zheng
