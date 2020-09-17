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
package io.inugami.monitoring.providers.els;

import java.util.Arrays;
import java.util.List;

import io.inugami.api.models.data.basic.JsonObject;

/**
 * ElsData
 * 
 * @author patrick_guillerm
 * @since 12 sept. 2018
 */
public class ElsData implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 4218925493514972317L;
    
    private String            index;
    
    private String            type;
    
    private List<JsonObject>  values;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ElsData() {
    }
    
    public ElsData(final String index, final String type, final List<JsonObject> values) {
        super();
        this.index = index;
        this.type = type;
        this.values = values;
    }
    
    public ElsData(final String index, final String type, final JsonObject... values) {
        super();
        this.index = index;
        this.type = type;
        this.values = Arrays.asList(values);
    }
    
    // =========================================================================
    // OVERRIDE
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ElsData [index=");
        builder.append(index);
        builder.append(", type=");
        builder.append(type);
        builder.append(", values=");
        builder.append(values);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getIndex() {
        return index;
    }
    
    public void setIndex(final String index) {
        this.index = index;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public List<JsonObject> getValues() {
        return values;
    }
    
    public void setValues(final List<JsonObject> values) {
        this.values = values;
    }
    
}
