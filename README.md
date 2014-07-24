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

Korra is a library for introspection of maven packages. The library provides mappings between different representations of the same jvm concept. 

- maven coordinate and the jar file 
- a 'resource' and its related jar and jar entry under a given context
    - the resource can be:
        - a symbol representing a clojure namespace
        - a path to a resource
        - a java class
    - the context can be:
        - the jvm classloader classpath
        - a single jar
        - a list of jars
        - a maven coordinate
        - a list of maven coordinates
        - the entire maven local-repo.

## Basics

There is a reversible mapping between the maven jar file and the coordinate. We use `maven-file` and `maven-coordinate` to transition from one to the other:

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

The main work-horse is for korra is `resolve-jar`. It resolves a `resource` and a `context`. The default context is the current jvm classpath:

```clojure
(use 'korra.resolve)
(resolve-jar 'clojure.core)
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

It will resolve classes:

```clojure
(resolve-jar java.lang.Object)
;;=> ["/Library/Java/JavaVirtualMachines/jdk1.7.0_60.jdk/Contents/Home/jre/lib/rt.jar" "java/lang/Object.class"]
```

It will also resolve strings:

```clojure
(resolve-jar "clojure/core.clj")
;;=> ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

Symbols with the last section capitalized will default to java classes instead of clojure files:

```clojure
(resolve-jar 'clojure.lang.IProxy)
;;=> ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/lang/IProxy.class"]
```

It will return nil if the resource cannot be found:

```clojure
(resolve-jar 'does.not.exist)
;; => nil
```

### Contexts

Apart from searching via the current jvm classpath, other search contexts can be set, the most simple being a string representation of the jar path:

```clojure
(resolve-jar 'clojure.core "/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar")
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

If the entry cannot be found, nil will be returned:

```clojure
(resolve-jar 'clojure.core "/Users/zhengc/.m2/repository/dynapath/dynapath/0.2.0/dynapath-0.2.0.jar")
;; => nil
```

In addition to the jar, one can use as the contexte a vector of jar-files:

```clojure
(resolve-jar 'clojure.core ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar"])
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

or a coordinate:

```clojure
(resolve-jar 'clojure.core '[org.clojure/clojure "1.6.0"])
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

or a vector of coordinates:

```clojure
(resolve-jar 'clojure.core '[[org.clojure/clojure "1.6.0"]])
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

or if you simply just want to explore, the context can be an entire maven local repository:

```clojure
(resolve-jar 'clojure.core :repository)
;; => ["/Users/zhengc/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar" "clojure/core.clj"]
```

### Coordinates and Dependencies

Once a mapping between the `resource` (path, class or namespace) and the actual jar and jar-entry on the file system, other very helpful functions can be built around `resolve-jar`: 

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


## Applications

korra was extracted out of [lein-repack](https://github.com/zcaudate/lein-repack), a leiningen plugin for analysing and repacking a larger project into a number of smaller ones.

## License

Copyright © 2014 Chris Zheng
