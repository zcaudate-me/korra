(ns korra.resolve.test-jar
  (:require [korra.common :refer :all]
            [korra.resolve.jar :refer :all]
            [midje.sweet :refer :all]))

(fact "resolve-jar"
  (resolve-jar String)
  => [(str *java-home* "/lib/rt.jar") "java/lang/String.class"]

  (resolve-jar String
               :jar-paths
               [(str *java-home* "/lib/rt.jar")])
  => [(str *java-home* "/lib/rt.jar") "java/lang/String.class"]

  (resolve-jar 'clojure.core
               :jar-paths
               [(str *java-home* "/lib/rt.jar")])
  => nil

  (resolve-jar 'clojure.core
               :jar-paths
               [(str *local-repo* "/org/clojure/clojure/1.6.0/clojure-1.6.0.jar")])
  => [(str *local-repo* "/org/clojure/clojure/1.6.0/clojure-1.6.0.jar")
      "clojure/core.clj"]

  (resolve-jar 'clojure.core
               :coordinate
               '[org.clojure/clojure "1.6.0"])
  => [(str *local-repo* "/org/clojure/clojure/1.6.0/clojure-1.6.0.jar") "clojure/core.clj"])


(comment (maven-file )
         (resolve-jar  :repository)
         (resolve-jar ' :repository))
