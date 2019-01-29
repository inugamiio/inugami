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

import java.nio.charset.Charset;

import org.inugami.api.models.data.JsonObject;

/**
 * Json
 * 
 * @author patrick_guillerm
 * @since 4 mai 2017
 */
public class Json implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5645613967034802896L;
    
    private final String      value;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public Json(final String value) {
        super();
        this.value = value;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((value == null) ? 0 : value.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = (this == obj) || ((value == null) && (obj == null));
        if (!result && (value != null) && (obj != null)) {
            final String realValue = String.valueOf(obj);
            result = value.equals(realValue);
        }
        
        return result;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        return convertToJson();
    }
    
    @Override
    public <T extends JsonObject> T convertToObject(final byte[] data, final Charset encoding) {
        return null;
    }
    
    @Override
    public String convertToJson() {
        return value;
    }
    
    @Override
    public JsonObject cloneObj() {
        return new Json(value);
    }
}
