package com.cis.gorecipe.repository;

import com.cis.gorecipe.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Long, Recipe> {
}
