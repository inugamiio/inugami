package org.inugami.core.alertings.dynamic.services;

import java.util.List;

import org.inugami.api.alertings.AlertingResult;
import org.inugami.api.alertings.DynamicAlertingLevel;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.providers.task.ProviderFutureResult;

public class ComputeDynamicAlert {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public AlertingResult compute(final SimpleEvent event, final ProviderFutureResult data,
                                  final List<DynamicAlertingLevel> levels, final String message,
                                  final String subMessage, final List<String> tags) {
        // TODO Auto-generated method stub
        return null;
    }
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
}
