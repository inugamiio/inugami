package org.inugami.core.alertings;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.inugami.commons.files.FilesUtils;
import org.inugami.commons.tools.TestUnitResources;
import org.inugami.core.alertings.dynamic.DynamicAlertEntity;
import org.junit.Test;

import flexjson.JSONDeserializer;

public class DynamicAlertsServiceTest implements TestUnitResources {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final DynamicAlertsService SERVICE = new DynamicAlertsService();
    
    // =========================================================================
    // ITEGRATION
    // =========================================================================
    public static void main(final String[] args) {
        
    }
    
    // =========================================================================
    // UNIT TEST
    // =========================================================================
    @Test
    public void testResolveAlertToProcess() throws Exception {
        final List<DynamicAlertEntity> entities = loadEntities("alerting/dynamicAlerts.01.json");
        SERVICE.resolveAlertToProcess(entities, buildDate("2019/05/16 14:00"));
        
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private List<DynamicAlertEntity> loadEntities(final String path) {
        final File file = FilesUtils.buildFile(initResourcesPath(), path);
        
        //@formatter:off
        final List<DynamicAlertEntity> result = new JSONDeserializer<List<DynamicAlertEntity>>().use(null,
                                                                                                     this.getClass()).deserialize(path);
        //@formatter:on
        return null;
    }
    
    private long buildDate(final String time) throws ParseException {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(time).getTime();
    }
}
