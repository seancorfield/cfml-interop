(try
  (binding [*compile-path* "classes"]
    (compile 'cfml.struct))
  (catch Throwable t
    (println "boom!\n" t)))
