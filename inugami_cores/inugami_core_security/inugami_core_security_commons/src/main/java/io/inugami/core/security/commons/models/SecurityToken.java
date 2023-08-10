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
package io.inugami.core.security.commons.models;

import org.picketlink.idm.credential.AbstractBaseCredentials;
import org.picketlink.idm.credential.Token;

/**
 * SecurityToken
 * 
 * @author patrick_guillerm
 * @since 11 déc. 2017
 */
public class SecurityToken extends AbstractBaseCredentials implements Token {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private String token;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SecurityToken(String token) {
        super();
        this.token = token;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public void invalidate() {
        this.token = null;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getToken() {
        return this.token;
    }
    
    @Override
    public String getType() {
        return null;
    }
    
    @Override
    public String getSubject() {
        return null;
    }
}
