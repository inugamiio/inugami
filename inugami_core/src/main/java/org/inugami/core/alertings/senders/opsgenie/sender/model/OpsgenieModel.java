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
package org.inugami.core.alertings.senders.opsgenie.sender.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.inugami.api.models.data.JsonObject;

import flexjson.JSONSerializer;
import org.inugami.core.alertings.senders.opsgenie.sender.model.responder.*;
import org.inugami.core.alertings.senders.opsgenie.sender.model.visibleto.*;


public class OpsgenieModel implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -3739397136756114790L;
    
    private String                  message;
    
    private String                  alias;
    
    private String                  description;
    
    private String                  priority;

    private String                  entity;

    private List<String>            actions;

    private List<String>            tags;

    private List<Responder>         responders;

    private List<VisibleTo>         visibleTo;

    private Map<String,String>      details;

    private String                  source;

    private String                  user;

    private String                  note;


    

    
    // =========================================================================
    // OVERRIDES
    // =========================================================================


    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("OpsgenieModel [message=");
        builder.append(message);
        builder.append(",alias=");
        builder.append(alias);
        builder.append(",description=");
        builder.append(description);
        builder.append(",priority=");
        builder.append(priority);
        builder.append(",entity=");
        builder.append(entity);
        builder.append(",action=");
        builder.append(actions);
        builder.append(",tags=");
        builder.append(tags);
        builder.append(",responders=");
        builder.append(responders);
        builder.append(",visibleTo=");
        builder.append(visibleTo);
        builder.append(",details=");
        builder.append(details);
        builder.append(",source");
        builder.append(source);
        builder.append(",user=");
        builder.append(user);
        builder.append(",note=");
        builder.append(note);
        builder.append("]");

        return builder.toString();
    }


    @Override
    public String convertToJson() {
        //@formatter:off
        return new JSONSerializer().exclude("*.class")
                                   .transform(new TeamVisibleToTransformer(), TeamVisibleTo.class)
                                   .transform(new UserVisibleToTransformer(), UserVisibleTo.class)
                                   .transform(new OtherResponderTransformer(), OtherResponder.class)
                                   .transform(new UserResponderTransformer(), UserResponder.class)
                                   .deepSerialize(this);
        //@formatter:on
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Responder> getResponders() {
        return responders;
    }

    public void setResponders(List<Responder> responders) {
        this.responders = responders;
    }

    public List<VisibleTo> getVisibleTo() {
        return visibleTo;
    }

    public void setVisibleTo(List<VisibleTo> visibleTo) {
        this.visibleTo = visibleTo;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
