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
package io.inugami.configuration.services.resolver.strategies;

import io.inugami.api.exceptions.TechnicalException;
import io.inugami.configuration.models.EventConfig;
import io.inugami.configuration.models.plugins.EventsFileModel;
import io.inugami.configuration.models.plugins.PluginConfiguration;
import io.inugami.configuration.services.resolver.ConfigurationResolverException;

import java.util.List;
import java.util.Optional;

/**
 * ConfigurationResolverStrategy
 * 
 * @author patrick_guillerm
 * @since 27 déc. 2016
 */
public interface PluginConfigResolverStrategy {

    Optional<List<PluginConfiguration>> resolve() throws ConfigurationResolverException;
    

    Optional<EventConfig> resolveEventFile(final PluginConfiguration config,
                                           final EventsFileModel eventFile) throws TechnicalException;
    
}
