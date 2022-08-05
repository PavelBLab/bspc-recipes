package com.cookbook.recipes.web.operations;

import com.cookbook.recipes.api.RecipesApi;
import com.cookbook.recipes.domain.services.RecipeService;
import com.cookbook.recipes.model.GeneratedRecipe;
import com.cookbook.recipes.model.GeneratedRecipes;
import com.cookbook.recipes.model.GeneratedSearchFilter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class RecipesOperation implements RecipesApi {

    private final RecipeService recipeService;

    @Override
    public ResponseEntity<GeneratedRecipes> getAllRecipes() {
        val generatedRecipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(GeneratedRecipes.builder().generatedRecipes(generatedRecipes).build());
    }

    @Override
    public ResponseEntity<GeneratedRecipe> getRecipeById(final Long recipeId) {
        return ResponseEntity.ok(recipeService.getRecipeById(recipeId));
    }

    @Override
    public ResponseEntity<Void> createRecipe(final GeneratedRecipe generatedRecipe) {
        val recipeId = recipeService.createRecipe(generatedRecipe);
        return ResponseEntity.created(getUri(recipeId)).build();
    }

    @Override
    public ResponseEntity<Void> updateRecipe(final Long recipeId, final GeneratedRecipe generatedRecipe) {
        recipeService.updateRecipe(recipeId, generatedRecipe);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteRecipe(final Long recipeId) {
        recipeService.deleteRecipe(recipeId);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<GeneratedRecipes> postRecipesBySearchFilter(
            final GeneratedSearchFilter generatedSearchFilter) {
        val generatedRecipes = recipeService.postRecipesBySearchFilter(generatedSearchFilter);
        return ResponseEntity.ok(GeneratedRecipes.builder().generatedRecipes(generatedRecipes).build());
    }

    private static URI getUri(final String identity) {
        return UriComponentsBuilder.fromUriString("/recipes/").path(identity).build().toUri();
    }

}
