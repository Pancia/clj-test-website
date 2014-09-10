(defproject clj-test-website "0.1.0-SNAPSHOT"
  :description "A simple clojure website for learning purposes."
  :url "https://github.com/Pancia/clj-test-website"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2322"]
                 [compojure "1.1.9"]
                 [domina "1.0.3-SNAPSHOT"]
                 [hiccup "1.0.5"]]
  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-ring "0.8.11"]]
  :source-paths ["src/clj"]
  :cljsbuild {:builds
               [{:source-paths ["src/cljs"],
                 :id "main",
                 :compiler {:optimizations :simple,
                            :output-to "resources/public/js/cljs.js",
                            :pretty-print true}}]}
  :main clj-test-website.server
  :ring {:handler clj-test-website.server/app})
