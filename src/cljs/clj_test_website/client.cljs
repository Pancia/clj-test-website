(ns clj-test-website.client
  (:require [clojure.browser.repl :as repl]
            [domina :as domina]
            [domina.events :as events]))

;; Move to connect.cljs
(repl/connect "http://localhost:9000/repl")
(.log js/console "Now listening on :9000")

(let [clickhere (domina/by-id "clickhere")]
     (events/listen! clickhere :click (fn [e] (js/alert "I was clicked!"))))

;; define the function to be attached to form submission event
(defn validate-form []
  ;; get email and password element from their ids in the HTML form
  (let [$email (domina/by-id "email")
        $password (domina/by-id "password")]
    (if (and (> (count (domina/value $email)) 0)
             (> (count (domina/value $password)) 0))
      true
      (do (js/alert "Please, complete the form!")
          false))))

;; define the function to attach validate-form to onsubmit event of
;; the form
(defn init []
  ;; verify that js/document exists and that it has a getElementById
  ;; property
  (if (and js/document
           (.-getElementById js/document))
    ;; get loginForm by element id and set its onsubmit property to
    ;; our validate-form function
    (let [login-form (domina/by-id "loginForm")]
      (set! (.-onsubmit login-form) validate-form))))

;; initialize the HTML page in unobtrusive way
(set! (.-onload js/window) init)