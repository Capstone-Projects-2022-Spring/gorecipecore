package com.cis.gorecipe.controller;

import com.cis.gorecipe.BaseTest;
import com.cis.gorecipe.repository.IngredientRepository;
import com.cis.gorecipe.repository.RecipeRepository;
import com.cis.gorecipe.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Disabled
class RecipeControllerTest extends BaseTest {


    private final Logger logger = LoggerFactory.getLogger(RecipeControllerTest.class);

    @Autowired
    UserController controller;

    @Autowired
    UserRepository userRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    RecipeRepository recipeRepository;

    /**
     * Test whether a valid recipe can be added
     */
    @Test
    public void testAddRecipe() {}

    /**
     * Test whether a recipe that exists can be deleted
     */
    @Test
    public void testDeleteRecipe() {}

    /**
     * Test whether the API will reject an attempt to delete a recipe that doesn't exist
     */
    @Test
    public void testDeleteRecipeDoesntExist() {}

    /**
     * Test whether a recipe can be retrieved via its ID
     */
    @Test
    public void testGetRecipe() {}

    /**
     * Test whether the API will reject an attempt to get a recipe using an invalid ID
     */
    @Test
    public void testGetRecipeDoesntExist() {}

    /**
     * Test whether a list of all recipes can be retrieved
     */
    @Test
    public void testGetAllRecipes() {}

    /**
     * Test whether a subset of recipes can be returned based on a search query
     */
    @Test
    public void testSearchRecipes() {}
}
