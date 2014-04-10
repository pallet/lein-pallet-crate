(defproject com.palletops/lein-pallet-crate "0.1.3-SNAPSHOT"
  :description "A lein plugin for managing pallet crates"
  :url "https://github.com/pallet/lein-pallet-crate"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[com.palletops/chiba "0.2.0"]
                 [stencil "0.3.2"]
                 [lein-resource "0.3.2"]]
  :exclusions [org.clojure/clojure]
  :eval-in-leiningen true)
