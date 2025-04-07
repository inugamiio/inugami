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
package io.inugami.framework.configuration.services.mapping;

import io.inugami.framework.api.marshalling.YamlMarshaller;
import io.inugami.framework.configuration.models.EventConfig;
import io.inugami.framework.configuration.models.ListenerModel;
import io.inugami.framework.configuration.models.ProviderConfig;
import io.inugami.framework.configuration.models.front.MenuLink;
import io.inugami.framework.configuration.models.front.PluginComponent;
import io.inugami.framework.configuration.models.front.PluginFrontConfig;
import io.inugami.framework.configuration.models.front.Route;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.configuration.models.plugins.PluginConfiguration;
import io.inugami.framework.configuration.models.plugins.Resource;
import io.inugami.framework.configuration.models.plugins.RessourceType;
import io.inugami.framework.configuration.tools.TestEngineListener;
import io.inugami.framework.configuration.tools.TestHandler;
import io.inugami.framework.configuration.tools.TestProcessor;
import io.inugami.framework.configuration.tools.TestProvider;
import io.inugami.framework.interfaces.models.event.Event;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.processors.ProcessorModel;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.inugami.commons.test.UnitTestHelper.assertText;

/**
 * PluginMappingTest
 *
 * @author patrick_guillerm
 * @since 20 janv. 2017
 */
@SuppressWarnings({"java:S5785"})
class PluginMappingTest {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Logger LOGGER = LoggerFactory.getLogger(PluginMappingTest.class);

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testMarshalling() throws Exception {
        assertText(YamlMarshaller.getInstance().convertToYaml(initConfig()),
                   """
                           ---
                           alertings: []
                           components: []
                           configFile: "file://foo/bar/config.xml"
                           dependencies:
                           - artifactId: "test-plugin-dependency"
                             groupId: "io.inugami.dashboard"
                             hash: "io.inugami.dashboard:test-plugin-dependency:0.0.1"
                             version: "0.0.1"
                           eventsFiles:
                           - "plugin-test-events.yaml"
                           frontConfig:
                             menuLinks:
                             - path: "home"
                               styleClass: "component-style-class"
                               title: "Hello the world"
                             module:
                               className: "PluignComponentModule"
                               file: "plugin.component.module.ts"
                             pluginBaseName: "foobarModule"
                             router:
                             - component: "foobarComponent"
                               path: "home"
                             routerModuleName: "RouterFoobarModule"
                           frontProperties: []
                           gav:
                             artifactId: "test-plugin"
                             groupId: "io.inugami.dashboard"
                             hash: "io.inugami.dashboard:test-plugin:0.0.1:test"
                             qualifier: "test"
                             version: "0.0.1"
                           handlers: []
                           listeners:
                           - className: "TestEngineListener"
                             name: "io.inugami.framework.configuration.tools.TestEngineListener"
                           processors:
                           - className: "TestProcessor"
                             configs: {}
                             name: "io.inugami.framework.configuration.tools.TestProcessor"
                           properties: {}
                           providers:
                           - className: "TestProvider"
                             name: "io.inugami.framework.configuration.tools.TestProvider"
                           resources:
                           - fullPath: "resources/path/application.css"
                             name: "application.css"
                             path: "resources/path"
                             type: "CSS"
                           security: []
                           """);
    }
    // =========================================================================
    // OVERRIDES
    // =========================================================================

    private <T> List<T> createList(final T value) {
        final List<T> result = new ArrayList<>();
        result.add(value);
        return result;
    }

    private PluginConfiguration initConfig() {
        return PluginConfiguration.builder()
                                  .configFile("file://foo/bar/config.xml")
                                  .frontConfig(PluginFrontConfig.builder()
                                                                .pluginBaseName("foobarModule")
                                                                .routerModuleName("foo/bar/commons.css")
                                                                .module(new PluginComponent("PluignComponentModule", "plugin.component.module.ts"))
                                                                .router(new Route("home", "foobarComponent"))
                                                                .menuLinks(new MenuLink("home", "Hello the world", "component-style-class"))
                                                                .routerModuleName("RouterFoobarModule")
                                                                .build())
                                  .gav(createGav())
                                  .resources(List.of(Resource.builder()
                                                             .path("resources/path")
                                                             .name("application.css")
                                                             .type(RessourceType.CSS)
                                                             .build()))
                                  .providers(ProviderConfig.builder()
                                                           .className(TestProvider.class.getSimpleName())
                                                           .name(TestProvider.class.getName())
                                                           .build())
                                  .listeners(ListenerModel.builder()
                                                          .className(TestEngineListener.class.getSimpleName())
                                                          .name(TestEngineListener.class.getName())
                                                          .build())
                                  .processors(ProcessorModel.builder()
                                                            .className(TestProcessor.class.getSimpleName())
                                                            .name(TestProcessor.class.getName())
                                                            .build())
                                  .eventsFiles("plugin-test-events.yaml")
                                  .dependencies(Gav.builder()
                                                   .groupId("io.inugami.dashboard")
                                                   .artifactId("test-plugin-dependency")
                                                   .version("0.0.1")
                                                   .build())
                                  .build();
    }

    private Gav createGav() {
        return Gav.builder()
                  .groupId("io.inugami.dashboard")
                  .artifactId("test-plugin")
                  .version("0.0.1")
                  .qualifier("test")
                  .build();
    }

    private EventConfig initEventConfig() {
        return EventConfig.builder()
                          .name("foobar")
                          .configFile("file:/path/to/event/file.xml")
                          .events(createEvent())
                          .simpleEvents(createSimpleEvent())
                          .scheduler("0 0/5 * * * ?")
                          .enable(true)
                          .gav(createGav())
                          .build();
    }

    private Event createEvent() {
        return Event.builder()
                    .name("event")
                    .fromFirstTime("3h")
                    .from("-10w")
                    .processors(ProcessorModel.builder()
                                              .name(TestProcessor.class.getSimpleName())
                                              .className(TestProcessor.class.getName())
                                              .configs(Map.of("foo", "bar"))
                                              .build())
                    .build();

    }

    private SimpleEvent createSimpleEvent() {
        return SimpleEvent.builder()
                          .name("simple-event")
                          .from("-10w")
                          .fromFirstTime("-1h")
                          .provider(TestProvider.class.getSimpleName())
                          .processors(ProcessorModel.builder()
                                                    .name(TestProcessor.class.getSimpleName())
                                                    .className(TestProcessor.class.getName())
                                                    .configs(Map.of("foo", "bar"))
                                                    .build())
                          .build();
    }
}
