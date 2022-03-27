package com.cis.gorecipe.repository;

import com.cis.gorecipe.model.RecipeCalendarItem;
import com.cis.gorecipe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface provides a way to use JPA to interface with the GoRecipe database to manage RecipeCalendarItems
 */
public interface RecipeCalendarItemRepository extends JpaRepository<RecipeCalendarItem, Long> {

    List<RecipeCalendarItem> getRecipeCalendarItemByUser(User user);
}
