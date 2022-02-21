package com.cis.gorecipe.controller;

import com.cis.gorecipe.dto.UserDTO;
import com.cis.gorecipe.model.FoodImage;
import com.cis.gorecipe.model.Ingredient;
import com.cis.gorecipe.repository.FoodImageRepository;
import com.cis.gorecipe.repository.IngredientRepository;
import com.cis.gorecipe.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

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
    private FoodImageRepository foodImageRepository;

    /**
     * For interfacing with the User table in the database
     */
    private UserRepository userRepository;

    /**
     * For interfacing with the Ingredient table in the database
     */
    private IngredientRepository ingredientRepository;

    public FoodImageController(FoodImageRepository foodImageRepository, UserRepository userRepository, IngredientRepository ingredientRepository) {
        this.foodImageRepository = foodImageRepository;
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * @param bytes a JPEG image as a bytestream
     * @param userDTO the user who has uploaded the image
     * @return the ingredient that GoRecipe has determined to be in the image
     */
    @PostMapping("/upload")
    public ResponseEntity<Ingredient> uploadImage(@RequestParam("image") Byte[] bytes,
                                                  @RequestParam("user") UserDTO userDTO) {
        return null;
    }

    /**
     * @param userDTO a GoRecipe user
     * @return a list of images uploaded by the specified user
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<List<FoodImage>> getUsersImages(@RequestBody UserDTO userDTO) {
        return null;
    }

    /**
     * @param id the FoodImage/AWS S3 object ID of the image
     * @return the image specified by the id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Byte[]> getImage(@PathVariable("id") String id) {
        return null;
    }
}
