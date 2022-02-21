package com.cis.gorecipe.repository;

import com.cis.gorecipe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface provides a way to use JPA to interface with the GoRecipe database to manage Users
 */
public interface UserRepository extends JpaRepository<Long, User> {
}
