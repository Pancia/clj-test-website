(ns clj-test-website.server
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as response]
            [hiccup.core :refer :all]
            [compojure.core :refer :all]
            [clj-test-website.avatar :as avatar]))

(defn html-login []
  (html
    [:head
     [:meta {:http-equiv "Content-type"
             :content "text/html; charset=utf-8"}]
     [:title "Clojure Test Website"]
     [:link {:rel "stylesheet" :href "/css/styles.css"}]]

    [:body
     [:img {:src "/images/clojure_logo.gif" :alt "clojure logo"}]
     [:h1 "clj-test-website"]
     [:p "Get yourself a nice alert by clicking on the button."]
     [:button#clickhere "Click Me!"]

     [:form {:id "login-form"
             :method "get"
             :action "calculator"
             :novalidate true}
      [:fieldset [:legend "Login"]

       [:div [:label {:for "email"} "Email Address"]
        [:input#email {:type "email" :name "email" :required true}]]

       [:div [:label {:for "password"} "Password"]
        [:input#password {:type "password" :name "password" :required true}]]

       [:div [:label {:for "submit"}]
        [:input#submit {:type "submit" :value "Login"}]]]]

     [:script {:src "http://code.jquery.com/jquery-1.11.1.js"}]
     [:script {:src "/js/cljs.js"}]
     [:script "clj_test_website.login.init();"]]))

(defn html-calculator []
  (html
    [:head
     [:meta {:http-equiv "Content-type"
             :content "text/html; charset=utf-8"}]
     [:title "Clojure Test Website"]
     [:link {:rel "stylesheet" :href "/css/styles.css"}]]

    [:body
     [:img {:src "/images/clojure_logo.gif" :alt "clojure logo"}]

     [:form {:id "shopping-form"
             :method "post"
             :action ""
             :novalidate true}
      [:legend "Shopping Calculator"]
      [:fieldset
       (for [[content name type value] [["Quanity"       "quantity" "number" "1"]
                                        ["Price Per Unit" "price"    "text"   "1.0"]
                                        ["Tax Rate (%)"   "tax"      "text"   "0.0"]
                                        ["Discount"       "discount" "text"   "0.00"]
                                        ["Total"          "total"    "text"   "0.00"]]]
         [:div
          [:label {:for   name} content]
          [:input {:name  name
                   :id    name
                   :type  type
                   :value value
                   :required (if (not= "total" name)
                               true
                               false)}]])
       [:div [:input {:value "Calculate"
                      :type  "submit"
                      :id    "submit"}]]]]

     [:script {:src "http://code.jquery.com/jquery-1.11.1.js"}]
     [:script {:src "/js/cljs.js"}]
     [:script "clj_test_website.shopping.init();"]]))

(defn html-rand-avatar
  [avatar-path]
  (let [avatar-path (apply str avatar-path)]
    (html
      [:head
       [:meta {:http-equiv "Content-type"
               :content "text/html; charset=utf-8"}]
       [:title "Clojure Test Website"]
       [:link {:rel "icon"
               :type "image/png"
               :href avatar-path}]
       [:link {:rel "stylesheet" :href "/css/avatar.css"}]

      [:body
       [:a {:href "/avatars/random"}
        [:img {:src avatar-path :alt "A randomly generated avatar"}]]
       [:script {:src "http://code.jquery.com/jquery-1.11.1.js"}]

       [:script {:src "/js/cljs.js"}]
       [:script "clj_test_website.avatars.init();"]]])))

(declare main-routes)
(declare id)
(defroutes main-routes
           (GET "/" []
                (html-rand-avatar (avatar/main 1 10)))

           (GET "/login" []
                (html-login))

           (GET "/calculator" []
                (html-calculator))

           (GET "/avatars/random" []                        ;;get a random (always gen-ed) avatar
                (html-rand-avatar (avatar/main 1 10)))

           (GET "/avatars/unseen" []                        ;;get an avatar unseen by the user
                (let [avatar-path (apply str (avatar/main 1 10))]
                  (println avatar-path)
                  ;(str "{\"data\":\"" (apply str (avatar/main 1 10)) "\"}")
                  (apply str avatar-path)))

           (GET "/avatars/:id" [id]
                (html-rand-avatar (str "/images/avatars/10_clojure_" id ".png")))

           (route/resources "/")

           (route/not-found "ERROR 404, NOT FOUND"))

(def app
  (handler/site main-routes))
