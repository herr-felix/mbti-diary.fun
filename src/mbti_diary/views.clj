(ns mbti-diary.views
  (:require [hiccup.core :refer [html h]]
            [mbti-diary.util :refer [mbti-types]]))


(defn types-bare []
  [:nav.navbar.navbar-light.bg-light
    (for [type (sort mbti-types)]
       [:a {:href (str "/type/" type)} type "&nbsp;"])])


(defn default-layout [title body]
  (html [:html 
         [:head
          [:title title]
          [:link {:rel "stylesheet"
                  :href "https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
                  :integrity "sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
                  :crossorigin "anonymous"}]]
         [:body 
          [:div.container 
           [:a.text-decoration-none {:href "/"}
            [:h1.text-center.title  "MBTI Diary"]]
           [:p.text-center "What's on your mind today?"]
           (types-bare)
           [:br]
           body
           [:hr]
           [:div.text-center
            [:a.text-muted {:target "_blank" :href "https://github.com/herr-felix/mbti-diary.fun"} "This website is open source"]]
           ]]]))


(defn entry-form []
  (default-layout "Write an entry - MBTI Diary" 
    [:div.row
     [:div.col-md-12
      [:form {:method "post" :action "/entry"}
       [:div.form-group
        [:select.form-control {:name "type"} 
         (for [type mbti-types] [:option {:value type} type] )]]
       [:div.form-group 
        [:textarea.form-control {:style "resize: none"
                                 :rows "10"
                                 :name "body"}]]
       [:input.btn.btn-block.btn-primary {:type "submit" :value "Publish"}] ]]]))

(defn home []
  (default-layout "Welcome - MBTI Diary"
    [:div.row 
     [:div.col-md-12
      [:h2 "Welcome!"]]]))

(defn per-type [type]
  (default-layout "Welcome - MBTI Diary"
    [:div.row 
     [:div.col-md-12
      [:h2 type]]]))