package com.cis.gorecipe.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class DietaryRestriction {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToMany
    private List<Ingredient> disallowedIngredients;
}
