package com.cis.gorecipe.repository;

import com.cis.gorecipe.model.DietaryRestriction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface provides a way to use JPA to interface with the GoRecipe database to manage DietaryRestrictions
 */
public interface DietaryRestrictionRepository extends JpaRepository<Long, DietaryRestriction> {
}
