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
package io.inugami.interfaces.models.basic;

import io.inugami.api.models.JsonBuilder;

/**
 * JsonStrings
 * 
 * @author patrick_guillerm
 * @since 3 mai 2018
 */
public class JsonStringArray implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -2599911917042218989L;
    
    private String[]          data;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public JsonStringArray(final String... data) {
        super();
        this.data = data;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String convertToJson() {
        final JsonBuilder json = new JsonBuilder();
        if (data == null) {
            json.valueNull();
        }
        else {
            json.openList();
            for (int i = 0; i < data.length; i++) {
                if (i != 0) {
                    json.addSeparator();
                }
                json.valueQuot(data[i]);
            }
            
            json.closeList();
        }
        
        return json.toString();
    }
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String[] getData() {
        final String[] result = new String[data.length];
        System.arraycopy(data, 0, result, 0, data.length);
        return result;
    }
    
    public void setData(final String... data) {
        this.data = data;
    }
    
}
