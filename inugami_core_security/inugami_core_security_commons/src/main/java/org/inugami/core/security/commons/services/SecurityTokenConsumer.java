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
package org.inugami.core.security.commons.services;

import org.inugami.core.security.commons.models.SecurityToken;
import org.picketlink.idm.credential.Token;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.annotation.StereotypeProperty.Property;

/**
 * SecurityTokenConsumer
 * 
 * @author patrick_guillerm
 * @since 12 déc. 2017
 */
public class SecurityTokenConsumer implements Token.Consumer<SecurityToken> {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public <I extends IdentityType> I extractIdentity(SecurityToken token, Class<I> identityType,
            Property stereotypeProperty, Object identifier) {
        // unused
        return null;
    }

    @Override
    public boolean validate(SecurityToken token) {
        // unused
        return false;
    }

    @Override
    public Class<SecurityToken> getTokenType() {
        return SecurityToken.class;
    }

}
