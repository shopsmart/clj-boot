(def project  'bradsdeals/clj-boot)
(def version  "0.1.8")
(def project-name "clj-boot")
(def project-openness :open-source)

(def description "Standard Brad's Deals boot build tasks")
(def scm-url "https://github.com/shopsmart/clj-boot")

(def test-sources "test")
(def test-resources nil)


(set-env! :resource-paths #{"resources"}
          :source-paths   #{"src"}

          :repositories #(conj % ["clojars-push" {:url "https://clojars.org/repo/"
                                                  :username (System/getenv "CLOJARS_USER")
                                                  :password (System/getenv "CLOJARS_PASS")}])

          :dependencies '[[org.clojure/clojure   "1.8.0"]
                          [clojure-future-spec   "1.9.0-alpha14"]

                          ; Boot tasks
                          [boot-codox             "0.10.2"]
                          [perun                  "0.3.0"]
                          [hiccup                 "1.0.5"]
                          [org.clojure/test.check "0.9.0"]
                          [samestep/boot-refresh  "0.1.0"]
                          [seancorfield/boot-new  "0.4.7"]
                          [tolitius/boot-check    "0.1.3"]

                          [adzerk/bootlaces       "0.1.13"]
                          [adzerk/boot-test       "1.1.2"]
                          [adzerk/boot-jar2bin    "1.1.0"]
                          [boot/pod               "2.6.0"]
                          [boot/aether            "2.6.0"]
                          [boot/worker            "2.6.0"]])


(require '[samestep.boot-refresh :refer [refresh]])
(require '[adzerk.boot-test :refer [test]])
(require '[adzerk.bootlaces :refer :all])
(require '[tolitius.boot-check :as check])
(require '[codox.boot :refer [codox]])
(require '[adzerk.boot-jar2bin :refer :all])
(require '[io.perun :refer :all])
(require '[clj-boot.core :refer :all])

(set-task-options! project project-name project-openness description version scm-url test-sources test-resources)
