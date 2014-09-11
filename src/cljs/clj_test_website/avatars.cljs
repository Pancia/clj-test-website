(ns clj-test-website.avatars
  (:require [jayq.core :as jq :refer [$]]
            [jayq.util :refer [log]]
            [ajax.core :refer [GET]]))

(defn ^:export init []
  (if (and js/document
           (.-getElementById js/document))
    (do
      (jq/bind ($ "body")
               :keydown (fn [e] (if (= (.-keyCode e) 40)
                                  (GET "http://localhost:3000/avatars/unseen"
                                       {:handler (fn [stuff] (do
                                                               (log stuff)
                                                               (jq/attr ($ "img") "src" stuff)))}))))
      (jq/bind ($ "body")
               :swipeleft (fn [e] (GET "http://localhost:3000/avatars/unseen"
                                       {:handler (fn [stuff] (do
                                                               (log stuff)
                                                               (jq/attr ($ "img") "src" stuff)))}))))))