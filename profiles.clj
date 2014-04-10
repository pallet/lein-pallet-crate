{:dev {:plugins [[lein-pallet-release "RELEASE"]
                 [lein-marginalia "0.7.1"]
                 [codox/codox.leiningen "0.6.4"]]
       :pallet-release
       {:url "https://pbors:${GH_TOKEN}@github.com/pallet/lein-pallet-crate.git",
        :branch "master"}}
 :doc {:dependencies [[codox-md "0.2.0"]]
       :codox {:writer codox-md.writer/write-docs
               :output-dir "doc/0.1/api"
               :src-dir-uri "https://github.com/pallet/lein-pallet-crate/blob/develop"
               :src-linenum-anchor-prefix "L"}
       :aliases {"marg" ["marg" "-d" "doc/0.1/annotated/"]
                 "codox" ["doc"]
                 "doc" ["do" "codox," "marg"]}}
 :release {:set-version
           {:updates
            [{:path "README.md",:no-snapshot true}
             {:path "src/leiningen/crates_pages.clj"
              :search-regex
              #"lein-pallet-crate \"\d+\.\d+\.\d+(-SNAPSHOT)?\""}]}}}
