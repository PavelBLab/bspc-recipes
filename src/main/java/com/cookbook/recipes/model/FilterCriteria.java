package com.cookbook.recipes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FilterCriteria {

    IS_VEGETARIAN("by_is_vegetarian"),
    NUMBER_OF_SERVINGS("by_number_of_servings"),
    INSTRUCTION("by_instruction"),
    NUMBER_OF_SERVINGS_GREATER_THAN_EQUAL("by_number_of_servings_greater_than_equal"),
    INCL_INGREDIENTS("by_incl_ingredients"),
    EXCL_INGREDIENTS("by_excl_ingredients"),
    IS_VEGETARIAN_AND_NUMBER_OF_SERVINGS("by_is_vegetarian_and_number_of_servings"),
    INGREDIENT_AND_NUMBER_OF_SERVINGS("by_ingredient_and_number_of_servings"),
    EXCL_INGREDIENT_AND_INCL_INSTRUCTION("excl_ingredient_and_incl_instruction"),
    INSTRUCTION_AND_IS_VEGETARIAN_AND_NUMBER_OF_SERVINGS
            ("by_instruction_and_is_vegetarian_and_number_of_servings"),
    INGREDIENTS_AND_INSTRUCTION_AND_IS_VEGETARIAN_AND_NUMBER_OF_SERVINGS(
            "by_ingredients_and_instruction_and_is_vegetarian_and_number_of_servings");

    private String value;
}
