package com.cis.gorecipe.repository;

import com.cis.gorecipe.model.FoodImage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface provides a way to use JPA to interface with the GoRecipe database to manage FoodImages
 */
public interface FoodImageRepository extends JpaRepository<FoodImage, String> {
}
