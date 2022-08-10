package com.cookbook.recipes.mapper;

import com.cookbook.recipes.mapper.utils.TransformHelper;
import com.cookbook.recipes.model.*;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    @Mapping(target = "recipeId", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "image", source = "image")
    @Mapping(target = "instruction", source = "instruction")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "isVegetarian", source = "isVegetarian")
    @Mapping(target = "recipeIngredients", source = "recipeIngredients", qualifiedByName = "mapGeneratedRecipeIngredients")
    GeneratedRecipe toGeneratedRecipe(final Recipe recipe);

    @Named("mapGeneratedRecipeIngredients")
    @IterableMapping(qualifiedByName = "mapGeneratedRecipeIngredient")
    Set<GeneratedRecipeIngredient> mapGeneratedRecipeIngredients(final Set<RecipeIngredient> recipeIngredients);

    @Named("mapGeneratedRecipeIngredient")
    @Mapping(target = "recipeIngredientId", source = "id")
    @Mapping(target = "recipeId", source = "recipe.id")
    @Mapping(target = "ingredient", source = "ingredient", qualifiedByName = "mapGeneratedIngredient")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "measure", source = "measure")
    GeneratedRecipeIngredient mapGeneratedRecipeIngredient(final RecipeIngredient recipeIngredient);

    @Named("mapGeneratedIngredient")
    @Mapping(target = "ingredientId", source = "id")
    GeneratedIngredient mapGeneratedIngredient(final Ingredient ingredient);

    @Mapping(target = "id", source = "recipeId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "image", source = "image")
    @Mapping(target = "instruction", source = "instruction", qualifiedByName = "sanitizeInvalidCharacters")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "isVegetarian", source = "isVegetarian")
    @Mapping(target = "recipeIngredients", ignore = true)
    Recipe toRecipe(final GeneratedRecipe generatedRecipe);

    @Named("mapRecipeIngredient")
    @Mapping(target = "id", source = "recipeIngredientId")
    @Mapping(target = "recipe", ignore = true)
    @Mapping(target = "ingredient", source = "ingredient", qualifiedByName = "mapIngredient")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "measure", source = "measure")
    RecipeIngredient mapRecipeIngredient(final GeneratedRecipeIngredient generatedRecipeIngredient);

    @Named("mapIngredient")
    @Mapping(target = "id", source = "ingredientId")
    @Mapping(target = "name", source = "name")
    Ingredient mapIngredient(final GeneratedIngredient generatedIngredient);

    Measure mapMeasure(final GeneratedMeasure generatedMeasure);

    @Named("sanitizeInvalidCharacters")
    default String sanitizeInvalidCharacters(final String instruction) {
        return TransformHelper.sanitize(instruction);
    }

}
