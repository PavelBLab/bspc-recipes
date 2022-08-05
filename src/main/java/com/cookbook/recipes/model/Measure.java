package com.cookbook.recipes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Measure {

    GRAM("gram"),
    ML("ml"),
    CUP("cup"),
    TEASPOON("teaspoon"),
    TABLESPOON("tablespoon");

    private String value;

}
