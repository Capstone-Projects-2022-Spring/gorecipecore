package com.cis.gorecipe.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * This class allows GoRecipe to keep track of all image objects stored in AWS S3 buckets
 */
@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class FoodImage {

    /**
     * The primary key of the FoodImage, which also serves as the S3 object lookup key
     */
    @Id
    private String S3objectId;

    /**
     * The user which uploaded the image to GoRecipe
     */
    @ManyToOne
    private User uploadedBy;

    /**
     * The ingredient displayed in the image
     */
    @ManyToOne
    private Ingredient imageOf;
}
