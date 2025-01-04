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
import io.inugami.configuration.models.ProviderConfig;
import io.inugami.configuration.models.plugins.PluginConfiguration;
import io.inugami.configuration.services.PluginConfigurationLoader;
import io.inugami.configuration.test.TestPlugin;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ClassLoaderPluginConfigStrategyTest
 *
 * @author patrick_guillerm
 * @since 27 déc. 2016
 */
class ClassLoaderPluginConfigStrategyTest {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Logger LOGGER = LoggerFactory.getLogger(ClassLoaderPluginConfigStrategyTest.class);

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testResolveFiles() throws Exception {
        LOGGER.trace("from dependency : {}:{}", TestPlugin.GROUP_ID, TestPlugin.ARTIFACT_ID);

        final ClassLoaderPluginConfigStrategy resolver = new ClassLoaderPluginConfigStrategy(null);
        final List<URL>                       urls     = resolver.resolveFiles();
        assertNotNull(urls, "resolved configurations files mustn't be null!");

        urls.forEach(url -> LOGGER.info("url : {}", url));
    }

    @Test
    void testLoad() throws Exception {
        final ClassLoaderPluginConfigStrategy resolver = new ClassLoaderPluginConfigStrategy(
                new PluginConfigurationLoader());

        final List<URL> urls = resolver.resolveFiles();
        assertNotNull(urls, "resolved configurations files mustn't be null!");
        urls.forEach(url -> LOGGER.info("url : {}", url));

        final List<PluginConfiguration> configs = resolver.load(urls);
        assertNotNull(configs, "resolved configurations mustn't be null!");
        assertEquals(2, configs.size());

        PluginConfiguration artifactTest = null;
        PluginConfiguration localConfig  = null;
        if ("dashboard_tv_configuration_test".equals(configs.get(0).getGav().getArtifactId())) {
            artifactTest = configs.get(0);
            localConfig = configs.get(1);
        } else {
            artifactTest = configs.get(1);
            localConfig = configs.get(0);
        }

        validateArtifactTest(artifactTest);
        validateLocalArtifact(localConfig);
    }

    @Test
    void testResolveEventFile() throws Exception {
        final ClassLoaderPluginConfigStrategy resolver = new ClassLoaderPluginConfigStrategy(
                new PluginConfigurationLoader());

        final Optional<List<PluginConfiguration>> configs = resolver.resolve();
        assertTrue(configs.isPresent());

        final List<PluginConfiguration> pluginsConfigs = configs.get();
        assertEquals(2, pluginsConfigs.size());
        PluginConfiguration artifactTest = null;

        if ("dashboard_tv_configuration_test".equals(pluginsConfigs.get(0).getGav().getArtifactId())) {
            artifactTest = pluginsConfigs.get(0);
        } else {
            artifactTest = pluginsConfigs.get(1);
        }

        assertNotNull(artifactTest.getEventsFiles());
        assertEquals(1, artifactTest.getEventsFiles().size());
        //@formatter:off
        final Optional<EventConfig> eventConfigOpt = resolver.resolveEventFile(artifactTest,artifactTest.getEventsFiles().get(0));
        //@formatter:on
        assertNotNull(eventConfigOpt, "event configuration mustn't be null!");
        assertTrue(eventConfigOpt.isPresent(), "event configuration must be found!");

        final EventConfig eventConfig = eventConfigOpt.get();
        assertNotNull(eventConfig.getSimpleEvents());
        assertEquals(1, eventConfig.getSimpleEvents().size());
        assertEquals("joe-pourcentage", eventConfig.getSimpleEvents().get(0).getName());
    }

    // =========================================================================
    // VALIDATE
    // =========================================================================
    private void validateLocalArtifact(final PluginConfiguration localConfig) {
        assertNotNull(localConfig);
        assertNotNull(localConfig.getGav());

        assertEquals("inugami_configuration", localConfig.getGav().getArtifactId());
        assertEquals("io.inugami", localConfig.getGav().getGroupId());
        assertEquals("x.y.z", localConfig.getGav().getVersion());

        assertNotNull(localConfig.getResources());
        assertTrue(localConfig.getResources().isEmpty());
        assertNull(localConfig.getProviders());
        //
        assertNotNull(localConfig.getListeners());
        assertEquals(1, localConfig.getListeners().size());
        assertEquals("testListener", localConfig.getListeners().get(0).getName());
        //@formatter:off
        assertEquals("io.inugami.configuration.listener.Listener", localConfig.getListeners().get(0).getClassName());
        //@formatter:on
        assertNotNull(localConfig.getListeners().get(0).getConfigs());
        assertEquals(2, localConfig.getListeners().get(0).getConfigs().size());
        assertEquals("FoobarData", localConfig.getListeners().get(0).getConfig("data").get());
        assertEquals("Hello the world", localConfig.getListeners().get(0).getConfig("title").get());
        //
        assertNull(localConfig.getProcessors());
        assertNull(localConfig.getEventsFiles());

        assertTrue(localConfig.getFrontConfig().isPresent(), "no front config found!");

    }

    private void validateArtifactTest(final PluginConfiguration artifactTest) {
        assertNotNull(artifactTest);
        assertNotNull(artifactTest.getGav());

        //
        assertNotNull(artifactTest.getResources());
        assertEquals(3, artifactTest.getResources().size());
        assertEquals("inugami_configuration_test/css/", artifactTest.getResources().get(0).getPath());
        assertEquals("theme.css", artifactTest.getResources().get(0).getName());
        assertEquals("inugami_configuration_test/css/theme.css", artifactTest.getResources().get(0).getFullPath());
        //
        assertEquals("inugami_configuration_test/js/", artifactTest.getResources().get(1).getPath());
        assertEquals("main.js", artifactTest.getResources().get(1).getName());
        assertEquals("inugami_configuration_test/js/main.js", artifactTest.getResources().get(1).getFullPath());
        //
        assertEquals("inugami_configuration_test/", artifactTest.getResources().get(2).getPath());
        assertEquals("index.html", artifactTest.getResources().get(2).getName());
        assertEquals("inugami_configuration_test/index.html", artifactTest.getResources().get(2).getFullPath());
        //
        assertNotNull(artifactTest.getProviders());
        assertEquals(1, artifactTest.getProviders().size());
        final ProviderConfig provider = artifactTest.getProviders().get(0);
        assertEquals("io.inugami.configuration.test.MockCaller", provider.getClassName());
        assertEquals("foobar.system", provider.getName());
        assertEquals("FoobarData", provider.getConfig("data").get());
        //
        assertNotNull(artifactTest.getProviders());
        assertNotNull(artifactTest.getListeners());
        //
        assertNotNull(artifactTest.getProcessors());
        assertEquals(1, artifactTest.getProcessors().size());
        assertEquals("foo", artifactTest.getProcessors().get(0).getName());
        assertEquals("io.inugami.configuration.test.PluginProcessor",
                     artifactTest.getProcessors().get(0).getClassName());
        //
        assertNotNull(artifactTest.getEventsFiles());
        assertEquals(1, artifactTest.getEventsFiles().size());
        assertEquals("event-config.xml", artifactTest.getEventsFiles().get(0).getName());

    }

}
