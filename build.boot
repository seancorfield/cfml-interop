(def project 'cfml-interop)
(def version "0.2.5")

(set-env! :resource-paths #{"src"}
          :source-paths   #{"test"}
          :dependencies   '[[org.clojure/clojure "1.10.0-beta3"]
                            [seancorfield/boot-expectations "1.0.11" :scope "test"]
                            [expectations "2.2.0-beta1" :scope "test"]
                            [org.clojure/test.check "0.10.0-alpha2" :scope "test"]])

(task-options!
 aot {:namespace   '[cfml.struct]}
 pom {:project     project
      :version     version
      :description "CFML/Clojure interop library extracted from World Singles code."
      :url         "https://github.com/seancorfield/cfml-interop"
      :scm         {:url "https://github.com/seancorfield/cfml-interop"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask deploy
  "Build and deploy (to your default repo)."
  []
  (comp (aot) (pom) (jar) (push)))

(defn cider-deps
  "Return current CIDER dependencies."
  []
  '[[cider/cider-nrepl "0.14.0"]
    [refactor-nrepl    "2.2.0"]])

(deftask with-cider
  "Add CIDER execution context."
  []
  (merge-env! :dependencies (cider-deps)))

(require '[seancorfield.boot-expectations :refer :all])
