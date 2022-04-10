package com.cis.gorecipe.controller;

import com.cis.gorecipe.BaseTest;
import com.cis.gorecipe.model.FoodImage;
import com.cis.gorecipe.model.Ingredient;
import com.cis.gorecipe.model.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FoodImageControllerTest extends BaseTest {

    private final Logger logger = LoggerFactory.getLogger(FoodImageControllerTest.class);

    @Autowired
    FoodImageController controller;

    /**
     * Test whether a JPG image can be uploaded and will return a list of ingredients
     */
    @DirtiesContext
    @Test
    public void testUploadImage() throws Exception {

        when(s3Service.uploadFile(any(), any(), any()))
                .thenReturn("aws.example.com/test_img_1.jpg");

        when(clarifaiService.processImage(same("aws.example.com/test_img_1.jpg")))
                .thenReturn(Arrays.asList(new Ingredient().setName("corn"),
                                          new Ingredient().setName("tomato")));

        User mockUser = new User().setUsername("username1")
                .setEmail("yakir@temple.edu")
                .setFirstName("Yakir")
                .setLastName("Lebovits")
                .setBirthDate(new Date(946702800000L))
                .setId(1L)
                .setPassword(encoder.encode("password"));

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

        FoodImage foodImage = serializer.readValue(result, FoodImage.class);

        assertEquals(foodImage.getImageOf().size(), 2);
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
                .setPassword(encoder.encode("password"));

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

        when(s3Service.uploadFile(any(), any(), any()))
                .thenReturn("aws.example.com/test_img_1.jpg");

        when(clarifaiService.processImage(same("aws.example.com/test_img_1.jpg")))
                .thenReturn(Arrays.asList(new Ingredient().setName("corn"),
                        new Ingredient().setName("tomato")));

        User mockUser = new User().setUsername("username1")
                .setEmail("yakir@temple.edu")
                .setFirstName("Yakir")
                .setLastName("Lebovits")
                .setBirthDate(new Date(946702800000L))
                .setId(1L)
                .setPassword(encoder.encode("password"));

        mockUser = userRepository.save(mockUser);

        String image = "test_img_1.jpg";

        MockMultipartFile mockFile = new MockMultipartFile("image", image, "image/jpg",
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

        FoodImage foodImage;

        mockUser = userRepository.getById(mockUser.getId());
        foodImage = foodImageRepository.getFoodImageByUploadedBy(mockUser).get(0);

        assertEquals(foodImage.getUploadedBy(), mockUser);
        assertNotEquals(foodImage.getImageOf().size(), 0);
        assertTrue(foodImage.getS3objectId().endsWith(".jpg"));
    }

    /**
     * Test whether the API will reject an attempt to the images of a user that doesn't exist
     */
    @Test
    public void testGetUserNotExistsImages() throws Exception {

        mockMvc.perform(get("/api/images/user/123456789"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test whether a specified image can be retrieved
     */
    @DirtiesContext
    @Test
    public void testGetImage() throws Exception {

        when(s3Service.uploadFile(any(), any(), any()))
                .thenReturn("aws.example.com/test_img_1.jpg");

        when(s3Service.getFileUrl(eq("test_img_1.jpg")))
                .thenReturn("aws.example.com/test_img_1.jpg");

        when(clarifaiService.processImage(same("aws.example.com/test_img_1.jpg")))
                .thenReturn(Arrays.asList(new Ingredient().setName("corn"),
                        new Ingredient().setName("tomato")));

        User mockUser = new User().setUsername("username1")
                .setEmail("yakir@temple.edu")
                .setFirstName("Yakir")
                .setLastName("Lebovits")
                .setBirthDate(new Date(946702800000L))
                .setId(1L)
                .setPassword(encoder.encode("password"));

        userRepository.saveAndFlush(mockUser);

        String image = "test_img_1.jpg";

        MockMultipartFile mockFile = new MockMultipartFile("image", image, "image/jpeg",
                Objects.requireNonNull(Objects.requireNonNull(classLoader.getResourceAsStream(image))
                        .readAllBytes()));

        mockMvc.perform(multipart("/api/images/upload/1")
                        .file(mockFile))
                .andExpect(status().isCreated());

        FoodImage foodImage = foodImageRepository.getFoodImageByUploadedBy(mockUser).get(0);

        String result = mockMvc.perform(get("/api/images/" + foodImage.getS3objectId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        logger.warn(result);

        assertEquals(result, "aws.example.com/test_img_1.jpg");
    }

    /**
     * Test whether the API will reject an attempt to retrieve an image that doesn't exist
     */
    @Test
    public void testGetImageNotExists() throws Exception {

        mockMvc.perform(get("/api/images/this_id_doesnt_exist.jpeg"))
                .andExpect(status().isNotFound());

    }
}
