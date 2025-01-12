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
import io.inugami.framework.configuration.models.plugins.*;
import io.inugami.framework.configuration.tools.TestEngineListener;
import io.inugami.framework.configuration.tools.TestHandler;
import io.inugami.framework.configuration.tools.TestProcessor;
import io.inugami.framework.configuration.tools.TestProvider;
import io.inugami.framework.interfaces.models.Config;
import io.inugami.framework.interfaces.models.event.Event;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.processors.ProcessorModel;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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

        final Plugin plugin = Plugin.builder()
                                    .config(initConfig())
                                    .events(initEventConfig())
                                    .listeners(new TestEngineListener())
                                    .processors(new TestProcessor())
                                    .handlers(new TestHandler())
                                    .providers(new TestProvider())
                                    .build();

        assertText(YamlMarshaller.getInstance().convertToYaml(plugin),
                   """
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
                                  .eventsFiles(EventsFileModel.builder().name("plugin-test-events.xml").build())
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
                                              .configs(new Config("foo", "bar"))
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
                                                    .configs(new Config("foo", "bar"))
                                                    .build())
                          .build();
    }
}
