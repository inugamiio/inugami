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

import flexjson.JSON;

public class GitlabMergeRequest {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @JSON(name = "created_at")
    private String      creationDate;
    
    @JSON(name = "project_id")
    private int         projectId;
    
    private String      projectName;
    
    private int         id;
    
    private String      title;
    
    private String      state;
    
    private int         upvotes;
    
    private int         downvotes;
    
    private MergeAuthor author;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public GitlabMergeRequest() {
    }
    
    public GitlabMergeRequest(final String creationDate, final int projectId, final String projectName, final int id,
                              final String title, final String state, final int upvotes, final int downvotes,
                              final MergeAuthor author) {
        this.creationDate = creationDate;
        this.projectId = projectId;
        this.projectName = projectName;
        this.id = id;
        this.title = title;
        this.state = state;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.author = author;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(final String creationDate) {
        this.creationDate = creationDate;
    }
    
    public int getProjectId() {
        return projectId;
    }
    
    public void setProjectId(final int projectId) {
        this.projectId = projectId;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(final String state) {
        this.state = state;
    }
    
    public int getUpvotes() {
        return upvotes;
    }
    
    public void setUpvotes(final int upvotes) {
        this.upvotes = upvotes;
    }
    
    public int getDownvotes() {
        return downvotes;
    }
    
    public void setDownvotes(final int downvotes) {
        this.downvotes = downvotes;
    }
    
    public MergeAuthor getAuthor() {
        return author;
    }
    
    public void setAuthor(final MergeAuthor author) {
        this.author = author;
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
        if (!result && (other instanceof GitlabMergeRequest)) {
            final GitlabMergeRequest obj = (GitlabMergeRequest) other;
            result = this.id == obj.getId();
        }
        return result;
    }
}
