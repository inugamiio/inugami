package org.inugami.core.alertings.dynamic.services;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.inugami.commons.files.FilesUtils;
import org.inugami.commons.tools.TestUnitResources;
import org.inugami.core.alertings.dynamic.entities.DynamicAlertEntity;
import org.inugami.core.context.Context;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DynamicAlertsServiceTest implements TestUnitResources {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final DynamicAlertsService SERVICE = initService();
    
    // =========================================================================
    // ITEGRATION
    // =========================================================================
    public static void main(final String[] args) {
        
    }
    
    private static DynamicAlertsService initService() {
        return new DynamicAlertsService(Context.initializeStandalone());
    }
    
    // =========================================================================
    // UNIT TEST
    // =========================================================================
    @Test
    public void testResolveAlertsToProcess() throws Exception {
        final List<DynamicAlertEntity> entities = loadEntities("alerting/dynamicAlerts.01.json");
        final List<DynamicAlertEntity> cas1 = SERVICE.resolveAlertsToProcess(entities, buildDate("2019/05/16 14:00"));
        assertEquals(2, cas1.size());
        
        final List<DynamicAlertEntity> cas2 = SERVICE.resolveAlertsToProcess(entities, buildDate("2019/05/16 14:03"));
        assertEquals(1, cas2.size());
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
}
