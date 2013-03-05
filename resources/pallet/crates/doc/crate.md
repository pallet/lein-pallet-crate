---
title: {{header}}
layout: crate
permalink: /doc/crates/{{crate-name}}
section: documentation
subsection: crates
tags: [{{crate-name}}-crate]
summary: {{title}}
artifact-id: {{artifact-id}}
group-id: {{group-id}}
version: {{version}}
tag-prefix: {{tag-prefix}}
versions:
{{#versions}}
  - {pallet: {{pallet}}, version: {{version}}, artifact: {{artifact}}, mvn-repo: {{repo}}, group-id: {{group-id}}, artifact-id: {{artifact-id}}, source-path: {{source-path}}}
{{/versions}}
git-repo: {{git-repo}}
mvn-repo: {{repo}}
path: {{source-path}}
---

{{title}}
