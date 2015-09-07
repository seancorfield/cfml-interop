;; Copyright (c) 2015 World Singles llc
;;
;; Released under the Eclipse Public License 1.0
;; http://www.eclipse.org/legal/epl-v10.html

(ns cfml.coerce
  "Data type coercions that are useful when dealing with CFML.
  These are also useful when dealing with string inputs and you
  need numeric / boolean / etc data values."
  (:require [clojure.string :as str]))

(defn ->long
  "Convert (string) value to a long. Return the default
  value if conversion fails."
  ([v] (->long v 0))
  ([v d]
   (try
     (if (number? v)
       (long v)
       (-> (java.text.NumberFormat/getInstance) (.parse v) .longValue))
     (catch Exception _
       d))))

(defn ->double
  "Convert (string) value to a double. Return the default
  value if conversion fails."
  ([v] (->double v 0.0))
  ([v d]
   (try
     (if (number? v)
       (double v)
       (-> (java.text.NumberFormat/getInstance) (.parse v) .doubleValue))
     (catch Exception _
       d))))

(defn ->boolean
  "Convert (string) value to a boolean. Return the default value
  if conversion fails.
  Accepts: Boolean, number, numeric strings, true/false and yes/no."
  ([v] (->boolean v false))
  ([v d]
   (try
     (cond
       (instance? Boolean v) v
       (number? v) (not (zero? v))
       (not (zero? (->long v))) true
       :else
       (boolean (#{"true" "yes"} (str/lower-case (str v)))))
     (catch Exception _
       d))))
