package com.cis.gorecipe.controller;

import com.cis.gorecipe.dto.UserDTO;
import com.cis.gorecipe.model.User;
import com.cis.gorecipe.repository.IngredientRepository;
import com.cis.gorecipe.repository.RecipeRepository;
import com.cis.gorecipe.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.Optional;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Autowired
    UserController controller;

    @Autowired
    UserRepository userRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper serializer = new ObjectMapper();

    private final User[] mockUsers = {new User().setUsername("username1")
                                        .setEmail("yakir@temple.edu")
                                        .setFirstName("Yakir")
                                        .setLastName("Lebovits")
                                        .setBirthDate(new Date(0))
                                        .setId(1L),
                                new User().setUsername("username2")
                                        .setEmail("cis1@temple.edu")
                                        .setFirstName("Sean")
                                        .setLastName("Williams")
                                        .setBirthDate(new Date(0))
                                        .setId(2L),
                                new User().setUsername("username3")
                                        .setEmail("cis2@temple.edu")
                                        .setFirstName("Olivia")
                                        .setLastName("Felmey")
                                        .setBirthDate(new Date(0))
                                        .setId(3L),
                                new User().setUsername("username4")
                                        .setEmail("cis3@temple.edu")
                                        .setFirstName("Phi")
                                        .setLastName("Truong")
                                        .setBirthDate(new Date(0))
                                        .setId(4L),
                                new User().setUsername("username5")
                                        .setEmail("cis4@temple.edu")
                                        .setFirstName("Anna")
                                        .setLastName("Gillen")
                                        .setBirthDate(new Date(0))
                                        .setId(5L),
                                new User().setUsername("username6")
                                        .setEmail("cis5@temple.edu")
                                        .setFirstName("Casey")
                                        .setLastName("Maloney")
                                        .setBirthDate(new Date(0))
                                        .setId(6L)};

    public UserControllerTest() {
        serializer.registerModule(new JavaTimeModule());
        serializer.setTimeZone(TimeZone.getTimeZone("EST"));
    }

    /**
     * Test whether a new user (with complete and valid data) can be created
     * @throws Exception
     */
    @Test
    public void testCreateNewUser() throws Exception {

        String result = mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializer.writeValueAsString(mockUsers[0])))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        User actual = UserDTO.mapToUser(serializer.readValue(result, UserDTO.class));
        Optional<User> storedUser = userRepository.findById(actual.getId());
        assertTrue(storedUser.isPresent());
        assertEquals(storedUser.get(), actual);
    }

    /**
     * Test whether the API will reject an attempt to create a user with invalid data
     */
    @Test
    public void testCreateNewUserWithMissingData() throws Exception {

        User badUser = new User(); /* user without any data */

       mockMvc.perform(post("/api/users/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(serializer.writeValueAsString(badUser)))
                    .andExpect(status().isUnprocessableEntity());
    }

    /**
     * Test whether the API will reject an attempt to create a user that contains non-unique information
     * such as an already used email or username
     */
    @Test
    public void testCreateUserWithNonUniqueData() throws Exception {

        mockUsers[0].setEmail("cis1@temple.edu");

        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializer.writeValueAsString(mockUsers[0])))
                .andExpect(status().isOk());


        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializer.writeValueAsString(mockUsers[1])))
                .andExpect(status().isUnprocessableEntity());

    }

    /**
     * Test whether a user that exists can be successfully deleted
     */
    @Test
    public void testDeleteUser() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to delete a user that does not exist
     */
    @Test
    public void testDeleteUserDoesNotExist() {
        fail("Not yet implemented");
    }

    /**
     * Test whether a user that exists can have their information (e.g. first name or email) updated
     */
    @Test
    public void testUpdateUser() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to update a user that does not exist
     */
    @Test
    public void testUpdateUserDoesNotExist() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the data of user that exists can be retrieved
     */
    @Test
    public void testGetUser() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to retrieve the data of a user that does not exist
     */
    @Test
    public void testGetUserDoesNotExist() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will return a user's data when they have successfully logged in
     */
    @Test
    public void testLoginSuccess() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to log in with an incorrect password
     */
    @Test
    public void testLoginFailure() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to log in with a username that does not belong to any account
     */
    @Test
    public void testLoginWithUserDoesNotExist() {
        fail("Not yet implemented");
    }

    /**
     * Test whether a user's saved recipes can be retrieved
     */
    @Test
    public void testGetSavedRecipes() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to retrieve recipes from an account that does not exist
     */
    @Test
    public void testGetSavedRecipesUserDoesNotExist() {
        fail("Not yet implemented");
    }

    /**
     * Test whether a recipe that exists can be saved to a user account that exists
     */
    @Test
    public void testSaveRecipeToAccount() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to save a recipe that exists to an account that does not exist
     */
    @Test
    public void testSaveRecipeToAccountUserDoesNotExist() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to save a recipe that does not exist to an account that does exist
     */
    @Test
    public void testSaveRecipeDoesNotExistToAccount() {
        fail("Not yet implemented");
    }

    /**
     * Test whether a dietary restriction can be added to an account
     */
    @Test
    public void testAddDietaryRestrictionToAccount() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to add a dietary restriction to an account that does not exist
     */
    @Test
    public void testAddDietaryRestrictionToAccountUserDoesNotExist() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to add a dietary restriction that does not exist to an account that does exist
     */
    @Test
    public void testAddDietaryRestrictionDoesNotExistToAccount() {
        fail("Not yet implemented");
    }

    /**
     * Test whether a dietary restriction can be removed from an account
     */
    @Test
    public void testRemoveDietaryRestrictionFromAccount() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to remove a dietary restriction from an account that does not exist
     */
    @Test
    public void testRemoveDietaryRestrictionFromAccountUserDoesNotExist() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to remove a dietary restriction that does not exist from an account that does exist
     */
    @Test
    public void testRemoveDietaryRestrictionDoesNotExistFromAccount() {
        fail("Not yet implemented");
    }
}
