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



import org.inugami.api.models.data.JsonObject;

import flexjson.JSONSerializer;



public class OpsgenieModel implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -3739397136756114790L;
    
    private String                  message;
    
    private String                  alias;
    
    private String                  description;


    
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
        builder.append("]");

        return builder.toString();
    }


    @Override
    public String convertToJson() {
        //@formatter:off
        return new JSONSerializer().exclude("*.class").deepSerialize(this);
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

}
