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
package io.inugami.core.providers;

import java.util.List;

import io.inugami.api.exceptions.services.ProviderException;
import io.inugami.api.models.Gav;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.Provider;
import io.inugami.api.providers.concurrent.FutureData;
import io.inugami.api.providers.task.ProviderFutureResult;

public class ProviderWithName implements Provider {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final ClassBehavior                 classBehavior;
    
    private final ConfigHandler<String, String> config;
    
    private String                              name;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ProviderWithName(final ClassBehavior classBehavior, final ConfigHandler<String, String> config) {
        this.classBehavior = classBehavior;
        this.config = config;
    }
    
    public ProviderWithName(final ClassBehavior classBehavior, final ConfigHandler<String, String> config,
                            final String name) {
        this.classBehavior = classBehavior;
        this.config = config;
        this.name = name;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav) {
        return null;
    }
    
    @Override
    public ProviderFutureResult aggregate(final List<ProviderFutureResult> data) throws ProviderException {
        return null;
    }
    
    @Override
    public String getType() {
        return null;
    }
    
    public ClassBehavior getClassBehavior() {
        return classBehavior;
    }
    
    @Override
    public ConfigHandler<String, String> getConfig() {
        return config;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
