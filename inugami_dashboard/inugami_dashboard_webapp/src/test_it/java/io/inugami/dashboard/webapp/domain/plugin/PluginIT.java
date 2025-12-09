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

import io.inugami.commons.test.api.LocalDateTimeLineMatcher;
import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.commons.test.api.UuidLineMatcher;
import io.inugami.commons.test.dto.WaitContext;
import io.inugami.dashboard.api.domain.engine.dto.EngineResultDTO;
import io.inugami.dashboard.interfaces.domain.plugin.PluginRestClient;
import io.inugami.dashboard.webapp.SpringBootIntegrationTest;
import io.inugami.dashboard.webapp.tools.EngineListenerHandler;
import io.inugami.framework.configuration.models.plugins.Plugin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.concurrent.TimeoutException;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.commons.test.UnitTestHelper.waitForDone;

public class PluginIT extends SpringBootIntegrationTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    @Autowired
    private PluginRestClient      pluginRestClient;
    @Autowired
    private EngineListenerHandler engineListenerHandler;

    //==================================================================================================================
    // TEST
    //==================================================================================================================
    @Test
    void plugin_nominal() throws TimeoutException {
        final Collection<Plugin> allPlugins = pluginRestClient.findAllPlugin();
        assertText(allPlugins,
                   """
                           [ {
                                "alertingProviders" : [ ],
                                "enabled" : false,
                                "eventConfigPresent" : false,
                                "events" : [ {
                                  "enable" : true,
                                  "events" : [ {
                                    "type" : "Event",
                                    "name" : "cpu-multi",
                                    "fromFirstTime" : null,
                                    "until" : null,
                                    "provider" : null,
                                    "mapper" : null,
                                    "processors" : [ ],
                                    "alertings" : [ ],
                                    "scheduler" : "0/2 * * * * ?",
                                    "targets" : [ ]
                                  } ],
                                  "name" : "events-test",
                                  "simpleEvents" : [ ]
                                } ],
                                "frontConfig" : {
                                  "menuLinks" : [ ],
                                  "router" : [ ]
                                },
                                "gav" : {
                                  "artifactId" : "it-basic-plugin",
                                  "groupId" : "io.inugami",
                                  "hash" : "io.inugami:it-basic-plugin:4.0.0",
                                  "version" : "4.0.0"
                                },
                                "handlers" : [ ],
                                "listeners" : [ ],
                                "processors" : [ ],
                                "providers" : [ ]
                              }, {
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
                                          .filter(plugin -> plugin.getGav().getArtifactId().equals("it-basic-plugin"))
                                          .findFirst()
                                          .orElse(null);


        EngineResultDTO engineEvent = waitForDone(50000L, engineListenerHandler.registerOnDone());

        assertText(engineEvent,
                   """
                           {
                             "end" : "2025-12-09T22:01:24.047522769",
                             "plugins" : [ ],
                             "processId" : "cb9f509a-2dff-465c-aea1-ab0b8597a371",
                             "start" : "2025-12-09T22:01:24.047511331",
                             "status" : "SUCCESS",
                             "traceId" : "b9789378-0165-4a4c-b958-62fb447cecea"
                           }
                           """,
                   LocalDateTimeLineMatcher.of(1, 4),
                   UuidLineMatcher.of(3, 6));




    }
}
