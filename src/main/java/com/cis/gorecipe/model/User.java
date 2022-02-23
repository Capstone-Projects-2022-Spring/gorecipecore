package com.cis.gorecipe.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
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
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * A salted and hashed string for authentication
     */
    @Column(nullable = false)
    private String password;

    /**
     * An email address to allow communication with the user
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * The user's first name
     */
    @Column(nullable = false)
    private String firstName;

    /**
     * The user's last name
     */
    @Column(nullable = false)
    private String lastName;

    /**mannin
     * The user's birthday
     */
    @Column(nullable = false)
    private Date birthDate;

    /**
     * A list of ingredients which the user would like to cook with
     */
    @ManyToMany
    @Column(nullable = false)
    private List<Ingredient> favoriteIngredients = new ArrayList<>();

    /**
     * A list of recipes which the user would like to revisit in the future
     */
    @ManyToMany
    @Column(nullable = false)
    private List<Recipe> savedRecipes = new ArrayList<>();;

    /**
     * A list of restrictions on which recipes the user can cook
     */
    @ManyToMany
    @Column(nullable = false)
    private List<DietaryRestriction> dietaryRestrictions = new ArrayList<>();;
}
