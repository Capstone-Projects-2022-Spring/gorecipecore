package com.cis.gorecipe.controller;

import com.cis.gorecipe.repository.IngredientRepository;
import com.cis.gorecipe.repository.RecipeRepository;
import com.cis.gorecipe.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FoodImageControllerTest {

    private final Logger logger = LoggerFactory.getLogger(FoodImageControllerTest.class);

    @Autowired
    UserController controller;

    @Autowired
    UserRepository userRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    FoodImageController foodImageController;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper serializer = new ObjectMapper();

    /**
     * Test whether a JPG image can be uploaded
     */
    @Test
    public void testUploadImage() {}

    /**
     * Test whether the API will reject an attempt to upload an image without being linked to a user
     */
    @Test
    public void testUploadImageWithoutUser() {}

    /**
     * Test whether the API will reject an upload of the wrong image type (e.g. BMP, SVG, etc.)
     */
    @Test
    public void testUploadWrongImageType() {}

    /**
     * Test whether the images uploaded by a specific user can be retrieved
     */
    @Test
    public void testGetUserImages() {}

    /**
     * Test whether the API will reject an attempt to the images of a user that doesn't exist
     */
    @Test
    public void testGetUserNotExistsImages() {}

    /**
     * Test whether a specified image can be retrieved
     */
    @Test
    public void testGetImage() {}

    /**
     * Test whether the API will reject an attempt to retrieve an image that doesn't exist
     */
    @Test
    public void testGetImageNotExists() {}
}
