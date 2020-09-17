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
package io.inugami.core.providers.mock;

import io.inugami.api.models.Gav;
import io.inugami.api.models.data.basic.Json;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.providers.task.ProviderFutureResultBuilder;
import io.inugami.api.providers.task.ProviderTask;
import io.inugami.commons.providers.MockJsonHelper;

/**
 * MockJsonTask
 * 
 * @author pguillerm
 * @since 26 sept. 2017
 */
public class MockJsonTask implements ProviderTask {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final MockJsonHelper mockJsonData;
    
    private final SimpleEvent    event;
    
    private final Gav            pluginGav;
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    public MockJsonTask(final MockJsonHelper mockJsonData, final SimpleEvent event, final Gav pluginGav) {
        this.mockJsonData = mockJsonData;
        this.event = event;
        this.pluginGav = pluginGav;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public ProviderFutureResult callProvider() {
        final String data = grabData(event);
        final ProviderFutureResultBuilder result = new ProviderFutureResultBuilder();
        result.addEvent(event);
        result.addData(data == null ? new Json("null") : new Json(data));
        
        return result.build();
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    private <T extends SimpleEvent> String grabData(final T event) {
        final String name = event.getName();
        String json = "null";
        
        if (mockJsonData.containsWithIndex(name)) {
            json = mockJsonData.getDataRandom(name);
        }
        else if (mockJsonData.contains(name)) {
            json = mockJsonData.getData(name);
        }
        
        return json;
    }
    
    @Override
    public GenericEvent getEvent() {
        return event;
    }
    
    @Override
    public Gav getPluginGav() {
        return pluginGav;
    }
}
