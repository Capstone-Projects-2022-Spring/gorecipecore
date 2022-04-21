package com.cis.gorecipe.controller;

import com.cis.gorecipe.BaseTest;
import com.cis.gorecipe.dto.UserDTO;
import com.cis.gorecipe.model.Recipe;
import com.cis.gorecipe.model.User;
import org.assertj.core.api.HamcrestCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends BaseTest {

    private final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Autowired
    UserController userController;

    private User[] mockUsers;

    @BeforeEach
    public void setup() {
        mockUsers = new User[]{
                new User().setUsername("username1")
                        .setEmail("yakir@temple.edu")
                        .setFirstName("Yakir")
                        .setLastName("Lebovits")
                        .setBirthDate(new Date(946702800000L))
                        .setId(1L)
                        .setPassword(encoder.encode("password")),
                new User().setUsername("username2")
                        .setEmail("cis1@temple.edu")
                        .setFirstName("Sean")
                        .setLastName("Williams")
                        .setBirthDate(new Date(946702800000L))
                        .setId(2L)
                        .setPassword(encoder.encode("password")),
                new User().setUsername("username3")
                        .setEmail("cis2@temple.edu")
                        .setFirstName("Olivia")
                        .setLastName("Felmey")
                        .setBirthDate(new Date(946702800000L))
                        .setId(3L)
                        .setPassword("password"),
                new User().setUsername(encoder.encode("password"))
                        .setEmail("cis3@temple.edu")
                        .setFirstName("Phi")
                        .setLastName("Truong")
                        .setBirthDate(new Date(946702800000L))
                        .setId(4L)
                        .setPassword(encoder.encode("password")),
                new User().setUsername("username5")
                        .setEmail("cis4@temple.edu")
                        .setFirstName("Anna")
                        .setLastName("Gillen")
                        .setBirthDate(new Date(946702800000L))
                        .setId(5L)
                        .setPassword(encoder.encode("password")),
                new User().setUsername("username6")
                        .setEmail("cis5@temple.edu")
                        .setFirstName("Casey")
                        .setLastName("Maloney")
                        .setBirthDate(new Date(946702800000L))
                        .setId(6L)
                        .setPassword(encoder.encode("password"))};
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
        User storedUser = userRepository.getById(actual.getId());
        assertEquals(storedUser, actual);
        assertEquals(storedUser, actual);
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

        userRepository.flush();

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

        userRepository.save(mockUsers[0]);

        User user = new User()
                .setUsername("Updated!")
                .setEmail("updated@email.com");

        String result = mockMvc.perform(patch("/api/users/" + mockUsers[0].getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializer.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User actual = UserDTO.mapToUser(serializer.readValue(result, UserDTO.class));
        User expected = mockUsers[0]
                .setUsername("Updated!")
                .setEmail("updated@email.com");

        assertEquals(actual, expected);
    }

    /**
     * Test whether the API will reject an attempt to update a user that does not exist
     */
    @Test
    @DirtiesContext
    public void testUpdateUserDoesNotExist() throws Exception {

        User user = new User()
                .setUsername("Updated!")
                .setEmail("updated@email.com");

        mockMvc.perform(patch("/api/users/123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializer.writeValueAsString(user)))
                .andExpect(status().isNotFound());
    }

    /**
     * Test whether the data of user that exists can be retrieved
     */
    @Test
    @DirtiesContext
    public void testGetUser() throws Exception {

        User expected = userRepository.save(mockUsers[4]);

        String result = mockMvc.perform(get("/api/users/" + expected.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User actual = serializer.readValue(result, User.class);

        assertEquals(expected, actual);
    }

    /**
     * Test whether the API will reject an attempt to retrieve the data of a user that does not exist
     */
    @Test
    @DirtiesContext
    public void testGetUserDoesNotExist() throws Exception {

        mockMvc.perform(get("/api/users/123456789"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test whether the API will return a user's data when they have successfully logged in
     */
    @Test
    @DirtiesContext
    public void testLoginSuccess() throws Exception {

        User user = userRepository.save(mockUsers[4]);

        String result = mockMvc.perform(post("/api/users/login")
                .param("username", "username5")
                .param("password", "password"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User actual = serializer.readValue(result, User.class);
        assertEquals(user, actual);
    }

    /**
     * Test whether the API will reject an attempt to log in with an incorrect password
     */
    @Test
    @DirtiesContext
    public void testLoginFailure() throws Exception {

        userRepository.save(mockUsers[4]);

        mockMvc.perform(post("/api/users/login")
                        .param("username", "username5")
                        .param("password", "fake password"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Test whether the API will reject an attempt to log in with a username that does not belong to any account
     */
    @Test
    @DirtiesContext
    public void testLoginWithUserDoesNotExist() throws Exception {

        mockMvc.perform(post("/api/users/login")
                        .param("username", "not an account")
                        .param("password", "fake password"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test whether a user's saved recipes can be retrieved
     */
    @Test
    @DirtiesContext
    public void testGetSavedRecipes() throws Exception {

        List<Recipe> recipes = Arrays.asList(
                new Recipe().setName("recipe1"),
                new Recipe().setName("recipe2"));

        recipes = recipeRepository.saveAll(recipes);

        User user = mockUsers[4].setSavedRecipes(new HashSet<>(recipes));

        user = userRepository.save(user);

        String result = mockMvc.perform(get("/api/users/"+ user.getId() + "/recipes"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Recipe> actual = Arrays.asList(serializer.readValue(result, Recipe[].class));

        for (Recipe r : actual)
            logger.warn(r.getId().toString());

        for (Recipe r: recipes)
            logger.warn(r.getId().toString());

        assertEquals(recipes.size(), actual.size());
        assertTrue(actual.containsAll(recipes));
    }

    /**
     * Test whether the API will reject an attempt to retrieve recipes from an account that does not exist
     */
    @Test
    @DirtiesContext
    public void testGetSavedRecipesUserDoesNotExist() throws Exception {

        mockMvc.perform(get("/api/users/42069/recipes"))
                .andExpect(status().isNotFound());

    }

    /**
     * Test whether a recipe that exists can be saved to a user account that exists
     */
    @Test
    @DirtiesContext
    public void testSaveRecipeToAccount() throws Exception {

        List<Recipe> recipes = Arrays.asList(
                new Recipe().setName("recipe1").setId(2L),
                new Recipe().setName("recipe2").setId(3L));

        recipes = recipeRepository.saveAll(recipes);

        User user = mockUsers[4].setSavedRecipes(new HashSet<>(recipes));

        user = userRepository.save(user);

        mockMvc.perform(post("/api/users/" + user.getId() + "/recipes/" +
                        recipes.get(0).getId()))
                .andExpect(status().isOk());
    }

    /**
     * Test whether the API will reject an attempt to save a recipe that exists to an account that does not exist
     */
    @Test
    @DirtiesContext
    public void testSaveRecipeToAccountUserDoesNotExist() throws Exception {

        mockMvc.perform(post("/api/users/42069/recipes/1"))
                .andExpect(status().isNotFound());

    }

    /**
     * Test whether the API will reject an attempt to save a recipe that does not exist to an account that does exist
     */
    @Test
    @DirtiesContext
    public void testSaveRecipeDoesNotExistToAccount() throws Exception {

        User user = mockUsers[0];
        user = userRepository.save(user);

        mockMvc.perform(post("/api/users/" + user.getId() + "/recipes/99999"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test whether a dietary restriction can be added to an account
     */
    @Test
    @DirtiesContext
    public void testAddDietaryRestrictionToAccount() throws Exception {

        User user = mockUsers[0];
        user = userRepository.save(user);

        mockMvc.perform(post("/api/users/" + user.getId() + "/dietary-restrictions")
                        .param("dietaryRestriction", "vegan"))
                .andExpect(status().isOk());

        user = userRepository.getById(user.getId());

        assertEquals(user.getDietaryRestrictions().size(), 1);
        assertTrue(user.getDietaryRestrictions().contains("vegan"));
    }

    /**
     * Test whether the API will reject an attempt to add a dietary restriction to an account that does not exist
     */
    @Test
    @DirtiesContext
    public void testAddDietaryRestrictionToAccountUserDoesNotExist() throws Exception {

        mockMvc.perform(post("/api/users/42069/dietary-restrictions")
                        .param("dietaryRestriction", "vegan"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test whether the API will reject an attempt to add a dietary restriction that does not exist to an account that does exist
     */
    @Test
    @DirtiesContext
    public void testAddDietaryRestrictionDoesNotExistToAccount() throws Exception {

        User user = mockUsers[0];
        user = userRepository.save(user);

        mockMvc.perform(post("/api/users/" + user.getId() + "/dietary-restrictions")
                        .param("dietaryRestriction", "something"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test whether a dietary restriction can be removed from an account
     */
    @Test
    @DirtiesContext
    public void testRemoveDietaryRestrictionFromAccount() throws Exception {

        User user = mockUsers[0];

        Set<String> restrictions = new HashSet<>();
        restrictions.add("vegan");
        restrictions.add("vegetarian");
        restrictions.add("gluten");
        user.setDietaryRestrictions(restrictions);
        user = userRepository.save(user);

        mockMvc.perform(delete("/api/users/" + user.getId() + "/dietary-restrictions")
                        .param("dietaryRestriction", "vegan"))
                .andExpect(status().isOk());

        restrictions.remove("vegan");
        user = userRepository.getById(user.getId());

        assertEquals(user.getDietaryRestrictions(), restrictions);
    }

    /**
     * Test whether the API will reject an attempt to remove a dietary restriction from an account that does not exist
     */
    @Test
    @DirtiesContext
    public void testRemoveDietaryRestrictionFromAccountUserDoesNotExist() throws Exception {

        mockMvc.perform(delete("/api/users/42069/dietary-restrictions")
                        .param("dietaryRestriction", "vegan"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test whether the API will reject an attempt to remove a dietary restriction that does not exist from an account that does exist
     */
    @Test
    @DirtiesContext
    public void testRemoveDietaryRestrictionDoesNotExistFromAccount() throws Exception {
        User user = mockUsers[0];

        Set<String> restrictions = new HashSet<>();
        restrictions.add("vegan");
        restrictions.add("vegetarian");
        restrictions.add("gluten");
        user.setDietaryRestrictions(restrictions);
        user = userRepository.save(user);

        System.out.println(mockMvc.perform(delete("/api/users/" + user.getId() + "/dietary-restrictions")
                        .param("dietaryRestriction", "something"))
                .andExpect(status().isBadRequest())
                .andReturn().getRequest().getRequestURL());
    }
}
