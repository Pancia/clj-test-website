(ns clj-test-website.shopping
  (:require [jayq.core :as jq :refer [$]]
            [jayq.util :refer [log]]))

(defn calculate []
  (let [quantity (jq/val ($ "#quantity"))
        price (jq/val ($ "#price"))
        tax (jq/val ($ "#tax"))
        discount (jq/val ($ "#discount"))]
    (jq/val ($ "#total") (-> (* quantity price)
                              (* (+ 1 (/ tax 100)))
                              (- discount)
                              (.toFixed 2)))
    false))

(defn ^:export init []
  (if (and js/document
           (.-getElementById js/document))
    (let [$shopping-form ($ "#shopping-form")]
      (jq/bind $shopping-form :submit calculate))))

