package com.cookbook.recipes.repository;

import com.cookbook.recipes.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>,
        JpaSpecificationExecutor<Ingredient> {

    Optional<Ingredient> findByName(String ingredientName);

    List<Ingredient> findAllByNameIn(Set<String> ingredientNames);
}
