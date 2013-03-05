(ns pallet.crates.version-doc
  "Generate version documentation for a pallet crate."
  (:require
   [clojure.java.io :refer [file resource]]
   [clojure.string :as string]
   [leiningen.core.main :refer [abort debug]]
   [stencil.core :refer [render-file]]))

(defn read-edn [f]
  (binding [*read-eval* false]
    (read-string f)))

(def defaults {:mvn-repo :clojars :tag-prefix ""})

(defn template-for
  "Return the resource path for the given template-name."
  [template-name]
  (let [f (str "pallet/crates/doc/" (name template-name) ".md")]
    (when-not (resource f)
      (abort f "doesn't exist on the classpath"))
    f))

(defn crate-doc-file [dir crate]
  (file dir "_posts" (format "2012-01-01-%s.md" (name crate))))

(defn crate-index-file [dir]
  (file dir "index.md"))

(def artifact-id {:clojars "com.palletops" :sonatype "org.cloudhoist"})

(defn version-info
  "Create a version info map for the named crate using the meta map v."
  [crate values {:keys [mvn-repo] :as v}]
  (->
   {:artifact-id (str (name crate) "-crate")
    :source-path (format "src/pallet/crate/%s.clj" (name crate))
    :group-id (artifact-id (or mvn-repo (:mvn-repo values :clojars)))}
   (merge
    (dissoc values :versions)
    v)
   (update-in [:mvn-repo] name)))

(defn generate-doc
  "Generate version document for the crate with the specified values."
  [[crate {:keys [versions] :as values}] outputs]
  (let [values (merge defaults values)
        versions (map (partial version-info crate values) versions)
        default-version (last (sort-by :version versions))
        values (-> values
                   (assoc :crate-name (name crate))
                   (assoc :versions versions)
                   (merge default-version)
                   (assoc :is-sonatype
                     (= :sonatype (:mvn-repo default-version)))
                   (assoc :any-sonatype
                     (some #(= :sonatype (:mvn-repo %)) versions)))]
    (debug "Generating for" crate)
    (debug "Generating from" values)
    (doseq [[output-file template] outputs]
      (spit output-file (render-file (template-for template) values)))))


(defn generate-docs [meta output-dir]
  (.mkdirs (file output-dir "_posts"))
  (doseq [[crate :as crate-entry] meta]
    (generate-doc crate-entry [[(crate-doc-file output-dir crate) :crate]])))

(defn generate-index [meta output-dir]
  (let [values (for [[crate {:keys [header] :as values}] meta]
                 {:header header :crate-name (name crate)})]
    (debug "Generating index" values)
    (spit (crate-index-file output-dir)
          (render-file "pallet/doc/index.md" {:crates values}))))

(defn generate-all [output-dir]
  (let [f (resource "pallet/crate/meta.edn")
        _ (debug "File" f)
        meta (read-edn (slurp f))]
    (debug "Meta" meta)
    (generate-docs meta output-dir)
    (generate-index meta output-dir)))

(defn generate
  "Generate a version document for the specified crate and output directory"
  [artifact-name crate-kw]
  (let [f (file "resources/pallet"
                (string/replace artifact-name #"-" "_") "meta.edn")
        _ (debug "Generating from file" (.getPath f))
        meta (read-edn (slurp f))]
    (debug "Meta" meta)
    (.mkdir (file "doc-src"))
    (generate-doc [crate-kw meta]
                  [["doc-src/INTRO.md" :intro]
                   ["doc-src/VERSIONS.md" :versions]])))
