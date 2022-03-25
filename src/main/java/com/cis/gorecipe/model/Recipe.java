package com.cis.gorecipe.model;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class allows GoRecipe to store the recipes that will power the core feature of the application
 */
@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class Recipe {

    /**
     * The primary key of the recipe
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The unique recipe name (e.g. French Onion Soup)
     */
    @Column(unique = true)
    private String name;

    /**
     * The formatted text containing the recipe instructions
     */
    @Lob
    private String content;

    /**
     * How many minutes does the recipe take to prepare
     */
    Integer prepTime;

    /**
     * If the recipe was sourced from the Spoonacular API, what is their ID for it
     */
    Long spoonacularId;

    /**
     * The list of ingredients to be used in the recipe
     */
    @ManyToMany
    private Set<Ingredient> ingredients = new HashSet<>();

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

    public Recipe addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        return this;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", prepTime=" + prepTime +
                ", spoonacularId=" + spoonacularId +
                ", ingredients=" + ingredients +
                ", imageURL='" + imageURL + '\'' +
                ", videoURL='" + videoURL + '\'' +
                '}';
    }
}
