package io.inugami.monitoring.springboot.actuator;

import io.inugami.commons.test.UnitTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.inugami.commons.test.UnitTestHelper.assertText;

@ExtendWith(MockitoExtension.class)
class VersionHealthIndicatorTest {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String NOMINAL = """
            {
              "details" : {
                "groupId" : "io.inugami",
                "artifactId" : "inu-test",
                "version" : "4.3.0",
                "commitId" : "bb895294-efe7-484b-b670-14d004eaf461",
                "commitDate" : "2025-12-21T11:26:00"
              },
              "status" : "UP"
            }
            """;

    // =================================================================================================================
    // TEST
    // =================================================================================================================
    @Test
    void health_nominal() {
        final var indicator = indicator();
        assertText(indicator.health(), NOMINAL);
        assertText(indicator.getHealth(true), NOMINAL);
    }


    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    VersionHealthIndicator indicator() {
        return VersionHealthIndicator.builder()
                                     .groupId("io.inugami")
                                     .artifactId("inu-test")
                                     .version("4.3.0")
                                     .commitId(UnitTestData.UID)
                                     .commitDate("2025-12-21T11:26:00")
                                     .build();
    }

}