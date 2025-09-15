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

import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.framework.configuration.models.plugins.PluginConfiguration;
import io.inugami.framework.configuration.services.PluginConfigurationLoader;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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


        assertText(configs,
                   """
                           [ {
                             "configFile" : "/META-INF/plugin-configuration.yaml",
                             "enable" : true,
                             "gav" : {
                               "artifactId" : "inugami_configuration",
                               "groupId" : "io.inugami",
                               "hash" : "io.inugami:inugami_configuration:x.y.z",
                               "version" : "x.y.z"
                             },
                             "listeners" : [ {
                               "className" : "io.inugami.configuration.listener.Listener",
                               "name" : "testListener"
                             } ]
                           } ]
                           """, SkipLineMatcher.of(1));
    }

    @Test
    void testResolveEventFile() throws Exception {
        final ClassLoaderPluginConfigStrategy resolver = new ClassLoaderPluginConfigStrategy(
                new PluginConfigurationLoader());

        final Optional<List<PluginConfiguration>> configs = resolver.resolve();
        assertText(configs.orElse(null),
                   """
                           [ {
                             "configFile" : "/META-INF/plugin-configuration.yaml",
                             "enable" : true,
                             "gav" : {
                               "artifactId" : "inugami_configuration",
                               "groupId" : "io.inugami",
                               "hash" : "io.inugami:inugami_configuration:x.y.z",
                               "version" : "x.y.z"
                             },
                             "listeners" : [ {
                               "className" : "io.inugami.configuration.listener.Listener",
                               "name" : "testListener"
                             } ]
                           } ]
                           """, SkipLineMatcher.of(1));
    }


}
