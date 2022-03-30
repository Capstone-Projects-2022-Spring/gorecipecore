package com.cis.gorecipe.controller;

import com.cis.gorecipe.BaseTest;
import com.cis.gorecipe.model.Recipe;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RecipeControllerTest extends BaseTest {

    private final Logger logger = LoggerFactory.getLogger(RecipeControllerTest.class);

    @Autowired
    RecipeController controller;

    /**
     * Test whether a valid recipe can be added
     */
    @DirtiesContext
    @Test
    public void testAddRecipe() throws Exception {

        Recipe mockRecipe = new Recipe()
                .setId(1L)
                .setName("Tomato Sauce");

        String result = mockMvc.perform(post("/api/recipes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializer.writeValueAsString(mockRecipe)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Recipe actual = serializer.readValue(result, Recipe.class);

        assertEquals(actual, mockRecipe);
    }

    /**
     * Test whether a recipe that exists can be deleted
     */
    @DirtiesContext
    @Test
    public void testDeleteRecipe() throws Exception {

        Recipe mockRecipe = new Recipe()
                .setId(1L)
                .setName("Tomato Sauce");

        mockRecipe = recipeRepository.save(mockRecipe);

        mockMvc.perform(delete("/api/recipes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertFalse(recipeRepository.existsById(mockRecipe.getId()));
    }

    /**
     * Test whether the API will reject an attempt to delete a recipe that doesn't exist
     */
    @Test
    public void testDeleteRecipeDoesntExist() throws Exception {

        mockMvc.perform(delete("/api/recipes/123456789")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Test whether a recipe can be retrieved via its ID
     */
    @DirtiesContext
    @Test
    public void testGetRecipe() throws Exception {

        Recipe mockRecipe = new Recipe()
                .setId(1L)
                .setName("Tomato Sauce");

        mockRecipe = recipeRepository.save(mockRecipe);

        String result = mockMvc.perform(get("/api/recipes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Recipe actual = serializer.readValue(result, Recipe.class);

        assertEquals(actual, mockRecipe);
    }

    /**
     * Test whether the API will reject an attempt to get a recipe using an invalid ID
     */
    @Test
    public void testGetRecipeDoesntExist() throws Exception {

        mockMvc.perform(get("/api/recipes/123456789")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Test whether a list of all recipes can be retrieved
     */
    @DirtiesContext
    @Test
    public void testGetAllRecipes() throws Exception {

        Recipe[] recipes = new Recipe[] {
                            new Recipe()
                                .setId(1L)
                                .setName("Egg Salad"),
                            new Recipe()
                                .setId(2L)
                                .setName("Grilled Cheese"),
                            new Recipe()
                                .setId(3L)
                                .setName("Fried Rice")};

        recipeRepository.saveAll(Arrays.asList(recipes));

        String result = mockMvc.perform(get("/api/recipes/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Recipe> actual = Arrays.asList(serializer.readValue(result, Recipe[].class));

        MatcherAssert.assertThat(actual, is(Arrays.asList(recipes)));
    }

    /**
     * Test whether a subset of recipes can be returned based on a search query
     */
    @Test
    public void testSearchRecipes() throws Exception {

        when(spoonacularService.search(any()))
                .thenReturn(Arrays.asList(new Recipe().setName("corn").setSpoonacularId(1L),
                                          new Recipe().setName("tomato").setSpoonacularId(2L)));

        String result = mockMvc.perform(get("/api/recipes/search")
                .param("query", "soup")
                .param("intolerances", "egg")
                .param("diet", "vegan"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Recipe> actual = Arrays.asList(serializer.readValue(result, Recipe[].class));

        assertEquals(actual.size(), 2);
    }
}
