package com.cis.gorecipe.controller;

import com.cis.gorecipe.exception.FoodImageNotFoundException;
import com.cis.gorecipe.exception.UserNotFoundException;
import com.cis.gorecipe.model.FoodImage;
import com.cis.gorecipe.model.Ingredient;
import com.cis.gorecipe.model.User;
import com.cis.gorecipe.repository.FoodImageRepository;
import com.cis.gorecipe.repository.IngredientRepository;
import com.cis.gorecipe.repository.UserRepository;
import com.cis.gorecipe.service.ClarifaiService;
import com.cis.gorecipe.service.S3Service;
import com.cis.gorecipe.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class handles the API endpoints related to uploading images of ingredients and the processing of
 * those images
 */
@RestController
@RequestMapping("/api/images")
public class FoodImageController {

    /**
     * For logging any errors that occur during runtime (e.g. a user is not found)
     */
    private final Logger logger = LoggerFactory.getLogger(FoodImageController.class);

    /**
     * For interfacing with the FoodImage table in the database
     */
    private final FoodImageRepository foodImageRepository;

    /**
     * For interfacing with the User table in the database
     */
    private final UserRepository userRepository;

    /**
     * For interfacing with the Ingredient table in the database
     */
    private final IngredientRepository ingredientRepository;

    /**
     * Handles all interactions with AWS S3
     */
    private final S3Service s3Service;

    /**
     * Handles all interactions with the Clarifai Food Recognition API
     */
    private final ClarifaiService clarifaiService;

    public FoodImageController(FoodImageRepository foodImageRepository, UserRepository userRepository,
                               IngredientRepository ingredientRepository, S3Service s3Service,
                               ClarifaiService clarifaiService) {
        this.foodImageRepository = foodImageRepository;
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
        this.s3Service = s3Service;
        this.clarifaiService = clarifaiService;
    }

    /**
     * @param image  a JPEG image
     * @param userId the ID of the user who uploaded the image
     * @return the ingredient that GoRecipe has determined to be in the image
     */
    @PostMapping(path = "/upload/{userId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FoodImage> uploadImage(@RequestPart("image") MultipartFile image,
                                                        @PathVariable("userId") Long userId) throws IOException {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("Unable to find user " + userId)
                );

        if (!FileUtil.isImage(image))
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();

        String fileName = userId + "-" + System.currentTimeMillis() +
                "." + Objects.requireNonNull(image.getContentType()).split("/")[1];

        String image_s3_URI = s3Service.uploadFile(fileName, image.getInputStream(), image.getContentType());

        List<Ingredient> ingredients = clarifaiService.processImage(image_s3_URI);

        ingredients = ingredients.stream()
                .map(ingredientRepository::saveAndFlush)
                .collect(Collectors.toList());

        /* this is stupid, but it makes testing easier */
        String s3ID = image_s3_URI.substring(image_s3_URI.lastIndexOf("/")+1);

        FoodImage foodImage = new FoodImage()
                .setImageOf(new HashSet<>(ingredients))
                .setS3objectId(s3ID)
                .setUploadedBy(user);

        logger.warn(foodImage.getS3objectId());

        foodImageRepository.saveAndFlush(foodImage);

        return ResponseEntity.status(HttpStatus.CREATED).body(foodImage);
    }

    /**
     * @param id the ID of a user
     * @return a list of images uploaded by the specified user
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<List<FoodImage>> getUserImages(@PathVariable("id") Long id) {

        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Unable to find user " + id)
                );

        List<FoodImage> images = foodImageRepository.getFoodImageByUploadedBy(user);

        return ResponseEntity.ok().body(images);
    }

    /**
     * @param id the FoodImage/AWS S3 object ID of the image
     * @return the URL of the image specified by the id
     */
    @GetMapping("/{id}")
    public String getImage(@PathVariable("id") String id) {

        FoodImage foodImage = foodImageRepository
                .findById(id)
                .orElseThrow(() ->
                        new FoodImageNotFoundException(id)
                );

        return s3Service.getFileUrl(foodImage.getS3objectId());
    }

    @PostMapping("/{id}")
    public ResponseEntity<FoodImage> updateImageIngredients(@PathVariable String id,
                                                            @RequestBody List<String> ingredients) {

        FoodImage foodImage = foodImageRepository
                .findById(id)
                .orElseThrow(() ->
                        new FoodImageNotFoundException(id)
                );

        Set<Ingredient> i = new HashSet<>();
        for (String s : ingredients)
            if (ingredientRepository.existsById(s))
                i.add(ingredientRepository.getById(s));

        foodImage.setImageOf(i);
        foodImageRepository.save(foodImage);

        return ResponseEntity.ok().body(foodImage);
    }
}
