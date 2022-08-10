# bspc-recipes

Service to manage recipes

## Run Maven
In terminal run
> mvn clean install

## Start develop environment

### 1. Run from terminal
```shell
java -jar target/bspc-recipes-0.0.1-SNAPSHOT.jar --spring.profiles.active='dev'
```
or
```shell
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev
```
### 2. Enable embedded GUI console for H2 database (Develop environment)
In a browser:
* For develop environment
    * http://localhost:8082/api/h2-console
    * Url: jdbc:h2:mem:test-db
    * Root username: dev and password: dev

### 3. Swagger UI
Swagger UI is available:
* Copy below link to a browser
```shell
http://localhost:8082/api/swagger-ui/index.html
```
* or copy recipes-api.yaml from resources/api folder to
```shell
https://editor.swagger.io/
```

## Start production environment

### 1. Run from terminal
```shell
java -jar target/bspc-recipes-0.0.1-SNAPSHOT.jar --spring.profiles.active='prod'
```
or
```shell
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=prod
```

### 2. Enable embedded GUI console for H2 database (Production environment)
* For production environment
    * http://localhost:9090/v1/h2-console
    * Url: jdbc:h2:mem:prod-db
    * Root username: prod and password: prod

### 3. Swagger UI
Swagger UI is available:
* Copy below link to a browser
```shell
http://localhost:9090/v1/swagger-ui/index.html
```
* or copy recipes-api.yaml from resources/api folder to
```shell
https://editor.swagger.io/
```

## SQL command to represent all data
 
```shell
SELECT 
r.id, r.names, r.descriptions, r.instructions, 
r.images, r.is_vegetarian, r.created_at, 
r.number_of_servings, i.names,ri.amount, ri.measures
FROM recipes r
INNER JOIN recipe_ingredients ri
ON r.id = ri.recipe_id
INNER JOIN ingredients i
ON i.id = ri.ingredient_id;
```








