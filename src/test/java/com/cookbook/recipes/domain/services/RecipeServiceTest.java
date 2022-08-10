package com.cookbook.recipes.domain.services;

import com.cookbook.recipes.domain.exception.BusinessServiceException;
import com.cookbook.recipes.mapper.RecipeMapper;
import com.cookbook.recipes.mapper.SearchFilterMapper;
import com.cookbook.recipes.model.*;
import com.cookbook.recipes.repository.IngredientRepository;
import com.cookbook.recipes.repository.RecipeIngredientRepository;
import com.cookbook.recipes.repository.RecipeRepository;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.cookbook.recipes.util.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.data.jpa.domain.Specification.where;

@ExtendWith({MockitoExtension.class})
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;
    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private RecipeMapper recipeMapper;
    @Mock
    private SearchFilterMapper searchFilterMapper;

    @InjectMocks
    private RecipeService recipeService;

    @Captor
    private ArgumentCaptor<List<RecipeIngredient>> recipeIngredientArgumentCaptor;

    @Test
    void getAllRecipes() {
        val recipe = getRecipe();
        val generatedRecipeExpected = getGeneratedRecipe();
        when(recipeRepository.findAll()).thenReturn(List.of(recipe));
        when(recipeMapper.toGeneratedRecipe(any())).thenReturn(generatedRecipeExpected);

        val generatedRecipesActual = recipeService.getAllRecipes();

        // Verify in Mockito simply means that you want to check if a certain method of a mock object has been called
        // by specific number of times. When doing verification that a method was called exactly once
        verify(recipeMapper).toGeneratedRecipe(recipe);
        assertThat(generatedRecipesActual.size()).isEqualTo(1);
        assertThat(generatedRecipesActual.get(0)).isEqualTo(generatedRecipeExpected);
    }

    @Test
    void getRecipeById() {
        val recipe = getRecipe();
        val generatedRecipeExpected = getGeneratedRecipe();
        when(recipeRepository.getRecipeById(RECIPE_ID_1)).thenReturn(Optional.of(recipe));
        when(recipeMapper.toGeneratedRecipe(recipe)).thenReturn(generatedRecipeExpected);

        val generatedRecipeActual = recipeService.getRecipeById(RECIPE_ID_1);

        // Verify in Mockito simply means that you want to check if a certain method of a mock object has been called
        // by specific number of times. When doing verification that a method was called exactly once
        verify(recipeMapper, times(1)).toGeneratedRecipe(recipe);
        assertThat(generatedRecipeActual).isEqualTo(generatedRecipeExpected);
    }

    @Test
    void getRecipeById_recipeDoesNotExist() {
        when(recipeRepository.getRecipeById(RECIPE_ID_1)).thenReturn(Optional.empty());

        val exception = assertThrows(BusinessServiceException.class,
                () -> recipeService.getRecipeById(RECIPE_ID_1));

        assertThat(exception.getMessage()).isEqualTo("Recipe with id: 1 does not exist");
    }

    @Test
    void postRecipesBySearchFilter_byInstruction() {
        val filterValues = FilterValues.builder()
                .instruction(INSTRUCTION_TEXT)
                .build();
        val searchFilter = getSearchFilter(FilterCriteria.INSTRUCTION, filterValues);
        val generatedFilterValues = GeneratedFilterValues.builder()
                .instruction(INSTRUCTION_TEXT)
                .build();
        val generatedSearchFilter = getGeneratedSearchFilter(GeneratedFilterCriteria.INSTRUCTION,
                generatedFilterValues);
        val recipe = getRecipe();
        val generatedRecipeExpected = getGeneratedRecipe();

        when(searchFilterMapper.toSearchFilter(any())).thenReturn(searchFilter);
        when(recipeRepository.findByInstructionContainingIgnoreCase(anyString())).thenReturn(List.of(recipe));
        when(recipeMapper.toGeneratedRecipe(any())).thenReturn(generatedRecipeExpected);

        val generatedRecipesActual = recipeService
                .postRecipesBySearchFilter(generatedSearchFilter);

        // Verify in Mockito simply means that you want to check if a certain method of a mock object has been called
        // by specific number of times. When doing verification that a method was called exactly once
        verify(recipeMapper).toGeneratedRecipe(recipe);
        assertThat(generatedRecipesActual.size()).isEqualTo(1);
        assertThat(generatedRecipesActual.get(0)).isEqualTo(generatedRecipeExpected);
    }

    @Test
    void postRecipesBySearchFilter_byIsVegetarian() {
        val filterValues = FilterValues.builder()
                .isVegetarian(IS_VEGETARIAN)
                .build();
        val searchFilter = getSearchFilter(FilterCriteria.IS_VEGETARIAN, filterValues);
        val generatedFilterValues = GeneratedFilterValues.builder()
                .isVegetarian(IS_VEGETARIAN)
                .build();
        val generatedSearchFilter = getGeneratedSearchFilter(GeneratedFilterCriteria.IS_VEGETARIAN,
                generatedFilterValues);
        val recipe = getRecipe();
        val generatedRecipeExpected = getGeneratedRecipe();

        when(searchFilterMapper.toSearchFilter(any())).thenReturn(searchFilter);
        when(recipeRepository.findByIsVegetarian(anyBoolean())).thenReturn(List.of(recipe));
        when(recipeMapper.toGeneratedRecipe(any())).thenReturn(generatedRecipeExpected);

        val generatedRecipesActual = recipeService
                .postRecipesBySearchFilter(generatedSearchFilter);

        // Verify in Mockito simply means that you want to check if a certain method of a mock object has been called
        // by specific number of times. When doing verification that a method was called exactly once
        verify(recipeMapper).toGeneratedRecipe(recipe);
        assertThat(generatedRecipesActual.size()).isEqualTo(1);
        assertThat(generatedRecipesActual.get(0)).isEqualTo(generatedRecipeExpected);
    }

    @Test
    void postRecipesBySearchFilter_ByNumberOfServings() {
        val filterValues = FilterValues.builder()
                .numberOfServings(NUMBER_OF_SERVINGS)
                .build();
        val searchFilter = getSearchFilter(FilterCriteria.NUMBER_OF_SERVINGS, filterValues);
        val generatedFilterValues = GeneratedFilterValues.builder()
                .numberOfServings(NUMBER_OF_SERVINGS)
                .build();
        val generatedSearchFilter =
                getGeneratedSearchFilter(GeneratedFilterCriteria.NUMBER_OF_SERVINGS, generatedFilterValues);
        val recipe = getRecipe();
        val generatedRecipeExpected = getGeneratedRecipe();

        when(searchFilterMapper.toSearchFilter(any())).thenReturn(searchFilter);
        when(recipeRepository.findByNumberOfServings(anyInt())).thenReturn(List.of(recipe));
        when(recipeMapper.toGeneratedRecipe(any())).thenReturn(generatedRecipeExpected);

        val generatedRecipesActual = recipeService
                .postRecipesBySearchFilter(generatedSearchFilter);

        // Verify in Mockito simply means that you want to check if a certain method of a mock object has been called
        // by specific number of times. When doing verification that a method was called exactly once
        verify(recipeMapper).toGeneratedRecipe(recipe);
        assertThat(generatedRecipesActual.size()).isEqualTo(1);
        assertThat(generatedRecipesActual.get(0)).isEqualTo(generatedRecipeExpected);
    }

    @Test
    void postRecipesBySearchFilter_ByNumberOfServingsGreaterThan() {
        val filterValues = FilterValues.builder()
                .numberOfServings(NUMBER_OF_SERVINGS)
                .build();
        val searchFilter =
                getSearchFilter(FilterCriteria.NUMBER_OF_SERVINGS_GREATER_THAN_EQUAL, filterValues);
        val generatedFilterValues = GeneratedFilterValues.builder()
                .numberOfServings(NUMBER_OF_SERVINGS)
                .build();
        val generatedSearchFilter =
                getGeneratedSearchFilter(GeneratedFilterCriteria.NUMBER_OF_SERVINGS_GREATER_THAN_EQUAL,
                        generatedFilterValues);
        val recipe = getRecipe();
        val generatedRecipeExpected = getGeneratedRecipe();

        when(searchFilterMapper.toSearchFilter(any())).thenReturn(searchFilter);
        when(recipeRepository.findByNumberOfServingsGreaterThanEqual(anyInt())).thenReturn(List.of(recipe));
        when(recipeMapper.toGeneratedRecipe(any())).thenReturn(generatedRecipeExpected);

        val generatedRecipesActual = recipeService
                .postRecipesBySearchFilter(generatedSearchFilter);

        // Verify in Mockito simply means that you want to check if a certain method of a mock object has been called
        // by specific number of times. When doing verification that a method was called exactly once
        verify(recipeMapper).toGeneratedRecipe(recipe);
        assertThat(generatedRecipesActual.size()).isEqualTo(1);
        assertThat(generatedRecipesActual.get(0)).isEqualTo(generatedRecipeExpected);
    }

    @Test
    void postRecipesBySearchFilter_ByIncludingIngredients() {
        val filterValues = FilterValues.builder()
                .ingredients(SET_OF_INGREDIENT_NAMES)
                .build();
        val searchFilter =
                getSearchFilter(FilterCriteria.INCL_INGREDIENTS, filterValues);
        val generatedFilterValues = GeneratedFilterValues.builder()
                .ingredients(SET_OF_INGREDIENT_NAMES)
                .build();
        val generatedSearchFilter =
                getGeneratedSearchFilter(GeneratedFilterCriteria.INCL_INGREDIENTS,
                        generatedFilterValues);
        val recipe = getRecipe();
        val generatedRecipeExpected = getGeneratedRecipe();

        when(searchFilterMapper.toSearchFilter(any())).thenReturn(searchFilter);
        when(recipeRepository.findByRecipeIngredientsIngredientNameIn(any())).thenReturn(Set.of(recipe));
        when(recipeRepository.findByIdIn(Set.of(RECIPE_ID_1))).thenReturn(List.of(recipe));
        when(recipeMapper.toGeneratedRecipe(any())).thenReturn(generatedRecipeExpected);

        val generatedRecipesActual = recipeService
                .postRecipesBySearchFilter(generatedSearchFilter);

        verify(recipeMapper).toGeneratedRecipe(recipe);
        assertThat(generatedRecipesActual.size()).isEqualTo(1);
        assertThat(generatedRecipesActual.get(0)).isEqualTo(generatedRecipeExpected);
    }

    @Test
    void postRecipesBySearchFilter_ByIncludingIngredients_recipeDoesNotExist() {
        val filterValues = FilterValues.builder()
                .ingredients(SET_OF_INGREDIENT_NAMES)
                .build();
        val searchFilter =
                getSearchFilter(FilterCriteria.INCL_INGREDIENTS, filterValues);
        val generatedFilterValues = GeneratedFilterValues.builder()
                .ingredients(SET_OF_INGREDIENT_NAMES)
                .build();
        val generatedSearchFilter =
                getGeneratedSearchFilter(GeneratedFilterCriteria.INCL_INGREDIENTS,
                        generatedFilterValues);

        when(searchFilterMapper.toSearchFilter(any())).thenReturn(searchFilter);

        val exception = assertThrows(BusinessServiceException.class,
                () -> recipeService
                        .postRecipesBySearchFilter(generatedSearchFilter));

        assertThat(exception.getMessage()).isEqualTo(
                String.format("Recipe with ingredients name %s was not found", SET_OF_INGREDIENT_NAMES));
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void postRecipesBySearchFilter_ByExcludingIngredients() {
        val filterValues = FilterValues.builder()
                .ingredients(SET_OF_INGREDIENT_NAMES)
                .build();
        val searchFilter =
                getSearchFilter(FilterCriteria.EXCL_INGREDIENTS, filterValues);
        val generatedFilterValues = GeneratedFilterValues.builder()
                .ingredients(SET_OF_INGREDIENT_NAMES)
                .build();
        val generatedSearchFilter =
                getGeneratedSearchFilter(GeneratedFilterCriteria.EXCL_INGREDIENTS,
                        generatedFilterValues);
        val recipe = getRecipe();
        val generatedRecipeExpected = getGeneratedRecipe();

        when(searchFilterMapper.toSearchFilter(any())).thenReturn(searchFilter);
        when(recipeRepository.findAll(where(any()))).thenReturn(List.of(recipe));
        when(recipeMapper.toGeneratedRecipe(any())).thenReturn(generatedRecipeExpected);

        val generatedRecipesActual = recipeService
                .postRecipesBySearchFilter(generatedSearchFilter);

        verify(recipeMapper).toGeneratedRecipe(recipe);
        assertThat(generatedRecipesActual.size()).isEqualTo(1);
        assertThat(generatedRecipesActual.get(0)).isEqualTo(generatedRecipeExpected);
    }

    @Test
    void postRecipesBySearchFilter_ByExcludingIngredients_recipeDoesNotExist() {
        val filterValues = FilterValues.builder()
                .ingredients(SET_OF_INGREDIENT_NAMES)
                .build();
        val searchFilter =
                getSearchFilter(FilterCriteria.EXCL_INGREDIENTS, filterValues);
        val generatedFilterValues = GeneratedFilterValues.builder()
                .ingredients(SET_OF_INGREDIENT_NAMES)
                .build();
        val generatedSearchFilter =
                getGeneratedSearchFilter(GeneratedFilterCriteria.EXCL_INGREDIENTS,
                        generatedFilterValues);

        when(searchFilterMapper.toSearchFilter(any())).thenReturn(searchFilter);

        val exception = assertThrows(BusinessServiceException.class,
                () -> recipeService.postRecipesBySearchFilter(generatedSearchFilter));

        assertThat(exception.getMessage()).isEqualTo(
                String.format("Recipe with ingredients name %s was not found", SET_OF_INGREDIENT_NAMES));
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void postRecipesBySearchFilter_ByIsVegetarianAndNumberOfServings() {
        val filterValues = FilterValues.builder()
                .isVegetarian(IS_VEGETARIAN)
                .numberOfServings(NUMBER_OF_SERVINGS)
                .build();
        val searchFilter =
                getSearchFilter(FilterCriteria.IS_VEGETARIAN_AND_NUMBER_OF_SERVINGS, filterValues);
        val generatedFilterValues = GeneratedFilterValues.builder()
                .isVegetarian(IS_VEGETARIAN)
                .numberOfServings(NUMBER_OF_SERVINGS)
                .build();
        val generatedSearchFilter =
                getGeneratedSearchFilter(GeneratedFilterCriteria.IS_VEGETARIAN_AND_NUMBER_OF_SERVINGS,
                        generatedFilterValues);
        val recipe = getRecipe();
        val generatedRecipeExpected = getGeneratedRecipe();

        when(searchFilterMapper.toSearchFilter(any())).thenReturn(searchFilter);
        when(recipeRepository.findByIsVegetarianAndNumberOfServings(anyBoolean(), anyInt())).thenReturn(List.of(recipe));
        when(recipeMapper.toGeneratedRecipe(any())).thenReturn(generatedRecipeExpected);

        val generatedRecipesActual = recipeService
                .postRecipesBySearchFilter(generatedSearchFilter);

        verify(recipeMapper).toGeneratedRecipe(recipe);
        assertThat(generatedRecipesActual.size()).isEqualTo(1);
        assertThat(generatedRecipesActual.get(0)).isEqualTo(generatedRecipeExpected);
    }


    @Test
    void postRecipesBySearchFilter_byIngredientAndNumberOfServings() {
        val filterValues = FilterValues.builder()
                .ingredients(SET_OF_INGREDIENT_NAMES)
                .numberOfServings(NUMBER_OF_SERVINGS)
                .build();
        val searchFilter =
                getSearchFilter(FilterCriteria.INGREDIENT_AND_NUMBER_OF_SERVINGS, filterValues);
        val generatedFilterValues = GeneratedFilterValues.builder()
                .ingredients(SET_OF_INGREDIENT_NAMES)
                .numberOfServings(NUMBER_OF_SERVINGS)
                .build();
        val generatedSearchFilter =
                getGeneratedSearchFilter(GeneratedFilterCriteria.INGREDIENT_AND_NUMBER_OF_SERVINGS,
                        generatedFilterValues);
        val recipe = getRecipe();
        val generatedRecipeExpected = getGeneratedRecipe();

        when(searchFilterMapper.toSearchFilter(any())).thenReturn(searchFilter);
        when(recipeRepository.findByRecipeIngredientsIngredientNameInAndNumberOfServings(any(), anyInt()))
                .thenReturn(Set.of(recipe));
        when(recipeMapper.toGeneratedRecipe(any())).thenReturn(generatedRecipeExpected);

        val generatedRecipesActual = recipeService
                .postRecipesBySearchFilter(generatedSearchFilter);

        verify(recipeMapper).toGeneratedRecipe(recipe);
        assertThat(generatedRecipesActual.size()).isEqualTo(1);
        assertThat(generatedRecipesActual.get(0)).isEqualTo(generatedRecipeExpected);
    }

    @Test
    void postRecipesBySearchFilter_ByInstructionAndExcludedIngredient() {
        val filterValues = FilterValues.builder()
                .ingredients(SET_OF_INGREDIENT_NAMES)
                .instruction(INSTRUCTION_TEXT)
                .build();
        val searchFilter =
                getSearchFilter(FilterCriteria.EXCL_INGREDIENT_AND_INCL_INSTRUCTION, filterValues);
        val generatedFilterValues = GeneratedFilterValues.builder()
                .ingredients(SET_OF_INGREDIENT_NAMES)
                .instruction(INSTRUCTION_TEXT)
                .build();
        val generatedSearchFilter =
                getGeneratedSearchFilter(GeneratedFilterCriteria.EXCL_INGREDIENT_AND_INCL_INSTRUCTION,
                        generatedFilterValues);
        val recipe = getRecipe();
        val generatedRecipeExpected = getGeneratedRecipe();

        when(searchFilterMapper.toSearchFilter(any())).thenReturn(searchFilter);
        when(recipeRepository.findAll(where(any()))).thenReturn(List.of(recipe));
        when(recipeMapper.toGeneratedRecipe(any())).thenReturn(generatedRecipeExpected);

        val generatedRecipesActual = recipeService
                .postRecipesBySearchFilter(generatedSearchFilter);

        verify(recipeMapper).toGeneratedRecipe(recipe);
        assertThat(generatedRecipesActual.size()).isEqualTo(1);
        assertThat(generatedRecipesActual.get(0)).isEqualTo(generatedRecipeExpected);
    }

    @Test
    void postRecipesBySearchFilter_ByInstructionAndExcludedIngredient_recipeDoesNotExist() {
        val filterValues = FilterValues.builder()
                .ingredients(SET_OF_INGREDIENT_NAMES)
                .instruction(INSTRUCTION_TEXT)
                .build();
        val searchFilter =
                getSearchFilter(FilterCriteria.EXCL_INGREDIENT_AND_INCL_INSTRUCTION, filterValues);
        val generatedFilterValues = GeneratedFilterValues.builder()
                .ingredients(SET_OF_INGREDIENT_NAMES)
                .instruction(INSTRUCTION_TEXT)
                .build();
        val generatedSearchFilter =
                getGeneratedSearchFilter(GeneratedFilterCriteria.EXCL_INGREDIENT_AND_INCL_INSTRUCTION,
                        generatedFilterValues);

        when(searchFilterMapper.toSearchFilter(any())).thenReturn(searchFilter);

        val exception = assertThrows(BusinessServiceException.class,
                () -> recipeService.postRecipesBySearchFilter(generatedSearchFilter));

        assertThat(exception.getMessage())
                .isEqualTo(String.format(
                        "Recipe with excluded ingredients \"%s\" and instruction \"%s\" was not found",
                        SET_OF_INGREDIENT_NAMES, INSTRUCTION_TEXT));
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void createRecipe() {
        val recipe = getRecipe();
        val ingredient = getIngredient();
        val generatedRecipe = getGeneratedRecipe();
        val recipeIngredientsExpected = List.of(getRecipeIngredient());

        when(recipeRepository.getRecipeById(anyLong())).thenReturn(Optional.empty());
        when(recipeMapper.toRecipe(any())).thenReturn(recipe);
        when(recipeMapper.mapRecipeIngredient(any())).thenReturn(getRecipeIngredient());
        when(ingredientRepository.findAll(where(any()))).thenReturn(List.of(ingredient));
        when(recipeIngredientRepository.saveAll(any())).thenReturn(recipeIngredientsExpected);

        recipeService.createRecipe(generatedRecipe);

        // Verify in Mockito simply means that you want to check if a certain method of a mock object has been called
        // by specific number of times. When doing verification that a method was called exactly once
        verify(recipeIngredientRepository, times(1)).saveAll(recipeIngredientArgumentCaptor.capture());

        val recipeIngredientArgumentCaptorValue = recipeIngredientArgumentCaptor.getValue();

        assertThat(recipeIngredientsExpected.size()).isEqualTo(recipeIngredientArgumentCaptorValue.size());
        // usingRecursiveComparison() comparing objects field by field as it offers more flexibility,
        // better reporting and an easier to use API.
        assertThat(recipeIngredientsExpected).usingRecursiveComparison().isEqualTo(recipeIngredientArgumentCaptorValue);
    }

    @Test
    void createRecipe_recipeAlreadyExists() {
        val recipe = getRecipe();
        val generatedRecipe = getGeneratedRecipe();
        when(recipeRepository.getRecipeById(anyLong())).thenReturn(Optional.of(recipe));

        val exception = assertThrows(BusinessServiceException.class,
                () -> recipeService.createRecipe(generatedRecipe));

        assertThat(exception.getMessage()).isEqualTo("Recipe with id: 1 already exists");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateRecipe() {
        val recipe = getRecipe();
        val ingredient = getIngredient();
        val generatedRecipe = getGeneratedRecipe();
        val recipeIngredients = List.of(getRecipeIngredient());
        val recipeIngredientExpected = getRecipeIngredient();
        recipeIngredientExpected.setAmount(200);

        when(recipeRepository.getRecipeById(anyLong())).thenReturn(Optional.of(recipe));
        when(recipeIngredientRepository.findAll(where(any()))).thenReturn(recipeIngredients);
        when(ingredientRepository.findAll(where(any()))).thenReturn(List.of(ingredient));
        when(recipeMapper.toRecipe(any())).thenReturn(recipe);
        when(recipeMapper.mapRecipeIngredient(any())).thenReturn(recipeIngredientExpected);
        when(recipeIngredientRepository.saveAll(any())).thenReturn(recipeIngredients);

        recipeService.updateRecipe(RECIPE_ID_1, generatedRecipe);

        verify(recipeIngredientRepository).saveAll(recipeIngredientArgumentCaptor.capture());

        val recipeIngredientArgumentCaptorValue = recipeIngredientArgumentCaptor.getValue();

        assertThat(recipeIngredientArgumentCaptorValue.size()).isEqualTo(1);
        // usingRecursiveComparison() comparing objects field by field as it offers more flexibility,
        // better reporting and an easier to use API.
        assertThat(recipeIngredientArgumentCaptorValue).usingRecursiveComparison()
                .isEqualTo(List.of(recipeIngredientExpected));
    }

    @Test
    void updateRecipe_recipeDoesNotExist() {
        val generatedRecipe = getGeneratedRecipe();
        when(recipeRepository.getRecipeById(anyLong())).thenReturn(Optional.empty());

        val exception = assertThrows(BusinessServiceException.class,
                () -> recipeService.updateRecipe(RECIPE_ID_1, generatedRecipe));

        assertThat(exception.getMessage()).isEqualTo("Recipe with id: 1 does not exist");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void deleteRecipe() {
        val recipe = getRecipe();
        when(recipeRepository.getRecipeById(anyLong())).thenReturn(Optional.of(recipe));

        recipeService.deleteRecipe(RECIPE_ID_1);

        // Verify in Mockito simply means that you want to check if a certain method of a mock object has been called
        // by specific number of times. When doing verification that a method was called exactly once
        verify(recipeRepository, times(1)).deleteById(RECIPE_ID_1);
    }

    @Test
    void deleteRecipe_recipeDoesNotExist() {
        when(recipeRepository.getRecipeById(anyLong())).thenReturn(Optional.empty());

        val exception = assertThrows(BusinessServiceException.class,
                () -> recipeService.deleteRecipe(RECIPE_ID_1));

        assertThat(exception.getMessage()).isEqualTo("Recipe with id: 1 does not exist");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}