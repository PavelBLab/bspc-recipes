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

/*
The problem with query methods is that we can only specify a fixed number of criteria.
Also, the number of query methods increases rapidly as the use cases increases.

At some point, there are many overlapping criteria across the query methods and if there is a change
in any one of those, we’ll have to make changes in multiple query methods.

Also, the length of the query method might increase significantly when we have lon
g field names and multiple criteria in our query. Plus, it might take a while for someone
to understand such a lengthy query and its purpose

Specifications allow us to write queries programmatically.
Because of this, we can build queries dynamically based on user input.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecipeIngredientSpecifications {

    /*
     * The Criteria Query API allows us to join the two tables when creating the Specification.
     *  As a result, we'll be able to include the fields from the Book entity inside our queries:
     * */
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
