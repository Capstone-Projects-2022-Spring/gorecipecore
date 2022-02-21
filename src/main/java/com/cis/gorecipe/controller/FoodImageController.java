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


@RestController
@RequestMapping("/api/images")
public class FoodImageController {

    private final Logger logger = LoggerFactory.getLogger(FoodImageController.class);

    private FoodImageRepository foodImageRepository;

    private UserRepository userRepository;

    private IngredientRepository ingredientRepository;

    public FoodImageController(FoodImageRepository foodImageRepository, UserRepository userRepository, IngredientRepository ingredientRepository) {
        this.foodImageRepository = foodImageRepository;
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<Ingredient> uploadImage(@RequestParam("image") Byte[] bytes,
                                                  @RequestParam("user") UserDTO userDTO) {
        return null;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<FoodImage>> getUsersImages(@RequestBody UserDTO userDTO) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Byte[]> getImage(@PathVariable("id") Long id) {
        return null;
    }
}
