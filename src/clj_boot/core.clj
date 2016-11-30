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

(def project-type (ref :private))


(deftask assert-project-type
  "Fails the build if the current project is not a legal type.  Legal types are members of the
project-types set.  In addition, may test that the current project is exactly a single type via
the 'expect' parameter."
  [e expect PROJECT-TYPE kw "The expected project type"]
  (fn middleware [next-handler]
    (fn handler [fileset]
      (if-not (project-types @project-type)
        (throw (ex-info (str "This project is " @project-type " but must be one of " project-types)
                        {})))
      (if-not (= expect @project-type)
        (throw (ex-info (str "This project is " @project-type " but must be " expect
                             ".\nto perform this operation.\n  Supported project types: " project-types)
                        {})))
      (next-handler fileset))))


(deftask test-with-settings
  "Run (test) with the specified settings added to the environment.  Restores the original environment
after running tests."
  [s sources PATH str "The directory where test source code is located."
   r resources PATH str "The directory where testing resources are located."]
  (let [test-middleware (test)
        test-handler (test-middleware identity)]
    (fn middleware [next-handler]
      (fn handler [fileset]
        (let [env (get-env)]
          (when sources
            (set-env! :source-paths #(conj % sources)))
          (when resources
            (set-env! :resource-paths #(conj % resources)))
          (test-handler fileset)
          (set-env! env)
          (next-handler fileset))))))


(deftask dev
  "Interactively dev/test/document"
  []
  (comp (repl)
     (watch)
     (refresh)
     (test-with-settings)
     (speak)))


(deftask lint
  "Reveal uncleanliness in the codebase."
  []
  (comp (check/with-yagni)
     (check/with-eastwood)
     (check/with-kibit)
     (check/with-bikeshed)))


(deftask release-local
  "Build a jar and release it to the local repo."
  []
  (comp (test-with-settings)
     (speak)
     (build-jar)
     (target)))


(deftask snapshot
  "Build and release a snapshot."
  []
  (comp (assert-project-type :expect :open-source)
     (release-local)
     (push-snapshot)))


(deftask release
  "Release a Jar.  If project-type is :open-source, pushes to Clojars.  If project-type is :private,
pushes to a configured repository.  If project-type is :private and no respository is configured,
aborts.

For Clojars, depends on CLOJARS_USER, CLOJARS_PASS, CLOJARS_GPG_USER, CLOJARS_GPG_PASS, envars."
  []
  (comp (assert-project-type :expect :open-source)
     (release-local)
     (push-release)))


(deftask uberjar
  "Run tests, and build an uberjar."
  []
  (comp (test-with-settings)
     (speak)
     (pom)
     (uber)
     (jar)
     (target)))


(deftask uberbin
  "Run tests, and build a direct-executable, aot'd uberjar."
  []
  (comp (uberjar)
     (bin)))


(defn set-task-options!
  "Set default options for standard tasks."
  [project project-name openness description version scm-url test-sources test-resources]

  (bootlaces! version)
  (dosync (ref-set project-type openness))

  (task-options!

   test-with-settings {:sources test-sources
                       :resources test-resources}

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
