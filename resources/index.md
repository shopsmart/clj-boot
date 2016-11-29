---
name: clj-boot
description: A "batteries included" build for Clojure based on Boot.
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

* [![Clojars Project](https://img.shields.io/clojars/v/bradsdeals/clj-boot.svg)](https://clojars.org/bradsdeals/clj-boot)

View your version's documentation for detailed usage instructions.

* Version [0.1.5](codox/0.1.5/index.html)


## Known tech debt / tasks

* Register a Markdown plugin to generate the documentation version list
* Copy the generated documentation out of target, into someplace else ("documentation?") where the main page overwrites the previous main page and the new version's documentation is merged with the set of prior versions' documentation
* Enable push to gh_pages from the publish tasks
* Document how to set up gpg, what to put inside bootx
* "Getting Started" document covering the vars at the top of build.boot and the ```(set-task-options!)``` command
