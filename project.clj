(defproject im.chit/korra "0.1.2"
  :description "jar and maven package introspection"
  :url "http://www.github.com/zcaudate/korra"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.cemerick/pomegranate "0.3.0"]
                 [version-clj "0.1.0"]
                 [im.chit/iroh "0.1.11"]]
  :profiles {:dev {:dependencies [[midje "1.6.3"]
                                  [javassist "3.12.1.GA"]
                                  [clojurewerkz/spyglass "1.1.0"]
                                  [com.jcraft/jsch "0.1.51"]]
                    :plugins [[lein-midje "3.1.3"]]}}
  :documentation {:files {"docs/index"
                         {:input "test/midje_doc/korra_guide.clj"
                          :title "korra"
                          :sub-title ""
                          :author "Chris Zheng"
                          :email  "z@caudate.me"}}})
