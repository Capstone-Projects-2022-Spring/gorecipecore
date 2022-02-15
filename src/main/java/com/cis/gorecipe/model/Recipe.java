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
public class Recipe {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    @Lob
    private String content;

    @ManyToMany
    private List<Ingredient> ingredients;

    private String videoURL;
}
