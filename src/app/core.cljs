(ns app.core
    (:require
     [ajax.core :as ajax]
     [reagent.core :as reagent]     
     [clojure.string :as string]))

(enable-console-print!)

(defn report-error! [event]
  (let [error (.-error event)]
    (-> (js/StackTrace.fromError error)
        (.then
         (fn [stacktrace]
           (ajax/POST "/error"
                      {:headers
                       {"x-csrf-token"
                        (.-value (js/document.getElementById "__anti-forgery-token"))}
                       :params
                       {:message    (.-message error)
                        :stacktrace (->> stacktrace
                                         (mapv #(.toString %))
                                         (string/join "\n "))}}))))))

(defn home-page []
  [:div>h2 "Error Test"
   [:div>button
    {:on-click #(throw (js/Error. "I'm an error"))}
    "throw an error"]])

(defn init! []
  (.addEventListener js/window "error" report-error!)
  (reagent/render [home-page] (.getElementById js/document "app")))

(init!)
