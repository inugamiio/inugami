package io.inugami.monitoring.springboot.actuator.feature;

import io.inugami.commons.test.dto.UserDataDTO;
import io.inugami.framework.api.tools.ReflectionUtils;
import io.inugami.framework.interfaces.feature.FeatureContext;
import io.inugami.framework.interfaces.feature.IFeatureService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Status;

import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeatureIndicatorTest {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String           NOMINAL = """
            {
               "details" : {
                 "features" : [ {
                   "enabledByDefault" : false,
                   "featureName" : "search_user",
                   "monitored" : false,
                   "status" : "ERROR"
                 } ]
               },
               "status" : "UNKNOWN"
             }
            """;
    @Mock
    private             IFeatureService  featureService;
    @InjectMocks
    private             FeatureIndicator indicator;

    // =================================================================================================================
    // HEALTH
    // =================================================================================================================
    @Test
    void health_nominal() {
        when(featureService.getFeatures()).thenReturn(List.of(buildFeatureContext()));
        assertText(indicator.health(), NOMINAL);
        assertText(indicator.getHealth(true), NOMINAL);
    }

    @Test
    void resolveStatus_nominal() {
        assertThat(indicator.resolveStatus(null)).isEqualTo(Status.UP);
        assertThat(indicator.resolveStatus(List.of())).isEqualTo(Status.UP);

        assertThat(indicator.resolveStatus(List.of(FeatureContext.builder()
                                                           .status(FeatureContext.Status.UNKNOWN)
                                                                 .build()))).isEqualTo(Status.UNKNOWN);

        assertThat(indicator.resolveStatus(List.of(FeatureContext.builder()
                                                                 .status(FeatureContext.Status.OUT_OF_SERVICE)
                                                                 .build()))).isEqualTo(Status.OUT_OF_SERVICE);

        assertThat(indicator.resolveStatus(List.of(FeatureContext.builder()
                                                                 .status(FeatureContext.Status.ERROR)
                                                                 .build()))).isEqualTo(Status.UNKNOWN);
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    FeatureContext buildFeatureContext() {
        MyService service = new MyService();
        return FeatureContext.builder()
                             .featureName("search_user")
                             .propertyPrefix("inugami")
                             .fallback("searchUserBySource")
                             .status(FeatureContext.Status.ERROR)
                             .instance(service)
                             .bean(MyService.class)
                             .method(ReflectionUtils.searchMethodByName(MyService.class, "searchUser"))
                             .args(new Object[]{})
                             .build();
    }

    private static class MyService {
        public UserDataDTO searchUser() {
            return null;
        }

        public UserDataDTO searchUserBySource(final String source) {
            return null;
        }
    }
}