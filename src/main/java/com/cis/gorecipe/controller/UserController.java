package com.cis.gorecipe.controller;

import com.cis.gorecipe.dto.UserDTO;
import com.cis.gorecipe.exception.RecipeNotFoundException;
import com.cis.gorecipe.exception.UserNotFoundException;
import com.cis.gorecipe.model.Recipe;
import com.cis.gorecipe.model.RecipeCalendarItem;
import com.cis.gorecipe.model.User;
import com.cis.gorecipe.repository.IngredientRepository;
import com.cis.gorecipe.repository.RecipeCalendarItemRepository;
import com.cis.gorecipe.repository.RecipeRepository;
import com.cis.gorecipe.repository.UserRepository;
import com.cis.gorecipe.service.S3Service;
import com.cis.gorecipe.util.FileUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiOperation;
import org.hibernate.PropertyValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class handles the API endpoints related to user account management
 */
@RestController
@RequestMapping("/api/users")
public class UserController {


    /**
     * For logging any errors that occur during runtime (e.g. a user is not found)
     */
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * For interfacing with the User table in the database
     */
    private final UserRepository userRepository;

    /**
     * For interfacing with the Recipe table in the database
     */
    private final RecipeRepository recipeRepository;

    /**
     * For interfacing with the Ingredient table in the database
     */
    private final IngredientRepository ingredientRepository;

    /**
     * Handles hashing passwords and checking password equality
     */
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * For interfacing with the RecipeCalendarItem table in the database
     */
    private final RecipeCalendarItemRepository calendarRepository;


    private final S3Service s3Service;

    /**
     * For parsing dates
     */
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public UserController(UserRepository userRepository, RecipeRepository recipeRepository,
                          IngredientRepository ingredientRepository, RecipeCalendarItemRepository calendarRepository, S3Service s3Service) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.calendarRepository = calendarRepository;
        this.s3Service = s3Service;
    }

    @PostMapping("/{id}/profile-picture")
    public ResponseEntity<User> uploadProfilePicture(@RequestPart("image") MultipartFile image,
                                                     @PathVariable("id") Long id) throws IOException {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(id)
                );

        if (!FileUtil.isImage(image))
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();

        String fileName = "profile_picture_" + user.getId() + "." +
                Objects.requireNonNull(image.getContentType()).split("/")[1];

        String image_s3_URI = s3Service.uploadFile(fileName, image.getInputStream(), image.getContentType());

        user.setProfilePicture(image_s3_URI);
        user = userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    /**
     * @param user the data from which a new user should be created
     * @return a DTO representing the newly created user
     */
    @PostMapping("/")
    @ApiOperation(value = "Create a new user")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {

        try {
            user = userRepository.save(user);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user = userRepository.save(user);

            return ResponseEntity.ok().body(UserDTO.mapFromUser(user));

            /* if the posted data is missing values that are required
             * or if we have a unique constraint violation */
        } catch (PropertyValueException | DataIntegrityViolationException | IllegalStateException e) {
            logger.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    /**
     * @param id the id of the user to be deleted
     * @return an HTTP response confirming if the user has been removed from the system
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete an existing user")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        if (!userRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        userRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * @param userDTO the data which should be used to update an existing user
     * @return a DTO representing the newly modified user
     */
    @PatchMapping("/{id}")
    @ApiOperation(value = "Update an existing user by providing 1 or more new field values")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {

        try {

            if (!userRepository.existsById(id))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            User user = userRepository.getById(id);

            if (userDTO.getUsername() != null)
                user.setUsername(userDTO.getUsername());

            if (userDTO.getPassword() != null)
                user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

            if (userDTO.getEmail() != null)
                user.setEmail(userDTO.getEmail());

            if (userDTO.getFirstName() != null)
                user.setFirstName(userDTO.getFirstName());

            if (userDTO.getBirthDate() != null)
                user.setBirthDate(userDTO.getBirthDate());

            user = userRepository.save(user);

            return ResponseEntity.ok().body(new UserDTO(user));

            /* if the posted data is missing values that are required
             * or if we have a unique constraint violation */
        } catch (PropertyValueException | DataIntegrityViolationException e) {
            logger.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    /**
     * @param id the id of the user to be fetched
     * @return a DTO representing the requested user
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Fetch a user's information based on their ID")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {

        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Unable to find user " + id)
                );

        return ResponseEntity.ok().body(new UserDTO(user));
    }

    /**
     * @param username the username of some user in the system
     * @param password a plaintext password corresponding to the user with the specified username
     * @return an HTTP response that contains a DTO of the specified user if the login was successful and an error message if it failed
     */
    @PostMapping("/login")
    @ApiOperation(value = "Log in to a user's account (returns the user's information on success)")
    public ResponseEntity<UserDTO> login(@RequestParam("username") String username,
                                         @RequestParam("password") String password) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            logger.error("Attempted login with username " + username + " failed due to bad username");
            return new UserNotFoundException(username);
        });

        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.ok().body(new UserDTO(user));
        } else {
            logger.error("Attempted login with username " + username + " failed due to incorrect password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /**
     * @param dateAsString the date on which the user wants to cook a recipe
     * @param userId       the user who wants to cook the recipe
     * @param recipeId     the recipe which the user wants to cook
     * @return an HTTP status indicating if the item was saved or not
     */
    @PostMapping("/{userId}/calendar/{recipeId}")
    @ApiOperation(value = "Add recipe to user's calendar", notes = "The date is formatted as yyyy-MM-dd, but the " +
            "backend parameter had to be a String because of parsing issues.")
    public ResponseEntity<Void> addRecipeToUsersCalendar(@RequestBody @JsonProperty("date") String dateAsString,
                                                         @PathVariable Long userId,
                                                         @PathVariable Long recipeId) {

        if (!(userRepository.existsById(userId) && recipeRepository.existsById(recipeId)))
            return ResponseEntity.notFound().build();

        try {
            Date date = formatter.parse(dateAsString);

            RecipeCalendarItem item = new RecipeCalendarItem()
                    .setRecipe(recipeRepository.getById(recipeId))
                    .setUser(userRepository.getById(userId))
                    .setDate(date);

            calendarRepository.save(item);

            return ResponseEntity.noContent().build();
        } catch (ParseException | DataIntegrityViolationException | PropertyValueException e) {
            logger.warn("Failed to add recipe to user's calendar: " + e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * @param id the ID of the calendar item to remove
     * @return an HTTP response indicating if the item has successfully been deleted
     */
    @DeleteMapping("/calendar/{id}")
    @ApiOperation(value = "Remove a recipe from a user's calendar")
    public ResponseEntity<Void> deleteRecipeFromDate(@PathVariable Long id) {

        try {
            calendarRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * @param userId the ID of the user whose calendar we want
     * @return a list of all items saved to the user's calendar
     */
    @GetMapping("/{userId}/calendar")
    @ApiOperation(value = "Fetch a list of all recipes that have been saved to a user's calendar")
    public ResponseEntity<List<RecipeCalendarItem>> getUsersCalendar(@PathVariable Long userId) {

        if (!userRepository.existsById(userId))
            return ResponseEntity.notFound().build();

        List<RecipeCalendarItem> items = calendarRepository
                .getRecipeCalendarItemByUser(userRepository.getById(userId));

        return ResponseEntity.ok(items);
    }

    /**
     * @param userId the id of the user who's saved recipes are being requested
     * @return a list of recipes that the specified user saved
     */
    @GetMapping("/{userId}/recipes")
    public ResponseEntity<List<Recipe>> getSavedRecipes(@PathVariable Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(userId)
                );

        return ResponseEntity.ok(new ArrayList<>(user.getSavedRecipes()));
    }

    /**
     * @param userId   the id of the user who is saving the specified recipe
     * @param recipeId the id of the recipe which the user is attempting to save
     * @return an HTTP response that confirms if the recipe has been saved to the user's account
     */
    @ApiIgnore
    @PostMapping("/{userId}/recipes/{recipeId}")
    public ResponseEntity<Void> saveRecipeToAccount(@PathVariable Long userId, @PathVariable Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(userId)
                );

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() ->
                        new RecipeNotFoundException(recipeId)
                );

        Set<Recipe> recipes = user.getSavedRecipes();
        recipes.add(recipe);
        user.setSavedRecipes(recipes);

        return ResponseEntity.ok().build();
    }

    /**
     * @param userId   the id of the user who is removing the specified recipe
     * @param recipeId the id of the recipe which the user is attempting to remove from their account
     * @return an HTTP response that confirms if the recipe has been unsaved to the user's account
     */
    @ApiIgnore
    @DeleteMapping("/{userId}/recipes/{recipeId}")
    public ResponseEntity<Void> removeSavedRecipeFromAccount(@PathVariable Long userId, @PathVariable Long recipeId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(userId)
                );

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() ->
                        new RecipeNotFoundException(recipeId)
                );

        Set<Recipe> recipes = user.getSavedRecipes();
        recipes.remove(recipe);
        user.setSavedRecipes(recipes);

        return ResponseEntity.ok().build();
    }

    /**
     * @param userId               the id of the user who is adding a dietary restriction to their account
     * @param dietaryRestrictionId the id of the dietary restriction that the user is attempting to add to their account
     * @return an HTTP response that confirms if the dietary restriction has been added to the user's account
     */
    @ApiIgnore
    @PostMapping("/{userId}/dietary-restrictions/{dietaryRestrictionId}")
    public ResponseEntity<Void> addDietaryRestrictionToAccount(@PathVariable Long userId,
                                                               @PathVariable Long dietaryRestrictionId) {
        return null;
    }

    /**
     * @param userId               the id of the user who is removing a dietary restriction from their account
     * @param dietaryRestrictionId the id of the dietary restriction that the user is attempting to remove from their account
     * @return an HTTP response that confirms if the dietary restriction has been removed from the user's account
     */
    @ApiIgnore
    @DeleteMapping("/{userId}/dietary-restrictions/{dietaryRestrictionId}")
    public ResponseEntity<Void> removeDietaryRestrictionToAccount(@PathVariable Long userId,
                                                                  @PathVariable Long dietaryRestrictionId) {
        return null;
    }
}
