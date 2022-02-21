package com.cis.gorecipe.repository;

import com.cis.gorecipe.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface provides a way to use JPA to interface with the GoRecipe database to manage Ingredients
 */
public interface IngredientRepository extends JpaRepository<Long, Ingredient> {
}
