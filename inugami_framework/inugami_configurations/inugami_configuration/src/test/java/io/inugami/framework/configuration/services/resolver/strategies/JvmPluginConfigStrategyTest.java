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
package io.inugami.framework.configuration.services.resolver.strategies;

import io.inugami.framework.configuration.models.plugins.PluginConfiguration;
import io.inugami.framework.configuration.services.PluginConfigurationLoader;
import io.inugami.framework.configuration.services.SystemProperties;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JvmPluginConfigStrategyTest
 *
 * @author patrick_guillerm
 * @since 27 déc. 2016
 */
class JvmPluginConfigStrategyTest {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static String RESOURCES = initResources();

    private final static Logger LOGGER = LoggerFactory.getLogger(HomePluginConfigStrategyTest.class);

    private static String initResources() {
        final File currentPath = new File(".");
        final File parent      = currentPath.getAbsoluteFile().getParentFile();
        return new File(parent.getAbsolutePath() + "/src/test/resources").getAbsolutePath();
    }

    // =========================================================================
    // METHODS
    // =========================================================================

    @Test
    void testResolveFiles() throws Exception {
        SystemProperties.setJvmParam("pluginA", RESOURCES + "/META-INF/plugin-configuration.yaml");
        SystemProperties.setJvmParam("pluginB", RESOURCES + "/home/plugin_override.xml");

        final JvmPluginConfigStrategy strategy = new JvmPluginConfigStrategy(null);
        final List<File>              files    = strategy.resolveFiles();
        assertThat(files).isNotNull();
        assertEquals(2, files.size());

        files.forEach(file -> LOGGER.info("file : {}", file));

        SystemProperties.removeJvmParam("pluginA");
        SystemProperties.removeJvmParam("pluginB");
    }

    @Test
    void testResolve() throws Exception {
        SystemProperties.setJvmParam("pluginA", RESOURCES + "/META-INF/plugin-configuration.yaml");
        SystemProperties.setJvmParam("pluginB", RESOURCES + "/home/plugin_foobar.xml");

        final JvmPluginConfigStrategy strategy = new JvmPluginConfigStrategy(
                new PluginConfigurationLoader());
        final Optional<List<PluginConfiguration>> optionnal = strategy.resolve();

        assertTrue(optionnal.isPresent(), "can't load JVM specific defined plugin configuration");
        final List<PluginConfiguration> configs = optionnal.get();

        assertEquals(2, configs.size());

        PluginConfiguration pluginFoo      = null;
        PluginConfiguration pluginOverride = null;

        if ("plugin-foo".equals(configs.get(0).getGav().getArtifactId())) {
            pluginFoo = configs.get(0);
            pluginOverride = configs.get(1);
        } else {
            pluginFoo = configs.get(1);
            pluginOverride = configs.get(0);
        }

        assertEquals("io.inugami", pluginOverride.getGav().getGroupId());
        assertEquals("inugami_configuration", pluginOverride.getGav().getArtifactId());
        assertEquals("x.y.z", pluginOverride.getGav().getVersion());

        assertEquals("plugin-group-id", pluginFoo.getGav().getGroupId());
        assertEquals("plugin-foo", pluginFoo.getGav().getArtifactId());
        assertEquals("0.1", pluginFoo.getGav().getVersion());

        SystemProperties.removeJvmParam("pluginA");
        SystemProperties.removeJvmParam("pluginB");
    }

}
