package com.cookbook.recipes.mapper;

import com.cookbook.recipes.model.*;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static com.cookbook.recipes.util.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearchFilterMapperTest {

    private final SearchFilterMapper searchFilterMapper = new SearchFilterMapperImpl();

    @ParameterizedTest
    @MethodSource("provideData")
    void toSearchFilter(final GeneratedFilterCriteria generatedFilterCriteria,
                        final FilterCriteria filterCriteria,
                        final Set<String> ingredients,
                        final String instruction,
                        final Boolean isVegetarian,
                        final Integer numberOfServings) {
        val generatedFilterValues = GeneratedFilterValues.builder()
                .ingredients(ingredients)
                .instruction(instruction)
                .isVegetarian(isVegetarian)
                .numberOfServings(numberOfServings)
                .build();
        val generatedSearchFilter = GeneratedSearchFilter.builder()
                .filterCriteria(generatedFilterCriteria)
                .filterValues(generatedFilterValues)
                .build();
        val filterValues = FilterValues.builder()
                .ingredients(ingredients)
                .instruction(instruction)
                .isVegetarian(isVegetarian)
                .numberOfServings(numberOfServings)
                .build();
        val searchFilterExpected = SearchFilter.builder()
                .filterCriteria(filterCriteria)
                .filterValues(filterValues)
                .build();

        val searchFilterActual = searchFilterMapper.toSearchFilter(generatedSearchFilter);

        assertThat(searchFilterActual).usingRecursiveComparison().isEqualTo(searchFilterExpected);

    }

    @Test
    void toSearchFilter_generatedSearchFilterIsNull() {
        val searchFilterActual = searchFilterMapper.toSearchFilter(null);

        assertThat(searchFilterActual).isNull();
    }

    @Test
    void mapFilterCriteria_generatedFilterCriteriaIsNull() {
        val filterCriteriaActual = searchFilterMapper.mapFilterCriteria(null);

        assertThat(filterCriteriaActual).isNull();
    }

    @Test
    void mapFilterCriteria_illegalArgumentException() {
        val exception = assertThrows(IllegalArgumentException.class,
                () -> GeneratedFilterCriteria.valueOf("WRONG VALUE"));

        assertThat(exception.getMessage())
                .isEqualTo("No enum constant com.cookbook.recipes.model.GeneratedFilterCriteria.WRONG VALUE");
    }

    @Test
    void mapFilterValue_generatedFilterValueIsNull() {
        val filterValuesActual = searchFilterMapper.mapFilterValue(null);

        assertThat(filterValuesActual).isNull();
    }

    private static Stream<Arguments> provideData() {
        return Stream.of(
                Arguments.of(GeneratedFilterCriteria.INSTRUCTION,
                        FilterCriteria.INSTRUCTION,
                        null, INSTRUCTION_TEXT, null, null),
                Arguments.of(GeneratedFilterCriteria.IS_VEGETARIAN,
                        FilterCriteria.IS_VEGETARIAN,
                        null, null, IS_VEGETARIAN, null),
                Arguments.of(GeneratedFilterCriteria.NUMBER_OF_SERVINGS,
                        FilterCriteria.NUMBER_OF_SERVINGS,
                        null, null, null, NUMBER_OF_SERVINGS),
                Arguments.of(GeneratedFilterCriteria.NUMBER_OF_SERVINGS_GREATER_THAN_EQUAL,
                        FilterCriteria.NUMBER_OF_SERVINGS_GREATER_THAN_EQUAL,
                        null, null, null, NUMBER_OF_SERVINGS),
                Arguments.of(GeneratedFilterCriteria.INCL_INGREDIENTS,
                        FilterCriteria.INCL_INGREDIENTS,
                        SET_OF_INGREDIENT_NAMES, null, null, null),
                Arguments.of(GeneratedFilterCriteria.EXCL_INGREDIENTS,
                        FilterCriteria.EXCL_INGREDIENTS,
                        SET_OF_INGREDIENT_NAMES, null, null, null),
                Arguments.of(GeneratedFilterCriteria.IS_VEGETARIAN_AND_NUMBER_OF_SERVINGS,
                        FilterCriteria.IS_VEGETARIAN_AND_NUMBER_OF_SERVINGS,
                        null, null, IS_VEGETARIAN, NUMBER_OF_SERVINGS),
                Arguments.of(GeneratedFilterCriteria.INGREDIENT_AND_NUMBER_OF_SERVINGS,
                        FilterCriteria.INGREDIENT_AND_NUMBER_OF_SERVINGS,
                        SET_OF_INGREDIENT_NAMES, null, null, NUMBER_OF_SERVINGS),
                Arguments.of(GeneratedFilterCriteria.EXCL_INGREDIENT_AND_INCL_INSTRUCTION,
                        FilterCriteria.EXCL_INGREDIENT_AND_INCL_INSTRUCTION,
                        SET_OF_INGREDIENT_NAMES, INSTRUCTION_TEXT, null, null)
        );
    }
}