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
package org.inugami.api.models.data;

import java.nio.charset.Charset;

import org.inugami.api.models.ClonableObject;

import flexjson.JSONDeserializer;

/**
 * JsonObject
 * 
 * @author patrick_guillerm
 * @since 12 janv. 2017
 */
public interface JsonObject extends JsonObjectToJson, ClonableObject<JsonObject> {
    
    default <T extends JsonObject> T convertToObject(final byte[] data, final Charset encoding) {
        final String json = new String(data, encoding);
        return new JSONDeserializer<T>().use(null, this.getClass()).deserialize(json);
    }
    
    @Override
    default JsonObject cloneObj() {
        return null;
    }
    
}
