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
package org.inugami.core.context.handlers.hesperides.model;

import java.nio.charset.Charset;
import java.util.List;

import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.data.JsonObject;

import flexjson.JSON;
import flexjson.JSONDeserializer;
import flexjson.JSONException;

public class Properties implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 8473531906905438007L;
    
    @JSON(name = "key_value_properties")
    private List<Property>    properties;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public Properties() {
    }
    
    public Properties(final List<Property> properties) {
        this.properties = properties;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public <T extends JsonObject> T convertToObject(final byte[] data, final Charset charset) {
        Properties result = null;
        
        if (data != null) {
            final String json = charset == null ? new String(data).trim() : new String(data, charset).trim();
            
            if (!json.isEmpty()) {
                try {
                    result = new JSONDeserializer<Properties>().deserialize(json, Properties.class);
                    
                }
                catch (final JSONException error) {
                    Loggers.DEBUG.error("{} : \n payload:\n----------\n{}\n----------\n", error.getMessage(), json);
                }
            }
        }
        
        return (T) result;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public List<Property> getProperties() {
        return properties;
    }
    
    public void setProperties(final List<Property> properties) {
        this.properties = properties;
    }
}
