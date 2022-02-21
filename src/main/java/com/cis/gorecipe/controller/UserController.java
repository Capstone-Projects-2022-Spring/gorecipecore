package com.cis.gorecipe.controller;

import com.cis.gorecipe.dto.UserDTO;
import com.cis.gorecipe.model.Recipe;
import com.cis.gorecipe.repository.IngredientRepository;
import com.cis.gorecipe.repository.RecipeRepository;
import com.cis.gorecipe.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserRepository userRepository;

    private RecipeRepository recipeRepository;

    private IngredientRepository ingredientRepository;

    public UserController(UserRepository userRepository, RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public ResponseEntity<UserDTO> createUser(UserDTO userDTO) {
        return null;
    }

    public ResponseEntity<Void> deleteUser(Long id) {
        return null;
    }

    public ResponseEntity<UserDTO> updateUser(UserDTO userDTO) {
        return null;
    }

    public ResponseEntity<UserDTO> getUser(Long id) {
        return null;
    }

    public ResponseEntity<UserDTO> login(String username, String password) {
        return null;
    }

    public ResponseEntity<List<Recipe>> getSavedRecipes(Long userId) {
        return null;
    }

    public ResponseEntity<Void> saveRecipeToAccount(Long userId, Long recipeId) {
        return null;
    }

    public ResponseEntity<Void> unsaveRecipeFromAccount(Long userId, Long recipeId) {
        return null;
    }

    public ResponseEntity<Void> addDietaryRestrictionToAccount(Long userId, Long dietaryRestrictionId) {
        return null;
    }

    public ResponseEntity<Void> removeDietaryRestrictionToAccount(Long userId, Long dietaryRestrictionId) {
        return null;
    }
}
