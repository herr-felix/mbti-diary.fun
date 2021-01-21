(ns mbti-diary.db
  (:require [clojure.java.jdbc :as j]
            [clojure.string :as str]))

(def h2-db {:dbtype "h2"
                :user "sa"
                :password ""
                :dbname (or (System/getenv "DB_NAME") "mbti_diary.db")
                })

(extend-protocol j/IResultSetReadColumn
  org.h2.jdbc.JdbcClob
  (result-set-read-column [^org.h2.jdbc.JdbcClob v _2 _3]
    (-> v .getAsciiStream slurp))
  )

(defn setup []
  (j/db-do-commands 
   h2-db
   (j/create-table-ddl 
    "ENTRIES"
    [[:id :uuid :primary :key]
     [:type "varchar(4)"]
     [:body "TEXT"]
     [:date "DATE"]]
    {:entities str/upper-case
     :conditional? true})))

(defn save-entry
  "Save an entry"
  [entry]
  (j/insert! h2-db "ENTRIES" entry))

(defn get-entry
  "Get an entry by ID"
  [id]
  (first
   (j/query h2-db
            ["SELECT * FROM ENTRIES WHERE ID = ?;", id])))

(defn delete-entry
  "Remove an entry"
  [id]
  (j/delete! h2-db "ENTRIES" ["id = ?" id] ))

(defn get-entries
  "Get entries by type and/or date"
  [& {:keys [type date limit] :or {type nil, date nil, limit 64}}]
  (j/query h2-db
           (cond
            ; Date and Type specified, show the 64 from the most recent day
             (and type date) ["SELECT * FROM ENTRIES WHERE type = ? AND date = ?, _ROWID_ desc limit ?;", type, date, limit]
            ; Only date specified: show from all the types for that day
             date ["SELECT * FROM ENTRIES WHERE date = ? order by _ROWID_ desc limit ?;", date, limit]
            ; Only type specified: Show the last 64
             type ["SELECT * FROM ENTRIES WHERE type = ? order by date desc, _ROWID_ desc limit ?;" type, limit]
            ; Nothing specified: show them all (with the limit)
             :else  ["SELECT * FROM ENTRIES order by date desc, _ROWID_ desc limit ?;", limit]) ) )

