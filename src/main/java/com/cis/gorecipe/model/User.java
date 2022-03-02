package com.cis.gorecipe.model;

import com.cis.gorecipe.util.Passwords;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
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

        System.out.println("1 " + id.equals(user.id));
        System.out.println("2 " + username.equals(user.username));
        System.out.println("3 " + password.equals(user.password));
        System.out.println("4 " + email.equals(user.email));
        System.out.println("5 " + firstName.equals(user.firstName) );
        System.out.println("6 " + lastName.equals(user.lastName));

        return id.equals(user.id) &&
               username.equals(user.username) &&
               password.equals(user.password) &&
               email.equals(user.email) &&
               firstName.equals(user.firstName) &&
               lastName.equals(user.lastName) &&
               birthDate.equals(user.birthDate);

        /*Objects.equals(favoriteIngredients, user.favoriteIngredients) &&
               Objects.equals(savedRecipes, user.savedRecipes) &&
               Objects.equals(dietaryRestrictions, user.dietaryRestrictions);*/
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, firstName, lastName, birthDate);//, favoriteIngredients, savedRecipes, dietaryRestrictions);
    }
}
