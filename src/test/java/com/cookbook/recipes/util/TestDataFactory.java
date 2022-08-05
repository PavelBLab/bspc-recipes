package com.cookbook.recipes.util;

import com.cookbook.recipes.model.*;

import java.time.LocalDate;
import java.util.Set;

public class TestDataFactory {

    public static final Long RECIPE_ID_1 = 1L;
    public static final Long RECIPE_ID_2 = 2L;
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE = "image";
    public static final String INSTRUCTION = "Add eggs, flour, to a pan. Bake at 200 for 1 hour";
    public static final String NON_SANITIZED_INSTRUCTION = "Add eggs, &&<>flour, to a pan. Bake at 200 for 1 hour";
    public static final String INSTRUCTION_TEXT = "Bake at 200 FOR";
    public static final LocalDate CREATED_BY = LocalDate.of(2022, 7, 21);
    public static final Boolean IS_VEGETARIAN = true;
    public static final Integer NUMBER_OF_SERVINGS = 3;

    public static final Long RECIPE_INGREDIENT_ID_LONG = 1L;
    public static final Integer RECIPE_INGREDIENT_ID_INT = 1;
    public static final Integer AMOUNT = 100;
    public static final Measure MEASURE = Measure.GRAM;
    public static final GeneratedMeasure GENERATED_MEASURE = GeneratedMeasure.GRAM;

    public static final Long INGREDIENT_ID_LONG = 1L;
    public static final Integer INGREDIENT_ID_INT = 1;
    public static final String INGREDIENT_NAME = "salmon";
    public static final Set<String> SET_OF_INGREDIENT_NAMES = Set.of("egg", "sugar");

    public static SearchFilter getSearchFilter(
            final FilterCriteria filterCriteria,
            final FilterValues filterValues) {
        return SearchFilter.builder()
                .filterCriteria(filterCriteria)
                .filterValues(filterValues)
                .build();
    }

    public static GeneratedSearchFilter getGeneratedSearchFilter(
            final GeneratedFilterCriteria generatedFilterCriteria,
            final GeneratedFilterValues generatedFilterValues) {
        return GeneratedSearchFilter.builder()
                .filterCriteria(generatedFilterCriteria)
                .filterValues(generatedFilterValues)
                .build();
    }

    public static Recipe getRecipe() {
        return Recipe.builder()
                .id(RECIPE_ID_1)
                .name(NAME)
                .description(DESCRIPTION)
                .image(IMAGE)
                .instruction(INSTRUCTION)
                .createdAt(CREATED_BY)
                .isVegetarian(IS_VEGETARIAN)
                .numberOfServings(NUMBER_OF_SERVINGS)
                .recipeIngredients(null)
                .build();
    }

    public static RecipeIngredient getRecipeIngredient() {
        return
                RecipeIngredient.builder()
                        .id(RECIPE_INGREDIENT_ID_LONG)
                        .recipe(Recipe.builder()
                                .id(RECIPE_ID_1)
                                .name(NAME)
                                .description(DESCRIPTION)
                                .image(IMAGE)
                                .instruction(INSTRUCTION)
                                .createdAt(CREATED_BY)
                                .isVegetarian(IS_VEGETARIAN)
                                .numberOfServings(NUMBER_OF_SERVINGS)
                                .build())
                        .ingredient(getIngredient())
                        .amount(AMOUNT)
                        .measure(MEASURE)
                        .build();
    }

    public static Ingredient getIngredient() {
        return Ingredient.builder()
                .id(INGREDIENT_ID_LONG)
                .name(INGREDIENT_NAME)
                .build();
    }

    public static GeneratedRecipe getGeneratedRecipe() {
        return GeneratedRecipe.builder()
                .recipeId(RECIPE_ID_1)
                .name(NAME)
                .description(DESCRIPTION)
                .image(IMAGE)
                .instruction(INSTRUCTION)
                .createdAt(CREATED_BY)
                .isVegetarian(IS_VEGETARIAN)
                .numberOfServings(NUMBER_OF_SERVINGS)
                .recipeIngredients(Set.of(getGeneratedRecipeIngredient()))
                .build();
    }

    public static GeneratedRecipeIngredient getGeneratedRecipeIngredient() {
        return
                GeneratedRecipeIngredient.builder()
                        .recipeIngredientId(RECIPE_INGREDIENT_ID_LONG)
                        .ingredient(getGeneratedIngredient())
                        .recipeId(RECIPE_ID_1)
                        .amount(AMOUNT)
                        .measure(GENERATED_MEASURE)
                        .build();
    }

    public static GeneratedIngredient getGeneratedIngredient() {
        return GeneratedIngredient.builder()
                .ingredientId(INGREDIENT_ID_LONG)
                .name(INGREDIENT_NAME)
                .build();
    }

}
