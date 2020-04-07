(ns mbti-diary.core
  (:require [org.httpkit.server :as http]
            [mbti-diary.server :as server]
            [ring.middleware.reload :as reload]
            [compojure.handler :refer [site]])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [handler (if (= (System/getenv "DEV_MODE") "true")
                  (reload/wrap-reload (site #'server/routes)) ;; only reload when dev
                  (site server/routes))]
    (http/run-server handler {:port (or (System/getenv "HTTP_PORT") 8080)}))
  (println "Server running..."))

