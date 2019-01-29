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
package org.inugami.core.providers.gitlab.models;

public class MergeAuthor {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String name;
    
    private String username;
    
    private int    id;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public MergeAuthor() {
    }
    
    public MergeAuthor(final String name, final String username, final int id) {
        this.name = name;
        this.username = username;
        this.id = id;
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
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    // =========================================================================
    // OTHER
    // =========================================================================
    @Override
    public boolean equals(final Object other) {
        boolean result = this == other;
        if (!result && (other instanceof MergeAuthor)) {
            final MergeAuthor obj = (MergeAuthor) other;
            result = this.id == obj.getId();
        }
        return result;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + id;
        return result;
    }
    
}
