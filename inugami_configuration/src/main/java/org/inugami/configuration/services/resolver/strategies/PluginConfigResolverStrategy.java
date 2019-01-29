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
package org.inugami.configuration.services.resolver.strategies;

import java.util.List;
import java.util.Optional;

import org.inugami.api.exceptions.TechnicalException;
import org.inugami.configuration.models.EventConfig;
import org.inugami.configuration.models.plugins.EventsFileModel;
import org.inugami.configuration.models.plugins.PluginConfiguration;
import org.inugami.configuration.services.resolver.ConfigurationResolverException;

/**
 * ConfigurationResolverStrategy
 * 
 * @author patrick_guillerm
 * @since 27 déc. 2016
 */
public interface PluginConfigResolverStrategy {
    /**
     * Allow to resolve plugins configurations
     * 
     * @return List of plugins configurations
     * @throws ConfigurationResolverException if exception is occur
     */
    Optional<List<PluginConfiguration>> resolve() throws ConfigurationResolverException;
    
    /**
     * Resolve event file.
     *
     * @param config the config
     * @param eventFile the event file
     * @return the optional
     * @throws TechnicalException
     */
    Optional<EventConfig> resolveEventFile(final PluginConfiguration config,
                                           final EventsFileModel eventFile) throws TechnicalException;
    
}
