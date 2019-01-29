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
package org.inugami.core.security.commons.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.inugami.commons.security.SecurityTools;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.User;

/**
 * UserModel
 * 
 * @author patrickguillerm
 * @since 10 déc. 2017
 */
public class UserModel implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 5738879239605508987L;
    
    private final String token;
    
    private String loginName;
    
    private String firstName;
    
    private String lastName;
    
    private String id;
    
    private List<String> roles = new ArrayList<>();
    
    private int level = -1;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public UserModel(String token, Account user) {
        this.token = token;
        
        if (user instanceof User) {
            final User userObj = (User) user;
            this.loginName = userObj.getLoginName();
            this.firstName = userObj.getFirstName();
            this.lastName = userObj.getLastName();
        }
        
        this.id = SecurityTools.escapeJavaScriptAndHtml(user.getId());
        
        Attribute<Serializable> attrRoles = user.getAttribute("roles");
        this.roles = attrRoles == null ? null : (List<String>) attrRoles.getValue();
    }
    
    // =========================================================================
    // OVERRIDE
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((loginName == null) ? 0 : loginName.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;
        if (!result && obj != null && obj instanceof UserModel) {
            final UserModel other = (UserModel) obj;
            result = loginName == null ? other.getLoginName() == null : loginName.equals(other.getLoginName());
        }
        return result;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserModel [loginName=");
        builder.append(loginName);
        builder.append(", firsName=");
        builder.append(firstName);
        builder.append(", firstName=");
        builder.append(lastName);
        builder.append(", id=");
        builder.append(id);
        builder.append(", roles=");
        builder.append(roles);
        builder.append(", level=");
        builder.append(level);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getLoginName() {
        return loginName;
    }
    
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public List<String> getRoles() {
        return roles;
    }
    
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public String getToken() {
        return token;
    }
    
}
