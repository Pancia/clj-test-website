(ns clj-test-website.login
  (:require [jayq.core :as jq :refer [$]]
            [jayq.util :refer [log]]))

(let [$clickhere ($ :#clickhere)]
  (jq/bind $clickhere :click (fn [e] (js/alert "I am clicked!"))))

(defn validate-form []
  (let [$email ($ "#email")
        $password ($ "#password")]
    (log (count (jq/val $email)) (count (jq/val $password)))
    (if (and (> (count (jq/val $email)) 0)
             (> (count (jq/val $password)) 0))
      true
      (do (js/alert "Please, complete the form!")
          false))))

(defn ^:export init []
  (if (and js/document
           (.-getElementById js/document))
    (let [$login-form ($ "#login-form")]
      (jq/bind $login-form :submit validate-form))))
