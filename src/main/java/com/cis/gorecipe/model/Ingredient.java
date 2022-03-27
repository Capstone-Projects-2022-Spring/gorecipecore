package com.cis.gorecipe.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * This class allows GoRecipe to store individual ingredients (e.g. tomato, steak, flour, etc) that may be used in Recipes
 */
@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class Ingredient {

    /**
     * A unique ingredient name (e.g. rice or salmon)
     */
    @Id
    private String name;

//    /**
//     * A list of recipes that this ingredient is in
//     */
//    @ManyToMany(mappedBy = "ingredients", fetch = FetchType.LAZY)
//    private List<Recipe> recipesThatContainIngredient = new ArrayList<>();
}
