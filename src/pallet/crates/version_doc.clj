(ns pallet.crates.version-doc
  "Generate version documentation for a pallet crate."
  (:require
   [clojure.java.io :refer [file resource]]
   [clojure.pprint :refer [pprint]]
   [clojure.string :as string]
   [stencil.core :refer [render-file]]))

(defn debug [& args]
  #_(apply println args))

(defn read-edn [f]
  (binding [*read-eval* false]
    (read-string f)))

(def defaults {:mvn-repo :clojars :tag-prefix ""})

(defn template-for
  "Return the resource path for the given template-name."
  [template-name]
  (let [f (str "pallet/crates/doc/" (name template-name) ".md")]
    (when-not (resource f)
      (throw (ex-info
              (str f "doesn't exist on the classpath")
              {:exit-code 1})))
    f))

(defn crate-doc-file [dir crate]
  (debug "crate-doc-file" dir crate)
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
  (debug "Generating doc " crate values)
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
  (doseq [[crate values :as crate-entry] meta]
    (generate-doc [crate values] [[(crate-doc-file output-dir crate) :crate]])))

(defn generate-index [meta output-dir]
  (let [values (->> (for [[crate {:keys [header]}] meta]
                      {:header header :crate-name (name crate)})
                    (sort-by :header))]
    (debug "Generating index" values)
    (spit (crate-index-file output-dir)
          (render-file "pallet/crates/doc/index.md" {:crates values}))))

(defn generate-meta [meta output-path]
  (debug "Generating meta")
  (spit output-path (with-out-str (pprint meta))))

(defn translate-code-blocks
  [s]
  (-> s
      (string/replace #"```clj" "{% highlight clojure %}")
      (string/replace #"```bash" "{% highlight bash %}")
      (string/replace #"```" "{% endhighlight %}")))

(defn combine-meta-resources [resource-paths]
  (reduce
   (fn [m [k v]]
     (update-in m [k] merge v))
   {}
   (remove
    nil?
    (for [resource-path resource-paths
          :let [meta (re-find #"pallet_crate/([a-z_]+)_crate/meta.edn"
                              resource-path)
                usage (re-find #"pallet_crate/([a-z_]+)_crate/USAGE.md"
                               resource-path)
                crate-name (or (second meta) (second usage))
                crate-kw (and crate-name
                              (keyword (string/replace crate-name #"_" "-")))
                _ (debug "resource-path" resource-path "for" crate-kw)]]
      (if crate-name
        (cond
         meta (when-let [path (resource resource-path)]
                (debug "path" path)
                [crate-kw (read-edn (slurp path))])
         usage (when-let [path (resource resource-path)]
                 (debug "path" path)
                 [crate-kw {:usage (translate-code-blocks (slurp path))}]))
        (debug "Ignoring unrecognised resource" resource-path))))))

(defn generate-all [meta-paths output-dir meta-path]
  (debug "meta-paths" meta-paths)
  (let [meta (combine-meta-resources meta-paths)]
    (debug "meta" meta)
    (generate-docs meta output-dir)
    (generate-index meta output-dir)
    (generate-meta meta meta-path)))

(defn generate
  "Generate a version document for the specified crate and output directory"
  [artifact-name crate-kw]
  (let [f (file "resources/pallet_crate"
                (string/replace artifact-name #"-" "_") "meta.edn")
        _ (debug "Generating from file" (.getPath f))
        meta (read-edn (slurp f))]
    (debug "Meta" meta)
    (.mkdir (file "doc-src"))
    (generate-doc [crate-kw meta]
                  [["doc-src/INTRO.md" :intro]
                   ["doc-src/VERSIONS.md" :versions]])))
