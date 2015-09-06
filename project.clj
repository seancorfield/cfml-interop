(defproject cfml-interop "0.1.2"
  :description "CFML/Clojure interop library extracted from World Singles code"
  :url "https://github.com/seancorfield/cfml-interop"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]]

  :aot [cfml.struct]

  :plugins [[cider/cider-nrepl "0.9.1"]
            [refactor-nrepl "1.1.0"]
            [lein-expectations "0.0.8"]]

  :profiles {:dev {:dependencies [[expectations "2.0.7"]
                                  [org.clojure/test.check "0.6.1"]]
                   :injections [(require 'expectations)
                                (expectations/disable-run-on-shutdown)]}
             :master {:repositories [["snapshots" "https://oss.sonatype.org/content/repositories/snapshots/"]]
                      :dependencies [[org.clojure/clojure "1.8.0-master-SNAPSHOT"]]}}

  :aliases {"test-all" ["with-profile" "dev,default:dev,master,default" "expectations"]}

  :min-lein-version "2.5.2")
