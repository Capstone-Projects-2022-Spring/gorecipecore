package com.cis.gorecipe.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class Ingredient {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String name;
}
