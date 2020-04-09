(ns mbti-diary.util)

(def mbti-types #{"INTP" "INFP" "ENTP" "ENFP" "INTJ" "INFJ" "ENTJ" "ENFJ"
             "ISTP" "ISFP" "ESTP" "ESFP" "ISTJ" "ISFJ" "ESTJ" "ESFJ"})

(defn uuid []
  (.toString (java.util.UUID/randomUUID)))

(def date-formatter (java.text.SimpleDateFormat. "yyyy-MM-dd"))

(defn format-date [date] 
  (.format date-formatter date))

(defn today [] (format-date (java.util.Date.)))