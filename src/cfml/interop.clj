;; Copyright (c) 2015 World Singles llc
;;
;; Released under the Eclipse Public License 1.0
;; http://www.eclipse.org/legal/epl-v10.html

(ns cfml.interop
  "Public API for CFML/Clojure interoperability functions."
  (:require [clojure.string :as str])
  (:import cfml.interop.CaseInsensitiveMap))

(defn struct-new
  "Make a new CaseInsensitiveMap."
  ([] (CaseInsensitiveMap.))
  ([m] (CaseInsensitiveMap. (reduce-kv (fn [m k v] (assoc m (str/upper-case (name k)) v)) {} m))))

(defn to-clj-struct
  "Converts a CFML or Clojure data structure to a case insensitive Clojure struct/map."
  [m]
  (cond (nil? m) (struct-new)
        (instance? java.util.List m) (mapv to-clj-struct m)
        (instance? java.util.Map m) (into (struct-new)
                                          (for [[k v] m]
                                            [k (and v (to-clj-struct v))]))
        (instance? java.math.BigDecimal m) (.doubleValue ^java.math.BigDecimal m)
        (instance? java.math.BigInteger m) (.longValue ^java.math.BigInteger m)
        (and (set? m)
             (every? (fn [e] (or (string? e) (keyword? e))) m))
        (to-clj-struct (zipmap m (repeat true)))
        :else m))
