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
package io.inugami.configuration.models.plugins;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * PropertyModel
 * 
 * @author patrick_guillerm
 * @since 10 mai 2017
 */
@XStreamAlias("property")
public class PropertyModel implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 1681249573619420546L;
    
    @XStreamAsAttribute
    private String            key;
    
    @XStreamAsAttribute
    private String            value;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public PropertyModel() {
        super();
    }
    
    public PropertyModel(final String key, final String value) {
        super();
        this.key = key;
        this.value = value;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        return 31 + ((key == null) ? 0 : key.hashCode());
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        if (!result && (obj != null) && (obj instanceof PropertyModel)) {
            final PropertyModel other = (PropertyModel) obj;
            result = key == null ? other.getKey() == null : key.equals(other.getKey());
            
        }
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("PropertyModel [key=");
        builder.append(key);
        builder.append(", value=");
        builder.append(value);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getKey() {
        return key;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
}
