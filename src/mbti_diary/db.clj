(ns mbti-diary.db
  (:require [clojure.java.jdbc :as j]
            [clojure.string :as str]))

(def sqlite-db {:dbtype "sqlite"
                :dbname (or (System/getenv "DB_NAME") "mbti_diary.db")
                })

(defn setup []
  (j/db-do-commands 
   sqlite-db
   (j/create-table-ddl 
    :posts
    [[:id :text :primary :key]
     [:type "varchar(4)"]
     [:body "TEXT"]
     [:date "TEXT"]]
    {:entities str/upper-case
     :conditional? true})))

(defn save-post 
  "Save a post"
  [post]
  (j/insert! sqlite-db :posts post))

(defn get-post 
  "Get a post by ID"
  [id]
  (j/query sqlite-db
           ["SELECT * FROM POSTS WHERE ID = ?;", id]))

(defn get-posts
  "Get posts by type and/or date"
  [& {:keys [type date limit] :or {type nil, date nil, limit 64}}]
  (j/query sqlite-db
           (cond
            ; Date and Type specified, show the 64 from the most recent day
             (and type date) ["SELECT * FROM POSTS WHERE type = ? AND date = ? limit ?;", type, date, limit]
            ; Only date specified: show from all the types for that day
             date ["SELECT * FROM POSTS WHERE date = ? limit ?;", date, limit]
            ; Only type specified: Show the last 64
             type ["SELECT * FROM POSTS WHERE type = ? order by date desc limit ?;" type, limit]
            ; Nothing specified: show them all (with the limit)
             :else  ["SELECT * FROM POSTS order by date desc limit ?;", limit]) ) )
