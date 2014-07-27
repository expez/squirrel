(ns squirrel.core
  (:require [clojure.java.io :as io]
            [robert.hooke :refer :all]))

(def mock (atom {}))

(defn- get-canonical-name [f]
  (-> f
      type
      str
      (.split  " ")
      second
      (.replaceAll  "\\$" "/")))

(defn record
  [f & args]
  (let [res (apply f args)]
    (swap! mock merge {(get-canonical-name f) {args res}})
    res))

(defmacro recording [fns dest & body]
  `(with-scope
     ~@(for [f fns]
         `(add-hook #'~f record))
     ~@body
     (spit (io/file ~dest) @mock)))

(defn create-impl [f mocks-file]
  (fn [& args]
    (let [mocks (clojure.edn/read-string (slurp (io/file mocks-file)))]
      (get-in mocks [(.replaceAll f "-" "_") args]))))

(defmacro with-recordings [fns mocks-file & body]
  `(with-redefs
     ~(reduce concat
              (for [f fns]
                [f `#((create-impl ~(str f) ~mocks-file) %)]))
     ~@body))
