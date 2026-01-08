package io.inugami.monitoring.core.sensors.aggregators;

import io.inugami.framework.configuration.services.ConfigHandlerHashMap;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModelDTO;
import io.inugami.monitoring.core.sensors.ServiceValueTypes;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

class ServicesSensorAggregatorResponseTimeTest {
    // =================================================================================================================
    // ACCEPT
    // =================================================================================================================
    @Test
    void accept_nominal() {
        final var service = buildService();
        assertThat(service.accept(GenericMonitoringModelDTO.builder()
                                                           .counterType(ServiceValueTypes.RESPONSE_TIME.getKeywork())
                                                           .build(), null)).isTrue();

        assertThat(service.accept(GenericMonitoringModelDTO.builder()
                                                           .counterType(ServiceValueTypes.HITS.getKeywork())
                                                           .build(), null)).isFalse();
    }


    @Test
    void compute_nominal() {
        final var service = buildService();
        assertText(service.compute(buildData(), List.of(15L, 125L, 164L, 785L, 12L, 5L, 22L), new ConfigHandlerHashMap()),
                   """
                           [ {
                                "asset" : "inugami",
                                "counterType" : "responseTime",
                                "environment" : "test",
                                "instanceName" : "inu",
                                "instanceNumber" : "001",
                                "nonTemporalHash" : "inugami:test:inu:001:responseTime:::service::min:min",
                                "service" : "service",
                                "time" : 0,
                                "timeUnit" : "min",
                                "timestamp" : 0,
                                "value" : 15,
                                "valueType" : "min"
                              }, {
                                "asset" : "inugami",
                                "counterType" : "responseTime",
                                "environment" : "test",
                                "instanceName" : "inu",
                                "instanceNumber" : "001",
                                "nonTemporalHash" : "inugami:test:inu:001:responseTime:::service::max:min",
                                "service" : "service",
                                "time" : 0,
                                "timeUnit" : "min",
                                "timestamp" : 0,
                                "value" : 22,
                                "valueType" : "max"
                              }, {
                                "asset" : "inugami",
                                "counterType" : "responseTime",
                                "environment" : "test",
                                "instanceName" : "inu",
                                "instanceNumber" : "001",
                                "nonTemporalHash" : "inugami:test:inu:001:responseTime:::service::p99:min",
                                "service" : "service",
                                "time" : 0,
                                "timeUnit" : "min",
                                "timestamp" : 0,
                                "value" : 22,
                                "valueType" : "p99"
                              }, {
                                "asset" : "inugami",
                                "counterType" : "responseTime",
                                "environment" : "test",
                                "instanceName" : "inu",
                                "instanceNumber" : "001",
                                "nonTemporalHash" : "inugami:test:inu:001:responseTime:::service::p95:min",
                                "service" : "service",
                                "time" : 0,
                                "timeUnit" : "min",
                                "timestamp" : 0,
                                "value" : 22,
                                "valueType" : "p95"
                              }, {
                                "asset" : "inugami",
                                "counterType" : "responseTime",
                                "environment" : "test",
                                "instanceName" : "inu",
                                "instanceNumber" : "001",
                                "nonTemporalHash" : "inugami:test:inu:001:responseTime:::service::p90:min",
                                "service" : "service",
                                "time" : 0,
                                "timeUnit" : "min",
                                "timestamp" : 0,
                                "value" : 22,
                                "valueType" : "p90"
                              }, {
                                "asset" : "inugami",
                                "counterType" : "responseTime",
                                "environment" : "test",
                                "instanceName" : "inu",
                                "instanceNumber" : "001",
                                "nonTemporalHash" : "inugami:test:inu:001:responseTime:::service::p75:min",
                                "service" : "service",
                                "time" : 0,
                                "timeUnit" : "min",
                                "timestamp" : 0,
                                "value" : 5,
                                "valueType" : "p75"
                              }, {
                                "asset" : "inugami",
                                "counterType" : "responseTime",
                                "environment" : "test",
                                "instanceName" : "inu",
                                "instanceNumber" : "001",
                                "nonTemporalHash" : "inugami:test:inu:001:responseTime:::service::p50:min",
                                "service" : "service",
                                "time" : 0,
                                "timeUnit" : "min",
                                "timestamp" : 0,
                                "value" : 785,
                                "valueType" : "p50"
                              }, {
                                "asset" : "inugami",
                                "counterType" : "responseTime",
                                "environment" : "test",
                                "instanceName" : "inu",
                                "instanceNumber" : "001",
                                "nonTemporalHash" : "inugami:test:inu:001:responseTime:::service::avg:min",
                                "service" : "service",
                                "time" : 0,
                                "timeUnit" : "min",
                                "timestamp" : 0,
                                "value" : {
                                  "decimal" : true,
                                  "value" : 161.14285714285714
                                },
                                "valueType" : "avg"
                              } ]
                           """);
    }

    @Test
    void compute_withoutValues() {
        final var service = buildService();
        assertThat(service.compute(buildData(), List.of(), new ConfigHandlerHashMap())).isEmpty();
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    ServicesSensorAggregatorResponseTime buildService() {
        return new ServicesSensorAggregatorResponseTime();
    }

    GenericMonitoringModelDTO buildData() {
        return GenericMonitoringModelDTO.builder()
                                        .asset("inugami")
                                        .environment("test")
                                        .instanceName("inu")
                                        .instanceNumber("001")
                                        .counterType(ServiceValueTypes.RESPONSE_TIME.getKeywork())
                                        .service("service")
                                        .build();
    }
}