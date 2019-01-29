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

public class QueryJson {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private int    id;
    
    private String projectName;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public QueryJson() {
    }
    
    public QueryJson(final int id, final String projectName) {
        this.id = id;
        this.projectName = projectName;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public int getId() {
        return id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(final String projectName) {
        this.projectName = projectName;
    }
    
    // =========================================================================
    // OTHER
    // =========================================================================
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + id;
        return result;
    }
    
    @Override
    public boolean equals(final Object other) {
        boolean result = this == other;
        if (!result && (other instanceof QueryJson)) {
            final QueryJson obj = (QueryJson) other;
            result = this.id == obj.getId();
        }
        return result;
    }
    
}
