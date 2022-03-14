package com.cis.gorecipe.controller;

import com.cis.gorecipe.BaseTest;
import com.cis.gorecipe.dto.UserDTO;
import com.cis.gorecipe.model.User;
import com.cis.gorecipe.repository.IngredientRepository;
import com.cis.gorecipe.repository.RecipeRepository;
import com.cis.gorecipe.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Optional;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:test.properties")
class UserControllerTest extends BaseTest {

    private final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Autowired
    UserController controller;

    @Autowired
    UserRepository userRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    RecipeRepository recipeRepository;

    private User[] mockUsers;

    @BeforeEach
    public void setup() {
        mockUsers = new User[]{
                new User().setUsername("username1")
                        .setEmail("yakir@temple.edu")
                        .setFirstName("Yakir")
                        .setLastName("Lebovits")
                        .setBirthDate(new Date(0))
                        .setId(1L)
                        .setPassword("password"),
                new User().setUsername("username2")
                        .setEmail("cis1@temple.edu")
                        .setFirstName("Sean")
                        .setLastName("Williams")
                        .setBirthDate(new Date(0))
                        .setId(2L)
                        .setPassword("password"),
                new User().setUsername("username3")
                        .setEmail("cis2@temple.edu")
                        .setFirstName("Olivia")
                        .setLastName("Felmey")
                        .setBirthDate(new Date(0))
                        .setId(3L)
                        .setPassword("password"),
                new User().setUsername("username4")
                        .setEmail("cis3@temple.edu")
                        .setFirstName("Phi")
                        .setLastName("Truong")
                        .setBirthDate(new Date(0))
                        .setId(4L)
                        .setPassword("password"),
                new User().setUsername("username5")
                        .setEmail("cis4@temple.edu")
                        .setFirstName("Anna")
                        .setLastName("Gillen")
                        .setBirthDate(new Date(0))
                        .setId(5L)
                        .setPassword("password"),
                new User().setUsername("username6")
                        .setEmail("cis5@temple.edu")
                        .setFirstName("Casey")
                        .setLastName("Maloney")
                        .setBirthDate(new Date(0))
                        .setId(6L)
                        .setPassword("password")};
    }


    /**
     * Test whether a new user (with complete and valid data) can be created
     */
    @Test
    @DirtiesContext
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
        logger.info("Test failure: " + storedUser.get().equals(actual));
        assertEquals(storedUser.get(), actual);
    }

    /**
     * Test whether the API will reject an attempt to create a user with invalid data
     */
    @Test
    @DirtiesContext
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
    @DirtiesContext
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
    @DirtiesContext
    public void testDeleteUser() throws Exception {

        User user = userRepository.save(mockUsers[0]);

        mockMvc.perform(delete("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    /**
     * Test whether the API will reject an attempt to delete a user that does not exist
     */
    @Test
    @DirtiesContext
    public void testDeleteUserDoesNotExist() throws Exception {

        mockMvc.perform(delete("/api/users/123456789"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test whether a user that exists can have their information (e.g. first name or email) updated
     */
    @Test
    @DirtiesContext
    public void testUpdateUser() throws Exception {

        User user = userRepository.save(mockUsers[0]);

        mockUsers[0].setFirstName("Updated");
        mockUsers[0].setEmail("updated@email.com");

        String result = mockMvc.perform(put("/api/users/" + user.getId())
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
     * Test whether the API will reject an attempt to update a user that does not exist
     */
    @Disabled
    @Test
    @DirtiesContext
    public void testUpdateUserDoesNotExist() throws Exception {

        mockMvc.perform(put("/api/users/123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializer.writeValueAsString(mockUsers[0])))
                .andExpect(status().isNotFound());
    }

    /**
     * Test whether the data of user that exists can be retrieved
     */
    @Disabled
    @Test
    @DirtiesContext
    public void testGetUser() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to retrieve the data of a user that does not exist
     */
    @Disabled
    @Test
    @DirtiesContext
    public void testGetUserDoesNotExist() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will return a user's data when they have successfully logged in
     */
    @Disabled
    @Test
    @DirtiesContext
    public void testLoginSuccess() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to log in with an incorrect password
     */
    @Disabled
    @Test
    @DirtiesContext
    public void testLoginFailure() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to log in with a username that does not belong to any account
     */
    @Disabled
    @Test
    @DirtiesContext
    public void testLoginWithUserDoesNotExist() {
        fail("Not yet implemented");
    }

    /**
     * Test whether a user's saved recipes can be retrieved
     */
    @Disabled
    @Test
    @DirtiesContext
    public void testGetSavedRecipes() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to retrieve recipes from an account that does not exist
     */
    @Disabled
    @Test
    @DirtiesContext
    public void testGetSavedRecipesUserDoesNotExist() {
        fail("Not yet implemented");
    }

    /**
     * Test whether a recipe that exists can be saved to a user account that exists
     */
    @Disabled
    @Test
    @DirtiesContext
    public void testSaveRecipeToAccount() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to save a recipe that exists to an account that does not exist
     */
    @Disabled
    @Test
    @DirtiesContext
    public void testSaveRecipeToAccountUserDoesNotExist() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to save a recipe that does not exist to an account that does exist
     */
    @Disabled
    @Test
    @DirtiesContext
    public void testSaveRecipeDoesNotExistToAccount() {
        fail("Not yet implemented");
    }

    /**
     * Test whether a dietary restriction can be added to an account
     */
    @Disabled
    @Test
    @DirtiesContext
    public void testAddDietaryRestrictionToAccount() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to add a dietary restriction to an account that does not exist
     */
    @Disabled
    @Test
    @DirtiesContext
    public void testAddDietaryRestrictionToAccountUserDoesNotExist() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to add a dietary restriction that does not exist to an account that does exist
     */
    @Disabled
    @Test
    @DirtiesContext
    public void testAddDietaryRestrictionDoesNotExistToAccount() {
        fail("Not yet implemented");
    }

    /**
     * Test whether a dietary restriction can be removed from an account
     */
    @Disabled
    @Test
    @DirtiesContext
    public void testRemoveDietaryRestrictionFromAccount() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to remove a dietary restriction from an account that does not exist
     */
    @Disabled
    @Test
    @DirtiesContext
    public void testRemoveDietaryRestrictionFromAccountUserDoesNotExist() {
        fail("Not yet implemented");
    }

    /**
     * Test whether the API will reject an attempt to remove a dietary restriction that does not exist from an account that does exist
     */
    @Disabled
    @Test
    @DirtiesContext
    public void testRemoveDietaryRestrictionDoesNotExistFromAccount() {
        fail("Not yet implemented");
    }
}
