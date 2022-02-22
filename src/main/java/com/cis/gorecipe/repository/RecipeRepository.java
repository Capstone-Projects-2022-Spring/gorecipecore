package com.cis.gorecipe.repository;

import com.cis.gorecipe.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface provides a way to use JPA to interface with the GoRecipe database to manage Recipes
 */
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
