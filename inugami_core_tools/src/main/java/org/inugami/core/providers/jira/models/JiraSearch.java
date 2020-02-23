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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.data.basic.JsonObject;

import flexjson.JSONDeserializer;
import flexjson.JSONException;

public class JiraSearch implements JsonObject {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -5530361340876738378L;
    
    private int               startAt;
    
    private int               maxResults;
    
    private int               total;
    
    private List<JiraIssue>   issues;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public JiraSearch() {
    }
    
    public JiraSearch(final int startAt, final int maxResults, final int total, final List<JiraIssue> issues) {
        this.startAt = startAt;
        this.maxResults = maxResults;
        this.total = total;
        this.issues = issues;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public <T extends JsonObject> T convertToObject(final byte[] data, final Charset charset, final Class<?> clazz) {
        JiraSearch result = null;
        
        if (data != null) {
            final String json = charset == null ? new String(data).trim() : new String(data, charset).trim();
            
            if (!json.isEmpty()) {
                try {
                    result = new JSONDeserializer<JiraSearch>().use(IssueFields.class,
                                                                    new CustomFieldsFactory(clazz)).deserialize(json,
                                                                                                                JiraSearch.class);
                    
                }
                catch (final JSONException error) {
                    Loggers.DEBUG.error("{} : \n payload:\n----------\n{}\n----------\n", error.getMessage(), json);
                    Loggers.PROVIDER.error(error.getMessage());
                }
            }
        }
        
        return (T) result;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public int getStartAt() {
        return startAt;
    }
    
    public void setStartAt(final int startAt) {
        this.startAt = startAt;
    }
    
    public int getMaxResults() {
        return maxResults;
    }
    
    public void setMaxResults(final int maxResults) {
        this.maxResults = maxResults;
    }
    
    public int getTotal() {
        return total;
    }
    
    public void setTotal(final int total) {
        this.total = total;
    }
    
    public List<JiraIssue> getIssues() {
        return issues;
    }
    
    public void setIssues(final List<JiraIssue> issues) {
        this.issues = issues;
    }
    
    // =========================================================================
    // CLONE
    // =========================================================================
    @Override
    public JsonObject cloneObj() {
        final List<JiraIssue> newJiraIssues = new ArrayList<>();
        if (this.issues != null) {
            this.issues.stream().forEach(jiraIssue -> newJiraIssues.add(new JiraIssue(jiraIssue.getId(),
                                                                                      jiraIssue.getKey(),
                                                                                      jiraIssue.getFields())));
        }
        return new JiraSearch(this.startAt, this.maxResults, this.total, newJiraIssues);
    }
}
