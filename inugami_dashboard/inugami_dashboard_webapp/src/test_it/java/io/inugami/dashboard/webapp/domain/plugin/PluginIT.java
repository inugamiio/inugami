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
package io.inugami.dashboard.webapp.domain.plugin;

import io.inugami.dashboard.interfaces.domain.plugin.PluginRestClient;
import io.inugami.dashboard.webapp.SpringBootIntegrationTest;
import io.inugami.framework.configuration.models.plugins.Plugin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

import static io.inugami.commons.test.UnitTestHelper.assertText;

public class PluginIT extends SpringBootIntegrationTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    @Autowired
    private PluginRestClient pluginRestClient;

    //==================================================================================================================
    // TEST
    //==================================================================================================================
    @Test
    void plugin_nominal() {
        final Collection<Plugin> allPlugins = pluginRestClient.findAllPlugin();
        assertText(allPlugins,
                   """
                           [ {
                             "alertingProviders" : [ ],
                             "enabled" : false,
                             "eventConfigPresent" : false,
                             "events" : [ ],
                             "frontConfig" : {
                               "menuLinks" : [ ],
                               "router" : [ ]
                             },
                             "gav" : {
                               "artifactId" : "basic-plugin",
                               "groupId" : "io.inugami",
                               "hash" : "io.inugami:basic-plugin:4.0.0",
                               "version" : "4.0.0"
                             },
                             "handlers" : [ ],
                             "listeners" : [ ],
                             "processors" : [ ],
                             "providers" : [ ]
                           } ]
                           """);


        final var basicPlugin = allPlugins.stream()
                                          .filter(plugin -> plugin.getGav().getArtifactId().equals("basic-plugin"))
                                          .findFirst()
                                          .orElse(null);


        assertText(pluginRestClient.findPluginDataByGav(basicPlugin.getGav().getGroupId(),
                                                        basicPlugin.getGav().getArtifactId()),
                   """
                           {
                             "events" : [ ],
                             "gav" : {
                               "artifactId" : "basic-plugin",
                               "groupId" : "io.inugami",
                               "hash" : "io.inugami:basic-plugin:null"
                             }
                           }
                           """);


    }
}
