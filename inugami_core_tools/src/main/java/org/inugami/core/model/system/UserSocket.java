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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.inugami.api.models.data.basic.JsonObject;

import flexjson.JSON;

/**
 * UserSocket
 * 
 * @author patrickguillerm
 * @since 4 oct. 2018
 */
public class UserSocket implements JsonObject {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long          serialVersionUID = -15027890804849413L;
    
    private String                     login;
    
    private final List<UserConnection> connections      = new ArrayList<>();
    
    @JSON(transformer = IsoDateTransformer.class)
    private Date                       lastConnection;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public UserSocket() {
        super();
    }
    
    public UserSocket(final String login) {
        super();
        this.login = login;
        lastConnection = new Date(System.currentTimeMillis());
    }
    
    public UserSocket(final String login, final Date lastConnection, final List<UserConnection> connections) {
        super();
        this.login = login;
        this.lastConnection = lastConnection;
        if (connections != null) {
            this.connections.addAll(connections);
        }
    }
    
    @Override
    public UserSocket cloneObj() {
        final List<UserConnection> newConnections = new ArrayList<>();
        if (connections != null) {
            for (final UserConnection item : connections) {
                newConnections.add(item.cloneObj());
            }
        }
        
        Date newLastConnection = null;
        if (lastConnection != null) {
            newLastConnection = new Date(lastConnection.getTime());
        }
        
        return new UserSocket(login, newLastConnection, newConnections);
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public int hashCode() {
        return 31 * login.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof UserSocket)) {
            final UserSocket other = (UserSocket) obj;
            result = login.equals(other.login);
        }
        
        return result;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public void newConnexion(final UserConnection userConnection) {
        connections.add(userConnection);
        updateLastConnection();
    }
    
    public void removeConnection(final String uid) {
        final UserConnection connectionToDelete = null;
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).getUid().equals(uid)) {
                connections.remove(i);
                break;
            }
        }
        
    }
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(final String login) {
        this.login = login;
    }
    
    public Date getLastConnection() {
        return lastConnection;
    }
    
    public void updateLastConnection() {
        lastConnection = new Date(System.currentTimeMillis());
    }
    
    public List<UserConnection> getConnections() {
        return connections;
    }
    
    public void setLastConnection(final Date lastConnection) {
        this.lastConnection = lastConnection;
    }
    
}
