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

/**
 * StringJson
 * 
 * @author patrick_guillerm
 * @since 23 janv. 2017
 */
public class StringJson implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -974633377401939295L;
    
    private String            value;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    public StringJson(final String value) {
        super();
        this.value = value;
    }
    
    public StringJson() {
        super();
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public int hashCode() {
        return 31 * ((value == null) ? 0 : value.hashCode());
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof StringJson)) {
            final StringJson other = (StringJson) obj;
            result = value == null ? other.getValue() == null : value.equals(other.getValue());
        }
        return result;
    }
    
    @Override
    public String toString() {
        //@formatter:off
        return new StringBuilder(this.getClass().getSimpleName())
                    .append('@')
                    .append(System.identityHashCode(this))
                    .append("value=").append(value)
                    .append(']')
                    .toString();
        //@formatter:on
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public <T extends JsonObject> T convertToObject(final byte[] data, final Charset encoding) {
        final String dataValue = new String(data, encoding);
        return (T) new StringJson(dataValue);
    }
    
    @Override
    public String convertToJson() {
        return new StringBuilder().append('"').append(value).append('"').toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getValue() {
        return value;
    }
    
    @Override
    public JsonObject cloneObj() {
        return new StringJson(value);
    }
    
}
