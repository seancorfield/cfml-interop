(defproject cfml-interop "0.2.3"
  :description "CFML/Clojure interop library extracted from World Singles code"
  :url "https://github.com/seancorfield/cfml-interop"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]]

  :aot [cfml.struct]

  :plugins [[lein-expectations "0.0.8"]]

  :profiles {:dev {:dependencies [[expectations "2.2.0-beta1"]
                                  [org.clojure/test.check "0.10.0-alpha2"]]
                   :injections [(require 'expectations)
                                (expectations/disable-run-on-shutdown)]}
             :master {:repositories [["snapshots" "https://oss.sonatype.org/content/repositories/snapshots/"]]
                      :dependencies [[org.clojure/clojure "1.9.0-master-SNAPSHOT"]]}}

  :aliases {"test-all" ["with-profile" "dev,default:dev,master,default" "expectations"]}

  :min-lein-version "2.5.2")
