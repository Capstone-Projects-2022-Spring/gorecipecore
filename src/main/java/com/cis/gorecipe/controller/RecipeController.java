package com.cis.gorecipe.controller;

import com.cis.gorecipe.model.Recipe;
import com.cis.gorecipe.repository.IngredientRepository;
import com.cis.gorecipe.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * This class handles the API endpoints related to recipes
 */
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    /**
     * For logging any errors that occur during runtime (e.g. a recipe is not found)
     */
    private final Logger logger = LoggerFactory.getLogger(RecipeController.class);

    /**
     * For interfacing with the Recipe table in the database
     */
    private RecipeRepository recipeRepository;

    /**
     * For interfacing with the Ingredient table in the database
     */
    private IngredientRepository ingredientRepository;

    public RecipeController(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * @param recipe a recipe to be added to GoRecipe's collection
     * @return the recipe object created by the upload
     */
    @PostMapping("/")
    public ResponseEntity<Recipe> addRecipe(@RequestBody Recipe recipe) {
        return null;
    }

    /**
     * @param id the ID of the recipe to be removed
     * @return an HTTP response confirming if the recipe has been removed from the system
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        return null;
    }

    /**
     * @param id the ID of the recipe to be fetched
     * @return the recipe object that has been requested
     */
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        return null;
    }

    /**
     * @return a list of all recipes in the database
     */
    @GetMapping("/all")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return null;
    }

    /**
     * @param searchQuery a string of parameters
     * @return a list of recipes that meet the searchQuery parameters
     */
    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(@PathParam("query") String searchQuery) {
        return null;
    }
}
