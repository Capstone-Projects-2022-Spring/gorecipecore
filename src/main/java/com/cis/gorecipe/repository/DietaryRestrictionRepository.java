package com.cis.gorecipe.repository;

import com.cis.gorecipe.model.DietaryRestriction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietaryRestrictionRepository extends JpaRepository<Long, DietaryRestriction> {
}
