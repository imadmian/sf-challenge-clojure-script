(ns pilloxa.clj.logic
  (:require  [clojure.string :as str]))

(defn generate-default-grid [w h]
  (mapv (fn [x] (mapv (fn [y] {:deactivate [x y]})
                      (range h)))
        (range w)))

(defn create-grid [w h]
  {:pre [(>= w 1) (>= h 1)]}
  (generate-default-grid w h))

(defn cells [grid-data]
  (apply concat grid-data))

(defn update-status [data x y]
  (let [current-value (-> data (get-in [x y]) keys first)]
    (if (= current-value :deactivate)
      :activate
      :deactivate)))

(defn transform [sub-grid status grid-data]
  (reduce (fn [data [x-pos y-pos]]
            (assoc-in data [x-pos y-pos]
                      {(if (= status "toggle")
                         (update-status data x-pos y-pos)
                         (keyword status))
                       [x-pos y-pos]}))
          grid-data sub-grid))

(defn subgrid [x-axis-min y-axis-min x-axis-max y-axis-max]
  (for [x-axis (range x-axis-min (+ 1 x-axis-max))
        y-axis (range y-axis-min (+ 1 y-axis-max))]
    [x-axis y-axis]))

(defn number
  "Ensure the argument is a number."
  [v]
  (cond
    (number? v) v
    (string? v) (try (Integer/parseInt v) (catch Exception _))))

(defn process-raw-input[raw-input]
  (mapv (fn [x]
          (let [act (str/split x #" ")
                status (-> act first)
                [x-min y-min] (map #(number %) (-> act second (str/split #",")))
                [x-max y-max] (map #(number %) (-> act last (str/split #",")))]
            [x-min y-min x-max y-max status]))
        raw-input))

(defn process-and-merge [data [x-min y-min x-max y-max status]]
  (transform (subgrid x-min y-min x-max y-max) status data))

(defn showRecord []
  (let [mygrid (create-grid 700 700)
        api-data (->> "resources/input-data.txt"
                      clojure.java.io/reader
                      line-seq
                      ;(take 2)
                      (process-raw-input)
                      (reduce process-and-merge mygrid)
                      (cells))]
    api-data))

(comment

  (def mygrid (create-grid 700 700))

  (time (->>
          "resources/small-data.txt"
             ;"resources/input-data.txt"
             clojure.java.io/reader
             line-seq
             (take 1)
             (process-raw-input)
             (reduce process-and-merge mygrid)
             (cells)
             (filter :activate)
             ;(count)
             ;(map vals)
             ;(apply concat)
             ;(count)

             ))

  (count (cells (create-grid 50 50)))

  (count (subgrid 0 0 2 2))


  (def matrix (->> "d://number.txt"
                   clojure.java.io/reader
                   line-seq
                   ;first
                   ;(#(str/split % #""))
                   ;(partition 20)
                   #_(mapv #(vec %))))

  (get-in matrix [0 3])

  (let [y 0]
    (->> (range y (+ 4 y))
         (map #(get-in matrix [0 %]))
         (str/join "")
         (read-string)))

  (def matrix (->> "d://number.txt"
                   clojure.java.io/reader
                   line-seq
                   first
                   (#(str/split % #""))
                   (partition 20)
                   (mapv #(vec %))))

  (get-in matrix [0 0])


  )