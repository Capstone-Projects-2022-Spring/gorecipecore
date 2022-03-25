package com.cis.gorecipe.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.TemporalType.DATE;

/**
 * This class allows GoRecipe to store user information, including login information and personal preferences (such as favorite ingredients, favorite recipes, etc.)
 */
@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class User {

    private static final Logger logger = LoggerFactory.getLogger(User.class);

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
    @Temporal(DATE)
    private java.util.Date birthDate;

    /**
     * A list of ingredients which the user would like to cook with
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<Ingredient> favoriteIngredients = new HashSet<>();

    /**
     * A list of recipes which the user would like to revisit in the future
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Set<Recipe> savedRecipes = new HashSet<>();

    /**
     * A list of restrictions on which recipes the user can cook
     */
    @ManyToMany(fetch = FetchType.LAZY)
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
        if (!(o instanceof User)) return false;

        User user = (User) o;

        /* call getters rather than fields so the method works with hibernate proxy classes during unit tests */
        return this.getId().equals(user.getId()) &&
                this.getUsername().equals(user.getUsername()) &&
                this.getPassword().equals(user.getPassword()) &&
                this.getEmail().equals(user.getEmail()) &&
                this.getFirstName().equals(user.getFirstName()) &&
                this.getLastName().equals(user.getLastName()) &&
                this.getBirthDate().equals(user.getBirthDate()) &&
                this.getDietaryRestrictions().equals(user.getDietaryRestrictions()) &&
                this.getSavedRecipes().equals(user.getSavedRecipes()) &&
                this.getFavoriteIngredients().equals(user.getFavoriteIngredients());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, firstName, lastName, birthDate, favoriteIngredients, savedRecipes, dietaryRestrictions);
    }
}
