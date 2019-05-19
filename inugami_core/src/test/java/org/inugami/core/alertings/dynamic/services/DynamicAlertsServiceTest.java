package org.inugami.core.alertings.dynamic.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.inugami.commons.files.FilesUtils;
import org.inugami.commons.tools.TestUnitResources;
import org.inugami.core.alertings.dynamic.entities.ActivationTime;
import org.inugami.core.alertings.dynamic.entities.DynamicAlertEntity;
import org.inugami.core.alertings.dynamic.entities.TimeSlot;
import org.inugami.core.context.ApplicationContext;
import org.inugami.core.context.Context;
import org.junit.After;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DynamicAlertsServiceTest implements TestUnitResources {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final ApplicationContext   CONTEXT = Context.initializeStandalone();
    
    private static final DynamicAlertsService SERVICE = new DynamicAlertsService(CONTEXT);
    
    // =========================================================================
    // ITEGRATION
    // =========================================================================
    public static void main(final String[] args) {
        
    }
    
    @After
    public void shutdown() {
        CONTEXT.forceShutdownSubContext();
    }
    
    // =========================================================================
    // UNIT TESTS
    // =========================================================================
    @Test
    public void testResolveAlertsToProcess() throws Exception {
        final List<DynamicAlertEntity> entities = loadEntities("alerting/dynamicAlerts.01.json");
        final List<DynamicAlertEntity> cas1 = SERVICE.resolveAlertsToProcess(entities, buildDate("2019/05/16 14:00"));
        assertEquals(2, cas1.size());
        
        final List<DynamicAlertEntity> cas2 = SERVICE.resolveAlertsToProcess(entities, buildDate("2019/05/16 14:03"));
        assertEquals(1, cas2.size());
    }
    
    @Test
    public void testAlertIsInTimeSlot() throws Exception {
        final long time = buildDate("2019/05/19 15:10");
        //@formatter:off
        assertTrue( SERVICE.alertIsInTimeSlot(buildActivation(asList("sunday","monday"),asList(new TimeSlot("06:00", "16:00"))), time));
        assertFalse(SERVICE.alertIsInTimeSlot(buildActivation(asList("sunday","monday"),asList(new TimeSlot("06:00", "16:00"))), time));
        assertFalse(SERVICE.alertIsInTimeSlot(buildActivation(asList(),asList(new TimeSlot("06:00", "16:00"))), time));
        assertFalse(SERVICE.alertIsInTimeSlot(buildActivation(asList("monday","tuesday"),asList(new TimeSlot("06:00", "16:00"))), time));
        //@formatter:on
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private List<DynamicAlertEntity> loadEntities(final String path) throws IOException {
        final File file = FilesUtils.buildFile(initResourcesPath(), path);
        final String json = FilesUtils.readContent(file);
        
        //@formatter:off
        return new ObjectMapper().readValue(json, new TypeReference<List<DynamicAlertEntity>>() {});
        //@formatter:on
    }
    
    private long buildDate(final String time) throws ParseException {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(time).getTime();
    }
    
    private List<ActivationTime> buildActivation(final List<String> days, final List<TimeSlot> hours) {
        return Arrays.asList(new ActivationTime(days, hours));
    }
    
    private <T> List<T> asList(final T... values) {
        return Arrays.asList(values);
    }
}
