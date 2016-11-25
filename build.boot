(def project  'bradsdeals/clj-boot)
(def prj-name "clj-boot")

(def version  "0.1.5")
(def description "Standard Brad's Deals boot build tasks")
(def scm-url "https://github.com/shopsmart/clj-boot")
(def nightlight-port 12000)


(set-env! :resource-paths #{"resources"}
          :source-paths   #{"src" "test"}

          :repositories #(conj % ["clojars-push" {:url "https://clojars.org/repo/"
                                                  :username (System/getenv "CLOJARS_USER")
                                                  :password (System/getenv "CLOJARS_PASS")}])

          :dependencies '[[org.clojure/clojure   "1.8.0"]
                          [clojure-future-spec   "1.9.0-alpha14"]

                          ; Boot tasks
                          [boot-codox             "0.10.2" :scope "test"]
                          [perun                  "0.3.0"  :scope "test"]
                          [hiccup                 "1.0.5"  :scope "test"]
                          [org.clojure/test.check "0.9.0"  :scope "test"]
                          [samestep/boot-refresh  "0.1.0"  :scope "test"]
                          [nightlight             "1.2.1"  :scope "test"]
                          [seancorfield/boot-new  "0.4.7"  :scope "test"]
                          [tolitius/boot-check    "0.1.3"  :scope "test"]

                          [adzerk/bootlaces       "0.1.13" :scope "test"]
                          [adzerk/boot-test       "1.1.2"  :scope "test"]
                          [adzerk/boot-jar2bin    "1.1.0"  :scope "test"]
                          [boot/pod               "2.6.0"  :scope "test"]
                          [boot/aether            "2.6.0"  :scope "test"]
                          [boot/worker            "2.6.0"  :scope "test"]])


(require '[nightlight.boot :refer [nightlight]])
(require '[samestep.boot-refresh :refer [refresh]])
(require '[adzerk.boot-test :refer [test]])
(require '[adzerk.bootlaces :refer :all])
(require '[tolitius.boot-check :as check])
(require '[codox.boot :refer [codox]])
(require '[adzerk.boot-jar2bin :refer :all])
(require '[io.perun :refer :all])
(require '[clj-boot.core :refer :all])

(set-task-options! project prj-name description version scm-url nightlight-port)
