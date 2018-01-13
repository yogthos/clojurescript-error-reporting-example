(ns app.handler
  (:require
   [cognitect.transit :as transit]
   [compojure.core :refer [GET POST defroutes]]
   [compojure.route :refer [not-found resources]]
   [hiccup.page :refer [include-js html5]]
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [ring.middleware.webjars :refer [wrap-webjars]])
  (:gen-class))

(defroutes handler
  (GET "/" []
    (html5
      [:head
       [:meta {:charset "utf-8"}]
       (anti-forgery-field)]
      [:body
       [:div#app]
        (include-js "/assets/stacktrace-js/dist/stacktrace.min.js"
                    "/js/app.js")]))

  (POST "/error" {:keys [body]}
    (let [{:keys [message stacktrace]}
          (-> body
              (transit/reader :json)
              (transit/read))]
      (println "Client error:" message "\n" stacktrace))
    "ok")

  (resources "/")
  (not-found "Not Found"))

(defn -main []
  (run-jetty
   (-> handler
       (wrap-webjars)
       (wrap-defaults site-defaults))
   {:port 3000 :join? false}))
