(defproject mbti-diary "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [hiccup "1.0.5"]
                 [org.xerial/sqlite-jdbc "3.30.1"]
                 [http-kit "2.3.0"]
                 [compojure "1.6.1"]
                 [ring/ring-devel "1.8.0"]
                 [javax.servlet/servlet-api "2.5"]
                 [ring/ring-core "1.8.0"]]
  :main ^:skip-aot mbti-diary.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
