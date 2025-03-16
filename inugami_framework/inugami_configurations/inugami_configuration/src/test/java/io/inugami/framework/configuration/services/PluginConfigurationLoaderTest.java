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

import io.inugami.framework.commons.files.FilesUtils;
import io.inugami.framework.commons.tools.TestUnitResources;
import io.inugami.framework.configuration.models.app.ApplicationConfig;
import io.inugami.framework.configuration.models.components.Components;
import io.inugami.framework.configuration.models.plugins.PluginConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Optional;

import static io.inugami.commons.test.UnitTestHelper.assertText;

/**
 * PluginConfigurationTest
 *
 * @author patrick_guillerm
 * @since 26 déc. 2016
 */
@SuppressWarnings({"java:S5961"})
class PluginConfigurationLoaderTest implements TestUnitResources {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final File RESOURCES_PATH = initResourcesPath();

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginConfigurationLoaderTest.class);

    // =========================================================================
    // APPLICATION CONFIG
    // =========================================================================
    @Test
    void testLoadApplicationConfig() throws Exception {
        final PluginConfigurationLoader   loader  = new PluginConfigurationLoader();
        final File                        appFile = new File(RESOURCES_PATH + "/application-configuration.yaml");
        final Optional<ApplicationConfig> confOpt = loader.loadApplicationConfig(appFile);

        assertText(confOpt.get(), """
                {
                  "alertingDynamicMaxThreads" : 50,
                  "alertingEnable" : true,
                  "applicationName" : "inugami-tv",
                  "dataStorage" : {
                    "dialect" : "org.hibernate.dialect.H2Dialect",
                    "driver" : "org.h2.Driver",
                    "password" : "",
                    "url" : "jdbc:h2:mem:inugami",
                    "user" : "sa",
                    "verbose" : false
                  },
                  "enableTechnicalsUsers" : true,
                  "httpDefaultConfig" : {
                    "headerFields" : [ {
                      "name" : "APPLICATION-NAME",
                      "value" : "inugami"
                    }, {
                      "name" : "APPLICATION-HOSTNAME",
                      "value" : "instanceName"
                    } ],
                    "maxConnections" : 300,
                    "maxPerRoute" : 50,
                    "socketTimeout" : 60000,
                    "timeout" : 65000,
                    "ttl" : 30000
                  },
                  "maxPluginRunning" : 5,
                  "maxPluginRunningStandalone" : 20,
                  "maxRunningEvents" : 5,
                  "maxThreads" : 1000,
                  "properties" : [ {
                    "key" : "foo",
                    "value" : "bar"
                  }, {
                    "key" : "security.max.level",
                    "value" : "1000"
                  } ],
                  "scriptTimeout" : 60000,
                  "security" : [ {
                    "configs" : {
                      "security.ldap.domain" : "FOOBAR",
                      "security.ldap.rootDn" : "dc=joegroup,dc=org",
                      "security.ldap.url" : "ldaps://1.2.3.4:1234",
                      "security.ldap.referral" : "follow",
                      "security.ldap.searchFilter" : "(name={0})",
                      "security.ldap.roleBase" : "dc=joegroup,dc=org",
                      "security.ldap.roleSearch" : "(member={0})",
                      "security.ldap.timeout" : "5000"
                    },
                    "name" : "ldap",
                    "roles" : [ {
                      "level" : 1000,
                      "matchers" : [ {
                        "expr" : "CN=joe-admin",
                        "type" : "EXACT"
                      }, {
                        "expr" : ".*JOE.*",
                        "type" : "EXACT"
                      } ],
                      "name" : "admin"
                    }, {
                      "level" : 1,
                      "matchers" : [ {
                        "expr" : "CN=foobar",
                        "type" : "EXACT"
                      }, {
                        "expr" : "OU=foobarService",
                        "type" : "EXACT"
                      } ],
                      "name" : "user"
                    } ]
                  }, {
                    "name" : "technical",
                    "users" : [ {
                      "firstName" : "Joe",
                      "lastName" : "Administrator",
                      "login" : "admin",
                      "password" : "ada76c3861a84664b172",
                      "token" : "ada76c38-61a8-4664-b172"
                    }, {
                      "firstName" : "Joe",
                      "lastName" : "User",
                      "login" : "user",
                      "password" : "da3b40659e3b7ff9f4d2ccfe",
                      "token" : "a3b-4065-9e3b-7ff9f4d2-ccfe"
                    } ]
                  } ],
                  "timeout" : 1200000
                }
                """);
    }


    // =========================================================================
    // PLUGIN CONFIG
    // =========================================================================
    @Test
    void testLoadFromFile() throws Exception {
        final PluginConfigurationLoader loader = new PluginConfigurationLoader();

        LOGGER.info("load plugin-configuration.yaml");
        final Optional<PluginConfiguration> configOpt = loader.loadFromFile(new File(
                RESOURCES_PATH + "META-INF/plugin-configuration.yaml"));

        assertText(configOpt.orElse(null), """
                """);
    }

    // =========================================================================
    // COMPONENTS CONFIG
    // =========================================================================
    @Test
    void testLoadComponentsConfiguration() throws Exception {
        final PluginConfigurationLoader loader = new PluginConfigurationLoader();
        final URL path = FilesUtils.buildFile(RESOURCES_PATH, "plugin-components.yaml")
                                   .toURI()
                                   .toURL();
        final Components config = loader.loadComponentsConfiguration(path);
        assertText(config, """
                """);

    }

}
