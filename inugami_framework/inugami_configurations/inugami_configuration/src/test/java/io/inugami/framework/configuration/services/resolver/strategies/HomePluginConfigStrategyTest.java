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

import io.inugami.commons.test.UnitTestHelper;
import io.inugami.framework.configuration.models.plugins.PluginConfiguration;
import io.inugami.framework.configuration.services.PluginConfigurationLoader;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.framework.interfaces.exceptions.Asserts.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

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
    private final static File TEST_HOME = UnitTestHelper.buildTestFilePath("META-INF");

    private final static Logger LOGGER = LoggerFactory.getLogger(HomePluginConfigStrategyTest.class);


    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testResolveFiles() throws Exception {

        final HomePluginConfigStrategy strategy = new HomePluginConfigStrategy(null, TEST_HOME);

        assertThat(strategy.resolveFiles().size()).isOne();
    }

    @Test
    void testResolve() throws Exception {
        final HomePluginConfigStrategy            strategy   = new HomePluginConfigStrategy(new PluginConfigurationLoader(), TEST_HOME);
        final Optional<List<PluginConfiguration>> configsOpt = strategy.resolve();
        assertTrue("can't load plugin configuration from home folder", configsOpt.isPresent());

        assertText(configsOpt.orElse(null),
                   """
                           [ {
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
                           """);
    }

    @Test
    void testResolveEventFile() throws Exception {
        final HomePluginConfigStrategy strategy = new HomePluginConfigStrategy(new PluginConfigurationLoader(), TEST_HOME);

        final Optional<List<PluginConfiguration>> configsOpt = strategy.resolve();
        assertText(configsOpt,
                   """
                           [ {
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
                           """);

    }

}
