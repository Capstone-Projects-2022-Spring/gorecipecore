package com.cis.gorecipe.service;

import com.cis.gorecipe.model.Ingredient;
import com.cis.gorecipe.model.Recipe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SpoonacularServiceImpl implements SpoonacularService {

    private Recipe parseRecipe(JsonObject result) {

        Recipe recipe = new Recipe()
                .setName(result.get("title").getAsString())
                .setImageURL(result.get("image").getAsString())
                .setPrepTime(result.get("readyInMinutes").getAsInt())
                .setSpoonacularId(result.get("id").getAsLong())
                .setContent(result.get("instructions").getAsString())
                .setSourceURL(result.get("sourceUrl").getAsString());

        for (JsonElement e : result.get("extendedIngredients").getAsJsonArray()) {

            JsonObject o = e.getAsJsonObject();
            recipe.addIngredient(new Ingredient()
                    .setName(o.get("name").getAsString()));
        }

        return recipe;
    }

    @Override
    public List<Recipe> search(Map<String, String> parameters) throws Exception {

        String url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/search?";

        if (parameters.containsKey("query"))
            url += "query=" + parameters.get("query") + "&";

        if (parameters.containsKey("instructionsRequired"))
            url += "instructionsRequired=" + parameters.get("instructionsRequired") + "&";

        if (parameters.containsKey("number"))
            url += "number=" + parameters.get("number");

        if (parameters.get("cuisine") != null)
            url += "cuisine=" + parameters.get("cuisine") + "&";

        if (parameters.get("diet") != null)
            url += "diet=" + parameters.get("diet") + "&";

        if (parameters.get("intolerances") != null)
            url += "intolerances=" + parameters.get("intolerances");

        if (url.endsWith("&"))
            url = url.substring(0, url.length() - 1);

        System.out.println(url);

        /* the initial search only includes basic recipe information, so we need to individually look up
         * recipes in a second set of requests to get ingredients, instructions, etc */
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .setHeader("X-RapidAPI-Host", APIHOST)
                .setHeader("X-RapidAPI-Key", APIKEY)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200)
            throw new Exception("Request to Spoonacular API failed!");

        JsonObject object = parser.fromJson(response.body(), JsonElement.class)
                .getAsJsonObject();

        /* perform secondary lookup to get detailed recipe information for each recipe */
        List<Recipe> recipes = new ArrayList<>();
        for (JsonElement element : object.get("results").getAsJsonArray()) {
            String id = element.getAsJsonObject().get("id").getAsString();

            request = HttpRequest.newBuilder()
                    .uri(URI.create("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/" +
                            id + "/information"))
                    .setHeader("X-RapidAPI-Host", APIHOST)
                    .setHeader("X-RapidAPI-Key", APIKEY)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200)
                throw new Exception("Request to Spoonacular API failed!");

            object = parser.fromJson(response.body(), JsonElement.class).getAsJsonObject();

            recipes.add(parseRecipe(object));
        }

        return recipes;
    }
}
