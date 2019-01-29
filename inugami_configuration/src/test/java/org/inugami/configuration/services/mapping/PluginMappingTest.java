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
package org.inugami.configuration.services.mapping;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.inugami.api.models.Gav;
import org.inugami.api.models.events.Event;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.models.events.TargetConfig;
import org.inugami.api.processors.Config;
import org.inugami.api.processors.ProcessorModel;
import org.inugami.configuration.models.EventConfig;
import org.inugami.configuration.models.ListenerModel;
import org.inugami.configuration.models.ProviderConfig;
import org.inugami.configuration.models.plugins.Dependency;
import org.inugami.configuration.models.plugins.EventsFileModel;
import org.inugami.configuration.models.plugins.Plugin;
import org.inugami.configuration.models.plugins.PluginConfiguration;
import org.inugami.configuration.models.plugins.ResourceCss;
import org.inugami.configuration.models.plugins.front.MenuLink;
import org.inugami.configuration.models.plugins.front.PluginComponent;
import org.inugami.configuration.models.plugins.front.PluginFrontConfig;
import org.inugami.configuration.models.plugins.front.Route;
import org.inugami.configuration.tools.TestEngineListener;
import org.inugami.configuration.tools.TestHandler;
import org.inugami.configuration.tools.TestProcessor;
import org.inugami.configuration.tools.TestProvider;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PluginMappingTest
 * 
 * @author patrick_guillerm
 * @since 20 janv. 2017
 */
public class PluginMappingTest {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Logger LOGGER = LoggerFactory.getLogger(PluginMappingTest.class);
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testMarshalling() throws Exception {
        //@formatter:off
        final Plugin plugin = new Plugin(initConfig(),
                                         createList(initEventConfig()),
                                         createList(new TestEngineListener()),
                                         createList(new TestProcessor()),
                                         createList(new TestHandler()),
                                         createList(new TestProvider()),
                                         null,
                                         null,
                                         null);
        //@formatter:on
        
        final String json = new PluginMapping().marshalling(createList(plugin));
        LOGGER.info("json : {}", json);
        assertNotNull(json);
        assertFalse("marshalling mustn't return empty list!", "[]".equals(json));
        
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
        final PluginConfiguration result = new PluginConfiguration();
        result.setConfigFile("file://foo/bar/config.xml");
        //@formatter:off
        result.setFrontConfig(new PluginFrontConfig("foobarModule",
                                                    "foo/bar/commons.css",
                                                    new PluginComponent("PluignComponentModule", "plugin.component.module.ts"),
                                                    createList(new Route("home", "foobarComponent")),
                                                    createList(new MenuLink("home","Hello the world", "component-style-class")),
                                                    "RouterFoobarModule"));
        result.setGav(createGav());
        result.setResources(createList(new ResourceCss("resources/path", "application.css")));
        result.setProviders(createList(new ProviderConfig(TestProvider.class.getSimpleName(), TestProvider.class.getName())));
        result.setListeners(createList(new ListenerModel(TestEngineListener.class.getSimpleName(),TestEngineListener.class.getName())));
        result.setProcessors(createList(new ProcessorModel(TestProcessor.class.getSimpleName(), TestProcessor.class.getName())));
        result.setEventsFiles(createList(new EventsFileModel("plugin-test-events.xml")));
        result.setDependencies(createList(new Dependency("org.inugami.dashboard", "test-plugin-dependency", "0.0.1")));
        //@formatter:on
        
        return result;
    }
    
    private Gav createGav() {
        return new Gav("org.inugami.dashboard", "test-plugin", "0.0.1", "test");
    }
    
    private EventConfig initEventConfig() {
        //@formatter:off
        final EventConfig result = new EventConfig(createList(createEvent()),
                                                   createList(createSimpleEvent()),
                                                   "file:/path/to/event/file.xml",
                                                   "foobar",
                                                   true,
                                                   createGav(),
                                                   "0 0/5 * * * ?");
        //@formatter:on
        
        return result;
    }
    
    private Event createEvent() {
        //@formatter:off
        return new Event("event",
                         "3h",
                         "-10w",
                         TestProvider.class.getSimpleName(),
                         createList(new ProcessorModel(TestProcessor.class.getSimpleName(), TestProcessor.class.getName(), new Config("foo","bar"))),
                         createList(new TargetConfig(
                                                     "target-event",
                                 "-1h",
                                 "-10w",
                                 TestProvider.class.getSimpleName(),
                                 createList(new ProcessorModel(TestProcessor.class.getSimpleName(), TestProcessor.class.getName(), new Config("foo","bar"))),
                                 "org.foo.bar.service.*.90p",
                                 null,
                                 null,
                                 null,
                                 null)),null,null,null);
        //@formatter:on
    }
    
    private SimpleEvent createSimpleEvent() {
        //@formatter:off
        return new SimpleEvent("simple-event",
                               "-1h",
                               "-10w",
                               TestProvider.class.getSimpleName(),
                               createList(new ProcessorModel(TestProcessor.class.getSimpleName(), TestProcessor.class.getName(), new Config("foo","bar"))),
                               "org.foo.bar.service.err.count.1min",
                               null,
                               null,
                               null,
                               null);
        //@formatter:on
    }
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
}
