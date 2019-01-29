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

import java.util.ArrayList;

import org.inugami.api.exceptions.services.MappingException;
import org.inugami.configuration.models.plugins.front.MenuLink;
import org.inugami.configuration.models.plugins.front.PluginComponent;
import org.inugami.configuration.models.plugins.front.PluginFrontConfig;
import org.inugami.configuration.models.plugins.front.Route;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

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
    
    public String marshalling(final PluginFrontConfig value) throws MappingException {
        return new JSONSerializer().exclude("*.class").deepSerialize(value);
    }
    
    // =========================================================================
    // VALIDATE
    // =========================================================================
    private void validateConfig(final PluginFrontConfig result) throws MappingException {
        assertNotNull("can't unmarshalling object!", result);
        assertNotEmpty("plugin base name is mandatory!", result.getPluginBaseName());
        assertNotNull("module definition is mandatory!", result.getModule());
        assertNotEmpty("module class name is mandatory!", result.getModule().getClassName());
        assertNotEmpty("module file is mandatory!", result.getModule().getFile());
    }
    
    // =========================================================================
    // Exception
    // =========================================================================
    private void assertNotNull(final String message, final Object value) throws PluginFrontConfigMappingException {
        if (value == null) {
            throw new PluginFrontConfigMappingException(message);
        }
    }
    
    private void assertNotEmpty(final String message, final Object value) throws PluginFrontConfigMappingException {
        
    }
    
    private class PluginFrontConfigMappingException extends MappingException {
        
        private static final long serialVersionUID = 1318572501610980271L;
        
        public PluginFrontConfigMappingException(final String message) {
            super(message);
        }
        
    }
}
