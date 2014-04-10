# lein-pallet-crate

A lein plugin for managing pallet crates.

## Install

Add the following to your `:plugins` in `~/.lein/profiles.clj`

    :plugins [[com.palletops/lein-pallet-crate "0.1.2"]]

## Usage

To generate the `README.md` documentation of a crate, based on the metadata in
`resources/pallet/<crate_artifact_id>/meta.edn` and usage documentation in
`doc-src/USAGE.md`.

    lein crate-doc

To generate a documentation site for all crate dependencies of a project:

    lein crates-pages

This will generate jekyll markdown pages in `target/site`.

## License

Copyright © 2013 Hugo Duncan

Distributed under the Eclipse Public License.
