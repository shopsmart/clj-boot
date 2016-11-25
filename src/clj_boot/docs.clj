(ns clj-boot.docs
  (:require [hiccup.core :refer [html]]
            [hiccup.page :refer [html5 include-css include-js]]))


(defn renderer [{content :entry}]
  (html5 {:lang "English" :itemscope "" :itemtype "http://schema.org/BlogPosting"}
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0, user-scalable=no"}]
     [:meta {:name "description" :itemprop "description" :content (str "Library Documentation - " (:name content))}]
     [:title {:itemprop "name"} (:name content)]
     [:link {:rel "shortcut icon" :href "/favicon.ico"}]
     (include-css "/css/app.css")]
    [:body
     [:div.row.content
      [:div.post.small-12.columns
       [:h1 {:itemprop "name"} (:name content)]
       (str (:content content))]]]))
