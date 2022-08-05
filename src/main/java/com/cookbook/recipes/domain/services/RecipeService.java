package com.cookbook.recipes.domain.services;

import com.cookbook.recipes.domain.exceptoin.ExceptionHandler;
import com.cookbook.recipes.mapper.RecipeMapper;
import com.cookbook.recipes.mapper.SearchFilterMapper;
import com.cookbook.recipes.model.*;
import com.cookbook.recipes.repository.IngredientRepository;
import com.cookbook.recipes.repository.RecipeIngredientRepository;
import com.cookbook.recipes.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cookbook.recipes.repository.spec.IngredientSpecifications.ingredientNamesIn;
import static com.cookbook.recipes.repository.spec.RecipeIngredientSpecifications.*;
import static com.cookbook.recipes.repository.spec.RecipeSpecifications.*;
import static org.springframework.data.jpa.domain.Specification.where;

/*
Generates a constructor with required arguments.
Required arguments are final fields and fields with constraints such as @NonNull.
Starting with Spring version 4.3, the single bean constructor does not need to
be annotated with the @Autowired annotation.

lombok will create a constructor with a single dependency field, and since this is
the only constructor, Spring will inject the dependencies through it.
*/
@Slf4j
@Service
@RequiredArgsConstructor // Automatic autowired when final
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeMapper recipeMapper;
    private final SearchFilterMapper searchFilterMapper;

    private static final String RECIPE_DOES_NOT_EXIST = "Recipe with id: %s does not exist";
    private static final String RECIPE_WITH_INGREDIENTS_NOT_FOUND = "Recipe with ingredients name %s was not found";

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

    public String createRecipe(final GeneratedRecipe generatedRecipe) {
        val generatedRecipeId = generatedRecipe.getRecipeId();
        val recipeDb = recipeRepository.getRecipeById(generatedRecipeId);

        if (recipeDb.isPresent()) {
            throw ExceptionHandler.createBusinessServiceException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Recipe with id: %s already exists", generatedRecipeId));
        }

        val recipe = recipeMapper.toRecipe(generatedRecipe);
        val ingredientNames = generatedRecipe.getRecipeIngredients()
                .stream()
                .map(recipeIngredient -> recipeIngredient.getIngredient().getName())
                .collect(Collectors.toSet());

        val ingredientsDb = ingredientRepository.findAll(
                where(ingredientNamesIn(ingredientNames)));
        // Alternative method
        // val ingredientsDb = ingredientRepository.findAllByNameIn(ingredientNames);

        val recipeIngredients = generatedRecipe.getRecipeIngredients()
                .stream()
                .map(generatedRecipeIngredient -> {
                    val recipeIngredient = recipeMapper.mapRecipeIngredient(generatedRecipeIngredient);
                    val ingredientDb = ingredientFilter(ingredientsDb, recipeIngredient.getIngredient());
                    ingredientDb.ifPresent(recipeIngredient::setIngredient);
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
                    HttpStatus.BAD_REQUEST, String.format(RECIPE_DOES_NOT_EXIST, recipeId));
        }

        val amountList = generatedRecipe.getRecipeIngredients()
                .stream()
                .map(GeneratedRecipeIngredient::getAmount)
                .collect(Collectors.toSet());
        val measures = generatedRecipe.getRecipeIngredients()
                .stream()
                .map(recipeIngredient -> recipeMapper.mapMeasure(recipeIngredient.getMeasure()))
                .collect(Collectors.toSet());
        val ingredientNames = generatedRecipe.getRecipeIngredients()
                .stream()
                .map(recipeIngredient -> recipeIngredient.getIngredient().getName())
                .collect(Collectors.toSet());

        val recipeIngredientsDb = recipeIngredientRepository.findAll(
                where(recipeIsEqual(generatedRecipe.getRecipeId())
                        .and(amountIn(amountList))
                        .and(recipeIngredientIngredientNamesIn(ingredientNames))
                        .and(measuresIn(measures))
                ));
        val ingredientsDb = ingredientRepository.findAll(
                where(ingredientNamesIn(ingredientNames)));

        val recipe = recipeMapper.toRecipe(generatedRecipe);
        recipeDb.get().setName(recipe.getName());
        recipeDb.get().setDescription(recipe.getDescription());
        recipeDb.get().setImage(recipe.getImage());
        recipeDb.get().setInstruction(recipe.getInstruction());
        recipeDb.get().setCreatedAt(recipe.getCreatedAt());
        recipeDb.get().setIsVegetarian(recipe.getIsVegetarian());
        recipeDb.get().setNumberOfServings(recipe.getNumberOfServings());

        val recipeIngredients =
                generatedRecipe.getRecipeIngredients().stream()
                        .map(generatedRecipeIngredient -> {
                            val recipeIngredient = recipeMapper.mapRecipeIngredient(generatedRecipeIngredient);
                            val ingredientDb = ingredientFilter(ingredientsDb, recipeIngredient.getIngredient());
                            ingredientDb.ifPresent(recipeIngredient::setIngredient);
                            recipeIngredient.setRecipe(recipeDb.get());
                            return recipeIngredient;
                        })
                        .filter(recipeIngredient -> recipeIngredientFilter(recipeIngredientsDb, recipeIngredient))
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

    public List<GeneratedRecipe> postRecipesBySearchFilter(final GeneratedSearchFilter generatedSearchFilter) {
        val searchFilter = searchFilterMapper.toSearchFilter(generatedSearchFilter);
        val filterCriteria = searchFilter.getFilterCriteria();
        val searchFilterValues = searchFilter.getFilterValues();

        switch (filterCriteria) {
            case INSTRUCTION:
                return getRecipeByInstruction(searchFilterValues.getInstruction());
            case IS_VEGETARIAN:
                return getRecipesByIsVegetarian(searchFilterValues.getIsVegetarian());
            case NUMBER_OF_SERVINGS:
                return getRecipeByNumberOfServings(searchFilterValues.getNumberOfServings());
            case NUMBER_OF_SERVINGS_GREATER_THAN_EQUAL:
                return getRecipeByNumberOfServingsGreaterThan(searchFilterValues.getNumberOfServings());
            case INCL_INGREDIENTS:
                return getRecipeByIncludingIngredients(searchFilterValues.getIngredients());
            case EXCL_INGREDIENTS:
                return getRecipeByExcludingIngredients(searchFilterValues.getIngredients());
            case IS_VEGETARIAN_AND_NUMBER_OF_SERVINGS:
                return getRecipeByIsVegetarianAndNumberOfServings(
                        searchFilterValues.getIsVegetarian(), searchFilterValues.getNumberOfServings());
            case INGREDIENT_AND_NUMBER_OF_SERVINGS:
                return getRecipeByIngredientAndNumberOfServings(
                        searchFilterValues.getIngredients(), searchFilterValues.getNumberOfServings());
            case EXCL_INGREDIENT_AND_INCL_INSTRUCTION:
                return getRecipeByInstructionAndExcludedIngredient(
                        searchFilterValues.getInstruction(), searchFilterValues.getIngredients());
            case INSTRUCTION_AND_IS_VEGETARIAN_AND_NUMBER_OF_SERVINGS:
                return getRecipeByInstructionAndIsVegetarianAndNumberOfServings(
                        searchFilterValues.getInstruction(), searchFilterValues.getIsVegetarian(),
                        searchFilterValues.getNumberOfServings());
            case INGREDIENTS_AND_INSTRUCTION_AND_IS_VEGETARIAN_AND_NUMBER_OF_SERVINGS:
                return getRecipeByIngredientsAndInstructionAndIsVegetarianAndNumberOfServings(
                        searchFilterValues.getIngredients(),
                        searchFilterValues.getInstruction(),
                        searchFilterValues.getIsVegetarian(),
                        searchFilterValues.getNumberOfServings());
            default:
                return List.of();
        }
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
                    HttpStatus.NOT_FOUND, String.format(RECIPE_WITH_INGREDIENTS_NOT_FOUND, ingredients));
        }

        return recipeRepository.findByIdIn(recipeIds)
                .stream()
                .map(recipeMapper::toGeneratedRecipe)
                .collect(Collectors.toList());
    }


    private List<GeneratedRecipe> getRecipeByExcludingIngredients(final Set<String> ingredients) {
        // Get unique recipe ids based on specific ingredients
        val recipeIds = recipeRepository.findAll(where(ingredientsContains(ingredients)))
                .stream()
                .map(Recipe::getId)
                .collect(Collectors.toSet());

        if (recipeIds.isEmpty()) {
            throw ExceptionHandler.createBusinessServiceException(
                    HttpStatus.NOT_FOUND, String.format(RECIPE_WITH_INGREDIENTS_NOT_FOUND, ingredients));
        }

        return recipeRepository.findAll(where(recipeIdsAreNot(recipeIds)))
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
        val recipeIds = recipeRepository.findAll(where(ingredientsContains(ingredients)))
                .stream()
                .map(Recipe::getId)
                .collect(Collectors.toSet());

        if (recipeIds.isEmpty()) {
            throw ExceptionHandler.createBusinessServiceException(
                    HttpStatus.NOT_FOUND,
                    String.format("Recipe with excluded ingredients \"%s\" and instruction \"%s\" was not found",
                            ingredients, instruction));
        }

        return recipeRepository.findAll(where(recipeIdsAreNot(recipeIds)
                        .and(instructionContainsIgnoreCase(instruction))))
                .stream()
                .map(recipeMapper::toGeneratedRecipe)
                .collect(Collectors.toList());
    }

    private List<GeneratedRecipe> getRecipeByInstructionAndIsVegetarianAndNumberOfServings(
            final String instruction, final Boolean isVegetarian, final Integer numberOfServings) {
        return recipeRepository.findAll(where(instructionContainsIgnoreCase(instruction))
                        .and(recipeIsVegetarian(isVegetarian))
                        .and(numberOfServingsEqual(numberOfServings)))
                .stream()
                .map(recipeMapper::toGeneratedRecipe)
                .collect(Collectors.toList());
    }

    private List<GeneratedRecipe> getRecipeByIngredientsAndInstructionAndIsVegetarianAndNumberOfServings(
            final Set<String> ingredients, final String instruction,
            final Boolean isVegetarian, final Integer numberOfServings) {
        return recipeRepository.findAll(where(ingredientsContains(ingredients))
                        .and(instructionContainsIgnoreCase(instruction))
                        .and(recipeIsVegetarian(isVegetarian))
                        .and(numberOfServingsEqual(numberOfServings)))
                .stream()
                .map(recipeMapper::toGeneratedRecipe)
                .distinct() // remove duplications
                .collect(Collectors.toList());
    }


    private Optional<Ingredient> ingredientFilter(final List<Ingredient> ingredientsDb,
                                                  final Ingredient ingredient) {
        // Check in DB that ingredient exists by id and name
        return ingredientsDb.stream()
                .filter(ingredientDb -> ingredientDb.getName().equals(ingredient.getName()))
                .findFirst();
    }

    private Boolean recipeIngredientFilter(final List<RecipeIngredient> recipeIngredientsDb,
                                           final RecipeIngredient recipeIngredient) {
        return recipeIngredientsDb.stream()
                .filter(recipeIngredientDb ->
                        Objects.equals(recipeIngredientDb.getAmount(),
                                recipeIngredient.getAmount()) &&
                                Objects.equals(recipeIngredientDb.getMeasure(),
                                        recipeIngredient.getMeasure()) &&
                                Objects.equals(recipeIngredientDb.getIngredient().getName(),
                                        recipeIngredient.getIngredient().getName()) &&
                                Objects.equals(recipeIngredientDb.getRecipe().getId(),
                                        recipeIngredient.getRecipe().getId())
                )
                .findFirst()
                .isEmpty();
    }

}