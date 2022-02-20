package com.cis.gorecipe.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

/**
 * This class allows GoRecipe to store various dietary restrictions, such as allergies, intolerances, religious restrictions and more
 */
@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class DietaryRestriction {

    /**
     * The primary key of the dietary restriction
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The unique name of the dietary restriction (e.g. Halal, Paleo, Gluten Free)
     */
    @Column(unique = true)
    private String name;

    /**
     * A list of ingredients which cannot be used by any user who has this dietary restriction
     */
    @ManyToMany
    private List<Ingredient> disallowedIngredients;
}
