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

public class Property {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String name;
    
    private String value;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public Property() {
    }
    
    public Property(final String name, final String value) {
        this.name = name;
        this.value = value;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getName() {
        return name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    // =========================================================================
    // OTHER
    // =========================================================================
    @Override
    public boolean equals(final Object other) {
        boolean result = this == other;
        if (!result && (other instanceof Property)) {
            final Property obj = (Property) other;
            result = this.name.equals(obj.getName());
        }
        return result;
    }
    
    @Override
    public int hashCode() {
        return 31 * ((this.name == null) ? 0 : this.name.hashCode());
    }
}
