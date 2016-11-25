(ns clj-boot.core
  (:refer-clojure :exclude [test])
  (:require [clojure.pprint :refer [pprint]]
            [boot.core :refer :all]
            [boot.util :refer :all]
            [boot.task.built-in :refer :all]
            [clj-boot.docs :as docs]
            [clj-boot.string :refer [delimeted-words]]

            [codox.boot :refer [codox]]
            [io.perun :refer [markdown render]]
            [nightlight.boot :refer [nightlight]]
            [samestep.boot-refresh :refer [refresh]]
            [adzerk.boot-test :refer [test]]
            [adzerk.bootlaces :refer :all]
            [tolitius.boot-check :as check]
            [adzerk.boot-jar2bin :refer :all]))


(deftask dev
  "Interactively dev/test/document"
  []
  (comp (nightlight)
     (repl)
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
  (comp (test)
     (speak)
     (build-jar)
     (push-snapshot)))


(deftask cmd
  "Run a shell command"
  [r run COMMAND str "The shell command to run."]
  (let [args (delimeted-words run)]
    (with-pre-wrap fileset
      (pprint `(apply dosh ~args))
      (apply dosh args)
      fileset)))


(deftask release-docs
  "Push updated documentation to gh-pages.  See https://gist.github.com/cobyism/4730490"
  [v version VERSION str "The current project version"]
  (comp (markdown)
     (render :renderer 'clj-boot.docs/renderer)
     (codox)
     (target)
     #_(cmd :run (str "git add site/codox/" version))
     #_(cmd :run (str "git stage site/index.html" version))
     #_(cmd :run (str "git commit -a -m 'Added documentation for version " version "'"))
     #_(cmd :run "git subtree push --prefix site origin gh-pages")))


(deftask release-local
  "Build a jar and release it to the local repo."
  []
  (comp (test)
     (speak)
     (build-jar)))


(deftask release-clojars
  "Release a Jar to Clojars.  Depends on CLOJARS_USER, CLOJARS_PASS, CLOJARS_GPG_USER, CLOJARS_GPG_PASS,
envars."
  []
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
  "Set "
  [project prj-name description version scm-url nightlight-port]

  (bootlaces! version)

  (task-options!
   release-docs {:version version}

   nightlight {:port nightlight-port}

   push {:repo "deploy-clojars"
         :gpg-sign true
         :ensure-release true
         :gpg-user-id (System/getenv "CLOJARS_GPG_USER")
         :gpg-passphrase (System/getenv "CLOJARS_GPG_PASS")}

   pom {:project     project
        :description description
        :version     version
        :scm         {:url scm-url}}

   codox {:name prj-name
          :description description
          :version     version
          :metadata    {:doc/format :markdown}
          :output-path (str "public/codox/" version)
          :source-uri  (str scm-url "/blob/{version}/{filepath}#L{line}")}))
