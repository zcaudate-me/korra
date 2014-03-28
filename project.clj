(defproject im.chit/korra "0.1.0"
  :description "jar and maven package introspection"
  :url "http://www.github.com/zcaudate/korra"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.cemerick/pomegranate "0.3.0"]
                 [version-clj "0.1.0"]
                 [im.chit/iroh "0.1.6"]
                 ;;[org.apache.bcel/bcel "5.2"]
                 ]
  :profiles {:dev {:dependencies [[midje "1.6.3"]]
                    :plugins [[lein-midje "3.1.3"]]}}
  :documentation {:files {"docs/index"
                         {:input "test/midje_doc/korra_guide.clj"
                          :title "korra"
                          :sub-title ""
                          :author "Chris Zheng"
                          :email  "z@caudate.me"}}})
