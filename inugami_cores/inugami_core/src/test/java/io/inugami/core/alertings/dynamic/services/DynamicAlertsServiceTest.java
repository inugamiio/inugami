package io.inugami.core.alertings.dynamic.services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.tools.TestUnitResources;
import io.inugami.core.alertings.dynamic.entities.ActivationTime;
import io.inugami.core.alertings.dynamic.entities.DynamicAlertEntity;
import io.inugami.core.alertings.dynamic.entities.TimeSlot;
import io.inugami.core.context.ApplicationContext;
import io.inugami.core.context.Context;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DynamicAlertsServiceTest implements TestUnitResources {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final ApplicationContext CONTEXT = Context.initializeStandalone();

    private static final DynamicAlertsService SERVICE = new DynamicAlertsService(CONTEXT);

    // =========================================================================
    // ITEGRATION
    // =========================================================================
    public static void main(final String[] args) {

    }

    @AfterEach
    public void shutdown() {
        CONTEXT.forceShutdownSubContext();
    }

    // =========================================================================
    // UNIT TESTS
    // =========================================================================
    @Test
    void testResolveAlertsToProcess() throws Exception {
        final List<DynamicAlertEntity> entities = loadEntities("alerting/dynamicAlerts.01.json");
        final List<DynamicAlertEntity> cas1 = SERVICE
                .resolveAlertsToProcess(entities, buildDate("2019/05/19 14:00"));
        assertEquals(2, cas1.size());

        final List<DynamicAlertEntity> cas2 = SERVICE.resolveAlertsToProcess(entities, buildDate("2019/05/19 14:03"));
        assertEquals(1, cas2.size());
    }

    @Test
    void testAlertIsInTimeSlot() throws Exception {
        final long time = buildDate("2019/05/19 15:10");

        //@formatter:off
        assertTrue( SERVICE.alertIsInTimeSlot(buildActivation(asList("sunday","monday"),asList(new TimeSlot("06:00", "16:00"))), time));
        assertFalse(SERVICE.alertIsInTimeSlot(buildActivation(asList("sunday","monday"),asList(new TimeSlot("16:00", "20:00"))), time));
        assertFalse(SERVICE.alertIsInTimeSlot(buildActivation(asList(),asList(new TimeSlot("06:00", "16:00"))), time));
        assertFalse(SERVICE.alertIsInTimeSlot(buildActivation(asList("monday","tuesday"),asList(new TimeSlot("06:00", "16:00"))), time));
        //@formatter:on
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private List<DynamicAlertEntity> loadEntities(final String path) throws IOException {
        final File   file = FilesUtils.buildFile(initResourcesPath(), path);
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
