;; Copyright (c) 2015 World Singles llc
;;
;; Released under the Eclipse Public License 1.0
;; http://www.eclipse.org/legal/epl-v10.html

(ns cfml.interop-test
  "Tests for case insensitive CFML/Clojure interoperability."
  (:require [cfml.interop :refer :all]
            [expectations :refer :all]))

;; simple struct tests

(expect {"A" 1 "BC" 2 "DEF" 3}
        (to-clj-struct {:a 1 :bC 2 :DeF 3}))

;; empty value tests

(expect [] (to-clj-struct []))

(expect {} (to-clj-struct {}))

;; nil => empty struct (special case)

(expect {} (to-clj-struct nil))
