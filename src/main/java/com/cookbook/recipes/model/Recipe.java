package com.cookbook.recipes.model;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RECIPES")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "names")
    @NonNull
    private String name;
    @Column(name = "descriptions")
    private String description;
    @Column(name = "images")
    private String image;
    @Column(name = "instructions")
    private String instruction;
    private LocalDate createdAt;
    private Boolean isVegetarian;
    private Integer numberOfServings;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    private Set<RecipeIngredient> recipeIngredients;
}
