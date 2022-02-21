package com.cis.gorecipe.repository;

import com.cis.gorecipe.model.FoodImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodImageRepository extends JpaRepository<String, FoodImage> {
}
