# cfml-interop

A Clojure library designed to provide interoperability with CFML. This library is intended to be used by projects that are loaded into a CFML engine, such as [Lucee](http://lucee.org) via [FW/1](https://github.com/framework-one/fw1) or [cfmljure](https://github.com/framework-one/cfmljure).

CFML can treat Clojure vectors as arrays (ArrayList variants) but CFML structs are case-insensitive hash maps with strings as keys. This library can convert both CFML structs and Clojure maps into a map-like data structure that is case-insensitive and can accept both keywords and strings as keys. This makes interoperability much easier.

In addition, this library provides a number of useful data coercions: when faced with string inputs via URL and form scope, you often want to robustly convert them to `Long`, `Double`, or `Boolean`. The `cfml.coerce` namespace provides `->long`, `->double`, and `->boolean` for this.

## Usage

The main function here is `to-clj-struct` which converts CFML and Clojure (and compatible Java) data structures to a format that can be iterated over, indexed, and dereferenced by both CFML and Clojure.

    ;; add this Leiningen dependency:
    
    [cfml-interop "0.2.1"]
    
    ;; NOTE: REQUIRES Clojure 1.7.0 OR LATER!
    
    ;; to use in Clojure:
    (ns my.ns
      (:require [cfml.interop :as cfml]))

    (cfml/to-clj-struct {:a 1 :bC 2 :DeF 3})
    ;;=> {"A" 1, "BC" 2, "DEF" 3}
    
    // to use in CFML:
    
    // ensure "cfml.interop" is exposed via cfmljure
    
    // convert Clojure data structure to usable CFML data structure:
    var data = cfml.interop.to_clj_struct( clj_data );
    // now data is usable in CFML!
    
    // convert CFML data structure to usable Clojure data structure:
    var data = cfml.interop.to_clj_struct( cfml_data );
    // now data is usable in Clojure!

However, this map can still be indexed by keywords or strings.

In addition, as of 0.2.0, this provides `->long`, `->double`, and `->boolean` data coercions in the `cfml.coerce` namespace.

## Testing

Clone this repo and then run:

    boot aot expectations

## Changes

* 0.2.4 -- 2017 Jul 18 -- Switch to Boot.
* 0.2.3 -- 2017 Jul 18 -- Minor dependency update; drop nREPL plugin.
* 0.2.2 -- 2016 Jun 28 -- Minor dependency update.
* 0.2.1 -- 2016 Jun 09 -- Update dependencies; test against 1.9.0 (master).
* 0.2.0 -- 2015 Sep 07 -- Move data coercions to `cfml.coerce` namespace (breaking change if you used them in 0.1.2).
* 0.1.2 -- 2015 Sep 06 -- Add data coercions.
* 0.1.1 -- 2015 Aug 05 -- Support - / _ translation #1.
* 0.1.0 -- 2015 Aug 04 -- Initial public release.

## License

Copyright © 2015-2017 World Singles llc

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
