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
package io.inugami.core.alertings.senders.slack.sender;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SlackModel
 * 
 * @author patrick_guillerm
 * @since 20 mars 2018
 */
public class SlackComplexModel extends AbstractSlackModel implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long     serialVersionUID = -2957530797423599471L;
    
    private List<SlackAttachment> attachments;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SlackComplexModel() {
    }
    
    public SlackComplexModel(final SlackAttachment attachment) {
        this.attachments = new ArrayList<>();
        if (attachment != null) {
            this.attachments.add(attachment);
        }
    }
    
    public SlackComplexModel(final String username, final String channel, final List<SlackAttachment> attachments) {
        super(username, channel);
        this.attachments = attachments;
    }
    
    // =========================================================================
    // CLONE
    // =========================================================================
    @Override
    public AbstractSlackModel clone(final String channel) {
        final List<SlackAttachment> newAttachments = new ArrayList<>();
        if (attachments != null) {
            for (final SlackAttachment item : attachments) {
                newAttachments.add(item.cloneObject());
            }
        }
        
        return new SlackComplexModel(getUsername(), channel, newAttachments);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public void childrenToString(final StringBuilder builder) {
        builder.append(", attachments=").append(attachments);
    }
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public List<SlackAttachment> getAttachments() {
        return attachments;
    }
    
    public void setAttachments(final List<SlackAttachment> attachments) {
        this.attachments = attachments;
    }
    
}
