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

import java.util.List;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.models.Gav;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.providers.Provider;
import org.inugami.api.providers.concurrent.FutureData;
import org.inugami.api.providers.task.ProviderFutureResult;

/**
 * SimpleProvider
 * 
 * @author patrick_guillerm
 * @since 6 janv. 2017
 */
public class ProviderWithConfig implements Provider {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String test;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ProviderWithConfig(final ConfigHandler<String, String> config) {
        test = config.grab("test");
        Asserts.notNull(test);
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ProviderFutureResult aggregate(final List<ProviderFutureResult> data) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getType() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
