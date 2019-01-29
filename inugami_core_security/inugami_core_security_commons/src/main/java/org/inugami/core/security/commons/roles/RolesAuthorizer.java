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
package org.inugami.core.security.commons.roles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.ws.rs.ForbiddenException;

import org.apache.deltaspike.security.api.authorization.Secures;
import org.inugami.api.loggers.Loggers;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.Attribute;

/**
 * RolesAuthorizer
 * 
 * @author patrick_guillerm
 * @since 8 déc. 2017
 */
@Named
@ApplicationScoped
public class RolesAuthorizer {
    
    // =========================================================================
    // ASSERTS
    // =========================================================================
    public void assertUserConnected(Identity identity) {
        if (identity == null) {
            throw new SecurityException("identification is require!");
        }
        
        if (!identity.isLoggedIn()) {
            throw new SecurityException("identification is require!");
        }
        
    }
    
    public void assertIsAdmin(Identity identity) {
        assertUserConnected(identity);
        boolean isAdmin = false;
        try {
            isAdmin = doAdminCheck(identity, null, null);
        }
        catch (Exception e) {
            Loggers.DEBUG.debug(e.getMessage(), e);
        }
        
        if (!isAdmin) {
            throw new ForbiddenException("Forbidden access!");
        }
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Secures
    @Admin
    public boolean doAdminCheck(Identity identity, IdentityManager identityManager,
                                RelationshipManager relationshipManager) throws Exception {
        return hasRole(relationshipManager, identity.getAccount(), identityManager, Admin.ROLE);
    }
    
    @Secures
    @UserConnected
    public boolean doUserCheck(Identity identity, IdentityManager identityManager,
                               RelationshipManager relationshipManager) throws Exception {
        return hasRole(relationshipManager, identity.getAccount(), identityManager, UserConnected.ROLE);
    }
    
    // =========================================================================
    // CHECKS
    // =========================================================================
    
    private boolean hasRole(RelationshipManager relationshipManager, Account account, IdentityManager identityManager,
                            String role) {
        final Attribute<Serializable> attribute = account.getAttribute("roles");
        
        // @formatter:off
        final List<String> rolesRaw = attribute != null && attribute.getValue() instanceof List<?>
                                    ? (List<String>) attribute.getValue()
                                    : new ArrayList<>();
        // @formatter:off
        final List<String> roles = rolesRaw.stream().map(String::toLowerCase).collect(Collectors.toList());
        return roles.contains(role);
    }
}
