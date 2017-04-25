---
name: clj-boot
description: Batteries included Clojure builds based on Boot.
---
## Benefits

* Common build tasks are maintained in a single place, reducing repetition in your build files.  (Either clone this repository and customize it for your site or use it as-is from Clojars.)
* Automatically generate documentation using Codox, supplement it using Markdown, and publish the results to gh_pages.
* ```boot dev``` automatically rebuilds, retests, and reloads the REPL whenever you save.
* Deploy to Clojars is preconfigured.  Just add your credentials.  See the documentation for details.
* Standard high-level tasks for common operations like running tests, then building a JAR or Uberjar.

clj-boot is built and deployed using itself.


## Install / use

Add the latest version to your build.boot dependencies:

* [![Clojars Project](https://img.shields.io/clojars/v/coconutpalm/clj-boot.svg)](https://clojars.org/coconutpalm/clj-boot)

View your version's documentation for detailed usage instructions.

* Version [0.2.2](codox/0.2.2/index.html)


## Future

* Automatically publish documentation
   * Register a Markdown plugin to generate the documentation version list
   * Copy the generated documentation out of target, into someplace else ("documentation?") where the main page overwrites the previous main page and the new version's documentation is merged with the set of prior versions' documentation
   * Enable push to gh_pages from the publish tasks for project-type :open-source; push docs to S3 for project-type :private
* Document how to set up gpg, what to put inside bootx
* "Getting Started" document covering the vars at the top of build.boot and the ```(set-task-options!)``` command
* ```boot new``` template for clj-boot projects
* Generate OSGi metadata / support OSGi runtime
* Deploy somewhere other than Clojars for :private projects
