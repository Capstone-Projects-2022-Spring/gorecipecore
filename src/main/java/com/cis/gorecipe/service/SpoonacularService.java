package com.cis.gorecipe.service;

import com.cis.gorecipe.model.Recipe;
import com.google.gson.Gson;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for SpoonacularServiceImpl
 */
public interface SpoonacularService {

    /**
     * The API key for the Spoonacular API
     */
    String APIKEY = System.getenv().get("SPOONACULAR_API_KEY");

    /**
     * The Spoonacular identifier for RapidApi
     */
    String APIHOST = "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com";

    /**
     * An HttpClient for sending POST/GET requests to the Spoonacular API
     */
    HttpClient client = HttpClient.newHttpClient();

    /**
     * A Google JSON Parser object for parsing the results of the Spoonacular API
     */
    Gson parser = new Gson();

    List<Recipe> search(Map<String, String> searchParameters) throws Exception;

    List<Recipe> recommend(Set<Recipe> userRecipes) throws Exception;
}
