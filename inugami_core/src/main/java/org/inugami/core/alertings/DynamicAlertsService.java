package org.inugami.core.alertings;

import java.util.List;

import javax.inject.Named;

import org.inugami.api.exceptions.Asserts;
import org.inugami.core.alertings.dynamic.DynamicAlertEntity;

@Named
public class DynamicAlertsService {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public void process(final List<DynamicAlertEntity> entities) {
        Asserts.notNull(entities);
        
        final List<DynamicAlertEntity> alertsToProcess = resolveAlertToProcess(entities, System.currentTimeMillis());
        
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    protected List<DynamicAlertEntity> resolveAlertToProcess(final List<DynamicAlertEntity> entities,
                                                             final long timestamp) {
        // TODO Auto-generated method stub
        return null;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
}
