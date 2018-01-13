(defproject app "0.1.0-SNAPSHOT"

  :dependencies
  [[org.clojure/clojure "1.9.0"]
   [cljs-ajax "0.7.3"]
   [reagent "0.7.0"]   
   [ring "1.6.3"]
   [ring/ring-defaults "0.3.1"]
   [compojure "1.6.0"]
   [hiccup "1.0.5"]
   [org.clojure/clojurescript "1.9.946" :scope "provided"]
   [ring-webjars "0.2.0"]
   [org.webjars.bower/stacktrace-js "2.0.0"]]

  :plugins [[lein-cljsbuild "1.1.7"]]

  :min-lein-version "2.5.0"
  :uberjar-name "app.jar"
  :main app.handler
  :aot :all

  :clean-targets ^{:protect false}
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :resource-paths ["resources" "target/cljsbuild"]

  :profiles {:dev
             {:cljsbuild
              {:builds
               [{:source-paths ["src"]
                 :compiler
                 {:main "app.core"
                  :asset-path "/js/out"
                  :output-to  "target/cljsbuild/public/js/app.js"
                  :output-dir "target/cljsbuild/public/js/out"
                  :optimizations :none}}]}}
             :uberjar
             {:prep-tasks ["compile" ["cljsbuild" "once"]]
              :omit-source true
              :cljsbuild
              {:builds
               [{:source-paths ["src"]
                 :compiler
                 {:output-dir "target/cljsbuild/public/js"
                  :output-to  "target/cljsbuild/public/js/app.js"
                  :source-map "target/cljsbuild/public/js/app.js.map"
                  :optimizations :advanced
                  :infer-externs true
                  :closure-warnings {:externs-validation :off
                                     :non-standard-jsdoc :off}}}]}}})
