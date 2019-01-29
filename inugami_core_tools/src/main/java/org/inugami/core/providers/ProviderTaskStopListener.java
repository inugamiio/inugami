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
package org.inugami.core.providers;

import org.inugami.api.listeners.TaskFinishListener;
import org.inugami.api.metrics.events.MetricsEvents;
import org.inugami.api.models.Gav;
import org.inugami.api.models.events.GenericEvent;

/**
 * ProviderTaskStopListener
 * 
 * @author pguillerm
 * @since 26 sept. 2017
 */
public class ProviderTaskStopListener implements TaskFinishListener {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final GenericEvent event;
    
    private final Gav          pluginGav;
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    public ProviderTaskStopListener(final GenericEvent event, final Gav pluginGav) {
        this.event = event;
        this.pluginGav = pluginGav;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void onFinish(final long time, final long delais, final String name, final Object result,
                         final Exception error) {
        MetricsEvents.onStop(pluginGav, event.getName());
        
    }
}
