(ns lein-pallet-crate.plugin
  (:require
   [leiningen.core.project :as project]))

(defn deep-merge
  "Recursively merge maps."
  [& ms]
  (letfn [(f [a b]
            (if (and (map? a) (map? b))
              (deep-merge a b)
              b))]
    (apply merge-with f ms)))

(defn add-profiles
  "A wrapper to fix bug in l.c.p/add-profiles."
  [project profiles]
  (-> project
      (project/add-profiles profiles)
      (vary-meta update-in [:profiles] deep-merge profiles)))

(defn merge-project
  "Merge something that looks like a profile into the project."
  [project profile]
  (->
   (merge project profile)
   (with-meta (-> (meta project)
                  (update-in [:without-profiles] merge profile)))))
(defn resources
  "Resources to add to the project.  We want this to be run whichever
  task is called (may be overridden by task specific profiles,
  e.g. test)."
  [project]
  {:resource {:resource-paths ["doc-src"]
              :target-path (str "target/classes/pallet_crate/"
                                (:name project) "/")
              :includes [#"doc-src/USAGE.*"]}
   :prep-tasks ["resource" "crate-doc"]})

(defn profiles
  "Profiles to add to the project.  These profiles are not activated
  by default."
  [project]
  {:test {:prep-tasks ^:replace []}
   :resources (resources project)})

(defn middleware
  "Middleware to add profiles."
  [project]
  (let [profiles (deep-merge (-> project meta :profiles) (profiles project))]
    (-> project
        (add-profiles profiles)
        ;; merge conditionally, so when called by e.g. test, the :test
        ;; profile doesn't get clobbered.
        (cond->
         (= ["javac" "compile"] (:prep-tasks project))
         (merge-project (resources project)))

        (vary-meta update-in [:included-profiles] conj :resources))))
