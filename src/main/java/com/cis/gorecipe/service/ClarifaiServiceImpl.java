package com.cis.gorecipe.service;

import com.cis.gorecipe.model.Ingredient;
import com.clarifai.grpc.api.*;
import com.clarifai.grpc.api.status.StatusCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This service class manages all interactions with the Clarifai API
 */
@Service
public class ClarifaiServiceImpl implements ClarifaiService {

    /**
     * @param imageUrl the AWS S3 URL of the image to be processed
     * @return the list of ingredients identified in the image by Clarifai with a confidence of >=50%
     */
    @Override
    public List<Ingredient> processImage(String imageUrl) {
        MultiOutputResponse response = stub.postModelOutputs(
                PostModelOutputsRequest.newBuilder()
                        .setModelId("bd367be194cf45149e75f01d59f77ba7") /* ID of the food recognition model */
                        .addInputs(
                                Input.newBuilder().setData(
                                        Data.newBuilder().setImage(
                                                Image.newBuilder().setUrl(imageUrl)
                                        )
                                )
                        )
                        .build()
        );

        if (response.getStatus().getCode() != StatusCode.SUCCESS) {
            throw new RuntimeException("Request failed, status: " + response.getStatus());
        }

        List<Ingredient> ingredients = new ArrayList<>();

        for (Concept c : response.getOutputs(0).getData().getConceptsList())
            if (c.getValue() >= 0.5)
                ingredients.add(new Ingredient().setName(c.getName()));

        return ingredients.isEmpty() ? null : ingredients;
    }
}
