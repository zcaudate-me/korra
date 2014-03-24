(ns korra.test-resolve
  (:require [korra.resolve :refer :all]
            [korra.common :refer :all]
            [midje.sweet :refer :all]))

(fact "resolve-dependents"
  (resolve-dependents
   'dynapath.util ['midje "1.6.1"])
  => [(str *local-repo* "/dynapath/dynapath/0.2.0/dynapath-0.2.0.jar")
      "dynapath/util.clj"]

  (resolve-dependents
   'dynapath.util (str *local-repo* "/midje/midje/1.6.1/midje-1.6.1.jar"))
  => [(str *local-repo* "/dynapath/dynapath/0.2.0/dynapath-0.2.0.jar")
      "dynapath/util.clj"]

  (resolve-dependents
   'midje.sweet ['midje "1.6.1"])
  => [(str *local-repo* "/midje/midje/1.6.1/midje-1.6.1.jar")
      "midje/sweet.clj"])
