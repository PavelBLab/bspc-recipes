package com.cookbook.recipes.web.operations;

import com.cookbook.recipes.domain.services.RecipeService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.cookbook.recipes.util.TestDataFactory.*;
import static org.mockito.ArgumentMatchers.any;

class RecipesOperationTest extends BaseIntegrationTest {

    @MockBean
    private RecipeService recipeService;

    @Test
    @SneakyThrows
    void getAllRecipes() {
        Mockito.when(recipeService.getAllRecipes()).thenReturn(List.of(getGeneratedRecipe()));
        RestAssuredMockMvc
                .given()
                .when()
                .get(getUrl())
                .peek()
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("generated_recipes.size()", Matchers.equalTo(1))
                .body("generated_recipes.get(0).name", Matchers.equalTo(NAME))
                .body("generated_recipes.get(0).description", Matchers.equalTo(DESCRIPTION))
                .body("generated_recipes.get(0).recipeIngredients.size()",
                        Matchers.equalTo(1))
                .body("generated_recipes.get(0).recipeIngredients.get(0).recipe_ingredient_id",
                        Matchers.equalTo(RECIPE_INGREDIENT_ID_INT))
                .body("generated_recipes.get(0).recipeIngredients.get(0).ingredient.ingredient_id",
                        Matchers.equalTo(INGREDIENT_ID_INT));
    }

    @Test
    void getRecipeById() {
        Mockito.when(recipeService.getRecipeById(RECIPE_ID_1)).thenReturn(getGeneratedRecipe());
        RestAssuredMockMvc
                .given()
                .when()
                .get(getUrl() + RECIPE_ID_1)
                .peek()
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("name", Matchers.equalTo(NAME))
                .body("description", Matchers.equalTo(DESCRIPTION))
                .body("recipeIngredients.size()",
                        Matchers.equalTo(1))
                .body("recipeIngredients.get(0).recipe_ingredient_id",
                        Matchers.equalTo(RECIPE_INGREDIENT_ID_INT))
                .body("recipeIngredients.get(0).ingredient.ingredient_id",
                        Matchers.equalTo(INGREDIENT_ID_INT));
    }

    @Test
    void postRecipesBySearchFilter() {
        Mockito.when(recipeService.postRecipesBySearchFilter(any()))
                .thenReturn(List.of(getGeneratedRecipe()));
        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .and()
                .body(getGeneratedRecipe())
                .when()
                .post(getUrl() + "filter")
                .peek()
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .and()
                .body("generated_recipes.get(0).name", Matchers.equalTo(NAME))
                .body("generated_recipes.get(0).description", Matchers.equalTo(DESCRIPTION))
                .body("generated_recipes.get(0).recipeIngredients.size()",
                        Matchers.equalTo(1))
                .body("generated_recipes.get(0).recipeIngredients.get(0).recipe_ingredient_id",
                        Matchers.equalTo(RECIPE_INGREDIENT_ID_INT))
                .body("generated_recipes.get(0).recipeIngredients.get(0).ingredient.ingredient_id",
                        Matchers.equalTo(INGREDIENT_ID_INT));
    }

    @Test
    void createRecipe() {
        Mockito.when(recipeService.createRecipe(any())).thenReturn(RECIPE_ID_1.toString());
        RestAssuredMockMvc
                .given()
                .header(getCustomerHeader(RECIPE_ID_1.toString()))
                .contentType("application/json")
                .and()
                .body(getGeneratedRecipe())
                .when()
                .post(getUrl())
                .peek()
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .and()
                .header("Location", Matchers.notNullValue());
    }

    @Test
    @SneakyThrows
    void updateRecipe() {
        Mockito.doNothing().when(recipeService).updateRecipe(RECIPE_ID_1, getGeneratedRecipe());
        RestAssuredMockMvc
                .given()
                .header(getCustomerHeader(RECIPE_ID_1.toString()))
                .contentType("application/json")
                .and()
                .body(getGeneratedRecipe())
                .when()
                .put(getUrl() + RECIPE_ID_1)
                .peek()
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deleteRecipe() {
        Mockito.doNothing().when(recipeService).deleteRecipe(RECIPE_ID_1);
        RestAssuredMockMvc
                .given()
                .header(getCustomerHeader(RECIPE_ID_1.toString()))
                .contentType("application/json")
                .and()
                .body(getGeneratedRecipe())
                .when()
                .delete(getUrl() + RECIPE_ID_1)
                .peek()
                .then()
                .assertThat()
                .statusCode(HttpStatus.ACCEPTED.value());
    }

//    private String getUrl(String filterPath, String queryParameter, String queryValue) {
//        return String.format("recipes/%s?%s=%s", filterPath, queryParameter, queryValue);
//    }

    private String getUrl() {
        return "/recipes/";
    }
}