package com.cis.gorecipe.service;

import com.cis.gorecipe.model.Ingredient;
import com.clarifai.channel.ClarifaiChannel;
import com.clarifai.credentials.ClarifaiCallCredentials;
import com.clarifai.grpc.api.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClarifaiServiceImpl implements ClarifaiService {

    private static final V2Grpc.V2BlockingStub stub = V2Grpc.newBlockingStub(ClarifaiChannel.INSTANCE.getGrpcChannel())
            .withCallCredentials(new ClarifaiCallCredentials(System.getenv().get("CLARIFAI_API_KEY")));

    @Override
    public List<Ingredient> processImage(String imageUrl) {
        MultiOutputResponse response = stub.postModelOutputs(
                PostModelOutputsRequest.newBuilder()
                        .setModelId("bd367be194cf45149e75f01d59f77ba7")
                        .addInputs(
                                Input.newBuilder().setData(
                                        Data.newBuilder().setImage(
                                                Image.newBuilder().setUrl(imageUrl)
                                        )
                                )
                        )
                        .build()
        );

        List<Ingredient> ingredients = new ArrayList<>();

        for (Concept c : response.getOutputs(0).getData().getConceptsList())
            if (c.getValue() >= 0.5)
                ingredients.add(new Ingredient().setName(c.getName()));

        return ingredients.isEmpty() ? null : ingredients;
    }
}
