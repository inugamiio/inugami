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
package org.inugami.configuration.services.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.inugami.configuration.models.EventConfig;
import org.inugami.configuration.models.plugins.PluginConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConfigurationResolverTests
 * 
 * @author patrick_guillerm
 * @since 27 déc. 2016
 */
public class ConfigurationResolverTest {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationResolverTest.class);
    
    // =========================================================================
    // METHODS
    // =========================================================================
    
    /**
     * can't be running parrallale with JVM test case.
     * 
     * @throws Exception
     */
    @Ignore
    @Test
    public void testResolvePluginsConfigurations() throws Exception {
        LOGGER.info("testResolvePluginsConfigurations");
        
        final ConfigurationResolver resolver = new ConfigurationResolver();
        
        final Optional<List<PluginConfiguration>> optional = resolver.resolvePluginsConfigurations();
        
        assertTrue("no one plugin configuration found!", optional.isPresent());
        final List<PluginConfiguration> configs = optional.get();
        
        assertFalse("no one plugin configuration in result set!", configs.isEmpty());
        
        configs.forEach((config) -> {
            //@formatter:off
            LOGGER.info("plugin -> {}:{}:{}", config.getGav().getGroupId(),
                                                      config.getGav().getArtifactId(),
                                                      config.getGav().getVersion());
            //@formatter:on
        });
            
        assertEquals(2, configs.size());
        
        //@formatter:off
        final String[] gavs = { 
            "org.inugami:inugami_configuration:x.y.z",
            "org.inugami:inugami_configuration_test:0.2.0-SNAPSHOT"
        };
        //@formatter:on
        
        for (int i = 0; i < gavs.length; i++) {
            assertEquals(gavs[i], configs.get(i).getGav().getHash());
        }
    }
    
    @Test
    public void testResolvePluginEventConfig() throws Exception {
        final ConfigurationResolver resolver = new ConfigurationResolver();
        final Optional<List<PluginConfiguration>> optional = resolver.resolvePluginsConfigurations();
        assertTrue("no one plugin configuration found!", optional.isPresent());
        
        PluginConfiguration pluginConf = null;
        
        for (final PluginConfiguration conf : optional.get()) {
            if ("org.inugami:inugami_configuration_test:0.2.0-SNAPSHOT".equals(conf.getGav().getHash())) {
                
                pluginConf = conf;
                break;
            }
        }
        
        assertNotNull(pluginConf);
        final Optional<List<EventConfig>> eventsOpt = resolver.resolvePluginEventConfig(pluginConf);
        assertTrue("no events configuration found!", eventsOpt.isPresent());
        
        final List<EventConfig> events = eventsOpt.get();
        assertEquals(1, events.size());
        
        final EventConfig event = events.get(0);
        assertNotNull(event.getSimpleEvents());
        assertEquals(1, event.getSimpleEvents().size());
        assertEquals("joe-pourcentage", event.getSimpleEvents().get(0).getName());
        
    }
    
}
