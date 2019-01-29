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
package org.inugami.api.providers;

import java.util.List;

import org.inugami.api.exceptions.services.ProviderException;
import org.inugami.api.models.Gav;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.providers.concurrent.FutureData;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.tools.NamedComponent;

/**
 * Provider
 * 
 * @author patrick_guillerm
 * @since 3 janv. 2017
 */
public interface Provider extends NamedComponent {
    String CONFIG_TIMEOUT = "timeout";
    
    <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav);
    
    ProviderFutureResult aggregate(final List<ProviderFutureResult> data) throws ProviderException;
    
    String getType();
    
    default long getTimeout() {
        return 0l;
    }
    
    default ConfigHandler<String, String> getConfig() {
        return null;
    }
}
