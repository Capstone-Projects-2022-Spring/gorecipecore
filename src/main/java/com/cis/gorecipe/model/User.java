package com.cis.gorecipe.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

/**
 * This class allows GoRecipe to store user information, including login information and personal preferences (such as favorite ingredients, favorite recipes, etc.)
 */
@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class User {

    /**
     * The primary key of the user
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * A unique string for user login
     */
    @Column(unique = true)
    private String username;

    /**
     * A salted and hashed string for authentication
     */
    @Getter(AccessLevel.NONE)
    private String password;

    /**
     * An email address to allow communication with the user
     */
    @Column(unique = true)
    private String email;

    /**
     * The user's first name
     */
    private String firstName;

    /**
     * The user's last name
     */
    private String lastName;

    /**
     * The user's birthday
     */
    private Date birthDate;

    /**
     * A list of ingredients which the user would like to cook with
     */
    @ManyToMany
    private List<Ingredient> favoriteIngredients;

    /**
     * A list of recipes which the user would like to revisit in the future
     */
    @ManyToMany
    private List<Recipe> savedRecipes;

    /**
     * A list of restrictions on which recipes the user can cook
     */
    @ManyToMany
    private List<DietaryRestriction> dietaryRestrictions;
}
