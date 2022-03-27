package com.cis.gorecipe.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

import static javax.persistence.TemporalType.DATE;

/**
 * This class allows for adding a recipe to a specified user's calendar
 */
@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class RecipeCalendarItem {

    /**
     * The primary key of the item
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The user whose calendar this item is being added to
     */
    @ManyToOne
    private User user;

    /**
     * The recipe being added to the user's calendar
     */
    @ManyToOne
    private Recipe recipe;

    /**
     * The date on which the user would like to cook the recipe
     */
    @Column(nullable = false)
    @Temporal(DATE)
    private java.util.Date date;
}
