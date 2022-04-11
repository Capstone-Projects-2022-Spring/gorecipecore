package com.cis.gorecipe.service;

import com.cis.gorecipe.model.Ingredient;
import com.cis.gorecipe.model.Recipe;
import com.cis.gorecipe.repository.RecipeRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class handles all interfacing with the Spoonacular Food and Recipe API
 */
@Service
public class SpoonacularServiceImpl implements SpoonacularService {

    Logger logger = LoggerFactory.getLogger(SpoonacularServiceImpl.class);

    RecipeRepository recipeRepository;

    public SpoonacularServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * @param url spoonacular API to get
     * @return API results deserialized as JSON
     */
    private JsonElement sendGetRequest(String url) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .setHeader("X-RapidAPI-Host", APIHOST)
                .setHeader("X-RapidAPI-Key", APIKEY)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            logger.warn(url);
            throw new Exception("Request to Spoonacular API failed!");
        }

        return parser.fromJson(response.body(), JsonElement.class);
    }

    /**
     * @param result the JSON object representation of the recipe
     * @return a Recipe object containing the information parsed from the JSON
     */
    private Optional<Recipe> parseRecipe(JsonObject result) {

        if (result.get("instructions").isJsonNull()) {
            return Optional.empty();
        }

        Recipe recipe = new Recipe()
                .setName(result.get("title").getAsString())
                .setPrepTime(result.get("readyInMinutes").getAsInt())
                .setSpoonacularId(result.get("id").getAsLong())
                .setSourceURL(result.get("sourceUrl").getAsString())
                .setInstructions(result.get("instructions").getAsString());

        if (!result.get("image").isJsonNull())
               recipe.setImageURL(result.get("image").getAsString());

        for (JsonElement e : result.get("extendedIngredients").getAsJsonArray()) {
            JsonObject o = e.getAsJsonObject();
            recipe.addIngredient(new Ingredient()
                    .setName(o.get("name").getAsString()));

            recipe.getVerboseIngredients().add(o.get("original").toString());
        }

        return Optional.of(recipe);
    }

    /**
     * @param parameters a map of the search parameters for the Spoonacular API
     *                    (see https://rapidapi.com/spoonacular/api/recipe-food-nutrition/)
     * @return a list of Recipe objects returned by the search
     */
    @Override
    public List<Recipe> search(Map<String, String> parameters) throws Exception {

        String url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/complexSearch?";

        if (parameters.containsKey("query"))
            url += "query=" + parameters.get("query") + "&";

        if (parameters.containsKey("instructionsRequired"))
            url += "instructionsRequired=" + parameters.get("instructionsRequired") + "&";

        if (parameters.containsKey("number"))
            url += "number=" + parameters.get("number") + "&";

        if (parameters.containsKey("type"))
            url += "type=" + parameters.get("type") + "&";

        if (parameters.get("cuisine") != null)
            url += "cuisine=" + parameters.get("cuisine") + "&";

        if (parameters.get("diet") != null)
            url += "diet=" + parameters.get("diet") + "&";

        if (parameters.get("intolerances") != null)
            url += "intolerances=" + parameters.get("intolerances");

        if (parameters.get("ingredients") != null)
            url += "includeIngredients=" + parameters.get("ingredients");

        if (url.endsWith("&"))
            url = url.substring(0, url.length() - 1);

        url = url.replace(" ", "%20");

        /* the initial search only includes basic recipe information, so we need to individually look up
         * recipes in a second set of requests to get ingredients, instructions, etc */
        JsonObject object = sendGetRequest(url).getAsJsonObject();

        /* perform secondary lookup to get detailed recipe information for each recipe */
        List<String> recipeIds = new ArrayList<>();
        List<Recipe> recipes = new ArrayList<>();
        for (JsonElement element : object.get("results").getAsJsonArray()) {
            String id = element.getAsJsonObject().get("id").getAsString();

            if (recipeRepository.existsBySpoonacularId(Long.parseLong(id))) {
                recipes.add(recipeRepository.findRecipeBySpoonacularId(Long.parseLong(id)));
                continue;
            }

            recipeIds.add(id);
        }

        /* send batch request */
        url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/informationBulk?ids=" +
                recipeIds.stream().map(Object::toString)
                        .collect(Collectors.joining("%2C"));

        JsonArray array = sendGetRequest(url).getAsJsonArray();

        for (JsonElement e : array)
            parseRecipe(e.getAsJsonObject())
                    .ifPresent(recipes::add);

        return recipes;
    }

    /**
     * @return a list of recommended recipes
     */
    @Override
    public List<Recipe> recommend(Set<Recipe> userRecipes) throws Exception {

        String url;

        if (userRecipes.size() == 0)
            url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/156992/similar";
        else {
            Recipe r = new ArrayList<>(userRecipes)
                    .get(new Random().nextInt(userRecipes.size()));

            url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/"
                    + r.getSpoonacularId() + "/similar";
        }

        JsonElement elem = sendGetRequest(url);

        /* perform secondary lookup to get detailed recipe information for each recipe */
        List<String> recipeIds = new ArrayList<>();
        List<Recipe> recipes = new ArrayList<>();
        for (JsonElement element : elem.getAsJsonArray()) {
            String id = element.getAsJsonObject().get("id").getAsString();

            if (recipeRepository.existsBySpoonacularId(Long.parseLong(id))) {
                recipes.add(recipeRepository.findRecipeBySpoonacularId(Long.parseLong(id)));
                continue;
            }

            recipeIds.add(id);
        }

        /* send batch request */
        url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/informationBulk?ids=" +
                recipeIds.stream().map(Object::toString)
                        .collect(Collectors.joining("%2C"));

        JsonArray array = sendGetRequest(url).getAsJsonArray();

        for (JsonElement e : array)
            parseRecipe(e.getAsJsonObject())
                    .ifPresent(recipes::add);

        return recipes;
    }
}
