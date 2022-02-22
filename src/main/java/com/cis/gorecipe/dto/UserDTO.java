package com.cis.gorecipe.dto;

import com.cis.gorecipe.model.DietaryRestriction;
import com.cis.gorecipe.model.Ingredient;
import com.cis.gorecipe.model.Recipe;
import com.cis.gorecipe.model.User;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

/**
 * This class acts as a transfer object for the User class to allow serialization of Users without
 * making the password field visible
 */
@Getter
@Setter
public class UserDTO {

    /**
     * The primary key of the user
     */
    private Long id;


    /**
     * A unique string for user login
     */
    private String username;

    /**
     * An email address to allow communication with the user
     */
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
    private List<Ingredient> favoriteIngredients;

    /**
     * A list of recipes which the user would like to revisit in the future
     */
    private List<Recipe> savedRecipes;

    /**
     * A list of restrictions on which recipes the user can cook
     */
    private List<DietaryRestriction> dietaryRestrictions;

    /**
     *
     * @param user a User object to be converted for serialization
     */
    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.birthDate = user.getBirthDate();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.dietaryRestrictions = user.getDietaryRestrictions();
        this.savedRecipes = user.getSavedRecipes();
        this.favoriteIngredients = user.getFavoriteIngredients();
    }

    /**
     *
     * @param user a User object to be converted for serialization
     * @return a UserDTO object to be used for serialization
     */
    public static UserDTO mapFromUser(User user) {
        return new UserDTO(user);
    }

    /**
     *
     * @param userDTO a UserDTO object to be converted for internal use
     * @return a User object to be used internally
     */
    public static User mapToUser(UserDTO userDTO) {

        return new User().setEmail(userDTO.getEmail())
                         .setUsername(userDTO.getUsername())
                         .setFirstName(userDTO.getFirstName())
                         .setLastName(userDTO.getLastName())
                         .setBirthDate(userDTO.getBirthDate())
                         .setId(userDTO.getId())
                         .setFavoriteIngredients(userDTO.getFavoriteIngredients())
                         .setDietaryRestrictions(userDTO.getDietaryRestrictions())
                         .setSavedRecipes(userDTO.getSavedRecipes());
    }
}
