(ns mbti-diary.core
  (:require [org.httpkit.server :as http]
            [mbti-diary.server :as server]
            [mbti-diary.db :as db]
            [ring.middleware.reload :as reload]
            [compojure.handler :refer [site]])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& _]
  (db/setup)
  (let [handler (if (= (System/getenv "DEV_MODE") "true")
                  (reload/wrap-reload (site #'server/routes)) ;; only reload when dev
                  (site server/routes))]
    (http/run-server handler {:port (or (System/getenv "HTTP_PORT") 8080)}))
  (println "Server running..."))

