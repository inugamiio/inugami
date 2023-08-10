package io.inugami.core.alertings.dynamic.services;


import io.inugami.api.alertings.AlerteLevels;
import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.alertings.DynamicAlertingLevel;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.data.basic.JsonObjects;
import io.inugami.api.models.data.graphite.TimeValue;
import io.inugami.api.models.data.graphite.number.FloatNumber;
import io.inugami.api.models.events.SimpleEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ComputeDynamicAlertTest {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testProcess() throws Exception {

        final ComputeDynamicAlert service = new ComputeDynamicAlert();

        final long   duration    = 300;
        final String nominal     = "2.1";
        final String unit        = "%";
        final String serviceName = "search";
        final String component   = "JOE";


        final List<DynamicAlertingLevel> levels = List.of(
                DynamicAlertingLevel.builder()
                                    .level(AlerteLevels.WARN)
                                    .threshold(5.5)
                                    .activationDelay(1)
                                    .duration(duration)
                                    .nominal(nominal)
                                    .unit(unit)
                                    .service(serviceName)
                                    .component(component)
                                    .inverse(false)
                                    .build(),

                DynamicAlertingLevel.builder()
                                    .level(AlerteLevels.ERROR)
                                    .threshold(10.5)
                                    .activationDelay(1)
                                    .duration(duration)
                                    .nominal(nominal)
                                    .unit(unit)
                                    .service(serviceName)
                                    .component(component)
                                    .inverse(false)
                                    .build());

        final String       message    = "error occurs";
        final String       subMessage = "some error is present";
        final List<String> tags       = Arrays.asList("PRD", "SEARCH", "MICRO_SERVICE");

        final List<AlertingResult> cas1 = service.process(SimpleEvent.simpleEventBuilder()
                                                                     .name("event-test")
                                                                     .build(), buildTimeValueData(), levels, message, subMessage,
                                                          tags, null);
        assertNotNull(cas1);
        assertEquals(AlerteLevels.WARN, cas1.get(0).getLevelType());
        assertEquals("warn PRD SEARCH MICRO_SERVICE", cas1.get(0).getLevel());

        final List<DynamicAlertingLevel> levels2 = List.of(
                DynamicAlertingLevel.builder()
                                    .level(AlerteLevels.WARN)
                                    .threshold(5.5)
                                    .activationDelay(2)
                                    .duration(duration)
                                    .nominal(nominal)
                                    .unit(unit)
                                    .service(serviceName)
                                    .component(component)
                                    .inverse(false)
                                    .build(),

                DynamicAlertingLevel.builder()
                                    .level(AlerteLevels.ERROR)
                                    .threshold(10.5)
                                    .activationDelay(2)
                                    .duration(duration)
                                    .nominal(nominal)
                                    .unit(unit)
                                    .service(serviceName)
                                    .component(component)
                                    .inverse(false)
                                    .build());

        final List<AlertingResult> cas2 = service.process(SimpleEvent.simpleEventBuilder()
                                                                     .name("event-test")
                                                                     .build(), buildTimeValueData(), levels2, message, subMessage,
                                                          tags, null);
        assertTrue(cas2.isEmpty());

    }
    // =========================================================================
    // OVERRIDES
    // =========================================================================

    private JsonObject buildTimeValueData() {
        final List<TimeValue> result    = new ArrayList<>();
        final String          path      = "org.foo.bar.cpu.p95";
        final long            timestamp = System.currentTimeMillis() / 1000;
        result.add(new TimeValue(path, new FloatNumber(6.2), timestamp));
        result.add(new TimeValue(path, new FloatNumber(0.2), timestamp));
        result.add(new TimeValue(path, new FloatNumber(5.0), timestamp));

        return new JsonObjects<>(result);
    }

}
