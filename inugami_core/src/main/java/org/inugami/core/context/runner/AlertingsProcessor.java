/* --------------------------------------------------------------------
 *  Inugami  
 * --------------------------------------------------------------------
 * 
 * This program is free software: you can redistribute it and/or modify  
 * it under the terms of the GNU General Public License as published by  
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.inugami.core.context.runner;

import java.util.ArrayList;
import java.util.List;

import org.inugami.api.alertings.AlertingProvider;
import org.inugami.api.alertings.AlertingResult;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.Gav;
import org.inugami.api.models.events.AlertingModel;
import org.inugami.api.models.events.GenericEvent;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.core.context.ApplicationContext;
import org.inugami.core.context.Context;

/**
 * AlertingProcessor
 * 
 * @author patrick_guillerm
 * @since 21 déc. 2017
 */
public class AlertingsProcessor {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final ApplicationContext context;
    
    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public AlertingsProcessor() {
        context = Context.getInstance();
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public List<AlertingResult> computeAlerts(final Gav gav, final GenericEvent event,
                                              final ProviderFutureResult data) {
        List<AlertingResult> result = null;
        if (context.getApplicationConfiguration().isAlertingEnable() && event.getAlertings().isPresent()) {
            result = process(gav, event, event.getAlertings().get(), data);
        }
        return result;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    private List<AlertingResult> process(final Gav gav, final GenericEvent event, final List<AlertingModel> alerts,
                                         final ProviderFutureResult data) {
        final List<AlertingResult> result = new ArrayList<>();
        
        for (final AlertingModel alert : alerts) {
            final AlertingResult alertResult = processAlert(alert, gav, event, data);
            if (alertResult != null) {
                result.add(alertResult);
            }
        }
        
        return result;
    }
    
    private AlertingResult processAlert(final AlertingModel alert, final Gav gav, final GenericEvent event,
                                        final ProviderFutureResult data) {
        AlertingResult result = null;
        final String providerName = context.getGlobalConfiguration().applyProperties(alert.getProvider());
        final AlertingProvider provider = context.getAlertingProvider(providerName);
        
        try {
            result = provider.process(gav, event, data, alert, null);
        }
        catch (final Exception e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Loggers.ALERTING.error(e.getMessage());
        }
        return result;
    }
    
}
