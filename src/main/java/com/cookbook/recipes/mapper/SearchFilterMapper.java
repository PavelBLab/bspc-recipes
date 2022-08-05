package com.cookbook.recipes.mapper;

import com.cookbook.recipes.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public interface SearchFilterMapper {

    @Mapping(target = "filterCriteria", source = "filterCriteria", qualifiedByName = "mapFilterCriteria")
    @Mapping(target = "filterValues", source = "filterValues", qualifiedByName = "mapFilterValue")
    SearchFilter toSearchFilter(final GeneratedSearchFilter generatedSearchFilter);

    @Named("mapFilterCriteria")
    @ValueMapping(target = "IS_VEGETARIAN", source = "IS_VEGETARIAN")
    @ValueMapping(target = "NUMBER_OF_SERVINGS", source = "NUMBER_OF_SERVINGS")
    @ValueMapping(target = "INSTRUCTION", source = "INSTRUCTION")
    @ValueMapping(
            target = "NUMBER_OF_SERVINGS_GREATER_THAN_EQUAL",
            source = "NUMBER_OF_SERVINGS_GREATER_THAN_EQUAL")
    @ValueMapping(target = "INCL_INGREDIENTS", source = "INCL_INGREDIENTS")
    @ValueMapping(target = "EXCL_INGREDIENTS", source = "EXCL_INGREDIENTS")
    @ValueMapping(
            target = "IS_VEGETARIAN_AND_NUMBER_OF_SERVINGS",
            source = "IS_VEGETARIAN_AND_NUMBER_OF_SERVINGS")
    @ValueMapping(
            target = "INGREDIENT_AND_NUMBER_OF_SERVINGS",
            source = "INGREDIENT_AND_NUMBER_OF_SERVINGS")
    @ValueMapping(
            target = "EXCL_INGREDIENT_AND_INCL_INSTRUCTION",
            source = "EXCL_INGREDIENT_AND_INCL_INSTRUCTION")
    FilterCriteria mapFilterCriteria(final GeneratedFilterCriteria generatedFilterCriteria);

    @Named("mapFilterValue")
    @Mapping(target = "ingredients", source = "ingredients")
    @Mapping(target = "instruction", source = "instruction")
    @Mapping(target = "isVegetarian", source = "isVegetarian")
    @Mapping(target = "numberOfServings", source = "numberOfServings")
    FilterValues mapFilterValue(final GeneratedFilterValues generatedFilterValues);

}
