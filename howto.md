

## make CC attributions
https://wiki.creativecommons.org/wiki/best_practices_for_attribution

## use H2 console to query in-memory DB
http://localhost:8090/h2-console


# TIPS
after adding a file to public, use maven to recompile the project.
 Then it will be included in the .jar
 
## thymeleaf
var in a path: `th:href="@{/parent/{id}(id=${parent.id})}"`
var inlined in text: `<p>Hello, [[${child.name}]]!</p>`


