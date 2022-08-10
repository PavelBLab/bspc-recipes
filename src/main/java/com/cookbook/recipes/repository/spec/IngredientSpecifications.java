package com.cookbook.recipes.repository.spec;

import com.cookbook.recipes.model.Ingredient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IngredientSpecifications {

    public static Specification<Ingredient> ingredientNamesIn(Set<String> ingredientNames) {
        return (ingredientRoot, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.in(ingredientRoot.get("name")).value(ingredientNames);
    }

    public static Specification<Ingredient> ingredientIsEqual(String ingredientName) {
        return (ingredientRoot, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(ingredientRoot.get("name"), ingredientName);
    }
}
