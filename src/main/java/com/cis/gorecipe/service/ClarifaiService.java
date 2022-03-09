package com.cis.gorecipe.service;

import com.cis.gorecipe.model.Ingredient;

import java.util.List;

public interface ClarifaiService {

    List<Ingredient> processImage(String imageURL);

}
