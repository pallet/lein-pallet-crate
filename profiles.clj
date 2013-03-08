{:provided {:dependencies [[leiningen "2.0.0"]]}
 :dev {:plugins [[lein-set-version "0.3.0"]
                 [lein-marginalia "0.7.1"]
                 [codox/codox.leiningen "0.6.4"]]}
 :doc {:dependencies [[codox-md "0.2.0"]]
       :codox {:writer codox-md.writer/write-docs
               :output-dir "doc/0.1/api"
               :src-dir-uri "https://github.com/pallet/lein-pallet-crate/blob/develop"
               :src-linenum-anchor-prefix "L"}
       :aliases {"marg" ["marg" "-d" "doc/0.1/annotated/"]
                 "codox" ["doc"]
                 "doc" ["do" "codox," "marg"]}}
 :release
 {:set-version
  {:updates [{:path "README.md" :no-snapshot true}]}}}
