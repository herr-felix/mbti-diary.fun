(ns mbti-diary.caching)

(defonce cache (atom {}))

;; TODO: Make these as macros

(defn get-or-do! [key action]
  (if-let [val (get @cache key)]
    val
    (get (swap! cache assoc key (action)) key)))

(defn invalidate! [& keys]
  (apply swap! cache dissoc keys))
