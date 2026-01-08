package io.inugami.monitoring.core.sensors;

import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.framework.configuration.services.ConfigHandlerHashMap;
import io.inugami.framework.interfaces.monitoring.models.GenericModelCallType;
import io.inugami.framework.interfaces.monitoring.models.GenericModelCounterType;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModelDTO;
import io.inugami.framework.interfaces.monitoring.models.MonitoringContextDTO;
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
                                   "asset" : "inugami",
                                   "counterType" : "hits",
                                   "date" : "2026-01-08T21:25:15.699374997",
                                   "environment" : "test",
                                   "instanceName" : "inu",
                                   "instanceNumber" : "001",
                                   "nonTemporalHash" : "inugami:test:inu:001:hits:::service::count:min",
                                   "service" : "service",
                                   "time" : 0,
                                   "timeUnit" : "min",
                                   "timestamp" : 1767907515,
                                   "value" : {
                                     "decimal" : false,
                                     "value" : 50
                                   },
                                   "valueType" : "count"
                                 } ]
                           """,
                   SkipLineMatcher.of(3,11));
    }

    @Test
    void process_withCustomKpi() {
        final var service = buildService();
        final var data = GenericMonitoringModelDTO.builder()
                                                  .asset("inugami")
                                                  .environment("test")
                                                  .instanceName("inu")
                                                  .instanceNumber("001")
                                                  .callType("REST")
                                                  .counterType(ServiceValueTypes.HITS.getKeywork())
                                                  .service("service");

        ServicesSensor.addData(List.of(
                data
                        .addCallType(GenericModelCallType.REST)
                        .service("user")
                        .subService("email_domain")
                        .addCounterType(GenericModelCounterType.HITS)
                        .valueType("inugami.io")
                        .value(2)
                        .build(),
                data
                        .addCallType(GenericModelCallType.REST)
                        .service("user")
                        .subService("email_domain")
                        .addCounterType(GenericModelCounterType.HITS)
                        .valueType("gmail.com")
                        .value(2)
                        .build(),
                data
                        .addCallType(GenericModelCallType.REST)
                        .service("user")
                        .subService("email_domain")
                        .addCounterType(GenericModelCounterType.HITS)
                        .valueType("inugami.io")
                        .value(4)
                        .build()));

        assertText(service.process(),
                   """
                           [ {
                                      "asset" : "inugami",
                                      "callType" : "REST",
                                      "counterType" : "hits",
                                      "date" : "2026-01-08T21:25:15.070482083",
                                      "environment" : "test",
                                      "instanceName" : "inu",
                                      "instanceNumber" : "001",
                                      "nonTemporalHash" : "inugami:test:inu:001:hits::REST:user:email_domain:gmail.com:",
                                      "service" : "user",
                                      "subService" : "email_domain",
                                      "time" : 0,
                                      "timestamp" : 1767907515,
                                      "value" : {
                                        "decimal" : false,
                                        "value" : 2
                                      },
                                      "valueType" : "gmail.com"
                                    }, {
                                      "asset" : "inugami",
                                      "callType" : "REST",
                                      "counterType" : "hits",
                                      "date" : "2026-01-08T21:25:15.079457059",
                                      "environment" : "test",
                                      "instanceName" : "inu",
                                      "instanceNumber" : "001",
                                      "nonTemporalHash" : "inugami:test:inu:001:hits::REST:user:email_domain:inugami.io:min",
                                      "service" : "user",
                                      "subService" : "email_domain",
                                      "time" : 0,
                                      "timeUnit" : "min",
                                      "timestamp" : 1767907515,
                                      "value" : {
                                        "decimal" : false,
                                        "value" : 6
                                      },
                                      "valueType" : "inugami.io"
                                    } ]
                           """, SkipLineMatcher.of(4,12,22,31));
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    ServicesSensor buildService() {
        final Map<String, String> configuration = new LinkedHashMap<>();
        return (ServicesSensor) new ServicesSensor().buildInstance(500,
                                                                   null,
                                                                   new ConfigHandlerHashMap(configuration),
                                                                   MonitoringContextDTO.builder().build());
    }
}