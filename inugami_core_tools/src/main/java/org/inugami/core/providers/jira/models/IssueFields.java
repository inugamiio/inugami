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

public class IssueFields {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private Object customFields;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public IssueFields() {
    }
    
    public IssueFields(final Object customFields) {
        this.customFields = customFields;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public <T> T getCustomFields() {
        return (T) customFields;
    }
    
    public void setCustomFields(final Object customFields) {
        this.customFields = customFields;
    }
}
