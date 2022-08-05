package com.cookbook.recipes.model;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RECIPE_INGREDIENTS")
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Cascade(CascadeType.PERSIST)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    @Cascade(CascadeType.PERSIST)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private Integer amount;

    @Column(name = "measures")
    private Measure measure;

}
