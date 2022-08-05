package com.cookbook.recipes.model.converter;

import com.cookbook.recipes.model.Measure;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MeasureConverterTest {

    private final MeasureConverter measureConverter = new MeasureConverter();

    @ParameterizedTest
    @MethodSource("provideMeasureData")
    void convertToDatabaseColumn(final Measure measureExpected, final String measureValueExpected) {
        val measureValueActual = measureConverter.convertToDatabaseColumn(measureExpected);

        assertThat(measureValueActual).isEqualTo(measureValueExpected);
    }

    @Test
    void convertToDatabaseColumn_measureIsNull() {
        val measureValueActual = measureConverter.convertToDatabaseColumn(null);

        assertThat(measureValueActual).isNull();
    }

    @ParameterizedTest
    @MethodSource("provideMeasureData")
    void convertToEntityAttribute(final Measure measureExpected, final String measureValueExpected) {
        val measureActual = measureConverter.convertToEntityAttribute(measureValueExpected);

        assertThat(measureActual).isEqualTo(measureExpected);

    }

    @ParameterizedTest
    @NullAndEmptySource
    void convertToEntityAttribute_measureValueIsNullOrEmpty(String input) {
        val measureActual = measureConverter.convertToEntityAttribute(input);

        assertThat(measureActual).isNull();

    }

    private static Stream<Arguments> provideMeasureData() {
        return Stream.of(
                Arguments.of(Measure.GRAM, "gram"),
                Arguments.of(Measure.ML, "ml"),
                Arguments.of(Measure.CUP, "cup"),
                Arguments.of(Measure.TEASPOON, "teaspoon"),
                Arguments.of(Measure.TABLESPOON, "tablespoon"));
    }

}