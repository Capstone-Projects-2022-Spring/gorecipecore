package com.cis.gorecipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MySQLContainer;

import java.util.TimeZone;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * This class establishes the testcontainer environment and all objects needed for any testing
 */
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:test.properties")
public abstract class BaseTest {

    /**
     * An isolated MySQL database inside a docker container to use for testing
     */
    private static final MySQLContainer<?> mySQLContainer;

    static {
        mySQLContainer = new MySQLContainer<>("mysql:latest")
                .withUsername("testcontainers")
                .withPassword("Testcontain3rs!")
                .withReuse(true);
        mySQLContainer.start();
    }

    /**
     * A JSON serializer to deserialize API responses
     */
    protected final ObjectMapper serializer = new ObjectMapper();

    protected final ClassLoader classLoader = getClass().getClassLoader();

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    protected MockMvc mockMvc;

    public BaseTest() {
        serializer.registerModule(new JavaTimeModule());
        serializer.setTimeZone(TimeZone.getTimeZone("EST"));
    }

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
    }

    @BeforeClass
    public void setupMockMvc() {
        mockMvc = webAppContextSetup(wac).build();
    }
}
