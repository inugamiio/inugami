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
        final File                  file      = new File(RESOURCES_PATH + "/events-test-configuration.yaml");
        final Optional<EventConfig> configOpt = loader.loadEventConfigFromFile(new Gav(), file);

        assertTrue(configOpt.isPresent());
        assertText(configOpt.get(), """
                {
                  "enable" : true,
                  "events" : [ {
                    "alertings" : [ {
                      "condition" : "var level=null; var message=null;\\nif(value>0.8){\\n  level=\\"error\\";\\n  message=\\"Erreur le seuil sur les sessions client est trop élevé\\";\\n}else if(value>0.6){\\n  level=\\"warning\\";\\n  message=\\"Attention le seuil sur les sessions client est haut\\";\\n}\\nreturn {\\n  \\"level\\"   :level,\\n  \\"message\\" :message\\n}\\n",
                      "level" : "error",
                      "message" : "Oups",
                      "name" : "prd-prod-002",
                      "provider" : "{{myAlertingProvider}}"
                    } ],
                    "mapper" : "foo.bar.MapperOnEvent",
                    "name" : "foobar-api-pourcentrage",
                    "processors" : [ {
                      "name" : "foo"
                    } ],
                    "provider" : "graphite.bigdata",
                    "scheduler" : "0 0/3 * * * ?",
                    "targets" : [ {
                      "alertings" : [ {
                        "condition" : "value > 0.7",
                        "level" : "info",
                        "message" : "_msg_oups_front",
                        "name" : "prd-prod-003",
                        "provider" : "{{myAlertingProvider}}"
                      } ],
                      "mapper" : "foo.bar.MapperOnTarget",
                      "name" : "foobar-sys",
                      "query" : "summarize(asPercent(sumSeries(org.foo.bar.jmx.joe.sessions),sumSeries(org.foo.bar.jmx.*.session)), \\"24h\\", \\"avg\\",true)"
                    }, {
                      "name" : "gravida-pourcentrage",
                      "query" : "summarize(asPercent(sumSeries(org.foo.bar.jmx.gravida.sessions),sumSeries(org.foo.bar.jmx.*.session)), \\"24h\\", \\"avg\\",true)\\n"
                    }, {
                      "name" : "sapien-pourcentrage",
                      "query" : "summarize(asPercent(sumSeries(org.foo.bar.jmx.sapien.sessions),sumSeries(org.foo.bar.jmx.*.session)), \\"24h\\", \\"avg\\",true)\\n"
                    } ]
                  }, {
                    "name" : "foobar-paiement",
                    "targets" : [ {
                      "name" : "current-paiement-cumul",
                      "provider" : "graphite.bigdata",
                      "query" : "summarize(sumSeries(org.foo.bar.paiement.*.count),\\"24h\\", true)"
                    }, {
                      "name" : "lastyear-paiement-cumul",
                      "provider" : "jdbc.provider",
                      "query" : "select max(dateTime), sum(OBJ) from FOOBAR"
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
                    "alertings" : [ {
                      "condition" : "value > 5",
                      "level" : "warn",
                      "message" : "Attention augmentation d'erreur sur le service JOE",
                      "name" : "prd-prod-001",
                      "provider" : "{{myAlertingProvider}}"
                    } ],
                    "mapper" : "oo.bar.Mapper",
                    "name" : "foobar-quality",
                    "processors" : [ {
                      "name" : "foo"
                    }, {
                      "name" : "bar"
                    } ],
                    "provider" : "graphite.bigdata",
                    "query" : "scale(summarize(avg(org.foobar.joe.*.count), '24h', 'avg', true),100)",
                    "scheduler" : "0 0/2 * * * ?"
                  }, {
                    "alertings" : [ {
                      "function" : "mySupraFaboulousJavaScriptFunctionInMyPlugin",
                      "name" : "prd-prod-0011",
                      "provider" : "{{myAlertingProvider}}"
                    } ],
                    "name" : "foobar-pourcentage",
                    "provider" : "graphite.bigdata",
                    "query" : "scale(summarize(avg(org.foo.bar.joe.percent), '24h', 'avg', true),100)"
                  }, {
                    "from" : "-10min",
                    "name" : "foobar-views-10mn",
                    "provider" : "graphite.bigdata",
                    "query" : "sumSeries(summarize(org.foo.bar.view,\\"10min\\",\\"avg\\",true))"
                  }, {
                    "from" : "-30min",
                    "name" : "foobar-views-30mn",
                    "provider" : "graphite.bigdata",
                    "query" : "sumSeries(summarize(org.foo.bar.view,\\"10min\\",\\"avg\\",true))"
                  } ]
                }
                """);


    }

}
