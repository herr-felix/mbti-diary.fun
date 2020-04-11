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
    "ENTRIES"
    [[:id :text :primary :key]
     [:type "varchar(4)"]
     [:body "TEXT"]
     [:date "TEXT"]]
    {:entities str/upper-case
     :conditional? true})))

(defn save-entry
  "Save an entry"
  [entry]
  (j/insert! sqlite-db "ENTRIES" entry))

(defn get-entry
  "Get an entry by ID"
  [id]
  (first
   (j/query sqlite-db
            ["SELECT * FROM ENTRIES WHERE ID = ?;", id])))

(defn delete-entry
  "Remove an entry"
  [id]
  (j/delete! sqlite-db "ENTRIES" ["id = ?" id] ))

(defn get-entries
  "Get entries by type and/or date"
  [& {:keys [type date limit] :or {type nil, date nil, limit 64}}]
  (j/query sqlite-db
           (cond
            ; Date and Type specified, show the 64 from the most recent day
             (and type date) ["SELECT * FROM ENTRIES WHERE type = ? AND date = ?, ROWID desc limit ?;", type, date, limit]
            ; Only date specified: show from all the types for that day
             date ["SELECT * FROM ENTRIES WHERE date = ? order by ROWID desc limit ?;", date, limit]
            ; Only type specified: Show the last 64
             type ["SELECT * FROM ENTRIES WHERE type = ? order by date desc, ROWID desc limit ?;" type, limit]
            ; Nothing specified: show them all (with the limit)
             :else  ["SELECT * FROM ENTRIES order by date desc, ROWID desc limit ?;", limit]) ) )
