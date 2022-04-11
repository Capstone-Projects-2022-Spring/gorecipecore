package com.cis.gorecipe.controller;

import com.cis.gorecipe.exception.RecipeNotFoundException;
import com.cis.gorecipe.exception.UserNotFoundException;
import com.cis.gorecipe.model.Ingredient;
import com.cis.gorecipe.model.Recipe;
import com.cis.gorecipe.model.User;
import com.cis.gorecipe.repository.DietaryRestrictionRepository;
import com.cis.gorecipe.repository.IngredientRepository;
import com.cis.gorecipe.repository.RecipeRepository;
import com.cis.gorecipe.repository.UserRepository;
import com.cis.gorecipe.service.SpoonacularService;
import io.swagger.annotations.ApiOperation;
import org.hibernate.PropertyValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private final UserRepository userRepository;

    /**
     * For interfacing with the DietaryRestriction table in the database
     */
    private final DietaryRestrictionRepository dietaryRestrictionRepository;

    /**
     * Handles all interactions with the Spoonacular API
     */
    private final SpoonacularService spoonacularService;

    public RecipeController(RecipeRepository recipeRepository, IngredientRepository ingredientRepository,
                            UserRepository userRepository, DietaryRestrictionRepository dietaryRestrictionRepository,
                            SpoonacularService spoonacularService) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.userRepository = userRepository;
        this.dietaryRestrictionRepository = dietaryRestrictionRepository;
        this.spoonacularService = spoonacularService;
    }

    private List<Recipe> saveRecipes(List<Recipe> recipes) {

        /* a very stupid workaround for ManyToMany relation b/c I don't really understand the best way to use them
         * save the recipe with no ingredients -> save the ingredients -> save the recipe with ingredients */
        for (Recipe r : recipes) {

            if (recipeRepository.existsByName(r.getName()))
                continue;

            List<Ingredient> i = r.getIngredients();
            r.setIngredients(new ArrayList<>());
            recipeRepository.save(r);
            ingredientRepository.saveAll(i);
            r.setIngredients(i);
        }

        recipes.forEach((r) -> {
            if (!recipeRepository.existsByName(r.getName()))
                recipeRepository.save(r);
        });

        recipes = recipeRepository.findAllByNameIn(recipes
                .stream()
                .map(Recipe::getName)
                .collect(Collectors.toList()));

        return recipes;
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
     * @param intolerances an optional comma separated string of 1 or more intolerances
     * @param diet         an optional comma separated string of 1 or more diets
     * @param cuisine      an optional comma separated string of 1 or more cuisines
     * @param query        a required string that should occur somewhere in the recipe (either recipe body or title)
     * @return a list of recipes that meet the searchQuery parameters
     * @throws Exception
     */
    @GetMapping("/search")
    @ApiOperation(value = "Search for recipes",
            notes = "<b>Intolerances is a comma separated string of 1 or more of the following:</b> dairy, egg, " +
                    "gluten, peanut, sesame, seafood, shellfish, soy, sulfite, tree nut, and wheat\n" +
                    "<b>Diet is a comma separated string of 1 or more of the following:</b> pescetarian," +
                    " lacto vegetarian, ovo vegetarian, vegan, and vegetarian\n" +
                    "<b>Cuisine is a comma separated string of 1 or more of the following:</b> african," +
                    " chinese, japanese, korean, vietnamese, thai, indian, british, irish, french, " +
                    "italian, mexican, spanish, middle eastern, jewish, american, cajun, southern," +
                    " greek, german, nordic, eastern european, caribbean, or latin american\n" +
                    "<b>Ingredients is a comma separated string of 0 or more ingredient names (e.g. tomato, mushroom, etc)")
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestParam(name = "intolerances", required = false) String intolerances,
                                                      @RequestParam(name = "diet", required = false) String diet,
                                                      @RequestParam(name = "cuisine", required = false) String cuisine,
                                                      @RequestParam(name = "query") String query,
                                                      @RequestParam(name = "ingredients", required = false) String ingredients) throws Exception {

        Map<String, String> searchParameters = new HashMap<>();

        searchParameters.put("query", query);
        searchParameters.put("instructionsRequired", "True");
        searchParameters.put("number", "25"); // return 100 results
        searchParameters.put("cuisine", cuisine);
        searchParameters.put("diet", diet);
        searchParameters.put("intolerances", intolerances);
        searchParameters.put("ingredients", ingredients);

        List<Recipe> recipes = spoonacularService.search(searchParameters);

        recipes = saveRecipes(recipes);

        return ResponseEntity.ok().body(recipes);
    }

    /**
     *
     * @param userId the ID of the user whom we would like to recommend recipes to
     * @return a list of recommended recipes
     */
    @GetMapping("/recommend/{userId}")
    public ResponseEntity<List<Recipe>> getRecommendedRecipes(@PathVariable Long userId) throws Exception {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(userId));


        return ResponseEntity.ok(spoonacularService.recommend(user.getSavedRecipes()));
    }

    @GetMapping("/explore")
    public ResponseEntity<Map<String, List<Recipe>>> getExplorePage() throws Exception {

        Map<String, List<Recipe>> explore_info = new HashMap<>();
        Map<String, String> searchParameters = new HashMap<>();

        searchParameters.put("instructionsRequired", "True");
        searchParameters.put("number", "10"); // return 100 results
        searchParameters.put("type", null);

        searchParameters.put("diet", "vegan");
        explore_info.put("vegan", spoonacularService.search(searchParameters));
        searchParameters.remove("diet");

        searchParameters.put("type", "main course");
        explore_info.put("dinner", spoonacularService.search(searchParameters));

        searchParameters.put("type", "breakfast");
        explore_info.put("breakfast", spoonacularService.search(searchParameters));

        searchParameters.put("type", "dessert");
        explore_info.put("dessert", spoonacularService.search(searchParameters));

        explore_info.replaceAll(
                (k, v) -> saveRecipes(explore_info.get(k))
        );

        explore_info.put("quick",
                recipeRepository.findAll()
                        .stream()
                        .filter((recipe) ->
                                recipe.getPrepTime() <= 30
                        )
                        .limit(10)
                        .collect(Collectors.toList())
        );

        return ResponseEntity.ok(explore_info);
    }
}
