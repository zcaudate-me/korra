(ns korra.test-common
  (:require [korra.common :refer :all]
            [midje.sweet :refer :all]))

(fact "maven-file"
  (maven-file '[org.clojure/clojure "1.5.1"])
  => (str *local-repo* "/org/clojure/clojure/1.5.1/clojure-1.5.1.jar")

  (maven-file '[org.clojure/clojure "1.5.1"] ".pom")
  => (str *local-repo* "/org/clojure/clojure/1.5.1/clojure-1.5.1.pom")

  (maven-file '[org.clojure/clojure "1.5.1"] ".pom" "/usr/local/maven/repository")
  => "/usr/local/maven/repository/org/clojure/clojure/1.5.1/clojure-1.5.1.pom")

(fact "maven-coordinate"
  (maven-coordinate
   (maven-file '[org.clojure/clojure "1.5.1"]))
  => '[org.clojure/clojure "1.5.1"])



(comment
  (>refresh))
