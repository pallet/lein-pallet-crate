(ns leiningen.crate-doc
  "A leiningen task to create pallet crate version documentation."
  (:require
   [clojure.string :as string]
   [pallet.crates.readme :refer [readme]]
   [pallet.crates.version-doc :refer [generate]]))

(defn crate-doc
  "Create pallet crate documentation from metadata and source files."
  [{:keys [root] :as project}]
  (when (:root project)
    (let [crate-name (string/replace (:name project) #"-crate" "")]
      (generate (:name project) (keyword crate-name)))
    (readme)))
