(defproject squirrel "0.1.0-SNAPSHOT"
  :description "A tiny library which instruments functions and
  persists the results for use in mocks."
  :url "https://github.com/expez/squirrel"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev {:dependencies [[me.raynes/fs "1.4.4"]]
                   :resource-paths ["test/resources"]}}
  :dependencies [[robert/hooke "1.3.0"]
                 [org.clojure/clojure "1.6.0"]])
