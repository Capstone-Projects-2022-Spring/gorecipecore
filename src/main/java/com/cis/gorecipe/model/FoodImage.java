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
public class FoodImage {

    @Id
    private String S3objectId;

    @ManyToOne
    private User uploadedBy;

    @ManyToOne
    private Ingredient imageOf;
}
