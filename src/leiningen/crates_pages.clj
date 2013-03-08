(ns leiningen.crates-pages
  "A leiningen task to create pallet crates pages for the web site."
  (:require
   [clojure.java.io :refer [file]]
   [clojure.string :as string]
   [leiningen.core.classpath :refer [resolve-dependencies]]
   [leiningen.core.eval :refer [eval-in]]
   [leiningen.core.main :refer [debug]]
   [leiningen.core.project :refer [merge-profiles]]
   [pallet.crates.version-doc :refer [generate-all]]))

(def self-profile
  {:dependencies [['com.palletops/lein-pallet-crate "0.1.0-SNAPSHOT"]]})

(defn chiba-list-form
  "Creates a form to list files matching a given prefix"
  [prefix target-path meta-path]
  [`(~'generate-all
     (~'data-plugins ~prefix)
     ~target-path
     ~meta-path)
   `(require '[chiba.plugin :refer [~'data-plugins]]
             '[pallet.crates.version-doc :refer [~'generate-all]])])

(defn crates-pages
  "Create pallet crate documentation from metadata and source files from your
project's dependencies."
  [{:keys [root] :as project}]
  (when (:root project)
    (let [project (merge-profiles project [self-profile])
          meta-path (file
                     (:target-path project)
                     "metadata" "pallet_crate" "crates.edn")]
      (.mkdirs (.getParentFile meta-path))
      (.mkdirs (file (:compile-path project "/tmp")))
      (resolve-dependencies :dependencies project)

      (let [[form requires] (chiba-list-form
                             "pallet_crate/"
                             (.getPath (file (:target-path project) "site"))
                             (.getPath meta-path))]
        (eval-in
         (assoc project :eval-in :classloader)
         `(do
            ~requires
            ~form)))
      nil)))
