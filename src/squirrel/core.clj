(ns squirrel.core
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [robert.hooke :refer :all]))

(def mock (atom {}))

(defn- get-canonical-name [f]
  (-> f
      resolve
      str
      (.substring 2)))

(defn record
  [f & args]
  (let [res (apply f args)]
    (swap! mock merge {(:name (meta f)) {args res}})
    res))

(defmacro recording [fns dest & body]
  `(with-scope
     ~@(for [f fns]
         `(do (alter-var-root #'~f vary-meta (constantly
                                              {:name ~(get-canonical-name f)}))
            (add-hook #'~f record)))
     ~@body
     (spit (io/file ~dest) @mock)))

(defn create-impl [f mocks-file]
  (fn [& args]
    (let [mocks (edn/read-string (slurp (io/file mocks-file)))]
      (get-in mocks [f args]))))

(defmacro with-recordings [fns mocks-file & body]
  `(with-redefs
     ~(reduce concat
              (for [f fns]
                [f `#((create-impl ~(get-canonical-name f) ~mocks-file) %)]))
     ~@body))
