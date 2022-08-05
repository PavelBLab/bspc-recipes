package com.cookbook.recipes.model;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterValues {

    private Set<String> ingredients;
    private String instruction;
    private Boolean isVegetarian;
    private Integer numberOfServings;

}
