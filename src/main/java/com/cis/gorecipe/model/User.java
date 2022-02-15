package com.cis.gorecipe.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    // will need to use Spring Security to handle properly
    @Getter(AccessLevel.NONE)
    private String password;

    @Column(unique = true)
    private String email;

    private String firstName;

    private String lastName;

    private Date birthDate;

    @ManyToMany
    private List<Ingredient> favoriteIngredients;

    @ManyToMany
    private List<Recipe> savedRecipes;

    @ManyToMany
    private List<DietaryRestriction> dietaryRestrictions;
}
