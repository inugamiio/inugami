package org.inugami.core.alertings.dynamic.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.inugami.api.alertings.AlerteLevels;
import org.inugami.api.alertings.AlertingResult;
import org.inugami.api.alertings.DynamicAlertingLevel;
import org.inugami.api.models.data.JsonObject;
import org.inugami.api.models.data.basic.JsonObjects;
import org.inugami.api.models.data.graphite.TimeValue;
import org.inugami.api.models.data.graphite.number.FloatNumber;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.models.events.SimpleEventBuilder;
import org.junit.Test;

public class ComputeDynamicAlertTest {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testProcess() throws Exception {
        final ComputeDynamicAlert service = new ComputeDynamicAlert();
        final SimpleEvent event = new SimpleEventBuilder().addName("event-test").build();
        final List<DynamicAlertingLevel> levels = Arrays.asList(new DynamicAlertingLevel(AlerteLevels.WARN, 5.5, 1),
                                                                new DynamicAlertingLevel(AlerteLevels.ERROR, 10.5, 1));
        final String message = "error occurs";
        final String subMessage = "some error is present";
        final List<String> tags = Arrays.asList("PRD", "SEARCH", "MICRO_SERVICE");
        
        final AlertingResult cas1 = service.process(event, buildTimeValueData(), levels, message, subMessage, tags);
        assertNotNull(cas1);
        assertEquals(AlerteLevels.WARN, cas1.getLevelType());
        assertEquals("warn PRD SEARCH MICRO_SERVICE", cas1.getLevel());
        
        final List<DynamicAlertingLevel> levels2 = Arrays.asList(new DynamicAlertingLevel(AlerteLevels.WARN, 5.5, 2),
                                                                 new DynamicAlertingLevel(AlerteLevels.ERROR, 10.5, 2));
        
        final AlertingResult cas2 = service.process(event, buildTimeValueData(), levels2, message, subMessage, tags);
        assertNull(cas2);
        
    }
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    
    private JsonObject buildTimeValueData() {
        final List<TimeValue> result = new ArrayList<>();
        final String path = "org.foo.bar.cpu.p95";
        final long timestamp = System.currentTimeMillis() / 1000;
        
        result.add(new TimeValue(path, new FloatNumber(0.2), timestamp));
        result.add(new TimeValue(path, new FloatNumber(5.0), timestamp));
        result.add(new TimeValue(path, new FloatNumber(6.2), timestamp));
        return new JsonObjects<>(result);
    }
    
}
