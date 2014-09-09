(ns clj-test-website.server
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as response]
            [hiccup.core :refer :all]
            [hiccup.form :as f]
            [compojure.core :refer :all]))

(defn view-layout [& content]
  (html
    [:head
     [:meta {:http-equiv "Content-type"
             :content "text/html; charset=utf-8"}]
     [:title "Clojure Test Website"]
     [:link {:rel "stylesheet" :href "css/styles.css"}]]
    [:body content]))

(defn view-content []
  (view-layout
    [:h1 "clj-test-website"]
    [:p "Get yourself a nice alert by clicking on the button."]
    [:button#clickhere "Click Me!"]

    [:form {:id "loginForm"
            :method "post"
            :action "login.php"
            :novalidate true}
     [:fieldset [:legend "Login"]

      [:div [:label {:for "email"} "Email Address"]
       [:input#email {:type "email" :name "email" :required true}]]

      [:div [:label {:for "password"} "Password"]
       [:input#password {:type "password" :name "password" :required true}]]

      [:div [:label {:for "submit"}]
       [:input#submit {:type "submit" :value "Login"}]]]]

    [:script {:src "/js/jquery-1.11.1.min.js"}]
    [:script {:src "/js/cljs.js"}]))

(declare main-routes)
(defroutes main-routes
           (GET "/" []
                (view-content))

           (route/resources "/")

           (route/not-found "ERROR 404"))

(def app
  (handler/site main-routes))