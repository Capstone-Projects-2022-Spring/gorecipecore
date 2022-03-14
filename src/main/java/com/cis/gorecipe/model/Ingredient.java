package com.cis.gorecipe.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;

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
     * A unique ingredient name (e.g. rice or salmon)
     */
    @Id
    private String name;
}
