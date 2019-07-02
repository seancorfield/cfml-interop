;; Copyright (c) 2015-2019 World Singles Networks llc
;;
;; Released under the Eclipse Public License 1.0
;; http://www.eclipse.org/legal/epl-v10.html

(ns cfml.interop-test
  "Tests for case insensitive CFML/Clojure interoperability."
  (:require [cfml.interop :refer :all]
            [clojure.string :as str]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [expectations.clojure.test :refer :all]))

;; ensure conversion from Clojure (keywords), Clojure (strings), Java

(defexpect simple-struct-tests

  (expect {"A" 1 "BC" 2 "DEF" 3 "G_H" 4}
          (to-clj-struct {:a 1 :bC 2 :DeF 3 :g-h 4}))

  (expect {"A" 1 "BC" 2 "DEF" 3 "G_H" 4}
          (to-clj-struct {"a" 1 "bC" 2 "DeF" 3 "G_h" 4}))

  (expect {"A" 1 "BC" 2 "DEF" 3 "G_H" 4}
          (to-clj-struct (doto (java.util.HashMap.)
                           (.put "a" 1)
                           (.put "bC" 2)
                           (.put "DeF" 3)
                           (.put "g-h" 4)))))

(defexpect simple-struct-access-tests

  (expect 1 (get (to-clj-struct {:a 1 :bC 2 :DeF 3}) :a))

  (expect 2 (get (to-clj-struct {:a 1 :bC 2 :DeF 3}) "bc"))

  (expect 3 (get (to-clj-struct {:a 1 :bC 2 :DeF 3}) "DEF"))

  (expect 4 (get (to-clj-struct {:a 1 :bC 2 :DeF 3 :g-h 4}) :G-h))
  (expect 4 (get (to-clj-struct {:a 1 :bC 2 :DeF 3 :g-h 4}) "g-H"))
  (expect 4 (get (to-clj-struct {:a 1 :bC 2 :DeF 3 :g-h 4}) "G_H")))

(defexpect invocation-tests

  (expect 1 ((to-clj-struct {:a 1 :bC 2 :DeF 3}) :a))

  (expect 2 ((to-clj-struct {:a 1 :bC 2 :DeF 3}) "Bc"))

  (expect 3 ((to-clj-struct {:a 1 :bC 2 :DeF 3}) "dEf")))

(defexpect keys-and-values-tests

  (expect #{1 2 3} (set (vals (to-clj-struct {:a 1 :bC 2 :DeF 3}))))

  (expect #{"A" "BC" "DEF"} (set (keys (to-clj-struct {:a 1 :bC 2 :DeF 3})))))

(defexpect assoc-tests

  (expect 4 (:foo/bar (assoc (to-clj-struct {:a 1 :bC 2 :DeF 3}) :foo/bar 4)))

  (expect nil (:bar (assoc (to-clj-struct {:a 1 :bC 2 :DeF 3}) :foo/bar 4)))

  (expect nil (:foo (assoc (to-clj-struct {:a 1 :bC 2 :DeF 3}) :foo/bar 4)))

  (expect 4 (get (assoc (to-clj-struct {:a 1 :bC 2 :DeF 3}) :foo/bar 4) "FOO/BAR")))

(defexpect nested-vector-and-map-tests

  (expect {"A" 4 "BC" 5 "DEF" 6}
          (nth (to-clj-struct [{:a 1 :bC 2 :DeF 3}
                               {:a 4 :bC 5 :DeF 6}
                               {:a 7 :bC 8 :DeF 9}]) 1))

  (expect {"A" 4 "BC" 5 "DEF" 6}
          ((to-clj-struct {:a 1
                           :bC {:a 4 :bC 5 :DeF 6}
                           :DeF {:a 7 :bC 8 :DeF 9}}) :BC)))

(defexpect sets-to-structs-tests

  (expect {"A" true "BC" true "DEF" true}
          (to-clj-struct #{:a "bC" :DeF}))

  (expect {"A" true "BC" true "DEF" true}
          ((to-clj-struct {:a 1
                           :bC #{:a :bC :DeF}
                           :DeF {:a 7 :bC 8 :DeF 9}}) :BC)))

(defexpect set-of-numbers-test

  (expect #{1 2 3} (to-clj-struct #{3 2 1})))

(defexpect empty-value-tests

  (expect [] (to-clj-struct []))

  (expect {} (to-clj-struct {})))

(defexpect nil-empty-struct-special-case

  (expect {} (to-clj-struct nil)))

(defexpect property-based-tests
  (more-of {:keys [result num-tests]}
           true result
           100 num-tests)
  (tc/quick-check
   100
   (prop/for-all [m (gen/such-that
                     not-empty
                     (gen/map (gen/one-of [gen/keyword gen/string-alphanumeric])
                              gen/small-integer))]
                 (let [k (rand-nth (keys m))
                       s (to-clj-struct m)]
                   (= (s k)
                      (s (str/lower-case (name k)))
                      (s (str/upper-case (name k)))
                      (s (keyword (name k))))))))
