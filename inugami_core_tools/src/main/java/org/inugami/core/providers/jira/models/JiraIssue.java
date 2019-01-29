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

public class JiraIssue {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String      id;
    
    private String      key;
    
    private IssueFields fields;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public JiraIssue() {
    }
    
    public JiraIssue(final String id, final String key, final IssueFields fields) {
        this.id = id;
        this.key = key;
        this.fields = fields;
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
    
    public String getKey() {
        return key;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public IssueFields getFields() {
        return fields;
    }
    
    public void setFields(final IssueFields fields) {
        this.fields = fields;
    }
    
    // =========================================================================
    // OTHER
    // =========================================================================
    @Override
    public boolean equals(final Object other) {
        boolean result = this == other;
        if (!result && (other instanceof JiraIssue)) {
            final JiraIssue obj = (JiraIssue) other;
            result = this.id.equals(obj.getId());
        }
        return result;
    }
    
    @Override
    public int hashCode() {
        return 31 * ((this.id == null) ? 0 : this.id.hashCode());
    }
}
