(ns squirrel.core
  (:require [clojure.java.io :as io]
            [robert.hooke :refer :all]))

(def mock (atom {}))

(defn record
      [f args]
      (let [res (apply f args [])]
        (swap! mock merge {(str (type f)) res})
        res))

(defmacro recording [fns dest & body]
  `(with-scope
     ~@(for [fn fns]
         `(add-hook #'~fn record))
     ~@body
     (spit (io/file ~dest) @mock)))
