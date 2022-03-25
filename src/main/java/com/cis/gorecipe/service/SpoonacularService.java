package com.cis.gorecipe.service;

import com.cis.gorecipe.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;

public interface SpoonacularService {

    String APIKEY = System.getenv().get("SPOONACULAR_API_KEY");

    String APIHOST = "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com";

    HttpClient client = HttpClient.newHttpClient();

    Gson parser = new Gson();

    List<Recipe> search() throws Exception;



}
