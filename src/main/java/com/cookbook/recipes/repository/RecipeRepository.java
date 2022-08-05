package com.cookbook.recipes.repository;

import com.cookbook.recipes.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Optional<Recipe> getRecipeById(Long recipeId);

    List<Recipe> findByIdIn(Set<Long> recipeIds);

    List<Recipe> findByIdNotIn(Set<Long> recipeIds);

    List<Recipe> findByInstructionContainingIgnoreCase(String instruction);

    // Alternative method
    /*
    @Query(value = "SELECT * FROM recipes r " +
            "WHERE LOWER(r.instruction) " +
            "LIKE LOWER(CONCAT('%', :instruction,'%'))",
            nativeQuery = true)
    List<Recipe> findByInstructionContainingIgnoreCase(@Param("instruction") String instruction);
    */

    List<Recipe> findByIsVegetarian(Boolean isVegetarian);

    List<Recipe> findByNumberOfServings(Integer numberOfServings);

    List<Recipe> findByNumberOfServingsGreaterThanEqual(Integer numberOfServings);

    List<Recipe> findByIsVegetarianAndNumberOfServings(Boolean isVegetarian, Integer numberOfServings);

    Set<Recipe> findByRecipeIngredientsIngredientNameIn(Set<String> ingredients);

    //Alternative method
    /*
    @Query(value = "SELECT * FROM recipes r " +
            "INNER JOIN recipe_ingredients ri " +
            "ON r.id = ri.recipe_id " +
            "INNER JOIN ingredients i " +
            "ON ri.ingredient_id = i.id " +
            "WHERE i.name IN (:ingredients)",
           nativeQuery = true)
    Set<Recipe> findByRecipeIngredientsIngredientNameIn(@Param("ingredients") Set<String> ingredients);
    */

    Set<Recipe> findByRecipeIngredientsIngredientNameInAndNumberOfServings(Set<String> ingredients,
                                                                           Integer numberOfServings);

    // Alternative method
    /*
    @Query(value = "SELECT * FROM recipes r " +
             "INNER JOIN recipe_ingredients ri " +
             "ON r.id = ri.recipe_id " +
             "INNER JOIN ingredients i " +
             "ON ri.ingredient_id = i.id " +
             "WHERE i.name IN (:ingredients) " +
             "AND r.number_of_servings = :number_of_servings",
            nativeQuery = true)
     Set<Recipe> findByRecipeIngredientsIngredientNameInAndNumberOfServings(
             @Param("ingredients") Set<String> ingredient, @Param("number_of_servings") Integer numberOfServings);
    */


    Set<Recipe> findByInstructionContainingIgnoreCaseAndRecipeIngredientsIngredientNameNotIn(String instruction,
                                                                                             Set<String> ingredients);

    // Alternative method
    /*
    @Query(value = "SELECT * FROM recipes r " +
            "INNER JOIN recipe_ingredients ri " +
            "ON r.id = ri.recipe_id " +
            "INNER JOIN ingredients i " +
            "ON ri.ingredient_id = i.id " +
            "WHERE i.name NOT IN (:ingredients) " +
            "AND LOWER(r.instruction) LIKE LOWER(CONCAT('%', :instruction,'%'))",
            nativeQuery = true)
    Set<Recipe> findByInstructionContainingIgnoreCaseAndRecipeIngredientsIngredientNameNotIn(
            @Param("instruction") String instruction, @Param("ingredients") Set<String> ingredientName);
    */

}
