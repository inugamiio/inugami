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

import io.inugami.configuration.models.EventConfig;
import io.inugami.configuration.models.plugins.PluginConfiguration;
import io.inugami.configuration.services.PluginConfigurationLoader;
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
 * HomePluginConfigStrategyTest
 *
 * @author patrick_guillerm
 * @since 27 déc. 2016
 */
class HomePluginConfigStrategyTest {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static File PATH_RESOURCES = initPathResources();

    private final static File TEST_HOME = initTestHome();

    private final static Logger LOGGER = LoggerFactory.getLogger(HomePluginConfigStrategyTest.class);

    private static File initPathResources() {
        final File currentPath = new File(".");
        return new File(currentPath.getAbsoluteFile().getParentFile().getAbsolutePath() + "/src/test/resources");
    }

    private static File initTestHome() {
        return new File(PATH_RESOURCES.getAbsolutePath() + "/home");
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testResolveFiles() throws Exception {

        final HomePluginConfigStrategy strategy = new HomePluginConfigStrategy(null, TEST_HOME);

        final List<File> files = strategy.resolveFiles();
        assertThat(files).isNotNull();
        assertEquals(2, files.size());

        files.forEach(item -> LOGGER.info("file : {}", item));
    }

    @Test
    void testResolve() throws Exception {
        final HomePluginConfigStrategy strategy = new HomePluginConfigStrategy(new PluginConfigurationLoader(),
                                                                               TEST_HOME);
        final Optional<List<PluginConfiguration>> configsOpt = strategy.resolve();
        assertTrue(configsOpt.isPresent(), "can't load plugin configuration from home folder");

        final List<PluginConfiguration> configs = configsOpt.get();
        assertEquals(2, configs.size());

        PluginConfiguration fooConfig      = null;
        PluginConfiguration overrideConfig = null;

        if ("plugin-foo".equals(configs.get(0).getGav().getArtifactId())) {
            fooConfig = configs.get(0);
            overrideConfig = configs.get(1);
        } else {
            fooConfig = configs.get(1);
            overrideConfig = configs.get(0);
        }

        validateFooConfig(fooConfig);
        validateOverrideConfig(overrideConfig);
    }

    @Test
    void testResolveEventFile() throws Exception {
        final HomePluginConfigStrategy strategy = new HomePluginConfigStrategy(new PluginConfigurationLoader(),
                                                                               new File(PATH_RESOURCES.getAbsolutePath()
                                                                                        + "/home-configs-events"));

        final Optional<List<PluginConfiguration>> configsOpt = strategy.resolve();
        assertTrue(configsOpt.isPresent(), "can't load plugin configuration from home folder");
        assertEquals(1, configsOpt.get().size());

        final PluginConfiguration pluginConfig = configsOpt.get().get(0);
        assertThat(pluginConfig.getEventsFiles()).isNotNull();
        assertEquals(1, pluginConfig.getEventsFiles().size());

        //@formatter:off
        final Optional<EventConfig> eventConfig = strategy.resolveEventFile(pluginConfig,pluginConfig.getEventsFiles().get(0));
        //@formatter:on
        assertTrue(eventConfig.isPresent(), "can't load events plugin configuration!");
        final EventConfig config = eventConfig.get();

    }

    // =========================================================================
    // VALIDATE
    // =========================================================================
    private void validateFooConfig(final PluginConfiguration config) {
        assertThat(config).isNotNull();
        assertThat(config.getGav()).isNotNull();
        assertEquals("plugin-group-id", config.getGav().getGroupId());
        assertEquals("plugin-foo", config.getGav().getArtifactId());
        assertEquals("0.1", config.getGav().getVersion());

        assertThat(config.getResources()).isNotNull();
        assertEquals(4, config.getResources().size());
        //
        assertEquals("theme.css", config.getResources().get(0).getName());
        assertEquals("inugami_plugin/css/", config.getResources().get(0).getPath());
        assertEquals("inugami_plugin/css/theme.css", config.getResources().get(0).getFullPath());
        //
        assertEquals("formatter.js", config.getResources().get(1).getName());
        assertEquals("inugami_plugin/js/", config.getResources().get(1).getPath());
        assertEquals("inugami_plugin/js/formatter.js", config.getResources().get(1).getFullPath());
        //
        assertEquals("loader.js", config.getResources().get(2).getName());
        assertEquals("inugami_plugin/js/", config.getResources().get(2).getPath());
        assertEquals("inugami_plugin/js/loader.js", config.getResources().get(2).getFullPath());
        //
        assertEquals("index.html", config.getResources().get(3).getName());
        assertEquals("inugami_plugin/", config.getResources().get(3).getPath());
        assertEquals("inugami_plugin/index.html", config.getResources().get(3).getFullPath());
        //
        assertThat(config.getProviders()).isNull();
        assertThat(config.getListeners()).isNull();
        assertThat(config.getProcessors()).isNull();
        assertThat(config.getEventsFiles()).isNull();

    }

    private void validateOverrideConfig(final PluginConfiguration config) {
        assertThat(config).isNotNull();
        assertThat(config.getGav()).isNotNull();
        assertEquals("io.inugami", config.getGav().getGroupId());
        assertEquals("inugami_configuration", config.getGav().getArtifactId());
        assertEquals("x.y.z", config.getGav().getVersion());

        assertThat(config.getResources()).isNotNull();
        assertEquals(4, config.getResources().size());
        //
        assertEquals("theme2.css", config.getResources().get(0).getName());
        assertEquals("inugami_plugin/css/", config.getResources().get(0).getPath());
        assertEquals("inugami_plugin/css/theme2.css", config.getResources().get(0).getFullPath());
        //
        assertEquals("formatter2.js", config.getResources().get(1).getName());
        assertEquals("inugami_plugin/js/", config.getResources().get(1).getPath());
        assertEquals("inugami_plugin/js/formatter2.js", config.getResources().get(1).getFullPath());
        //
        assertEquals("loader2.js", config.getResources().get(2).getName());
        assertEquals("inugami_plugin/js/", config.getResources().get(2).getPath());
        assertEquals("inugami_plugin/js/loader2.js", config.getResources().get(2).getFullPath());
        //
        assertEquals("index2.html", config.getResources().get(3).getName());
        assertEquals("inugami_plugin/", config.getResources().get(3).getPath());
        assertEquals("inugami_plugin/index2.html", config.getResources().get(3).getFullPath());
        //
        assertThat(config.getProviders()).isNull();
        assertThat(config.getListeners()).isNull();
        assertThat(config.getProcessors()).isNull();
        assertThat(config.getEventsFiles()).isNull();
    }
}
