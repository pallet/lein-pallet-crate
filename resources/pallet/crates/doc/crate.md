---
title: {{header}}
layout: crate-ref
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

[Repository]({{git-repo}}) &#xb7;
[Issues]({{git-repo}}/issues) &#xb7;
[API docs]({{api-docs}}) &#xb7;
[Annotated source]({{annotated-docs}}) &#xb7;
[Release Notes]({{git-repo}}/blob/develop/ReleaseNotes.md)

{{title}}

{{{usage}}}

### Dependency Information

{% highlight clojure %}
:dependencies [[{{group-id}}/{{artifact-id}} "{{version}}"]]
{{#is-sonatype}}:repositories
  {"sonatype"
   {:url "https://oss.sonatype.org/content/repositories/releases/"}}
{{/is-sonatype}}
{% endhighlight %}

### Releases

<table>
<thead>
  <tr><th>Pallet</th><th>Crate Version</th><th>Repo</th><th>GroupId</th></tr>
</thead>
<tbody>
{{#versions}}
  <tr>
    <th>{{pallet}}</th>
    <td>{{version}}</td>
    <td>{{mvn-repo}}</td>
    <td>{{group-id}}</td>
    <td><a href='{{git-repo}}/blob/{{tag-prefix}}{{version}}/ReleaseNotes.md'>Release Notes</a></td>
    <td><a href='{{git-repo}}/blob/{{tag-prefix}}{{version}}/{{path}}'>Source</a></td>
  </tr>
{{/versions}}
</tbody>
</table>
{{#any-sonatype}}

For sonatype (versions 0.7.x and earlier) you will need to specify the sonatype
repository.

<pre>
:repositories
  {"sonatype"
   {:url "https://oss.sonatype.org/content/repositories/releases/"}}
</pre>
{{/any-sonatype}}
