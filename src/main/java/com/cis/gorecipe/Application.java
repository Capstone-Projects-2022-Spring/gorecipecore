package com.cis.gorecipe;

import com.cis.gorecipe.model.User;
import com.cis.gorecipe.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;


/**
 * The entry point of the REST application
 */
@EnableSwagger2
@SpringBootApplication
public class Application {

    private final UserRepository userRepository;

    public Application(UserRepository repository) {
        this.userRepository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cis.gorecipe"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "GoRecipe Core",
                "REST API for GoRecipe",
                "1.0.0",
                null,
                new Contact("Yaki Lebovits", "https://www.cis.temple.edu", "yakir@temple.edu"),
                "GPL 2",
                "https://www.gnu.org/licenses/old-licenses/gpl-2.0.html",
                Collections.emptyList()
        );
    }

    /**
     * persist some mock data on startup
     */
    @Profile("!test")
    @Bean
    public CommandLineRunner createMockUsers() {
        return (args) -> {
            userRepository.deleteAll();

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            User[] mockUsers = new User[]{
                    new User().setUsername("username1")
                            .setEmail("yakir@temple.edu")
                            .setFirstName("Yakir")
                            .setLastName("Lebovits")
                            .setBirthDate(new Date(0))
                            .setId(1L)
                            .setPassword(encoder.encode("password")),
                    new User().setUsername("username2")
                            .setEmail("cis1@temple.edu")
                            .setFirstName("Sean")
                            .setLastName("Williams")
                            .setBirthDate(new Date(0))
                            .setId(2L)
                            .setPassword(encoder.encode("password")),
                    new User().setUsername("username3")
                            .setEmail("cis2@temple.edu")
                            .setFirstName("Olivia")
                            .setLastName("Felmey")
                            .setBirthDate(new Date(0))
                            .setId(3L)
                            .setPassword(encoder.encode("password")),
                    new User().setUsername("username4")
                            .setEmail("cis3@temple.edu")
                            .setFirstName("Phi")
                            .setLastName("Truong")
                            .setBirthDate(new Date(0))
                            .setId(4L)
                            .setPassword(encoder.encode("password")),
                    new User().setUsername("username5")
                            .setEmail("cis4@temple.edu")
                            .setFirstName("Anna")
                            .setLastName("Gillen")
                            .setBirthDate(new Date(0))
                            .setId(5L)
                            .setPassword(encoder.encode("password")),
                    new User().setUsername("username6")
                            .setEmail("cis5@temple.edu")
                            .setFirstName("Casey")
                            .setLastName("Maloney")
                            .setBirthDate(new Date(0))
                            .setId(6L)
                            .setPassword(encoder.encode("password"))};

            userRepository.saveAll(Arrays.asList(mockUsers));
        };
    }
}
