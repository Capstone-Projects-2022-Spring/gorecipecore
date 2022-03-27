package com.cis.gorecipe.repository;

import com.cis.gorecipe.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * This interface provides a way to use JPA to interface with the GoRecipe database to manage Recipes
 */
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    boolean existsBySpoonacularId(Long spoonacularId);

    Recipe findRecipeBySpoonacularId(Long spoonacularId);

    boolean existsByName(String name);

    List<Recipe> findAllByNameIn(Iterable<String> names);
}
