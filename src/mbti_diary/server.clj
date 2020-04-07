(ns mbti-diary.server
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [ring.util.response :refer [redirect]]
            [mbti-diary.views :as views]))

(defn home [req] 
  (views/home))

(defn entry-form [req]
  (views/entry-form))

(defn post-form [req] 
  (redirect "/type/INTP"))

(defn view-entry [req] 
  "blah")

(defn per-type [req] 
  (views/per-type (get-in req [:route-params :type])))

(defn not-found [req] 
  "<h1>Not found</h1>")

(defroutes routes
  (GET "/" [] home)
  (GET "/entry" [] entry-form)
  (POST "/entry" [] post-form)
  (GET "/entry/:uuid" [] view-entry)
  (GET "/type/:type" req (per-type req))
  (route/not-found not-found))
