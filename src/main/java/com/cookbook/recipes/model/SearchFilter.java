package com.cookbook.recipes.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchFilter {

    private FilterCriteria filterCriteria;
    private FilterValues filterValues;
}
