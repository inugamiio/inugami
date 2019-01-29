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
package org.inugami.api.models.data.basic;

import org.inugami.api.models.data.JsonObject;

import flexjson.JSONDeserializer;

/**
 * RawJsonObject
 * 
 * @author patrick_guillerm
 * @since 18 mai 2018
 */
public class RawJsonObject implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -7034224735029402485L;
    
    private Object            data;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public RawJsonObject() {
    }
    
    public RawJsonObject(final Object data) {
        super();
    }
    
    public static JsonObject buildFromObject(final Object data) {
        final RawJsonObject result = new RawJsonObject();
        result.setData(data);
        return result;
    }
    
    public static JsonObject buildFromJson(final String data) {
        final RawJsonObject result = new RawJsonObject();
        result.setData(new JSONDeserializer<Object>().deserialize(data));
        return result;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public Object getData() {
        return data;
    }
    
    public void setData(final Object data) {
        this.data = data;
    }
    
}
