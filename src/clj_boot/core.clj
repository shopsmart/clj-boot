(ns clj-boot.core
  (:refer-clojure :exclude [test])
  (:require [clojure.pprint :refer [pprint]]
            [boot.core :refer :all]
            [boot.util :refer :all]
            [boot.task.built-in :refer :all]

            [codox.boot :refer [codox]]
            [io.perun :refer [markdown render]]
            [samestep.boot-refresh :refer [refresh]]
            [adzerk.boot-test :refer [test]]
            [adzerk.bootlaces :refer :all]
            [tolitius.boot-check :as check]
            [adzerk.boot-jar2bin :refer :all]))


(def project-types #{:open-source :private})

(def project-type (atom :private))

(defn assert-project-type
  "Fails the build if the current project is not of the specified type."
  [type]
  (if-not (= type @project-type)
    (fail (str "This project is of type " @project-type " but must be of type " type " to run this task.\n  Supported project types: " project-types))))


(deftask dev
  "Interactively dev/test/document"
  []
  (comp (repl)
     (watch)
     (refresh)
     (test)
     (speak)))


(deftask lint
  "Reveal uncleanliness in the codebase."
  []
  (comp (check/with-yagni)
     (check/with-eastwood)
     (check/with-kibit)
     (check/with-bikeshed)))


(deftask snapshot
  "Build and release a snapshot."
  []
  (assert-project-type :open-source)
  (comp (test)
     (speak)
     (build-jar)
     (push-snapshot)))


(deftask release-local
  "Build a jar and release it to the local repo."
  []
  (comp (test)
     (speak)
     (build-jar)))


(deftask release
  "Release a Jar.  If project-type is :open-source, pushes to Clojars.  If project-type is :private,
pushes to a configured repository.  If project-type is :private and no respository is configured,
aborts.

For Clojars, depends on CLOJARS_USER, CLOJARS_PASS, CLOJARS_GPG_USER, CLOJARS_GPG_PASS, envars."
  []
  (assert-project-type :open-source)
  (comp (release-local)
     (push-release)))


(deftask uberjar
  "Run tests, and build an uberjar."
  []
  (comp (test)
     (speak)
     (pom)
     (uber)
     (jar)))


(deftask uberbin
  "Run tests, and build a direct-executable, aot'd uberjar."
  []
  (comp (uberjar)
     (bin)))


(defn set-task-options!
  "Set default options for standard tasks."
  [project project-name openness description version scm-url]

  (bootlaces! version)
  (reset! project-type openness)

  (task-options!

   push {:repo "deploy-clojars"
         :gpg-sign true
         :ensure-release true
         :gpg-user-id (System/getenv "CLOJARS_GPG_USER")
         :gpg-passphrase (System/getenv "CLOJARS_GPG_PASS")}

   pom {:project     project
        :description description
        :version     version
        :scm         {:url scm-url}}

   codox {:name project-name
          :description description
          :version     version
          :metadata    {:doc/format :markdown}
          :output-path (str "public/codox/" version)
          :source-uri  (str scm-url "/blob/{version}/{filepath}#L{line}")}))
