package com.cookbook.recipes.repository;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static com.cookbook.recipes.util.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RecipeRepositoryTest {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeRepositoryTest(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Test
    void getRecipeById() {
        val recipeActual = recipeRepository.getRecipeById(RECIPE_ID_1);

        recipeActual.ifPresent(recipe -> assertThat(recipe.getId()).isEqualTo(RECIPE_ID_1));
    }

    @Test
    void findByIdIn() {
        val recipeActual = recipeRepository.findByIdIn(Set.of(RECIPE_ID_1, RECIPE_ID_2));

        assertThat(recipeActual.size()).isEqualTo(2);
    }

    @Test
    void findByIdNotIn() {
        val recipeActual = recipeRepository.findByIdNotIn(Set.of(RECIPE_ID_1, RECIPE_ID_2));

        assertThat(recipeActual.size()).isEqualTo(2);
    }

    @Test
    void findByInstructionContainingIgnoreCase() {
        val recipeActual = recipeRepository.findByInstructionContainingIgnoreCase(INSTRUCTION_TEXT);

        assertThat(recipeActual.size()).isEqualTo(2);
    }

    @Test
    void findByIsVegetarian() {
        val recipeActual = recipeRepository.findByIsVegetarian(IS_VEGETARIAN);

        assertThat(recipeActual.size()).isEqualTo(2);
    }

    @Test
    void findByNumberOfServings() {
        val recipeActual = recipeRepository.findByNumberOfServings(NUMBER_OF_SERVINGS);

        assertThat(recipeActual.size()).isEqualTo(1);
    }

    @Test
    void findByNumberOfServingsGreaterThanEqual() {
        val recipeActual = recipeRepository.findByNumberOfServingsGreaterThanEqual(NUMBER_OF_SERVINGS);

        assertThat(recipeActual.size()).isEqualTo(2);
    }

    @Test
    void findByIsVegetarianAndNumberOfServings() {
        val recipeActual = recipeRepository
                .findByIsVegetarianAndNumberOfServings(IS_VEGETARIAN, NUMBER_OF_SERVINGS);

        assertThat(recipeActual.size()).isEqualTo(1);
    }

    @Test
    void findByRecipeIngredientsIngredientNameIn() {
        val recipeActual = recipeRepository
                .findByRecipeIngredientsIngredientNameIn(SET_OF_INGREDIENT_NAMES);

        assertThat(recipeActual.size()).isEqualTo(2);
    }

    @Test
    void findByRecipeIngredientsIngredientNameAndNumberOfServings() {
        val recipeActual = recipeRepository
                .findByRecipeIngredientsIngredientNameInAndNumberOfServings(SET_OF_INGREDIENT_NAMES, NUMBER_OF_SERVINGS);

        assertThat(recipeActual.size()).isEqualTo(1);
    }

    @Test
    void findByInstructionContainingIgnoreCaseAndRecipeIngredientsIngredientNameNot() {
        val recipeActual = recipeRepository
                .findByInstructionContainingIgnoreCaseAndRecipeIngredientsIngredientNameNotIn(
                        INSTRUCTION_TEXT, SET_OF_INGREDIENT_NAMES);

        assertThat(recipeActual.size()).isEqualTo(2);
    }
}