(ns squirrel.core-test
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.test :refer :all]
            [squirrel.core :refer :all]))

(defn bar [arg]
  (inc arg))

(defn foo [arg]
  (bar (dec arg)))

(deftest saves-output
  (recording [foo bar] "resources/nuts.edn"
             (foo 1))
  (is (=  {"class squirrel.core_test$foo" 1,
           "class squirrel.core_test$bar" 1}
          (edn/read-string (slurp (io/file "resources/nuts.edn"))))))
