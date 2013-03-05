(ns pallet.crates.readme
  "Create a readme from document fragments"
  (:require
   [clojure.java.io :refer [file]]
   [clojure.string :as string]))

(def fragments ["INTRO.md" "VERSIONS.md" "USAGE.md" "FOOTER.md"])

(defn readme
  []
  (spit "README.md"
        (string/join \newline (map #(slurp (file "doc-src" %)) fragments))))
