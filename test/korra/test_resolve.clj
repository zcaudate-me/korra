(ns korra.test-resolve
  (:require [korra.resolve :refer :all]
            [korra.common :refer :all]
            [midje.sweet :refer :all]))

(fact "coordinate-dependecies"
  (coordinate-dependencies '[[midje "1.6.1"]])
  => (just '[[midje "1.6.1"] [ordered "1.2.0"] [colorize "0.1.1"]
             [org.clojure/core.unify "0.5.2"] [utilize "0.2.3"]
             [net.cgrand/regex "1.1.0"] [gui-diff "0.5.0"]
             [joda-time "2.0"] [clj-time "0.6.0"]
             [org.clojars.trptcolin/sjacket "0.1.3"]
             [commons-codec "1.6"] [slingshot "0.10.3"]
             [net.cgrand/parsley "0.9.1"] [dynapath "0.2.0"]
             [org.clojure/tools.namespace "0.2.4"] [org.clojure/clojure "1.4.0"]
             [org.clojure/math.combinatorics "0.0.7"]
             [org.clojure/tools.macro "0.1.1"] [swiss-arrows "1.0.0"]] :in-any-order))

(fact "resolve-jar"
  (-> (resolve-jar "clojure/core.clj") second)
  => "clojure/core.clj"

  (-> (resolve-jar 'clojure.core) second)
  "clojure/core.clj"

  (-> (resolve-jar com.jcraft.jsch.Channel) second)
  "com/jcraft/jsch/Channel.class"

  (-> (resolve-jar 'com.jcraft.jsch.Channel) second)
  => "com/jcraft/jsch/Channel.class")

(fact "resolve-coordinates"
  (resolve-coordinates "clojure/core.clj")
  => '[org.clojure/clojure "1.6.0"]

  (resolve-coordinates 'com.jcraft.jsch.Channel)
  => '[com.jcraft/jsch "0.1.51"])

(fact "resolve-with-deps"
  (resolve-with-deps 'clojure.core)
  => [(str *local-repo* "/org/clojure/clojure/1.6.0/clojure-1.6.0.jar")
      "clojure/core.clj"]

  (resolve-with-deps 'dynapath.util ['midje "1.6.1"])
  => [(str *local-repo* "/dynapath/dynapath/0.2.0/dynapath-0.2.0.jar")
      "dynapath/util.clj"]

  (resolve-with-deps 'dynapath.util (str *local-repo* "/midje/midje/1.6.1/midje-1.6.1.jar"))
  => [(str *local-repo* "/dynapath/dynapath/0.2.0/dynapath-0.2.0.jar")
      "dynapath/util.clj"]

  (resolve-with-deps 'midje.sweet ['midje "1.6.1"])
  => [(str *local-repo* "/midje/midje/1.6.1/midje-1.6.1.jar")
      "midje/sweet.clj"])
