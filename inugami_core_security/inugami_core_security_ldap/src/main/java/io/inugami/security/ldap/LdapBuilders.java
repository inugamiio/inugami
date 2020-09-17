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
package io.inugami.security.ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import io.inugami.api.exceptions.FatalException;

/**
 * LdapBuilders
 * 
 * @author patrick_guillerm
 * @since 13 déc. 2017
 */
public class LdapBuilders {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public Hashtable<String, String> buildUserBinding(final String username, final String password,
                                                      final LdapConnectorData connectorData) {
        final Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, createBindPrincipal(username, connectorData.getDomain()));
        env.put(Context.PROVIDER_URL, connectorData.getUrl());
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put("com.sun.jndi.ldap.read.timeout", String.valueOf(connectorData.getTimeout()));
        connectorData.getReferral().ifPresent(referral -> env.put(Context.REFERRAL, referral));
        return env;
    }
    
    String createBindPrincipal(final String username, final String domain) {
        if (domain == null || username.toLowerCase().startsWith(domain)) {
            return username;
        }
        return buildDomain(domain) + username;
    }
    
    private String buildDomain(final String domain) {
        return domain.endsWith("\\") ? domain : domain + "\\";
    }
    
    // =========================================================================
    // GETTERS
    // =========================================================================
    String getPrincipalName(final DirContext context) {
        try {
            return (String) context.getEnvironment().get(Context.SECURITY_PRINCIPAL);
        }
        catch (final NamingException e) {
            throw new FatalException(e.getMessage(), e);
        }
    }
    
}
