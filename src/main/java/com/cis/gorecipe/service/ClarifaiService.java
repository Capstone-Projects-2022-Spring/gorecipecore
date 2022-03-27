package com.cis.gorecipe.service;

import com.cis.gorecipe.model.Ingredient;
import com.clarifai.channel.ClarifaiChannel;
import com.clarifai.credentials.ClarifaiCallCredentials;
import com.clarifai.grpc.api.V2Grpc;

import java.util.List;

/**
 * Interface for ClarifaiServiceImpl
 */
public interface ClarifaiService {

    /**
     * Allows for sending requests to the Clarifai API
     */
    V2Grpc.V2BlockingStub stub = V2Grpc.newBlockingStub(ClarifaiChannel.INSTANCE.getGrpcChannel())
            .withCallCredentials(new ClarifaiCallCredentials(System.getenv().get("CLARIFAI_API_KEY")));

    List<Ingredient> processImage(String imageURL);

}
