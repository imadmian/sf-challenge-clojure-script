(defproject pilloxa "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.7.122"]
                 [ring/ring-core "1.5.0"]
                 [ring/ring-json "0.4.0"]
                 [aleph "0.4.1"]
                 [compojure "1.5.1"]
                 [hiccup "1.0.5"]
                 [reagent "0.6.0-rc"]
                 [cljs-ajax "0.5.8"]]

  :plugins [[lein-figwheel "0.5.4-7"]
            [lein-cljsbuild "1.1.3"]]

  :clean-targets ^{:protect false} ["resources/public/js" "target"]

  :cljsbuild
    {:builds
     [{:id "dev"
       :source-paths ["src"]
       :figwheel {:on-jsload "pilloxa.cljs.core/on-js-reload"
                  :open-urls ["http://localhost:8000"]}
       :compiler {:main pilloxa.cljs.core
                  :output-to "resources/public/js/main.js"
                  :output-dir "resources/public/js/out"
                  :asset-path "/js/out"
                  :source-map-timestamp true }}]}

  :jvm-opts ^:replace ["-Xmx1g" "-server"])
