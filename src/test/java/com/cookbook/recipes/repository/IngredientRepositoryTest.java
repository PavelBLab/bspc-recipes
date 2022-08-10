package com.cookbook.recipes.repository;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static com.cookbook.recipes.util.TestDataFactory.INGREDIENT_NAME;
import static com.cookbook.recipes.util.TestDataFactory.getIngredient;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class IngredientRepositoryTest {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientRepositoryTest(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Test
    void findByName() {
        val ingredientActual = ingredientRepository.findByName(INGREDIENT_NAME);

        ingredientActual.ifPresent(ingredient -> assertThat(ingredient.getName()).isEqualTo(INGREDIENT_NAME));
    }

    @Test
    void findAllByNameIn() {
        val ingredientNames = Set.of(getIngredient().getName());
        val ingredientsActual = ingredientRepository.findAllByNameIn(ingredientNames);

        assertThat(ingredientsActual.size()).isEqualTo(1);
        assertThat(ingredientsActual.get(0).getName()).isEqualTo(INGREDIENT_NAME);

    }
}