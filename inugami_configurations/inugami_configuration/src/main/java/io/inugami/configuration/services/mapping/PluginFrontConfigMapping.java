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
package io.inugami.configuration.services.mapping;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.services.MappingException;
import io.inugami.configuration.models.plugins.front.MenuLink;
import io.inugami.configuration.models.plugins.front.PluginComponent;
import io.inugami.configuration.models.plugins.front.PluginFrontConfig;
import io.inugami.configuration.models.plugins.front.Route;

import java.util.ArrayList;

/**
 * PluginFrontConfigMapping
 *
 * @author patrick_guillerm
 * @since 19 janv. 2017
 */
public class PluginFrontConfigMapping {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================

    //@formatter:off
    private final JSONDeserializer<PluginFrontConfig> deserializer = new JSONDeserializer<PluginFrontConfig>()
                                                                            .use(null, PluginFrontConfig.class)
                                                                            .use("group.module", PluginComponent.class)
                                                                            .use("group.router", ArrayList.class)
                                                                            .use("group.router.values", Route.class)
                                                                            .use("group.menuLinks", ArrayList.class)
                                                                            .use("group.menuLinks.values", MenuLink.class);
    //@formatter:on

    // =========================================================================
    // METHODS
    // =========================================================================
    public PluginFrontConfig unmarshalling(final String json) throws MappingException {
        if (json == null) {
            throw new PluginFrontConfigMappingException("can't unmarshalling null object!");
        }
        final PluginFrontConfig result = deserializer.deserialize(json);
        validateConfig(result);
        return result;
    }

    public String marshalling(final PluginFrontConfig value) {
        return new JSONSerializer().exclude("*.class").deepSerialize(value);
    }

    // =========================================================================
    // VALIDATE
    // =========================================================================
    private void validateConfig(final PluginFrontConfig result) {
        Asserts.assertNotNull("can't unmarshalling object!", result);
        Asserts.assertNotEmpty("plugin base name is mandatory!", result.getPluginBaseName());
        Asserts.assertNotNull("module definition is mandatory!", result.getModule());
        Asserts.assertNotEmpty("module class name is mandatory!", result.getModule().getClassName());
        Asserts.assertNotEmpty("module file is mandatory!", result.getModule().getFile());
    }

    // =========================================================================
    // Exception
    // =========================================================================
    @SuppressWarnings({"java:S110"})
    private class PluginFrontConfigMappingException extends MappingException {

        private static final long serialVersionUID = 1318572501610980271L;

        public PluginFrontConfigMappingException(final String message) {
            super(message);
        }

    }
}
