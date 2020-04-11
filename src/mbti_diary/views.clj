(ns mbti-diary.views
  (:require [hiccup.core :refer [html h]]
            [hiccup.form :refer [select-options]]
            [mbti-diary.util :refer [mbti-types]]))


(defn types-bare []
  [:nav.navbar.navbar-light.bg-light
    (for [type (sort mbti-types)]
       [:a {:href (format "/type/%s" type)} type "&nbsp;"])])


(defn entry-item [entry]
  [:div.col-md-6.offset-md-3.mt-3.mb-3
   [:blockquote (h (:body entry))]
   [:div.text-right [:em (format "%s, %s" (:type entry) (:date entry))]]])

(defn entry-link [entry]
  [:div.col-md-6.offset-md-3.mt-3.mb-3
   [:a.text-decoration-none.text-body
    {:href (format "/entry/%s" (:id entry))}
    [:blockquote (h (:body entry))]
    [:div.text-right [:em (format "%s, %s" (:type entry) (:date entry))]]]])

(defn entry-list [entries]
  (if (seq entries)
    (list (map entry-link entries))
    [:div.col-md-12.text-center "..."]))


(defn default-layout [title body & {:keys [preset-type] :or {preset-type nil}}]
  (html [:html 
         [:head
          [:title title]
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1, shrink-to-fit=no"}]
          [:link {:rel "stylesheet"
                  :href "https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
                  :integrity "sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
                  :crossorigin "anonymous"}]]
         [:body 
          [:div.container 
           [:a.text-decoration-none {:href "/"}
            [:h1.text-center.title.mt-2  "MBTI Diary"]]
           [:p.text-center "What's on your mind today?"]
           (types-bare)
           [:br]
           [:a.btn.btn-block.btn-light.font-italic 
            {:href (if preset-type (format "/entry?type=%s" preset-type) "/entry")} 
            "Write an entry"]
           [:br]
           body
           [:hr]
           [:div.text-center
            [:a.text-muted.mb-3 {:target "_blank" :href "https://github.com/herr-felix/mbti-diary.fun"} "This website is open source"]]
           ]]]))

(def select-type-options (into ['("What's your type?" "")] (apply map list (repeat 2 (sort mbti-types)))))

(defn entry-form [& {:keys [preset-type] :or {preset-type nil}}]
  (default-layout "Write an entry - MBTI Diary" 
    [:div.row
     [:div.col-md-12
      [:form {:method "post" :action "/entry"}
       [:div.form-group
        [:select.form-control {:name "type" :required "true"} (select-options select-type-options preset-type)]]
       [:div.form-group 
        [:textarea.form-control {:style "resize: none"
                                 :rows "10"
                                 :name "body"
                                 :placeholder "What happened?"
                                 :required "true"}]]
       [:input.btn.btn-block.btn-primary {:type "submit" :value "Publish"}] ]]]))


(defn home [entries]
  (default-layout "The MBTI Diary"
    [:div.row
     [:div.col-md-12
      [:h2.text-center "Hello unique human being!"]
      [:p.text-center "This is a public diary where people, with different types, tell events of their day anonymously. Please, don't write anything too obscene (looking at you, ENTP)."]]
     (entry-list entries)]))
     

(defn per-type [type entries]
  (default-layout (format "%s - MBTI Diary" type)
    [:div.row 
     [:div.col-md-12
      [:h2.text-center type]]
     (if (seq entries)
       (list (map entry-link entries))
       [:div.col-md-12.text-center 
        [:a {:href (format "/entry?type=%s" type)} (format "Be the first %s to write an entry!" type)]])] :preset-type type))

(defn entry [entry]
  (default-layout 
    (format "%s, %s - MBTI Diary" (:type entry) (:date entry)) 
    [:div.row (entry-item entry)]))
