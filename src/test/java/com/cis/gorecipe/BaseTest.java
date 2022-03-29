package com.cis.gorecipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public abstract class BaseTest {

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

    @BeforeClass
    public void setupMockMvc() {
        mockMvc = webAppContextSetup(wac).build();
    }
}
