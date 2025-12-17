package io.inugami.monitoring.core.sensors;

import io.inugami.framework.configuration.services.ConfigHandlerHashMap;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModelDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.monitoring.core.sensors.ServicesSensor.SERVICES_SENSOR;
import static org.assertj.core.api.Assertions.assertThat;

class ServicesSensorTest {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================

    @BeforeEach
    public void init() {
        ServicesSensor.clean();
    }

    @Test
    void defineInterval_nominal() {
        final var service = buildService();
        service.defineInterval(1000L);
        assertThat(service.getInterval()).isEqualTo(1000L);
    }

    @Test
    void getName_nominal() {
        assertThat(buildService().getName()).isEqualTo(SERVICES_SENSOR);
    }

    @Test
    void process_nominal() {
        final var service = buildService();
        final var data = GenericMonitoringModelDTO.builder()
                                                  .asset("inugami")
                                                  .environment("test")
                                                  .instanceName("inu")
                                                  .instanceNumber("001")
                                                  .counterType(ServiceValueTypes.HITS.getKeywork())
                                                  .service("service");

        ServicesSensor.addData(List.of(
                data.value(10L)
                    .build(),
                data.value(15L)
                    .build(),
                data.value(25L)
                    .build()));

        assertText(service.process(),
                   """
                           [ {
                             "nonTemporalHash" : ":::::::::count:min",
                             "time" : 0,
                             "timeUnit" : "min",
                             "timestamp" : 0,
                             "value" : 50,
                             "valueType" : "count"
                           } ]
                           """);
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    ServicesSensor buildService() {
        final Map<String, String> configuration = new LinkedHashMap<>();
        return (ServicesSensor) new ServicesSensor().buildInstance(500,
                                                                   null,
                                                                   new ConfigHandlerHashMap(configuration));
    }
}