package com.cis.gorecipe;

import com.cis.gorecipe.repository.FoodImageRepository;
import com.cis.gorecipe.repository.IngredientRepository;
import com.cis.gorecipe.repository.RecipeRepository;
import com.cis.gorecipe.repository.UserRepository;
import com.cis.gorecipe.service.ClarifaiService;
import com.cis.gorecipe.service.S3Service;
import com.cis.gorecipe.service.SpoonacularService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.TimeZone;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * This class establishes the testcontainer environment and all objects needed for any testing
 */
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public abstract class BaseTest {

    /**
     * A JSON serializer to deserialize API responses
     */
    protected final ObjectMapper serializer = new ObjectMapper();

    protected final ClassLoader classLoader = getClass().getClassLoader();

    protected final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /* mock the services to isolate the API for testing */
    @MockBean
    protected S3Service s3Service;

    @MockBean
    protected ClarifaiService clarifaiService;

    @MockBean
    protected SpoonacularService spoonacularService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected IngredientRepository ingredientRepository;

    @Autowired
    protected FoodImageRepository foodImageRepository;

    @Autowired
    protected RecipeRepository recipeRepository;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    protected MockMvc mockMvc;

    public BaseTest() {
        serializer.registerModule(new JavaTimeModule());
        serializer.setTimeZone(TimeZone.getTimeZone("EST"));
    }

    @BeforeClass
    public void setupMockMvc() {
        mockMvc = webAppContextSetup(wac).build();
    }
}
