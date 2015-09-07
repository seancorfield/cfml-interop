;; Copyright (c) 2015 World Singles llc
;;
;; Released under the Eclipse Public License 1.0
;; http://www.eclipse.org/legal/epl-v10.html

(ns cfml.coerce-test
  "Tests for data type coercions."
  (:require [cfml.coerce :refer :all]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [expectations :refer :all]))

;; conversion tests

(expect 0 (->long "bar"))

(expect 100 (->long "" 100))

(expect 100 (->long nil 100))

(expect 100 (->long "foo" 100))

(expect 123 (->long "123" 100))

(expect 1234 (->long "1,234"))

(expect 0.0 (->double "bar"))

(expect 1.0 (->double "" 1.0))

(expect 2.0 (->double nil 2.0))

(expect 3.0 (->double "foo" 3.0))

(expect 123.0 (->double "123.0" 4.0))

(expect 123.0 (->double "123" 5.0))

(expect 12345.0 (->double "1,234.5E1"))

(expect true (->boolean true))

(expect true (->boolean 42))

(expect true (->boolean "13"))

(expect true (->boolean "YES"))

(expect true (->boolean "True"))

(expect false (->boolean false))

(expect false (->boolean 0))

(expect false (->boolean "0"))

(expect false (->boolean "NO"))

(expect false (->boolean "False"))

(expect (more-of {:keys [result num-tests]}
                 true result
                 100 num-tests)
        (tc/quick-check 100
                        (prop/for-all [n gen/int]
                                      (= n (->long (str n))))))
