(ns pilloxa.clj.core
  (:require [aleph.http :as aleph]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-params]]
            [ring.util.response :refer [response]]
            [compojure.core :refer [defroutes GET POST]]
            [hiccup.page :refer [html5]]
            [pilloxa.clj.logic :as logic]))

(defn home []
  (html5
    [:head
     [:title "Pilloxa Assignment"]]
    [:body
     [:div#app "Blank"]
     [:script {:src "/js/main.js"}]
     [:script "pilloxa.cljs.core.start();"]]))

(defroutes approutes
           (GET "/" [] (home))
           (GET "/api" [number] (response (logic/showRecord)))
           (GET "/favicon.ico" [] ""))

(def handler
  (-> approutes
      (wrap-keyword-params)
      (wrap-json-params)
      (wrap-json-response)
      (wrap-resource "public")))

;; Web server

(defonce S (atom nil))

(defn start-server! []
  (reset! S (aleph/start-server handler {:port 8000})))

(defn stop-server! []
  (when-let [s @S] (.close s))
  (reset! S nil))

(defn restart-server! []
  (stop-server!)
  (start-server!))

(comment
(restart-server!)
(stop-server!)
(starts-server!)
)
