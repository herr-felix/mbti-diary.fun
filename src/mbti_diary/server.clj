(ns mbti-diary.server
  (:require 
   [clojure.spec.alpha :as s]
   [clojure.walk :refer [keywordize-keys]]
   [compojure.core :refer [defroutes GET POST]]
   [compojure.route :as route]
   [ring.util.response :refer [redirect]]
   [ring.middleware.params :refer [wrap-params]]
   [mbti-diary.util :refer [mbti-types uuid today]]
   [mbti-diary.db :as db]
   [mbti-diary.caching :as cache]
   [mbti-diary.views :as views]))

(s/def ::type mbti-types)
(s/def ::body string?)
(s/def ::entry-post (s/keys :req-un [::type ::body]))


(defn home [_]
  (let [entries (cache/get-or-do! :home-feed #(db/get-entries :limit 16))]
       (views/home entries)))

(defn entry-form [req]
  (views/entry-form :preset-type (get-in req [:query-params "type"])))

(defn post-entry-form [req]
  (let [entry (merge (-> req :form-params keywordize-keys) {:id (uuid) :date (today)})]
    (if (s/valid? ::entry-post entry)
      (do
        (db/save-entry entry)
        (cache/invalidate! :home-feed
                          (:type entry))
        (redirect (str "/entry/" (:id entry))))
      ;; TODO: Error message
      (redirect "/entry"))))

(defn view-entry [req]
  (if-let [entry (db/get-entry (get-in req [:route-params :uuid]))]
    (views/entry entry)
    (redirect "/")) )

(defn per-type [req]
  (let [type (get-in req [:route-params :type])]
    (if (contains? mbti-types type)
      (views/per-type type (cache/get-or-do! type #(db/get-entries :type type)))
      (redirect "/")) ))

(defn not-found [_] 
  (redirect "/"))


(defroutes routes
  (GET "/" [] home)
  (GET "/entry" [] wrap-params entry-form)
  (POST "/entry" [] wrap-params post-entry-form)
  (GET "/entry/:uuid" req (view-entry req))
  (GET "/type/:type" req (per-type req))
  (route/not-found not-found))
