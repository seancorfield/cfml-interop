;; Copyright (c) 2015 World Singles llc
;;
;; Released under the Eclipse Public License 1.0
;; http://www.eclipse.org/legal/epl-v10.html

(ns cfml.struct
  "A variant on clojure.lang.PersistentHashMap that is case insensitive
  on its keys. This implements just enough methods to be able to upper
  case keys on inbound operations that CFML would invoke."
  (:require [clojure.string :as str])
  (:gen-class :name cfml.interop.CaseInsensitiveMap
              :extends clojure.lang.APersistentMap
              :init clojure-struct
              :state "m"
              :constructors {[] []
                             [clojure.lang.IPersistentMap] []})
  (:import cfml.interop.CaseInsensitiveMap))

(defn -clojure-struct
  "Allow an empty struct and clone to be easily created.
  Do not attempt to create a struct from another data structure directly:
  Use cfml.interop/struct-new or cfml.interop/to-clj-struct instead."
  ([]
   [[] {}])
  ([s]
   [[] s]))

;; IPersistentMap

(defn -assoc [this key val]
  (CaseInsensitiveMap. (assoc (.-m this) (str/upper-case (name key)) val)))

(defn -assocEx [this key val]
  (if (.containsKey (.-m this) (str/upper-case (name key)) val)
    (throw (ex-info "Key already present" {}))
    (CaseInsensitiveMap. (assoc (.-m this) (str/upper-case (name key)) val))))

(defn -without [this key]
  (CaseInsensitiveMap. (dissoc (.-m this) (str/upper-case (name key)))))

;; Seqable

(defn -seq [this]
  (.seq (.-m this)))

;; Iterable

(defn -iterator [this]
  (.iterator (.-m this)))

;; Associative

(defn -containsKey [this key]
  (.containsKey (.-m this) (str/upper-case (name key))))

(defn -entryAt [this key]
  (.entryAt (.-m this) (str/upper-case (name key))))

;; ILookup

(defn -valAt
  ([this key]
   (.valAt (.-m this) (str/upper-case (name key)) nil))
  ([this key notFound]
   (.valAt (.-m this) (str/upper-case (name key)) notFound)))

;; Counted

(defn -count [this]
  (.count (.-m this)))

;; IPersistentCollection

(defn -empty [this]
  (CaseInsensitiveMap.))
