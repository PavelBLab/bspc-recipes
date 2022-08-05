package com.cookbook.recipes.repository.spec;

import com.cookbook.recipes.model.Ingredient;
import com.cookbook.recipes.model.Recipe;
import com.cookbook.recipes.model.RecipeIngredient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.Set;

/*
The problem with query methods is that we can only specify a fixed number of criteria.
Also, the number of query methods increases rapidly as the use cases increases.

At some point, there are many overlapping criteria across the query methods and if there is a change
in any one of those, we’ll have to make changes in multiple query methods.

Also, the length of the query method might increase significantly when we have long
field names and multiple criteria in our query. Plus, it might take a while for someone
to understand such a lengthy query and its purpose

Specifications allow us to write queries programmatically.
Because of this, we can build queries dynamically based on user input.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecipeSpecifications {

    /*
    * The Criteria Query API allows us to join the two tables when creating the Specification.
    *  As a result, we'll be able to include the fields from the Recipe entity inside our queries:
    * */
    public static Specification<Recipe> ingredientsContains(Set<String> ingredients) {
        return (recipeRoot, criteriaQuery, criteriaBuilder) -> {
            Join<Recipe, RecipeIngredient> recipeIngredientsJoin = recipeRoot
                    .join("recipeIngredients", JoinType.INNER);
            Join<RecipeIngredient, Ingredient> ingredientsJoin = recipeIngredientsJoin
                    .join("ingredient", JoinType.INNER);
            return  criteriaBuilder.in(ingredientsJoin.get("name")).value(ingredients);
        };
    }

    public static Specification<Recipe> recipeIdsAreNot(Set<Long> recipeIds) {
        return (recipeRoot, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = recipeRoot.get("id").in(recipeIds);
            return  criteriaBuilder.not(predicate);
        };
    }

    public static Specification<Recipe> instructionContainsIgnoreCase(String instruction) {
        return (recipeRoot, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(recipeRoot.get("instruction")),
                        "%" + instruction.toLowerCase() + "%");
    }

    public static Specification<Recipe> recipeIsVegetarian(Boolean isVegetarian) {
        return (recipeRoot, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(recipeRoot.get("isVegetarian"), isVegetarian);
    }

    public static Specification<Recipe> numberOfServingsEqual(Integer numberOfServings) {
        return (recipeRoot, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(recipeRoot.get("numberOfServings"), numberOfServings);
    }
}
