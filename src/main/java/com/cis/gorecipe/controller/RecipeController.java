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

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final Logger logger = LoggerFactory.getLogger(RecipeController.class);

    private RecipeRepository recipeRepository;

    private IngredientRepository ingredientRepository;

    public RecipeController(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @PostMapping("/")
    public ResponseEntity<Recipe> addRecipe(@RequestBody Recipe recipe) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return null;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(@PathParam("query") String searchQuery) {
        return null;
    }
}
