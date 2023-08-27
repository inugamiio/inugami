package io.inugami.monitoring.springboot.actuator.feature;

import io.inugami.commons.test.api.NumberLineMatcher;
import io.inugami.monitoring.springboot.SpringBootIntegrationTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestHelper.assertTextIntegration;

class FeatureIndicatorIT extends SpringBootIntegrationTest {


    @Test
    void health_nominal() {
        assertTextIntegration(RestAssured.given().get("/actuator/health").asString(),
                              "io/inugami/monitoring/springboot/actuator/feature/featureIndicatorIT/health_nominal.json",
                              NumberLineMatcher.of(13, 14, 15)
        );
    }
}