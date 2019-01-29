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
package org.inugami.core.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.inugami.api.exceptions.FatalException;
import org.inugami.api.exceptions.TechnicalException;
import org.inugami.commons.tools.TestUnitResources;
import org.inugami.configuration.models.plugins.PluginConfiguration;
import org.inugami.configuration.services.resolver.ConfigurationResolver;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConfigurationHolderTest
 * 
 * @author patrick_guillerm
 * @since 30 déc. 2016
 */
public class ConfigurationHolderTest implements TestUnitResources {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Logger LOGGER = LoggerFactory.getLogger(ConfigurationHolderTest.class);
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testBuildPlugins() throws TechnicalException {
        try {
            process();
        }
        catch (final TechnicalException e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
        catch (final FatalException e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
        
    }
    
    private void process() throws TechnicalException {
        LOGGER.info("testBuildPlugins");
        final Optional<List<PluginConfiguration>> pluginsConfigsOpt = new ConfigurationResolver().resolvePluginsConfigurations();
        assertTrue(pluginsConfigsOpt.isPresent());
        
        final List<PluginConfiguration> pluginsConfigs = pluginsConfigsOpt.get();
        assertEquals(1, pluginsConfigs.size());
        final PluginConfiguration config = pluginsConfigs.get(0);
        LOGGER.info("Config : {}", config);
        
    }
    
}
