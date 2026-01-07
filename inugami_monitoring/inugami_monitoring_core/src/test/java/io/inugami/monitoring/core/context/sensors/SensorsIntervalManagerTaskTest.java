package io.inugami.monitoring.core.context.sensors;

import io.inugami.commons.test.UnitTestHelper;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModelDTO;
import io.inugami.framework.interfaces.monitoring.senders.MonitoringSender;
import io.inugami.framework.interfaces.monitoring.senders.MonitoringSenderException;
import io.inugami.framework.interfaces.monitoring.sensors.MonitoringSensor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.internal.verification.AtMost;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModelDTO.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SensorsIntervalManagerTaskTest {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String                                       NOMINAL = """
            [ {
              "asset" : "inugami",
              "callType" : "REST",
              "counterType" : "hits",
              "data" : "additional data",
              "date" : "2025-12-02T20:53:42",
              "device" : "ANY",
              "environment" : "test",
              "errorCode" : "USER-0_0",
              "errorType" : "functional",
              "instanceName" : "inu",
              "instanceNumber" : "001",
              "nonTemporalHash" : "inugami:test:inu:001:hits:ANY:REST:user:NONE:hit:s",
              "path" : "datacenter.europe",
              "service" : "user",
              "subService" : "NONE",
              "time" : 1,
              "timeUnit" : "s",
              "timestamp" : 1764708822,
              "value" : 2,
              "valueType" : "hit"
            } ]
            """;
    public static       Clock                                        CLOCK   = Clock.fixed(Instant.parse("2025-12-02T20:53:42.00Z"), ZoneOffset.UTC);
    @Mock
    private             MonitoringSender                             sender;
    @Mock
    private             MonitoringSensor                             sensor;
    @Captor
    private             ArgumentCaptor<List<GenericMonitoringModel>> dataCaptor;


    // =================================================================================================================
    // TEST
    // =================================================================================================================
    @Test
    void nominal() throws TimeoutException, ExecutionException, InterruptedException, MonitoringSenderException {
        final var service = buildSensorManager();
        try {
            when(sensor.process()).thenReturn(List.of(buildGenericMonitoringModel()));

            final var future = service.add(sensor);
            UnitTestHelper.waitForDone(10000, future);


            assertText(future.get(), NOMINAL);


            verify(sender, new AtMost(5)).process(dataCaptor.capture());
        } finally {
            service.shutdown(null);
        }
    }


    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    private SensorsIntervalManagerTask buildSensorManager() {
        final var result = new SensorsIntervalManagerTask(1, 500, List.of(sender), null);
        result.initialize(null);
        return result;
    }

    private GenericMonitoringModel buildGenericMonitoringModel() {
        return GenericMonitoringModelDTO.builder()
                                        .asset("inugami")
                                        .environment("test")
                                        .instanceName("inu")
                                        .instanceNumber("001")
                                        .counterType("hits")
                                        .callType(CALL_TYPE_REST)
                                        .device(ANY)
                                        .service("user")
                                        .subService(NONE)
                                        .valueType("hit")
                                        .timeUnit("s")
                                        .date(LocalDateTime.now(CLOCK))
                                        .time(1L)
                                        .errorCode("USER-0_0")
                                        .errorType("functional")
                                        .value(2L)
                                        .path("datacenter.europe")
                                        .data("additional data")
                                        .timestamp(LocalDateTime.now(CLOCK)
                                                                .toEpochSecond(ZoneOffset.UTC))
                                        .build();
    }

}