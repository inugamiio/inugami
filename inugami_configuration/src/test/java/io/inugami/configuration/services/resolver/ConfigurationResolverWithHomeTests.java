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
package io.inugami.configuration.services.resolver;

import io.inugami.configuration.models.plugins.PluginConfiguration;
import io.inugami.configuration.services.SystemProperties;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ConfigurationResolverTests
 *
 * @author patrick_guillerm
 * @since 27 déc. 2016
 */
public class ConfigurationResolverWithHomeTests {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static File TEST_HOME = initTestHome();

    public static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationResolverWithHomeTests.class);

    private static File initTestHome() {
        final File currentPath = new File(".");
        final File parent      = currentPath.getAbsoluteFile().getParentFile();
        return new File(parent.getAbsolutePath() + "/src/test/resources/home");
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Disabled
    @Test
    public void testWithHome() throws Exception {
        LOGGER.info("testResolvePluginsConfigurations");
        SystemProperties.setHome(TEST_HOME.getAbsolutePath());

        final ConfigurationResolver               resolver = new ConfigurationResolver();
        final Optional<List<PluginConfiguration>> optional = resolver.resolvePluginsConfigurations();
        assertTrue(optional.isPresent(), "no one plugin configuration found!");
        final List<PluginConfiguration> configs = optional.get();
        assertFalse(configs.isEmpty(), "no one plugin configuration in result set!");

        assertEquals(3, configs.size());
        configs.sort((r, v) -> r.getGav().buildHash().compareTo(v.getGav().buildHash()));
        //@formatter:off
        final String[] gavs = { 
            "io.inugami:inugami_configuration:x.y.z",
            "io.inugami:inugami_configuration_test:0.2.0-SNAPSHOT",
            "plugin-group-id:plugin-foo:0.1",
        };
        //@formatter:on

        for (int i = 0; i < gavs.length; i++) {
            assertEquals(gavs[i], configs.get(i).getGav().getHash());
        }

        SystemProperties.clearHomeProperty();
    }
}
