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
package org.inugami.api.alertings;

import java.util.List;

import org.inugami.api.models.Gav;
import org.inugami.api.models.events.AlertingModel;
import org.inugami.api.models.events.GenericEvent;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.tools.NamedComponent;

/**
 * AlertingProvider
 * 
 * @author patrick_guillerm
 * @since 21 déc. 2017
 */
public interface AlertingProvider extends NamedComponent {
    
    void postConstruct();
    
    AlertingResult process(final Gav gav, final GenericEvent event, final ProviderFutureResult data,
                           final AlertingModel alert, String preload);
    
    void appendAlert(final AlertingResult alert);
    
    void processSavedAlerts(final List<AlertingResult> entities);
    
    void processDisableAlerts(final List<String> alertsUids, final String channel);
    
    default void processDynamicAlert(final Gav gav, final SimpleEvent event, final ProviderFutureResult data,
                                     final List<DynamicAlertingLevel> levels, final String message,
                                     final String subMessage, final List<String> tags,
                                     final List<String> alertSenders) {
    }
}
