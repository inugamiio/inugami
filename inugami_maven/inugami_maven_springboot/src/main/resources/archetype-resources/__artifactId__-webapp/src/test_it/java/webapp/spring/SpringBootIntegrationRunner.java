package ${package}.webapp.spring;

import ${package}.webapp.Application;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * @since 2025-12-28
 */
@ActiveProfiles("test")
@ContextConfiguration(
        initializers = {
                SpringBootInitializer.class
        })
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SpringBootIntegrationRunner {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public static final String CONTENT_TYPE     = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";

    //==================================================================================================================
    // INIT
    //==================================================================================================================
    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port    = SpringBootInitializer.SERVER_PORT;
    }
}
