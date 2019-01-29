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
package org.inugami.security.ldap;

import java.util.Hashtable;
import static javax.naming.Context.SECURITY_PRINCIPAL;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.inugami.api.loggers.Loggers;

/**
 * LdapConnector
 * 
 * @author patrick_guillerm
 * @since 13 déc. 2017
 */
public class LdapConnector {
    

    // =========================================================================
    // METHODS
    // =========================================================================
    public DirContext connect(Hashtable<String, String> context) throws SecurityException {
        DirContext result = null;
        try {
            result = new InitialDirContext(context);
        }
        catch (NamingException e) {
            if ((e instanceof javax.naming.AuthenticationException) || (e instanceof OperationNotSupportedException)) {
                handleBindException(context, e);
            }
            else {
                throw new SecurityException(e.getMessage(), e);
            }
        }
        catch (Exception e) {
            throw new SecurityException(e.getMessage(), e);
        }
        return result;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    private void handleBindException(Hashtable<String, String> context, NamingException e) {
        final String bindPrincipal = context.get(SECURITY_PRINCIPAL);
        Loggers.SECURITY.error("can't authentify user {}", bindPrincipal);
        
    }
}
