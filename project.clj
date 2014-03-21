(defproject im.chit/korra "0.1.0"
  :description "package management and introspection"
  :url "http://www.github.com/zcaudate/korra"
  :license {:name "The MIT License"
            :url "http://http://opensource.org/licenses/MIT"}
  :java-source-paths ["src/java"]
  :source-paths ["src/clojure"]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.tcrawley/dynapath "0.2.3"]]
  :profiles {:dev {:dependencies [[midje "1.6.3"]]
                    :plugins [[lein-midje "3.1.3"]]}}
  :documentation {:files {"docs/index"
                         {:input "test/midje_doc/korra_guide.clj"
                          :title "korra"
                          :sub-title ""
                          :author "Chris Zheng"
                          :email  "z@caudate.me"}}})
