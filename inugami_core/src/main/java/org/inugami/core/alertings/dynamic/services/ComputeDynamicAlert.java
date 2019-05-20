package org.inugami.core.alertings.dynamic.services;

import java.util.List;

import org.inugami.api.alertings.AlertingResult;
import org.inugami.api.alertings.DynamicAlertingLevel;
import org.inugami.api.models.data.JsonObject;
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
        AlertingResult result = null;
        if ((data != null) && (data.getData().isPresent()) && (levels != null) && (!levels.isEmpty())) {
            result = process(event, data.getData().get(), levels, message, subMessage, tags);
        }
        return result;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    protected AlertingResult process(final SimpleEvent event, final JsonObject data,
                                     final List<DynamicAlertingLevel> levels, final String message,
                                     final String subMessage, final List<String> tags) {
        
        return null;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
}
