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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.data.JsonObject;

import flexjson.JSONDeserializer;
import flexjson.JSONException;

public class GitlabMergeRequests implements JsonObject {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long        serialVersionUID = 7120692619596049005L;
    
    private List<GitlabMergeRequest> mergeRequests;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public GitlabMergeRequests() {
    }
    
    public GitlabMergeRequests(final List<GitlabMergeRequest> mergeRequests) {
        this.mergeRequests = mergeRequests;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public <T extends JsonObject> T convertToObject(final byte[] data, final Charset charset) {
        GitlabMergeRequests result = null;
        List<GitlabMergeRequest> mergeRequests;
        
        if (data != null) {
            final String json = charset == null ? new String(data).trim() : new String(data, charset).trim();
            
            if (!json.isEmpty()) {
                try {
                    mergeRequests = new JSONDeserializer<List<GitlabMergeRequest>>().use(null,
                                                                                         ArrayList.class).use("values",
                                                                                                              GitlabMergeRequest.class).deserialize(json);
                    
                    result = new GitlabMergeRequests(mergeRequests);
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
    public List<GitlabMergeRequest> getMergeRequests() {
        return mergeRequests;
    }
    
    public void setMergeRequests(final List<GitlabMergeRequest> mergeRequests) {
        this.mergeRequests = mergeRequests;
    }
    
    // =========================================================================
    // CLONE
    // =========================================================================
    @Override
    public JsonObject cloneObj() {
        final List<GitlabMergeRequest> newMergeRequests = new ArrayList<>();
        if (this.mergeRequests != null) {
            for (final GitlabMergeRequest item : this.mergeRequests) {
                MergeAuthor newAuthor = null;
                if (item.getAuthor() != null) {
                    newAuthor = new MergeAuthor(item.getAuthor().getName(), item.getAuthor().getUsername(),
                                                item.getAuthor().getId());
                }
                
                newMergeRequests.add(new GitlabMergeRequest(item.getCreationDate(), item.getProjectId(),
                                                            item.getProjectName(), item.getId(), item.getTitle(),
                                                            item.getState(), item.getUpvotes(), item.getDownvotes(),
                                                            newAuthor));
            }
        }
        return new GitlabMergeRequests(newMergeRequests);
    }
}
