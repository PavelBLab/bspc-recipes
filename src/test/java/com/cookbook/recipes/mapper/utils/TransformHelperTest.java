package com.cookbook.recipes.mapper.utils;

import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class TransformHelperTest {

    @ParameterizedTest
    @CsvSource(value = {
            "test, te&&<>st",
            " , ",
            "null, null"},
            nullValues = "null")
    void sanitize(final String sanitizedTextExpected, final String unSanitizedText) {
        val sanitizedTextActual = TransformHelper.sanitize(unSanitizedText);

        assertThat(sanitizedTextActual).isEqualTo(sanitizedTextExpected);
    }
}