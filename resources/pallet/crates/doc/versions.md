### Dependency Information

```clj
:dependencies [[{{group-id}}/{{artifact-id}} "{{version}}"]]
{{#is-sonatype}}:repositories
  {"sonatype"
   {:url "https://oss.sonatype.org/content/repositories/releases/"}}
{{/is-sonatype}}
```

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
