(ns lein-pallet-crate.plugin
  (:require
   [leiningen.core.project :refer [add-profiles]]))

(def profiles
  {})

(defn resource [project]
  {:resource {:resource-paths ["doc-src"]
              :target-path (str "target/classes/pallet_crate/"
                                (:name project) "/")
              :includes [#"doc-src/USAGE.*"]}})

(def prep-tasks
  {:prep-tasks ["resource" "crate-doc"]})

(defn merge-project
  [project profile]
  (with-meta
    (merge profile project)
    (meta project)))

(defn merge-force-project
  [project profile]
  (with-meta
    (merge project profile)
    (meta project)))

(defn middleware
  "Middleware to add profiles."
  [project]
  (-> project
      (add-profiles profiles)
      (merge-project (resource project))
      (merge-force-project prep-tasks)))
