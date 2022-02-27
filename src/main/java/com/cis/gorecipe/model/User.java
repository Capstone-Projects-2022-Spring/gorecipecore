package com.cis.gorecipe.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.sql.Date;
import java.util.*;

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

    /**
     * The user's birthday
     */
    @Column(nullable = false)
    private Date birthDate;

    /**
     * A list of ingredients which the user would like to cook with
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private Set<Ingredient> favoriteIngredients = new HashSet<>();

    /**
     * A list of recipes which the user would like to revisit in the future
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private Set<Recipe> savedRecipes = new HashSet<>();

    /**
     * A list of restrictions on which recipes the user can cook
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private Set<DietaryRestriction> dietaryRestrictions = new HashSet<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", favoriteIngredients=" + favoriteIngredients +
                ", savedRecipes=" + savedRecipes +
                ", dietaryRestrictions=" + dietaryRestrictions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
               Objects.equals(username, user.username) &&
               Objects.equals(password, user.password) &&
               Objects.equals(email, user.email) &&
               Objects.equals(firstName, user.firstName) &&
               Objects.equals(lastName, user.lastName) &&
               Objects.equals(birthDate, user.birthDate);

        /*Objects.equals(favoriteIngredients, user.favoriteIngredients) &&
               Objects.equals(savedRecipes, user.savedRecipes) &&
               Objects.equals(dietaryRestrictions, user.dietaryRestrictions);*/
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, firstName, lastName, birthDate, favoriteIngredients, savedRecipes, dietaryRestrictions);
    }
}
