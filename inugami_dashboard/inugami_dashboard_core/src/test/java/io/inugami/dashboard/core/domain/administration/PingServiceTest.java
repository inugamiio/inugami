package io.inugami.dashboard.core.domain.administration;

import io.inugami.dashboard.core.configuration.InugamiConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static io.inugami.commons.test.UnitTestHelper.assertText;

@ExtendWith(MockitoExtension.class)
class PingServiceTest {

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public static Clock CLOCK = Clock.fixed(Instant.parse("2025-12-02T20:53:42.00Z"), ZoneOffset.UTC);

    //==================================================================================================================
    // TEST
    //==================================================================================================================
    @Test
    void ping_nominal() {
        assertText(service().ping(),
                   """
                           {
                             "applicationName" : "my-application",
                             "now" : "2025-12-02T20:53:42"
                           }
                           """);
    }

    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    PingService service() {
        return PingService.builder()
                          .clock(CLOCK)
                          .properties(InugamiConfiguration.builder()
                                                          .application(InugamiConfiguration.InugamiConfigurationApplication.builder()
                                                                                                                           .name("my-application")
                                                                                                                           .build())
                                                          .build())
                          .build();
    }
}