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
package io.inugami.core.context.runner;

import io.inugami.api.listeners.TaskStartListener;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.configuration.models.plugins.Plugin;

/**
 * PluginTaskStartListener
 * 
 * @author pguillerm
 * @since 26 sept. 2017
 */
public class PluginTaskStartListener implements TaskStartListener {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final Plugin       plugin;
    
    private final GenericEvent event;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public PluginTaskStartListener(final Plugin plugin, final GenericEvent event) {
        this.plugin = plugin;
        this.event = event;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void onStart(final long time, final String name) {
        // context.setEventRunning(plugin, event);
    }
    
}
