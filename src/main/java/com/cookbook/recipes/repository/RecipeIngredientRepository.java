package com.cookbook.recipes.repository;

import com.cookbook.recipes.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

}
