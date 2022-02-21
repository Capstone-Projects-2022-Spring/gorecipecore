package com.cis.gorecipe.repository;

import com.cis.gorecipe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long, User> {
}
