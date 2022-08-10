package com.cookbook.recipes.repository.spec;

import com.cookbook.recipes.model.Ingredient;
import com.cookbook.recipes.model.Measure;
import com.cookbook.recipes.model.Recipe;
import com.cookbook.recipes.model.RecipeIngredient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.Set;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecipeIngredientSpecifications {

    public static Specification<RecipeIngredient> amountIn(Set<Integer> amountList) {
        return (recipeIngredientRoot, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.in(recipeIngredientRoot.get("amount")).value(amountList);
    }

    public static Specification<RecipeIngredient> measuresIn(Set<Measure> measures) {
        return (recipeIngredientRoot, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.in(recipeIngredientRoot.get("measure")).value(measures);
    }

    public static Specification<RecipeIngredient> recipeIngredientIngredientNamesIn(Set<String> ingredientNames) {
        return (recipeIngredientRoot, criteriaQuery, criteriaBuilder) -> {
            Join<RecipeIngredient, Ingredient> ingredientJoin = recipeIngredientRoot
                    .join("ingredient", JoinType.INNER);
            return criteriaBuilder.in(ingredientJoin.get("name")).value(ingredientNames);
        };
    }

    public static Specification<RecipeIngredient> recipeIsEqual(Long recipeId) {
        return (recipeIngredientRoot, criteriaQuery, criteriaBuilder) -> {
            Join<RecipeIngredient, Recipe> recipeJoin = recipeIngredientRoot
                    .join("recipe", JoinType.INNER);
            return criteriaBuilder.equal(recipeJoin.get("id"), recipeId);
        };
    }

}
