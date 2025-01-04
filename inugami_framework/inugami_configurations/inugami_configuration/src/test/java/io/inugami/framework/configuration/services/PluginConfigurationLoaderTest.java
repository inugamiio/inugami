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

import io.inugami.api.alertings.AlertingProviderModel;
import io.inugami.api.models.Gav;
import io.inugami.api.processors.Config;
import io.inugami.api.processors.ProcessorModel;
import io.inugami.commons.connectors.config.HttpDefaultConfig;
import io.inugami.commons.files.FilesUtils;
import io.inugami.commons.tools.TestUnitResources;
import io.inugami.configuration.models.ListenerModel;
import io.inugami.configuration.models.ProviderConfig;
import io.inugami.configuration.models.app.ApplicationConfig;
import io.inugami.configuration.models.app.DataProviderModel;
import io.inugami.configuration.models.app.SecurityConfiguration;
import io.inugami.configuration.models.plugins.*;
import io.inugami.configuration.models.plugins.components.config.ComponentModel;
import io.inugami.configuration.models.plugins.components.config.Components;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        final File                        appFile = new File(RESOURCES_PATH + "/application-configuration.xml");
        final Optional<ApplicationConfig> confOpt = loader.loadApplicationConfig(appFile);
        assertTrue(confOpt.isPresent());

        final ApplicationConfig config = confOpt.get();
        assertEquals("inugami-tv", config.getApplicationName());
        assertEquals(1200000, config.getTimeout());
        assertEquals(5, config.getMaxPluginRunning());
        assertEquals(20, config.getMaxPluginRunningStandalone());
        assertEquals(5, config.getMaxRunningEvents());

        assertDataStorage(config.getDataStorage());
        assertThat(config.getHttpDefaultConfig()).isNotNull();
        assertHttpDefaultConfig(config.getHttpDefaultConfig());
        assertThat(config.getSecurity()).isNotNull();
        assertSecurityConfig(config.getSecurity());
    }

    private void assertDataStorage(final DataProviderModel dataStorage) {
        assertThat(dataStorage).isNotNull();
        assertEquals("org.h2.Driver", dataStorage.getDriver());
        assertEquals("org.hibernate.dialect.H2Dialect", dataStorage.getDialect());
        assertEquals("jdbc:h2:mem:inugami", dataStorage.getUrl());
        assertEquals("sa", dataStorage.getUser());
        assertEquals("", dataStorage.getPassword());
    }

    private void assertHttpDefaultConfig(final HttpDefaultConfig config) {
        assertEquals(65000, config.getTimeout());
        assertEquals(60000, config.getSocketTimeout());
        assertEquals(30000, config.getTtl());
        assertEquals(300, config.getMaxConnections());
        assertEquals(50, config.getMaxPerRoute());
        assertEquals(2, config.getHeaderFields().size());
        assertEquals("APPLICATION-NAME", config.getHeaderFields().get(0).getName());
        assertEquals("inugami", config.getHeaderFields().get(0).getValue());
        assertEquals("APPLICATION-HOSTNAME", config.getHeaderFields().get(1).getName());
        assertEquals("instanceName", config.getHeaderFields().get(1).getValue());

    }

    private void assertSecurityConfig(final List<SecurityConfiguration> config) {
        assertEquals(2, config.size());
        assertEquals("ldap", config.get(0).getName());
        assertEquals("technical", config.get(1).getName());

        final SecurityConfiguration ldap = config.get(0);
        assertThat(ldap.getUsers()).isNull();
        assertEquals(8, ldap.getConfigs().size());
        assertConfKeyValue("security.ldap.domain", "FOOBAR", ldap.getConfigs().get(0));
        assertConfKeyValue("security.ldap.rootDn", "dc=joegroup,dc=org", ldap.getConfigs().get(1));
        assertConfKeyValue("security.ldap.url", "ldaps://1.2.3.4:1234", ldap.getConfigs().get(2));
        assertConfKeyValue("security.ldap.referral", "follow", ldap.getConfigs().get(3));
        assertConfKeyValue("security.ldap.searchFilter", "(name={0})", ldap.getConfigs().get(4));
        assertConfKeyValue("security.ldap.roleBase", "dc=joegroup,dc=org", ldap.getConfigs().get(5));
        assertConfKeyValue("security.ldap.roleSearch", "(member={0})", ldap.getConfigs().get(6));
        assertConfKeyValue("security.ldap.timeout", "5000", ldap.getConfigs().get(7));

        final SecurityConfiguration defaultConf = config.get(1);
        assertThat(defaultConf.getRoles()).isNull();
        assertEquals(2, defaultConf.getUsers().size());
        assertEquals("admin", defaultConf.getUsers().get(0).getLogin());
        assertEquals("ada76c3861a84664b172", defaultConf.getUsers().get(0).getPassword());
        assertEquals("ada76c38-61a8-4664-b172", defaultConf.getUsers().get(0).getToken());
        assertEquals(2, defaultConf.getUsers().get(0).getUserRoles().size());
        assertEquals("admin", defaultConf.getUsers().get(0).getUserRoles().get(0));
        assertEquals("user", defaultConf.getUsers().get(0).getUserRoles().get(1));

        assertEquals("user", defaultConf.getUsers().get(1).getLogin());
        assertEquals("da3b40659e3b7ff9f4d2ccfe", defaultConf.getUsers().get(1).getPassword());
        assertEquals("a3b-4065-9e3b-7ff9f4d2-ccfe", defaultConf.getUsers().get(1).getToken());
    }

    private void assertConfKeyValue(final String key, final String value, final Config config) {
        assertEquals(key, config.getKey());
        assertEquals(value, config.getValue());
    }

    // =========================================================================
    // PLUGIN CONFIG
    // =========================================================================
    @Test
    void testLoadFromFile() throws Exception {
        final PluginConfigurationLoader loader = new PluginConfigurationLoader();

        LOGGER.info("load plugin-configuration.xml");
        // @formatter:off
        final Optional<PluginConfiguration> configOpt = loader.loadFromFile(new File(RESOURCES_PATH + "/plugin-configuration.xml"));
        // @formatter:on

        assertTrue(configOpt.isPresent());
        final PluginConfiguration config = configOpt.get();
        LOGGER.info("validate data");

        validateGav(config.getGav());
        validateProperties(config.getProperties());
        validateResources(config.getResources());
        validateProviders(config.getProviders());
        validateListeners(config.getListeners());
        validateProcessors(config.getProcessors());
        validateEventsFiles(config.getEventsFiles());
        validateDependencies(config.getDependencies());
        validateAlertings(config.getAlertings());
        assertSecurityConfig(config.getSecurity());

    }

    private void validateProperties(final List<PropertyModel> properties) {
        assertThat(properties).isNotNull();
        assertEquals(2, properties.size());
        assertEquals("data", properties.get(0).getKey());
        assertEquals("FoobarData", properties.get(0).getValue());

        assertEquals("joe", properties.get(1).getKey());
        assertEquals("Joe {{data}}", properties.get(1).getValue());
    }

    /**
     * Validate gav.
     *
     * @param gav the gav
     */
    private void validateGav(final Gav gav) {
        LOGGER.info("validateGav : {}", gav);

        assertThat(gav).isNotNull();
        assertEquals("plugin-group-id", gav.getGroupId());
        assertEquals("plugin-artifact-id", gav.getArtifactId());
        assertEquals("0.1", gav.getVersion());

    }

    /**
     * Validate resources.
     *
     * @param resources the resources
     */
    private void validateResources(final List<Resource> resources) {
        LOGGER.info("validateResources");
        assertThat(resources).isNotNull();
        assertEquals(4, resources.size());

        assertTrue(resources.get(0) instanceof ResourceCss);
        assertTrue(resources.get(1) instanceof ResourceJavaScript);
        assertTrue(resources.get(2) instanceof ResourceJavaScript);
        assertTrue(resources.get(3) instanceof ResourcePage);

        assertEquals("inugami_plugin/css/", resources.get(0).getPath());
        assertEquals("theme.css", resources.get(0).getName());
        assertEquals("inugami_plugin/css/theme.css", resources.get(0).getFullPath());

        assertEquals("inugami_plugin/js/", resources.get(1).getPath());
        assertEquals("formatter.js", resources.get(1).getName());
        assertEquals("inugami_plugin/js/formatter.js", resources.get(1).getFullPath());

        assertEquals("inugami_plugin/js/", resources.get(2).getPath());
        assertEquals("loader.js", resources.get(2).getName());
        assertEquals("inugami_plugin/js/loader.js", resources.get(2).getFullPath());

        assertEquals("inugami_plugin/", resources.get(3).getPath());
        assertEquals("index.html", resources.get(3).getName());
        assertEquals("inugami_plugin/index.html", resources.get(3).getFullPath());

    }

    /**
     * Validate callers.
     *
     * @param callers the callers
     */
    private void validateCallers(final List<ProviderConfig> callers) {
        LOGGER.info("validateCallers");
        assertThat(callers).isNotNull();
        assertEquals(1, callers.size());

        assertEquals("system", callers.get(0).getName());
        // @formatter:off
        assertEquals("io.inugami.plugins.callers.SystemCaller", callers.get(0).getClassName());
        // @formatter:on

        assertThat(callers.get(0).getConfigs()).isNotNull();
        assertEquals("var", callers.get(0).getConfig("foo").get());
    }

    /**
     * Validate providers.
     *
     * @param providers the providers
     */
    private void validateProviders(final List<ProviderConfig> providers) {
        LOGGER.info("validateProviders");
        assertThat(providers).isNotNull();
        assertEquals(1, providers.size());
        assertEquals("io.inugami.plugins.callers.SystemCaller", providers.get(0).getClassName());
        assertEquals("foobar.system", providers.get(0).getName());

        assertThat(providers.get(0).getConfigs()).isNotNull();
        assertEquals("FoobarData", providers.get(0).getConfig("data").get());
    }

    /**
     * Validate listeners.
     *
     * @param listeners the listeners
     */
    private void validateListeners(final List<ListenerModel> listeners) {
        LOGGER.info("validateListeners");
        assertThat(listeners).isNotNull();
        assertEquals(1, listeners.size());
        assertEquals("customListener", listeners.get(0).getName());
        assertEquals("io.inugami.plugins.listeners.CustomListerner", listeners.get(0).getClassName());
        assertThat(listeners.get(0).getConfigs()).isNull();
    }

    /**
     * Validate processors.
     *
     * @param processors the processors
     */
    private void validateProcessors(final List<ProcessorModel> processors) {
        LOGGER.info("validateProcessors");
        assertThat(processors).isNotNull();
        assertEquals(1, processors.size());
        assertEquals("foo", processors.get(0).getName());
        assertEquals("io.inugami.plugins.processors.Foo", processors.get(0).getClassName());
        assertThat(processors.get(0).getConfigs()).isNull();

    }

    /**
     * Validate events files.
     *
     * @param eventsFiles the events files
     */
    private void validateEventsFiles(final List<EventsFileModel> eventsFiles) {
        LOGGER.info("validateEventsFiles");
        assertThat(eventsFiles).isNotNull();
        assertEquals(2, eventsFiles.size());

        assertEquals("foobar.xml", eventsFiles.get(0).getName());
        assertEquals("titi.xml", eventsFiles.get(1).getName());
    }

    private void validateDependencies(final List<Dependency> dependencies) {
        LOGGER.info("dependencies");
        assertThat(dependencies).isNotNull();
        assertEquals(2, dependencies.size());

        LOGGER.info("dependency : {}", dependencies.get(0));
        assertEquals("plugin-group-id-foobar", dependencies.get(0).getGroupId());
        assertEquals("other-plugin", dependencies.get(0).getArtifactId());
        assertEquals("0.15", dependencies.get(0).getVersion());

        LOGGER.info("dependency : {}", dependencies.get(1));
        assertEquals("org.foo.bar", dependencies.get(1).getGroupId());
        assertEquals("plugin-joe", dependencies.get(1).getArtifactId());
        assertEquals("0.2", dependencies.get(1).getVersion());
    }

    private void validateAlertings(final List<AlertingProviderModel> alertings) {
        LOGGER.info("alertings");
        assertThat(alertings).isNotNull();
        assertEquals(2, alertings.size());
        assertEquals("simpleGraphiteAlerting", alertings.get(0).getName());
        assertEquals("{{simpleGraphiteAlerting}}", alertings.get(0).getClassName());
        assertThat(alertings.get(0).getConfigs()).isNull();

        assertEquals("withOptionAlerting", alertings.get(1).getName());
        assertEquals("org.foo.bar.Alerting", alertings.get(1).getClassName());
        assertThat(alertings.get(1).getConfigs()).isNotNull();
        assertEquals(2, alertings.get(1).getConfigs().size());
        assertEquals("sender.sse", alertings.get(1).getConfigs().get(0).getKey());
        assertEquals("SseAlertingSenderService", alertings.get(1).getConfigs().get(0).getValue());
        assertEquals("min-level", alertings.get(1).getConfigs().get(1).getKey());
        assertEquals("warn", alertings.get(1).getConfigs().get(1).getValue());
    }

    // =========================================================================
    // COMPONENTS CONFIG
    // =========================================================================
    @Test
    void testLoadComponentsConfiguration() throws Exception {
        final PluginConfigurationLoader loader = new PluginConfigurationLoader();
        final URL path = FilesUtils.buildFile(RESOURCES_PATH, "plugin-components.xml").toURI()
                                   .toURL();
        final Components config = loader.loadComponentsConfiguration(path);
        assertThat(config).isNotNull();
        assertThat(config.getComponents()).isNotNull();
        assertEquals(1, config.getComponents().size());

        assertEquals("foo", config.getGav().getGroupId());
        assertEquals("bar", config.getGav().getArtifactId());
        assertEquals("0.0.1", config.getGav().getVersion());

        final ComponentModel compo = config.getComponents().get(0);
        assertEquals("rich.text", compo.getName());
        assertEquals(2, compo.getViews().size());
        assertEquals("display", compo.getViews().get(0).getType());
        assertEquals("r-rich-text", compo.getViews().get(0).getSelector());

        assertEquals("form", compo.getViews().get(1).getType());
        assertEquals("r-rich-text-form", compo.getViews().get(1).getSelector());

        //@formatter:off
        assertThat(compo.getDescriptions()).isNotNull();
        assertEquals(2, compo.getDescriptions().getDescriptions().size());
        assertEquals("default", compo.getDescriptions().getDescriptions().get(0).getType());
        assertEquals("/js/app/plugins/rastackware-front-plugins-commons/components/rich_text/rich.text.description.html",
                     compo.getDescriptions().getDescriptions().get(0).getPath());
        
        assertEquals("pl", compo.getDescriptions().getDescriptions().get(1).getType());
        assertEquals("/js/app/plugins/rastackware-front-plugins-commons/components/rich_text/rich.text.description_pl.html",
                     compo.getDescriptions().getDescriptions().get(1).getPath());
        
        assertEquals(2, compo.getDescriptions().getScreenshots().size());
        assertEquals("/js/app/plugins/rastackware-front-plugins-commons/components/rich_text/rich.text.screenshot_1.png",
                     compo.getDescriptions().getScreenshots().get(0).getPath());
        
        assertEquals("/js/app/plugins/rastackware-front-plugins-commons/components/rich_text/rich.text.screenshot_2.png",
                     compo.getDescriptions().getScreenshots().get(1).getPath());
        //@formatter:on

        assertEquals(2, compo.getModels().size());
        assertEquals("styleClass", compo.getModels().get(0).getName());
        assertEquals("string", compo.getModels().get(0).getType());
        assertEquals("foo", compo.getModels().get(0).getDefaultValue());
        assertEquals("_rich.text.fields.styleClass.description", compo.getModels().get(0).getDescription());

        assertEquals("style", compo.getModels().get(1).getName());
        assertEquals("string", compo.getModels().get(1).getType());
        assertEquals("bar", compo.getModels().get(1).getDefaultValue());
        assertEquals("_rich.text.fields.style.description", compo.getModels().get(1).getDescription());

        assertEquals(2, compo.getEvents().size());
        assertEquals("onChange", compo.getEvents().get(0).getName());
        assertEquals("emit", compo.getEvents().get(0).getType());
        assertEquals("_rich.text.fields.events.onChange.description", compo.getEvents().get(0).getDescription());

        assertEquals("refresh", compo.getEvents().get(1).getName());
        assertEquals("listener", compo.getEvents().get(1).getType());
        assertEquals("_rich.text.fields.events.refresh.description", compo.getEvents().get(1).getDescription());

    }

}
