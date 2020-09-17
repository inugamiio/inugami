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
package io.inugami.configuration.test;

import java.util.List;

import io.inugami.api.exceptions.services.ProviderException;
import io.inugami.api.models.Gav;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.Provider;
import io.inugami.api.providers.concurrent.FutureData;
import io.inugami.api.providers.task.ProviderFutureResult;

/**
 * SystemCaller
 * 
 * @author patrick_guillerm
 * @since 3 janv. 2017
 */
public class MockCaller implements Provider {
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public MockCaller() {
        this(null);
    }
    
    public MockCaller(final ConfigHandler<String, String> config) {
        super();
        if (config != null) {
            
        }
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    
    @Override
    public <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ProviderFutureResult aggregate(final List<ProviderFutureResult> data) throws ProviderException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getType() {
        // TODO Auto-generated method stub
        return null;
    }
}
