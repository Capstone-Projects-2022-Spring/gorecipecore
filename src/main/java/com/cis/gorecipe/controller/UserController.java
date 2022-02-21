package com.cis.gorecipe.controller;

import com.cis.gorecipe.dto.UserDTO;
import com.cis.gorecipe.model.Recipe;
import com.cis.gorecipe.model.User;
import com.cis.gorecipe.repository.IngredientRepository;
import com.cis.gorecipe.repository.RecipeRepository;
import com.cis.gorecipe.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class handles the API endpoints related to user account management
 */
@RestController
@RequestMapping("/api/users")
public class UserController {


    /**
     * For logging any errors that occur during runtime (e.g. a user is not found)
     */
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * For interfacing with the User table in the database
     */
    private UserRepository userRepository;

    /**
     * For interfacing with the Recipe table in the database
     */
    private RecipeRepository recipeRepository;

    /**
     * For interfacing with the Ingredient table in the database
     */
    private IngredientRepository ingredientRepository;

    public UserController(UserRepository userRepository, RecipeRepository recipeRepository,
                          IngredientRepository ingredientRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * @param user the data from which a new user should be created
     * @return a DTO representing the newly created user
     */
    @PostMapping("/")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        return null;
    }

    /**
     * @param id the id of the user to be deleted
     * @return an HTTP response confirming if the user has been removed from the system
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return null;
    }

    /**
     * @param userDTO the data which should be used to update an existing user
     * @return a DTO representing the newly modified user
     */
    @PutMapping("/")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        return null;
    }

    /**
     * @param id the id of the user to be fetched
     * @return a DTO representing the requested user
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return null;
    }

    /**
     * @param username the username of some user in the system
     * @param password a plaintext password corresponding to the user with the specified username
     * @return an HTTP response that contains a DTO of the specified user if the login was successful and an error message if it failed
     */
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestParam("username") String username,
                                         @RequestParam("password") String password) {
        return null;
    }

    /**
     * @param userId the id of the user who's saved recipes are being requested
     * @return a list of recipes that the specified user saved
     */
    @GetMapping("/{userId}/recipes")
    public ResponseEntity<List<Recipe>> getSavedRecipes(@PathVariable Long userId) {
        return null;
    }

    /**
     * @param userId the id of the user who is saving the specified recipe
     * @param recipeId the id of the recipe which the user is attempting to save
     * @return an HTTP response that confirms if the recipe has been saved to the user's account
     */
    @PostMapping("/{userId}/recipes/{recipeId}")
    public ResponseEntity<Void> saveRecipeToAccount(@PathVariable Long userId, @PathVariable Long recipeId) {
        return null;
    }

    /**
     * @param userId the id of the user who is removing the specified recipe
     * @param recipeId the id of the recipe which the user is attempting to remove from their account
     * @return an HTTP response that confirms if the recipe has been unsaved to the user's account
     */
    @DeleteMapping("/{userId}/recipes/{recipeId}")
    public ResponseEntity<Void> unsaveRecipeFromAccount(@PathVariable Long userId, @PathVariable Long recipeId) {
        return null;
    }

    /**
     * @param userId the id of the user who is adding a dietary restriction to their account
     * @param dietaryRestrictionId the id of the dietary restriction that the user is attempting to add to their account
     * @return an HTTP response that confirms if the dietary restriction has been added to the user's account
     */
    @PostMapping("/{userId}/dietary-restrictions/{dietaryRestrictionId}")
    public ResponseEntity<Void> addDietaryRestrictionToAccount(@PathVariable Long userId,
                                                               @PathVariable Long dietaryRestrictionId) {
        return null;
    }

    /**
     * @param userId the id of the user who is removing a dietary restriction from their account
     * @param dietaryRestrictionId the id of the dietary restriction that the user is attempting to remove from their account
     * @return an HTTP response that confirms if the dietary restriction has been removed from the user's account
     */
    @DeleteMapping("/{userId}/dietary-restrictions/{dietaryRestrictionId}")
    public ResponseEntity<Void> removeDietaryRestrictionToAccount(@PathVariable Long userId,
                                                                  @PathVariable Long dietaryRestrictionId) {
        return null;
    }
}
