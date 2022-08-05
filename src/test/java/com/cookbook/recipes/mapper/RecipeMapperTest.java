package com.cookbook.recipes.mapper;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.cookbook.recipes.util.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

class RecipeMapperTest {

    private final RecipeMapper recipeMapper = new RecipeMapperImpl();

    @Test
    void toGeneratedRecipe() {
        val recipe = getRecipe();
        recipe.setRecipeIngredients(Set.of(getRecipeIngredient()));
        val generatedRecipeExpected = getGeneratedRecipe();

        val generatedRecipeActual = recipeMapper.toGeneratedRecipe(recipe);

        assertThat(generatedRecipeActual).usingRecursiveComparison()
                .ignoringCollectionOrder().isEqualTo(generatedRecipeExpected);
    }

    @Test
    void toGeneratedRecipe_recipeIsNull() {
        val generatedRecipeActual = recipeMapper.toGeneratedRecipe(null);

        assertThat(generatedRecipeActual).isNull();
    }

    @Test
    void toGeneratedRecipeIngredients_recipeIngredientsIsNull() {
        val generatedRecipeIngredientsActual = recipeMapper.mapGeneratedRecipeIngredients(null);

        assertThat(generatedRecipeIngredientsActual).isNull();
    }

    @Test
    void toGeneratedRecipeIngredient_recipeIngredientIsNull() {
        val generatedRecipeIngredientActual = recipeMapper.mapGeneratedRecipeIngredient(null);

        assertThat(generatedRecipeIngredientActual).isNull();
    }

    @Test
    void mapGeneratedIngredient_ingredientIsNull() {
        val generatedIngredientActual = recipeMapper.mapGeneratedIngredient(null);

        assertThat(generatedIngredientActual).isNull();
    }

    @Test
    void toRecipe() {
        val generatedRecipe = getGeneratedRecipe();
        val recipeExpected = getRecipe();
        recipeExpected.setRecipeIngredients(null);

        val recipeActual = recipeMapper.toRecipe(generatedRecipe);

        assertThat(recipeActual).usingRecursiveComparison().isEqualTo(recipeExpected);
    }

    @Test
    void toRecipe_sanitizeInstruction() {
        val generatedRecipe = getGeneratedRecipe();
        generatedRecipe.setInstruction(NON_SANITIZED_INSTRUCTION);

        val recipeActual = recipeMapper.toRecipe(generatedRecipe);

        assertThat(recipeActual.getInstruction()).isEqualTo(INSTRUCTION);
    }

    @Test
    void toRecipe_generatedRecipeIsNull() {
        val recipeActual = recipeMapper.toRecipe(null);

        assertThat(recipeActual).isNull();
    }

    @Test
    void mapRecipeIngredient() {
        val generatedRecipeIngredient = getGeneratedRecipeIngredient();
        val recipeIngredientExpected = getRecipeIngredient();
        recipeIngredientExpected.setRecipe(null);

        val recipeIngredientActual = recipeMapper.mapRecipeIngredient(generatedRecipeIngredient);

        assertThat(recipeIngredientActual).usingRecursiveComparison().isEqualTo(recipeIngredientExpected);
    }

    @Test
    void mapRecipeIngredient_generatedRecipeIngredientIsNull() {
        val recipeIngredientActual = recipeMapper.mapRecipeIngredient(null);

        assertThat(recipeIngredientActual).isNull();
    }

    @Test
    void mapIngredient_generatedIngredientIsNull() {
        val ingredientActual = recipeMapper.mapIngredient(null);

        assertThat(ingredientActual).isNull();
    }

    @Test
    void sanitizeInvalidCharacters() {
        val sanitizedInstructionActual = recipeMapper.sanitizeInvalidCharacters(NON_SANITIZED_INSTRUCTION);
        assertThat(sanitizedInstructionActual).isEqualTo(INSTRUCTION);
    }
}