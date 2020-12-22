(ns pilloxa.cljs.core
  (:require
    [reagent.core :as reagent]
    [ajax.core :as ajax]))

(enable-console-print!)

(defonce api-data (reagent/atom ""))

(def tile-size 1)

(defn draw-tile! [ctx x y color]
  (.setTransform ctx 1, 0, 0, 1, 0.5, 0.5)

  (.beginPath ctx)
  (.rect ctx x y tile-size tile-size)
  (set! (.-fillStyle ctx) color)
  (.fill ctx))

(defn draw-canvas-contents [ canvas ]
  (let [ctx (.getContext canvas "2d")]
    (doall (->> @api-data
                (map (fn [item]
                       (let [tile-color (if (= (-> item keys first) :activate) "green" "black")
                             [[x y]] (vals item)]
                         (draw-tile! ctx (* tile-size x) (* tile-size y) tile-color))))))))

;//////////////////////Canvas configuration ////////////////////////////////////////////////////

(defn home [ ]
  (let [dom-node (reagent/atom nil)]
    (reagent/create-class
      {:component-did-update
       (fn [ this ]
         (draw-canvas-contents (.-firstChild @dom-node)))

       :component-did-mount
       (fn [ this ]
         (reset! dom-node (reagent/dom-node this)))

       :reagent-render
       (fn [ ]
         [:div.with-canvas
          [:canvas (if-let [ node @dom-node]
                     {:width 4980
                      :height 4980})]]
         )})))

(defn mount-root []
  (reagent/render-component [home]
                            (. js/document (getElementById "app"))))

(defn on-js-reload []
  (mount-root))


(defn ^:export start []
  (mount-root)
  (ajax/GET "/api"
            {:handler #(reset! api-data %)
             :params {:number 0}
             :response-format (ajax/json-response-format {:keywords? true})}))