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
package io.inugami.framework.configuration.services;

import io.inugami.framework.configuration.models.EventConfig;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.models.maven.Gav;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PluginConfigurationTest
 *
 * @author patrick_guillerm
 * @since 26 déc. 2016
 */
class EventFileConfigurationLoaderTest {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final String RESOURCES_PATH = initResourcesPath();
    private static final Logger LOGGER         = LoggerFactory.getLogger(EventFileConfigurationLoaderTest.class);

    // =================================================================================================================
    // INIT
    // =================================================================================================================
    private static String initResourcesPath() {
        final File   file       = new File(".");
        final String currentDir = file.getAbsoluteFile().getParentFile().getAbsolutePath();

        return currentDir + "/src/test/resources";
    }

    // =================================================================================================================
    // METHODS
    // =================================================================================================================
    @Test
    void testLoadFromFile() throws TechnicalException {
        final PluginConfigurationLoader loader = new PluginConfigurationLoader();

        LOGGER.info("load events-test-configuration.yaml");
        final File file = new File(RESOURCES_PATH + "/events-test-configuration.yaml");
        final Optional<EventConfig> configOpt = loader.loadEventConfigFromFile(Gav.builder()
                                                                                  .groupId("io.inugami.test")
                                                                                  .artifactId("configuration-testing")
                                                                                  .version("4.0.0")
                                                                                  .qualifier("for-test")
                                                                                  .build(),
                                                                               file);

        assertTrue(configOpt.isPresent());
        assertText(configOpt.get(), """
                {
                  "enable" : true,
                  "events" : [ {
                    "type" : "Event",
                    "name" : "foobar-api-pourcentrage",
                    "fromFirstTime" : null,
                    "until" : null,
                    "provider" : "graphite.bigdata",
                    "mapper" : "foo.bar.MapperOnEvent",
                    "processors" : [ {
                      "name" : "foo"
                    } ],
                    "alertings" : [ {
                      "condition" : "var level=null; var message=null;\\nif(value>0.8){\\n  level=\\"error\\";\\n  message=\\"Erreur le seuil sur les sessions client est trop élevé\\";\\n}else if(value>0.6){\\n  level=\\"warning\\";\\n  message=\\"Attention le seuil sur les sessions client est haut\\";\\n}\\nreturn {\\n  \\"level\\"   :level,\\n  \\"message\\" :message\\n}\\n",
                      "level" : "error",
                      "message" : "Oups",
                      "name" : "prd-prod-002",
                      "provider" : "{{myAlertingProvider}}"
                    } ],
                    "scheduler" : "0 0/3 * * * ?",
                    "targets" : [ {
                      "name" : "foobar-sys",
                      "fromFirstTime" : null,
                      "until" : null,
                      "provider" : null,
                      "mapper" : "foo.bar.MapperOnTarget",
                      "processors" : null,
                      "alertings" : [ {
                        "condition" : "value > 0.7",
                        "level" : "info",
                        "message" : "_msg_oups_front",
                        "name" : "prd-prod-003",
                        "provider" : "{{myAlertingProvider}}"
                      } ],
                      "query" : "summarize(asPercent(sumSeries(org.foo.bar.jmx.joe.sessions),sumSeries(org.foo.bar.jmx.*.session)), \\"24h\\", \\"avg\\",true)",
                      "parent" : null,
                      "scheduler" : null
                    }, {
                      "name" : "gravida-pourcentrage",
                      "fromFirstTime" : null,
                      "until" : null,
                      "provider" : null,
                      "mapper" : null,
                      "processors" : null,
                      "alertings" : null,
                      "query" : "summarize(asPercent(sumSeries(org.foo.bar.jmx.gravida.sessions),sumSeries(org.foo.bar.jmx.*.session)), \\"24h\\", \\"avg\\",true)\\n",
                      "parent" : null,
                      "scheduler" : null
                    }, {
                      "name" : "sapien-pourcentrage",
                      "fromFirstTime" : null,
                      "until" : null,
                      "provider" : null,
                      "mapper" : null,
                      "processors" : null,
                      "alertings" : null,
                      "query" : "summarize(asPercent(sumSeries(org.foo.bar.jmx.sapien.sessions),sumSeries(org.foo.bar.jmx.*.session)), \\"24h\\", \\"avg\\",true)\\n",
                      "parent" : null,
                      "scheduler" : null
                    } ]
                  }, {
                    "type" : "Event",
                    "name" : "foobar-paiement",
                    "fromFirstTime" : null,
                    "until" : null,
                    "provider" : null,
                    "mapper" : null,
                    "processors" : null,
                    "alertings" : null,
                    "scheduler" : null,
                    "targets" : [ {
                      "name" : "current-paiement-cumul",
                      "fromFirstTime" : null,
                      "until" : null,
                      "provider" : "graphite.bigdata",
                      "mapper" : null,
                      "processors" : null,
                      "alertings" : null,
                      "query" : "summarize(sumSeries(org.foo.bar.paiement.*.count),\\"24h\\", true)",
                      "parent" : null,
                      "scheduler" : null
                    }, {
                      "name" : "lastyear-paiement-cumul",
                      "fromFirstTime" : null,
                      "until" : null,
                      "provider" : "jdbc.provider",
                      "mapper" : null,
                      "processors" : null,
                      "alertings" : null,
                      "query" : "select max(dateTime), sum(OBJ) from FOOBAR",
                      "parent" : null,
                      "scheduler" : null
                    } ]
                  } ],
                  "gav" : {
                    "artifactId" : "configuration-testing",
                    "groupId" : "io.inugami.test",
                    "hash" : "io.inugami.test:configuration-testing:4.0.0:for-test",
                    "qualifier" : "for-test",
                    "version" : "4.0.0"
                  },
                  "name" : "events-test",
                  "scheduler" : "0 0/5 * * * ?",
                  "simpleEvents" : [ {
                    "type" : "SimpleEvent",
                    "name" : "foobar-quality",
                    "fromFirstTime" : null,
                    "until" : null,
                    "provider" : "graphite.bigdata",
                    "mapper" : "oo.bar.Mapper",
                    "processors" : [ {
                      "name" : "foo"
                    }, {
                      "name" : "bar"
                    } ],
                    "alertings" : [ {
                      "condition" : "value > 5",
                      "level" : "warn",
                      "message" : "Attention augmentation d'erreur sur le service JOE",
                      "name" : "prd-prod-001",
                      "provider" : "{{myAlertingProvider}}"
                    } ],
                    "query" : "scale(summarize(avg(org.foobar.joe.*.count), '24h', 'avg', true),100)",
                    "parent" : null,
                    "scheduler" : "0 0/2 * * * ?"
                  }, {
                    "type" : "SimpleEvent",
                    "name" : "foobar-pourcentage",
                    "fromFirstTime" : null,
                    "until" : null,
                    "provider" : "graphite.bigdata",
                    "mapper" : null,
                    "processors" : null,
                    "alertings" : [ {
                      "function" : "mySupraFaboulousJavaScriptFunctionInMyPlugin",
                      "name" : "prd-prod-0011",
                      "provider" : "{{myAlertingProvider}}"
                    } ],
                    "query" : "scale(summarize(avg(org.foo.bar.joe.percent), '24h', 'avg', true),100)",
                    "parent" : null,
                    "scheduler" : null
                  }, {
                    "type" : "SimpleEvent",
                    "name" : "foobar-views-10mn",
                    "fromFirstTime" : null,
                    "until" : null,
                    "provider" : "graphite.bigdata",
                    "mapper" : null,
                    "processors" : null,
                    "alertings" : null,
                    "query" : "sumSeries(summarize(org.foo.bar.view,\\"10min\\",\\"avg\\",true))",
                    "parent" : null,
                    "scheduler" : null
                  }, {
                    "type" : "SimpleEvent",
                    "name" : "foobar-views-30mn",
                    "fromFirstTime" : null,
                    "until" : null,
                    "provider" : "graphite.bigdata",
                    "mapper" : null,
                    "processors" : null,
                    "alertings" : null,
                    "query" : "sumSeries(summarize(org.foo.bar.view,\\"10min\\",\\"avg\\",true))",
                    "parent" : null,
                    "scheduler" : null
                  } ]
                }
                """);


    }

}
