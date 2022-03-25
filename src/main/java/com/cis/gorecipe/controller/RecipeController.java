package com.cis.gorecipe.controller;

import com.cis.gorecipe.exception.RecipeNotFoundException;
import com.cis.gorecipe.model.DietaryRestriction;
import com.cis.gorecipe.model.Ingredient;
import com.cis.gorecipe.model.Recipe;
import com.cis.gorecipe.repository.DietaryRestrictionRepository;
import com.cis.gorecipe.repository.IngredientRepository;
import com.cis.gorecipe.repository.RecipeRepository;
import com.cis.gorecipe.service.SpoonacularService;
import org.hibernate.PropertyValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final RecipeRepository recipeRepository;

    /**
     * For interfacing with the Ingredient table in the database
     */
    private final IngredientRepository ingredientRepository;

    private final DietaryRestrictionRepository dietaryRestrictionRepository;

    private final SpoonacularService spoonacularService;

    public RecipeController(RecipeRepository recipeRepository, IngredientRepository ingredientRepository,
                            DietaryRestrictionRepository dietaryRestrictionRepository, SpoonacularService spoonacularService) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.dietaryRestrictionRepository = dietaryRestrictionRepository;
        this.spoonacularService = spoonacularService;
    }

    /**
     * @param recipe a recipe to be added to GoRecipe's collection
     * @return the recipe object created by the upload
     */
    @PostMapping("/")
    public ResponseEntity<Recipe> addRecipe(@RequestBody Recipe recipe) {
        try {
            recipe = recipeRepository.save(recipe);
            return ResponseEntity.ok().body(recipe);

            /* if the posted data is missing values that are required
             * or if we have a unique constraint violation */
        } catch (PropertyValueException | DataIntegrityViolationException | IllegalStateException e) {
            logger.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    /**
     * @param id the ID of the recipe to be removed
     * @return an HTTP response confirming if the recipe has been removed from the system
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        if (!recipeRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        recipeRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * @param id the ID of the recipe to be fetched
     * @return the recipe object that has been requested
     */
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {

        Recipe recipe = recipeRepository
                .findById(id)
                .orElseThrow(() ->
                        new RecipeNotFoundException("Unable to find user " + id)
                );

        return ResponseEntity.ok().body(recipe);
    }

    /**
     * @return a list of all recipes in the database
     */
    @GetMapping("/all")
    public ResponseEntity<List<Recipe>> getAllRecipes() {

        return ResponseEntity.ok().body(recipeRepository.findAll());
    }

    /**
     * @param ingredients an optional string of comma-separated ingredient names (e.g. "tomato,salmon,flour")
     * @param restrictions an optional string of comma-separated dietary restriction IDs (e.g. "2,5,1")
     * @param query an optional string that should occur somewhere in the recipe (either recipe body or title)
     * @return a list of recipes that meet the searchQuery parameters
     */
    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestParam(name = "ingredients", required = false) String ingredients, // comma separated ingredient IDs
                                                      @RequestParam(name = "restrictions", required = false) String restrictions, // comma separated restriction IDs
                                                      @RequestParam(name = "query", required = false) String query) {

        // if no search params are provided, we don't return anything
        if (ingredients == null && restrictions == null && query == null)
            return ResponseEntity.badRequest().build();

        if (query == null)
            query = "(.*?)";
        else
            query = ".*" + query + ".*";

        List<Ingredient> ingredientList = new ArrayList<>();
        if (ingredients != null)
            ingredientList = ingredientRepository.findAllById(Arrays.asList(ingredients.split(",")));

        List<DietaryRestriction> restrictionList = new ArrayList<>();
        if (restrictions != null)
            restrictionList = dietaryRestrictionRepository
                    .findAllById(
                              Stream.of(restrictions.split(","))
                                    .map(Long::valueOf)
                                    .collect(Collectors.toList())
                    );

        List<DietaryRestriction> finalRestrictionList = restrictionList;
        List<Ingredient> finalIngredientList = ingredientList;
        String finalQuery = query;

        List<Recipe> recipes = recipeRepository.findAll()
                .stream()
                .filter(recipe -> {
                    for (DietaryRestriction d : finalRestrictionList)
                        for (Ingredient i : d.getDisallowedIngredients())
                            if (recipe.getIngredients().contains(i))
                                return false;

                    return true;
                })
                .filter(recipe -> {
                    for (Ingredient recipeI : recipe.getIngredients())
                        for (Ingredient i : finalIngredientList)
                            if (recipeI.equals(i))
                                return true;

                    return false;
                })
                .filter(recipe -> (recipe.getContent().matches(finalQuery) ||
                                   recipe.getName().matches(finalQuery)))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(recipes);
    }
}
