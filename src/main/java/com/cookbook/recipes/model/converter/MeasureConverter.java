package com.cookbook.recipes.model.converter;

import com.cookbook.recipes.model.Measure;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class MeasureConverter implements AttributeConverter<Measure, String> {

    @Override
    public String convertToDatabaseColumn(Measure measure) {
        if (measure == null) {
            return null;
        }
        return measure.getValue();
    }

    @Override
    public Measure convertToEntityAttribute(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        return Stream.of(Measure.values())
                .filter(measure -> measure.getValue().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}