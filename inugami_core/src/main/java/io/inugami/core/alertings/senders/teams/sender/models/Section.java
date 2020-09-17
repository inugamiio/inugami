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
package io.inugami.core.alertings.senders.teams.sender.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Section
 * 
 * @author patrick_guillerm
 * @since 21 août 2018
 */
public class Section implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 456341419938515650L;
    
    private String            activityTitle;
    
    private String            activitySubtitle;
    
    private String            activityImage;
    
    private List<Facts>       facts;
    
    private boolean           markdown;
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((activityTitle == null) ? 0 : activityTitle.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof Section)) {
            final Section other = (Section) obj;
            result = activityTitle == null ? other.getActivityTitle() == null
                                           : activityTitle.equals(other.getActivityTitle());
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Section [activityTitle=");
        builder.append(activityTitle);
        builder.append(", activitySubtitle=");
        builder.append(activitySubtitle);
        builder.append(", activityImage=");
        builder.append(activityImage);
        builder.append(", facts=");
        builder.append(facts);
        builder.append(", markdown=");
        builder.append(markdown);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getActivityTitle() {
        return activityTitle;
    }
    
    public void setActivityTitle(final String activityTitle) {
        this.activityTitle = activityTitle;
    }
    
    public String getActivitySubtitle() {
        return activitySubtitle;
    }
    
    public void setActivitySubtitle(final String activitySubtitle) {
        this.activitySubtitle = activitySubtitle;
    }
    
    public String getActivityImage() {
        return activityImage;
    }
    
    public void setActivityImage(final String activityImage) {
        this.activityImage = activityImage;
    }
    
    public List<Facts> getFacts() {
        return facts;
    }
    
    public void setFacts(final List<Facts> facts) {
        this.facts = facts;
    }
    
    public void addFact(final Facts fact) {
        if (facts == null) {
            facts = new ArrayList<>();
        }
        if (fact != null) {
            facts.add(fact);
        }
    }
    
    public boolean isMarkdown() {
        return markdown;
    }
    
    public void setMarkdown(final boolean markdown) {
        this.markdown = markdown;
    }
    
}
