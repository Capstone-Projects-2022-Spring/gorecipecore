package com.cis.gorecipe.controller;

import com.cis.gorecipe.dto.UserDTO;
import com.cis.gorecipe.model.Recipe;
import com.cis.gorecipe.repository.IngredientRepository;
import com.cis.gorecipe.repository.RecipeRepository;
import com.cis.gorecipe.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserRepository userRepository;

    private RecipeRepository recipeRepository;

    private IngredientRepository ingredientRepository;

    public UserController(UserRepository userRepository, RecipeRepository recipeRepository,
                          IngredientRepository ingredientRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @PostMapping("/")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return null;
    }

    @PutMapping("/")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestParam("username") String username,
                                         @RequestParam("password") String password) {
        return null;
    }

    @GetMapping("/{userId}/recipes")
    public ResponseEntity<List<Recipe>> getSavedRecipes(@PathVariable Long userId) {
        return null;
    }

    @PostMapping("/{userId}/recipes/{recipeId}")
    public ResponseEntity<Void> saveRecipeToAccount(@PathVariable Long userId, @PathVariable Long recipeId) {
        return null;
    }

    @DeleteMapping("/{userId}/recipes/{recipeId}")
    public ResponseEntity<Void> unsaveRecipeFromAccount(@PathVariable Long userId, @PathVariable Long recipeId) {
        return null;
    }

    @PostMapping("/{userId}/dietary-restrictions/{dietaryRestrictionId}")
    public ResponseEntity<Void> addDietaryRestrictionToAccount(@PathVariable Long userId,
                                                               @PathVariable Long dietaryRestrictionId) {
        return null;
    }

    @DeleteMapping("/{userId}/dietary-restrictions/{dietaryRestrictionId}")
    public ResponseEntity<Void> removeDietaryRestrictionToAccount(@PathVariable Long userId,
                                                                  @PathVariable Long dietaryRestrictionId) {
        return null;
    }
}
