package com.cis.gorecipe.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
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
     * The list of ingredients to be used in the recipe
     */
    @ManyToMany
    private List<Ingredient> ingredients;

    /**
     * An optional hyperlink to a 3rd party video demonstrating the recipe
     */
    private String videoURL;
}
