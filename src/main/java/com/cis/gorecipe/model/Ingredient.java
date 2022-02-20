package com.cis.gorecipe.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * This class allows GoRecipe to store individual ingredients (e.g. tomato, steak, flour, etc) that may be used in Recipes
 */
@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class Ingredient {

    /**
     * The primary key of the ingredient
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * A unique ingredient name (e.g. rice or salmon)
     */
    @Column(unique=true)
    private String name;
}
