package com.cookbook.recipes.repository;

import com.cookbook.recipes.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long>,
        JpaSpecificationExecutor<RecipeIngredient> {

}
