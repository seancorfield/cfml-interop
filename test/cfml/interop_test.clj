;; Copyright (c) 2015 World Singles llc
;;
;; Released under the Eclipse Public License 1.0
;; http://www.eclipse.org/legal/epl-v10.html

(ns cfml.interop-test
  "Tests for case insensitive CFML/Clojure interoperability."
  (:require [cfml.interop :refer :all]
            [expectations :refer :all]))

;; simple struct tests:
;; ensure conversion from Clojure (keywords), Clojure (strings), Java

(expect {"A" 1 "BC" 2 "DEF" 3}
        (to-clj-struct {:a 1 :bC 2 :DeF 3}))

(expect {"A" 1 "BC" 2 "DEF" 3}
        (to-clj-struct {"a" 1 "bC" 2 "DeF" 3}))

(expect {"A" 1 "BC" 2 "DEF" 3}
        (to-clj-struct (doto (java.util.HashMap.)
                         (.put "a" 1)
                         (.put "bC" 2)
                         (.put "DeF" 3))))

;; simple struct access tests

(expect 1 (get (to-clj-struct {:a 1 :bC 2 :DeF 3}) :a))

(expect 2 (get (to-clj-struct {:a 1 :bC 2 :DeF 3}) "bc"))

(expect 3 (get (to-clj-struct {:a 1 :bC 2 :DeF 3}) "DEF"))

;; invocation tests:

(expect 1 ((to-clj-struct {:a 1 :bC 2 :DeF 3}) :a))

(expect 2 ((to-clj-struct {:a 1 :bC 2 :DeF 3}) "Bc"))

(expect 3 ((to-clj-struct {:a 1 :bC 2 :DeF 3}) "dEf"))

;; keys and values tests:

(expect #{1 2 3} (set (vals (to-clj-struct {:a 1 :bC 2 :DeF 3}))))

(expect #{"A" "BC" "DEF"} (set (keys (to-clj-struct {:a 1 :bC 2 :DeF 3}))))

;; empty value tests

(expect [] (to-clj-struct []))

(expect {} (to-clj-struct {}))

;; nil => empty struct (special case)

(expect {} (to-clj-struct nil))
