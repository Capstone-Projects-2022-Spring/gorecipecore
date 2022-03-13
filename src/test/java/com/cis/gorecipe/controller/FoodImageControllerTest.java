package com.cis.gorecipe.controller;

import com.cis.gorecipe.dto.UserDTO;
import com.cis.gorecipe.model.FoodImage;
import com.cis.gorecipe.model.Ingredient;
import com.cis.gorecipe.model.User;
import com.cis.gorecipe.repository.FoodImageRepository;
import com.cis.gorecipe.repository.IngredientRepository;
import com.cis.gorecipe.repository.UserRepository;
import com.cis.gorecipe.util.PasswordUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:test.properties")
class FoodImageControllerTest {

    private final Logger logger = LoggerFactory.getLogger(FoodImageControllerTest.class);

    @Autowired
    UserController controller;

    @Autowired
    UserRepository userRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    FoodImageRepository foodImageRepository;

    @Autowired
    FoodImageController foodImageController;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper serializer = new ObjectMapper();

    private final ClassLoader classLoader = getClass().getClassLoader();

    public FoodImageControllerTest() {
        serializer.registerModule(new JavaTimeModule());
        serializer.setTimeZone(TimeZone.getTimeZone("EST"));
    }

    /**
     * Test whether a JPG image can be uploaded and will return a list of ingredients
     */
    @DirtiesContext
    @Test
    public void testUploadImage() throws Exception {

        User mockUser = new User().setUsername("username1")
                .setEmail("yakir@temple.edu")
                .setFirstName("Yakir")
                .setLastName("Lebovits")
                .setBirthDate(new Date(0))
                .setId(1L)
                .setPassword(PasswordUtil.hash("password"));

        userRepository.saveAndFlush(mockUser);

        String image = "test_img_1.jpg";

        MockMultipartFile mockFile = new MockMultipartFile("image", image, "image/jpeg",
                Objects.requireNonNull(Objects.requireNonNull(classLoader.getResourceAsStream(image))
                        .readAllBytes()));

        String result = mockMvc.perform(multipart("/api/images/upload/1")
                        .file(mockFile))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Ingredient> list = Arrays.asList(serializer.readValue(result, Ingredient[].class));

        assertNotEquals(list.size(), 0);
    }

    /**
     * Test whether the API will reject an attempt to upload an image by a user that doesn't exist
     */
    @Test
    public void testUploadImageUserNotExists() throws Exception {

        String image = "test_img_1.jpg";

        MockMultipartFile mockFile = new MockMultipartFile("image", image, "image/jpeg",
                Objects.requireNonNull(Objects.requireNonNull(classLoader.getResourceAsStream(image))
                        .readAllBytes()));

        mockMvc.perform(multipart("/api/images/upload/1")
                        .file(mockFile))
                .andExpect(status().isNotFound());
    }

    /**
     * Test whether the API will reject an upload of the wrong image type (e.g. BMP, SVG, etc.)
     */
    @DirtiesContext
    @Test
    public void testUploadWrongImageType() throws Exception {

        User mockUser = new User().setUsername("username1")
                .setEmail("yakir@temple.edu")
                .setFirstName("Yakir")
                .setLastName("Lebovits")
                .setBirthDate(new Date(0))
                .setId(1L)
                .setPassword(PasswordUtil.hash("password"));

        userRepository.saveAndFlush(mockUser);

        String image = "test.pdf";

        MockMultipartFile mockFile = new MockMultipartFile("image", image, "application/pdf",
                Objects.requireNonNull(Objects.requireNonNull(classLoader.getResourceAsStream(image))
                        .readAllBytes()));

        mockMvc.perform(multipart("/api/images/upload/1")
                        .file(mockFile))
                .andExpect(status().isUnsupportedMediaType());
    }

    /**
     * Test whether the images uploaded by a specific user can be retrieved
     */
    @DirtiesContext
    @Test
    public void testGetUserImages() throws Exception {

        User mockUser = new User().setUsername("username1")
                .setEmail("yakir@temple.edu")
                .setFirstName("Yakir")
                .setLastName("Lebovits")
                .setBirthDate(new Date(0))
                .setId(1L)
                .setPassword(PasswordUtil.hash("password"));

        logger.warn(mockUser.getPassword());

        mockUser = userRepository.saveAndFlush(mockUser);

        String image = "test_img_1.jpg";

        MockMultipartFile mockFile = new MockMultipartFile("image", image, "image/jpeg",
                Objects.requireNonNull(Objects.requireNonNull(classLoader.getResourceAsStream(image))
                        .readAllBytes()));

        mockMvc.perform(multipart("/api/images/upload/1")
                        .file(mockFile))
                .andExpect(status().isCreated());

        String result = mockMvc.perform(get("/api/images/user/" + mockUser.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<FoodImage> list = Arrays.asList(serializer.readValue(result, FoodImage[].class));
        assertEquals(list.size(), 1);

        FoodImage foodImage = list.get(0);

        logger.warn(foodImage.getUploadedBy().getPassword());

        foodImage = foodImageRepository.getFoodImageByUploadedBy(mockUser).get(0);

        System.out.println(foodImage);

        assertEquals(foodImage.getUploadedBy(), mockUser);
        assertNotEquals(foodImage.getImageOf().size(), 0);
        assertTrue(foodImage.getS3objectId().startsWith(String.valueOf(mockUser.getId())));
        assertTrue(foodImage.getS3objectId().endsWith(".jpg"));
    }

    /**
     * Test whether the API will reject an attempt to the images of a user that doesn't exist
     */
    @Test
    public void testGetUserNotExistsImages() {
    }

    /**
     * Test whether a specified image can be retrieved
     */
    @Test
    public void testGetImage() {
    }

    /**
     * Test whether the API will reject an attempt to retrieve an image that doesn't exist
     */
    @Test
    public void testGetImageNotExists() {
    }
}
