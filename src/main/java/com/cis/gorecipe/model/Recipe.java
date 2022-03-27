package com.cis.gorecipe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.checkerframework.common.reflection.qual.ClassBound;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class allows GoRecipe to store the recipes that will power the core feature of the application
 */
@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class Recipe {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * How many minutes does the recipe take to prepare
     */
    private Integer prepTime;

    /**
     * If the recipe was sourced from the Spoonacular API, what is their ID for it
     */
    @Column(unique = true)
    private Long spoonacularId;

    /**
     * The unique recipe name (e.g. French Onion Soup), also serves as PK
     */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * The formatted text containing the recipe instructions
     */
    @Lob
    private String instructions;

    /**
     * The list of ingredients to be used in the recipe
     */
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "recipe_ingredients",
//            joinColumns = @JoinColumn(name = "recipe_id"),
//            inverseJoinColumns = @JoinColumn(name = "ingredient_name"))
    private List<Ingredient> ingredients = new ArrayList<>();

    /**
     * This collection stores the ingredient names and quantities
     */
    @ElementCollection
    private List<String> verboseIngredients = new ArrayList<>();

    /**
     * An optional hyperlink to an image of the prepared recipe
     */
    private String imageURL;

    /**
     * An optional hyperlink to a 3rd party video demonstrating the recipe
     */
    private String videoURL;

    /**
     * The hyperlink to where the recipe is from (e.g. NYT Cooking, AllRecipes, Bon Appétit, etc.)
     */
    private String sourceURL;

    /**
     * @param ingredient an ingredient to be added to the recipe's ingredient list
     * @return the recipe object
     */
    public Recipe addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        return this;
    }
}
