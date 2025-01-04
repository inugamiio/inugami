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

import io.inugami.configuration.models.plugins.front.MenuLink;
import io.inugami.configuration.models.plugins.front.PluginComponent;
import io.inugami.configuration.models.plugins.front.PluginFrontConfig;
import io.inugami.configuration.models.plugins.front.Route;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * PluginFrontConfigMappingTest
 *
 * @author patrick_guillerm
 * @since 19 janv. 2017
 */
class PluginFrontConfigMappingTest {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testUnmarshalling() throws Exception {
        final PluginFrontConfigMapping mapper = new PluginFrontConfigMapping();

        //@formatter:off
        final String[] lines = {
        "{                                                                                      ",
        "    \"pluginBaseName\": \"foobar_plugin\",                                             ",
        "    \"commonsCss\": \"/css/foobar_plugin.css\",                                        ",
        "    \"module\": {                                                                      ",
        "        \"className\": \"FoobarModule\",                                               ",
        "        \"file\"     : \"./view/foobar_plugin/foobar.module.ts\"                       ",
        "    },                                                                                 ",
        "    \"router\": [                                                                      ",
        "        {                                                                              ",
        "            \"path\": \"home\",                                                        ",
        "            \"component\": \"FoobarHomeView\"                                          ",
        "        }                                                                              ",
        "    ],                                                                                 ",
        "    \"menuLinks\": [                                                                   ",
        "        {                                                                              ",
        "            \"path\"      : \"home\",                                                  ",
        "            \"title\"     : \"dashboard\",                                             ",
        "            \"styleClass\": \"foo-dashboard\"                                          ",
        "        }                                                                              ",
        "    ]                                                                                  ",
        "}                                                                                      "
         };
        //@formatter:on
        final String json = String.join("\n", lines);

        final PluginFrontConfig config = mapper.unmarshalling(json);
        assertNotNull(config);
        assertEquals("foobar_plugin", config.getPluginBaseName());
        assertEquals("/css/foobar_plugin.css", config.getCommonsCss());
        assertNotNull(config.getModule());
        assertEquals("FoobarModule", config.getModule().getClassName());
        assertEquals("./view/foobar_plugin/foobar.module.ts", config.getModule().getFile());
        //
        assertNotNull(config.getRouter());
        assertEquals(1, config.getRouter().size());
        assertEquals("home", config.getRouter().get(0).getPath());
        assertEquals("FoobarHomeView", config.getRouter().get(0).getComponent());
        //
        assertNotNull(config.getMenuLinks());
        assertEquals(1, config.getRouter().size());
        assertEquals("home", config.getRouter().get(0).getPath());
        assertEquals("FoobarHomeView", config.getRouter().get(0).getComponent());
    }

    @Test
    void testMarshalling() throws Exception {
        final PluginFrontConfigMapping mapper = new PluginFrontConfigMapping();

        final PluginFrontConfig config = new PluginFrontConfig("testPlugin");
        config.setCommonsCss("commonsCss");
        config.setRouterModuleName("pluginRouter");
        config.setModule(new PluginComponent("moduleClassName", "./module.class.name.ts"));
        config.addRoute(new Route("home", "PluginHomeComponent"));
        config.addMenuLink(new MenuLink("home", "Plugin home title", "plugin-style-class"));

        final String json = mapper.marshalling(config);
        assertNotNull(json);

        //@formatter:off
        final StringBuilder ref = new StringBuilder();
        ref.append('{');
        ref.append("\"commonsCss\":\"commonsCss\",");
        ref.append("\"menuLinks\":[{\"path\":\"home\",\"styleClass\":\"plugin-style-class\",\"title\":\"Plugin home title\"}],");
        ref.append("\"module\":{\"className\":\"moduleClassName\",\"file\":\"./module.class.name.ts\"},");
        ref.append("\"pluginBaseName\":\"testPlugin\",");
        ref.append("\"router\":[{\"component\":\"PluginHomeComponent\",\"path\":\"home\"}],");
        ref.append("\"routerModuleName\":\"pluginRouter\"");
        ref.append(",\"systemMap\":null");
        ref.append('}');
        //@formatter:on
        assertEquals(ref.toString(), json);
    }
}
