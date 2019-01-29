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
package org.inugami.core.model.system;

import java.util.Date;

import org.inugami.api.models.data.JsonObject;
import org.inugami.commons.security.EncryptionUtils;

import flexjson.JSON;

/**
 * UserConnexion
 * 
 * @author patrickguillerm
 * @since 4 oct. 2018
 */
public class UserConnection implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -785196215806520832L;
    
    private final String      login;
    
    private final String      ip;
    
    private final String      agents;
    
    private final String      uid;
    
    @JSON(transformer = IsoDateTransformer.class)
    private Date              date;
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    public UserConnection(final String login, final String ip, final String agents, final String uuid) {
        this.ip = ip;
        this.agents = agents;
        //@formatter:off
        this.uid = new EncryptionUtils().encodeSha1(
                            String.join("_", login,
                                             ip,
                                             agents,
                                             uuid)
                   );
        //@formatter:on
        this.login = login;
        this.date = new Date();
    }
    
    public UserConnection(final String login, final String ip, final String agents, final String uid, final Date date) {
        super();
        this.login = login;
        this.ip = ip;
        this.agents = agents;
        this.uid = uid;
        this.date = date;
    }
    
    @Override
    public UserConnection cloneObj() {
        Date newDate = null;
        if (date != null) {
            newDate = new Date(date.getTime());
        }
        return new UserConnection(login, ip, agents, uid, newDate);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        return uid.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof UserConnection)) {
            final UserConnection other = (UserConnection) obj;
            result = uid.equals(other.uid);
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        return convertToJson();
    }
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getIp() {
        return ip;
    }
    
    public String getAgents() {
        return agents;
    }
    
    public String getUid() {
        return uid;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(final Date date) {
        this.date = date;
    }
    
    public String getLogin() {
        return login;
    }
    
}
