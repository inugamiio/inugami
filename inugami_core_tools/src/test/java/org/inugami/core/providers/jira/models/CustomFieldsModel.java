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

import java.util.List;

import flexjson.JSON;

public class CustomFieldsModel {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String                  summary;
    
    private List<FieldVersionModel> versions;
    
    private FieldStatusModel        status;
    
    @JSON(name = "customfield_17030")
    private String                  availabilityDate;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public CustomFieldsModel() {
    }
    
    public CustomFieldsModel(final String summary, final List<FieldVersionModel> versions,
                             final FieldStatusModel status, final String availabilityDate) {
        this.summary = summary;
        this.versions = versions;
        this.status = status;
        this.availabilityDate = availabilityDate;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(final String summary) {
        this.summary = summary;
    }
    
    public List<FieldVersionModel> getVersions() {
        return versions;
    }
    
    public void setVersions(final List<FieldVersionModel> versions) {
        this.versions = versions;
    }
    
    public FieldStatusModel getStatus() {
        return status;
    }
    
    public void setStatus(final FieldStatusModel status) {
        this.status = status;
    }
    
    public String getAvailabilityDate() {
        return availabilityDate;
    }
    
    public void setAvailabilityDate(final String availabilityDate) {
        this.availabilityDate = availabilityDate;
    }
}
