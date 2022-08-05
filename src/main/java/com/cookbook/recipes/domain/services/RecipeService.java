package com.cookbook.recipes.domain.services;

import com.cookbook.recipes.domain.exceptoin.ExceptionHandler;
import com.cookbook.recipes.mapper.RecipeMapper;
import com.cookbook.recipes.mapper.SearchFilterMapper;
import com.cookbook.recipes.model.FilterCriteria;
import com.cookbook.recipes.model.GeneratedRecipe;
import com.cookbook.recipes.model.GeneratedSearchFilter;
import com.cookbook.recipes.model.Recipe;
import com.cookbook.recipes.repository.IngredientRepository;
import com.cookbook.recipes.repository.RecipeIngredientRepository;
import com.cookbook.recipes.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeMapper recipeMapper;
    private final SearchFilterMapper searchFilterMapper;

    private static final String RECIPE_DOES_NOT_EXIST = "Recipe with id: %s does not exist";

    public List<GeneratedRecipe> getAllRecipes() {
        return recipeRepository.findAll()
                .stream()
                .map(recipeMapper::toGeneratedRecipe)
                .collect(Collectors.toList());
    }

    public GeneratedRecipe getRecipeById(final Long recipeId) {
        val generateRecipe = recipeRepository.getRecipeById(recipeId).orElseThrow(
                () -> ExceptionHandler.createBusinessServiceException(
                        HttpStatus.NOT_FOUND, String.format(RECIPE_DOES_NOT_EXIST, recipeId)
                ));

        return recipeMapper.toGeneratedRecipe(generateRecipe);
    }

    public List<GeneratedRecipe> postRecipesBySearchFilter(final GeneratedSearchFilter generatedSearchFilter) {
        val searchFilter = searchFilterMapper.toSearchFilter(generatedSearchFilter);
        val filterCriteria = searchFilter.getFilterCriteria();
        val searchFilterValues = searchFilter.getFilterValues();

        if (filterCriteria.equals(FilterCriteria.INSTRUCTION)) {
            return getRecipeByInstruction(searchFilterValues.getInstruction());
        }

        if (filterCriteria.equals(FilterCriteria.IS_VEGETARIAN)) {
            return getRecipesByIsVegetarian(searchFilterValues.getIsVegetarian());
        }

        if (filterCriteria.equals(FilterCriteria.NUMBER_OF_SERVINGS)) {
            return getRecipeByNumberOfServings(searchFilterValues.getNumberOfServings());
        }

        if (filterCriteria.equals(FilterCriteria.NUMBER_OF_SERVINGS_GREATER_THAN_EQUAL)) {
            return getRecipeByNumberOfServingsGreaterThan(searchFilterValues.getNumberOfServings());
        }

        if (filterCriteria.equals(FilterCriteria.INCL_INGREDIENTS)) {
            return getRecipeByIncludingIngredients(searchFilterValues.getIngredients());
        }

        if (filterCriteria.equals(FilterCriteria.EXCL_INGREDIENTS)) {
            return getRecipeByExcludingIngredients(searchFilterValues.getIngredients());
        }

        if (filterCriteria.equals(FilterCriteria.IS_VEGETARIAN_AND_NUMBER_OF_SERVINGS)) {
            return getRecipeByIsVegetarianAndNumberOfServings(
                    searchFilterValues.getIsVegetarian(), searchFilterValues.getNumberOfServings());
        }

        if (filterCriteria.equals(FilterCriteria.INGREDIENT_AND_NUMBER_OF_SERVINGS)) {
            return getRecipeByIngredientAndNumberOfServings(
                    searchFilterValues.getIngredients(), searchFilterValues.getNumberOfServings());
        }

        if (filterCriteria.equals(FilterCriteria.EXCL_INGREDIENT_AND_INCL_INSTRUCTION)) {
            return getRecipeByInstructionAndExcludedIngredient(
                    searchFilterValues.getInstruction(), searchFilterValues.getIngredients());
        }

        return List.of();
    }

    public String createRecipe(final GeneratedRecipe generatedRecipe) {
        val generatedRecipeId = generatedRecipe.getRecipeId();
        val recipeDb = recipeRepository.getRecipeById(generatedRecipeId);

        if (recipeDb.isPresent()) {
            throw ExceptionHandler.createBusinessServiceException(
                    HttpStatus.FORBIDDEN, String.format("Recipe with id: %s already exists", generatedRecipeId));
        }

        val recipe = recipeMapper.toRecipe(generatedRecipe);
        val recipeIngredients = generatedRecipe.getRecipeIngredients()
                .stream()
                .map(generatedRecipeIngredient -> {
                    val recipeIngredient = recipeMapper.mapRecipeIngredient(generatedRecipeIngredient);
                    val ingredientId = recipeIngredient.getIngredient().getId();
                    val ingredientName = recipeIngredient.getIngredient().getName();

                    if (ingredientId != null || ingredientName != null) {
                        // Check in DB that ingredient exists by id and name
                        val ingredientDb = ingredientRepository
                                .findByName(ingredientName);
                        ingredientDb.ifPresent(recipeIngredient::setIngredient);
                    }

                    recipeIngredient.setRecipe(recipe);
                    return recipeIngredient;
                })
                .collect(Collectors.toList());

        val recipeId = recipeIngredientRepository.saveAll(recipeIngredients).get(0).getRecipe().getId();

        return recipeId.toString();
    }

    public void updateRecipe(final Long recipeId, final GeneratedRecipe generatedRecipe) {
        val recipeDb = recipeRepository.getRecipeById(recipeId);

        if (recipeDb.isEmpty()) {
            throw ExceptionHandler.createBusinessServiceException(
                    HttpStatus.FORBIDDEN, String.format(RECIPE_DOES_NOT_EXIST, recipeId));
        }

        val recipe = recipeMapper.toRecipe(generatedRecipe);
        recipeDb.get().setName(recipe.getName());
        recipeDb.get().setDescription(recipe.getDescription());
        recipeDb.get().setImage(recipe.getImage());
        recipeDb.get().setInstruction(recipe.getInstruction());
        recipeDb.get().setCreatedAt(recipe.getCreatedAt());
        recipeDb.get().setIsVegetarian(recipe.getIsVegetarian());
        recipeDb.get().setNumberOfServings(recipe.getNumberOfServings());

        val recipeIngredients = generatedRecipe.getRecipeIngredients()
                .stream()
                .map(generatedRecipeIngredient -> {
                    val recipeIngredient = recipeMapper.mapRecipeIngredient(generatedRecipeIngredient);
                    val ingredientId = recipeIngredient.getIngredient().getId();
                    val ingredientName = recipeIngredient.getIngredient().getName();

                    if (ingredientId != null || ingredientName != null) {
                        // Check in DB that ingredient exists by id and name
                        val ingredientDb = ingredientRepository
                                .findByName(ingredientName);
                        ingredientDb.ifPresent(recipeIngredient::setIngredient);
                    }

                    recipeIngredient.setRecipe(recipeDb.get());
                    return recipeIngredient;
                })
                .collect(Collectors.toList());

        recipeIngredientRepository.saveAll(recipeIngredients);
    }

    public void deleteRecipe(final Long recipeId) {
        val recipe = recipeRepository.getRecipeById(recipeId);

        if (recipe.isEmpty()) {
            throw ExceptionHandler.createBusinessServiceException(
                    HttpStatus.FORBIDDEN, String.format(RECIPE_DOES_NOT_EXIST, recipeId));
        }

        recipeRepository.deleteById(recipeId);
    }

    private List<GeneratedRecipe> getRecipeByInstruction(final String instruction) {
        return recipeRepository.findByInstructionContainingIgnoreCase(instruction)
                .stream()
                .map(recipeMapper::toGeneratedRecipe)
                .collect(Collectors.toList());
    }

    private List<GeneratedRecipe> getRecipesByIsVegetarian(final Boolean isVegetarian) {
        return recipeRepository.findByIsVegetarian(isVegetarian)
                .stream()
                .map(recipeMapper::toGeneratedRecipe)
                .collect(Collectors.toList());
    }

    private List<GeneratedRecipe> getRecipeByNumberOfServings(final Integer numberOfServings) {
        return recipeRepository.findByNumberOfServings(numberOfServings)
                .stream()
                .map(recipeMapper::toGeneratedRecipe)
                .collect(Collectors.toList());
    }

    private List<GeneratedRecipe> getRecipeByNumberOfServingsGreaterThan(final Integer numberOfServings) {
        return recipeRepository.findByNumberOfServingsGreaterThanEqual(numberOfServings)
                .stream()
                .map(recipeMapper::toGeneratedRecipe)
                .collect(Collectors.toList());
    }

    private List<GeneratedRecipe> getRecipeByIncludingIngredients(final Set<String> ingredients) {
        // Get unique recipe ids based on specific ingredients
        val recipeIds = recipeRepository.findByRecipeIngredientsIngredientNameIn(ingredients)
                .stream()
                .map(Recipe::getId)
                .collect(Collectors.toSet());

        if (recipeIds.isEmpty()) {
            throw ExceptionHandler.createBusinessServiceException(
                    HttpStatus.NOT_FOUND, String.format("Recipe with ingredients name %s was not found", ingredients));
        }

        return recipeRepository.findByIdIn(recipeIds)
                .stream()
                .map(recipeMapper::toGeneratedRecipe)
                .collect(Collectors.toList());
    }

    private List<GeneratedRecipe> getRecipeByExcludingIngredients(final Set<String> ingredients) {
        // Get unique recipe ids based on specific ingredients
        val recipeIds = recipeRepository.findByRecipeIngredientsIngredientNameIn(ingredients)
                .stream()
                .map(Recipe::getId)
                .collect(Collectors.toSet());

        if (recipeIds.isEmpty()) {
            throw ExceptionHandler.createBusinessServiceException(
                    HttpStatus.NOT_FOUND, String.format("Recipe with ingredients name %s was not found", ingredients));
        }

        return recipeRepository.findByIdNotIn(recipeIds)
                .stream()
                .map(recipeMapper::toGeneratedRecipe)
                .collect(Collectors.toList());
    }

    private List<GeneratedRecipe> getRecipeByIsVegetarianAndNumberOfServings(final Boolean isVegetarian,
                                                                             final Integer numberOfServings) {
        return recipeRepository.findByIsVegetarianAndNumberOfServings(isVegetarian, numberOfServings)
                .stream()
                .map(recipeMapper::toGeneratedRecipe)
                .collect(Collectors.toList());
    }

    private List<GeneratedRecipe> getRecipeByIngredientAndNumberOfServings(final Set<String> ingredients,
                                                                           final Integer numberOfServings) {
        return recipeRepository.findByRecipeIngredientsIngredientNameInAndNumberOfServings(ingredients, numberOfServings)
                .stream()
                .map(recipeMapper::toGeneratedRecipe)
                .collect(Collectors.toList());
    }

    private List<GeneratedRecipe> getRecipeByInstructionAndExcludedIngredient(final String instruction,
                                                                              final Set<String> ingredients) {
        val recipeIds = recipeRepository
                .findByInstructionContainingIgnoreCaseAndRecipeIngredientsIngredientNameNotIn(instruction, ingredients)
                .stream()
                .map(Recipe::getId)
                .collect(Collectors.toSet());

        if (recipeIds.isEmpty()) {
            throw ExceptionHandler.createBusinessServiceException(
                    HttpStatus.NOT_FOUND,
                    String.format("Recipe with excluded ingredients \"%s\" and instruction \"%s\" was not found",
                            ingredients, instruction));
        }

        return recipeRepository.findByIdIn(recipeIds)
                .stream()
                .map(recipeMapper::toGeneratedRecipe)
                .collect(Collectors.toList());
    }

}