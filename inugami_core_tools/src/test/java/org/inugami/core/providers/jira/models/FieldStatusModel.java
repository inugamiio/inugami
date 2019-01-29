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
package org.inugami.core.providers.jira.models;

public class FieldStatusModel {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String id;
    
    private String name;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public FieldStatusModel() {
    }
    
    public FieldStatusModel(final String id, final String name) {
        this.id = id;
        this.name = name;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getId() {
        return id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    // =========================================================================
    // OTHER
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object other) {
        boolean result = this == other;
        if (!result && (other instanceof FieldStatusModel)) {
            final FieldStatusModel obj = (FieldStatusModel) other;
            result = this.id.equals(obj.getId());
        }
        return result;
    }
}
