(ns leiningen.crates-pages
  "A leiningen task to create pallet crates pages for the web site."
  (:require
   [clojure.java.io :refer [file]]
   [clojure.string :as string]
   [leiningen.core.main :refer [debug]]
   [leiningen.core.eval :refer [eval-in-project]]
   [leiningen.core.project :refer [merge-profiles]]
   [pallet.crates.version-doc :refer [generate-all]]))

(def self-profile '{:dependencies
                    [[com.palletops/lein-pallet-crate "0.1.0-SNAPSHOT"]]})

(defn chiba-list-form
  "Creates a form to list files matching a given prefix"
  [prefix target-path]
  [`(~'generate-all
     (~'data-plugins ~prefix)
     ~target-path)
   `(require '[chiba.plugin :refer [~'data-plugins]]
             '[pallet.crates.version-doc :refer [~'generate-all]])])

(defn crates-pages
  "Create pallet crate documentation from metadata and source files from your
project's dependencies."
  [{:keys [root] :as project}]
  (when (:root project)
    (let [project (merge-profiles project [self-profile])
          [form requires] (chiba-list-form
                           "pallet_crate/"
                           (.getPath (file (:target-path project) "site")))]
      (eval-in-project
       (assoc project :eval-in :classloader)
       form
       requires)
      nil)))
