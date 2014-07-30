(ns squirrel.core-test
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.test :refer :all]
            [me.raynes.fs :as fs]
            [squirrel.core :refer :all]))

(defn bar [arg]
  (inc arg))

(defn foo [arg]
  (bar (dec arg)))

(let [nuts (fs/temp-file "nuts")]
  (recording [foo bar] nuts
             (foo 1))

  (deftest saves-output
    (is (=  {"squirrel.core-test/foo" {'(1) 1},
             "squirrel.core-test/bar" {'(0) 1}}
            (edn/read-string (slurp nuts)))))

  (deftest creates-mocks
    (with-redefs [foo (RuntimeException. "foo")
                  bar (RuntimeException. "bar")]
      (with-recordings [squirrel.core-test/foo
                        squirrel.core-test/bar] nuts
                        (is (= 1 (foo 1)))))))
