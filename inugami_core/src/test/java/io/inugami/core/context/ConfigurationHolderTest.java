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
package io.inugami.core.context;

import io.inugami.api.exceptions.FatalException;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.commons.tools.TestUnitResources;
import io.inugami.configuration.models.plugins.PluginConfiguration;
import io.inugami.configuration.services.resolver.ConfigurationResolver;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        final Optional<List<PluginConfiguration>> pluginsConfigsOpt = new ConfigurationResolver()
                .resolvePluginsConfigurations();
        assertTrue(pluginsConfigsOpt.isPresent());

        final List<PluginConfiguration> pluginsConfigs = pluginsConfigsOpt.get();
        assertEquals(1, pluginsConfigs.size());
        final PluginConfiguration config = pluginsConfigs.get(0);
        LOGGER.info("Config : {}", config);

    }

}
