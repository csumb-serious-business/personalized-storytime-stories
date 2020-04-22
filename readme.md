
# bootstrapping
create the serious-stories database by running `mysql-create.sql`

add file containing your MySQL credentials in:
`src/main/resources/secret.properties`

```
# mysql username & password should be in secret.properties as:
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

run the data loading scripts

# build, test & run
building the application: `mvn clean compile`

checking code coverage: `mvn test jacoco:report` 
after generation, coverage report will be located at: `target/site/jacoco/index.html`

run the application: `mvn spring-boot:run`

the application should then be accessable at: [localhost:8090/](http://localhost:8090/)

---
# Attributions
[Font Awesome Icons](https://github.com/FortAwesome/Font-Awesome) by [Fonticons, Inc.](https://fontawesome.com/) is licensed under [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/)

