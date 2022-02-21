package com.cis.gorecipe.repository;

import com.cis.gorecipe.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Long, Ingredient> {
}
