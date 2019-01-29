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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapName;

import org.inugami.api.loggers.Loggers;
import org.inugami.configuration.models.app.SecurityConfiguration;
import org.inugami.core.security.commons.exceptions.SecurityException;
import org.inugami.core.security.commons.services.LoginPasswordAuthentificator;
import org.inugami.security.ldap.mapper.LdapMapper;
import org.picketlink.idm.credential.Credentials;
import org.picketlink.idm.credential.Credentials.Status;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.UsernamePasswordCredentials;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.User;

/**
 * LdapLoginPasswordAuthentificator
 * 
 * @author patrick_guillerm
 * @since 13 déc. 2017
 */
@Priority(100)
public class LdapLoginPasswordAuthentificator implements LoginPasswordAuthentificator {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    
    @Inject
    private LdapConfigurationProvider ldapConfiguration;
    
    private LdapBuilders              builder     = new LdapBuilders();
    
    private LdapRolesFinder           rolesFinder = new LdapRolesFinder();
    
    private LdapConnectorData         connectorData;
    
    private SecurityConfiguration     securityConfig;
    
    private LdapMapper                mapper;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @PostConstruct
    private void init() {
        mapper = ldapConfiguration.getMapper();
        connectorData = ldapConfiguration.getConnectorData();
        securityConfig = ldapConfiguration.getSecurityConfig();
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public Credentials authentificate(String login, Password password) {
        Credentials result = null;
        DirContext context = null;
        try {
            context = connect(login, password);
            result = loadUser(login, password, context);
        }
        catch (SecurityException e) {
            Loggers.SECURITY.error(e.getMessage());
        }
        finally {
            close(context);
        }
        return result;
    }
    
    // =========================================================================
    // LOAD USER
    // =========================================================================
    protected Credentials loadUser(String login, Password password, DirContext context) throws SecurityException {
        final SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<SearchResult> userInformations = search(context, searchControls, connectorData.getRootDn(),
                                                     connectorData.getSearchFilter(), new Object[] { login });
        if (userInformations == null || userInformations.isEmpty() || userInformations.size() > 1) {
            throw new SecurityException("error on loading user information (" + login + ")");
        }
        
        final SearchResult userInfo = userInformations.get(0);
        final Name dn = buildLdapDn(userInfo);
        final List<Object> userGroups = loadGroups(context, dn);
        
        final Account account = buildAccount(login, userInfo, userGroups);
        UsernamePasswordCredentials result = new UsernamePasswordCredentials(login, password);
        result.setValidatedAccount(account);
        result.setStatus(Status.VALID);
        return result;
    }
    
    protected List<Object> loadGroups(DirContext context, Name dn) throws SecurityException {
        final List<Object> result = new ArrayList<>();
        
        final SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        
        final String searchFilter = MessageFormat.format(connectorData.getRoleSearch(), dn);
        final Name searchBase = dn.getPrefix(2);
        
        NamingEnumeration<SearchResult> answer;
        try {
            answer = context.search(searchBase, searchFilter, searchCtls);
            while (answer.hasMoreElements()) {
                SearchResult sr = answer.next();
                result.add(sr.getNameInNamespace());
            }
        }
        catch (NamingException e) {
            throw new SecurityException(e.getMessage(), e);
        }
        
        return result;
    }
    
    // =========================================================================
    // BUILD ACCOUNT
    // =========================================================================
    protected Account buildAccount(String login, SearchResult userInfo, List<Object> userGroups) {
        final User result = mapper.mapping(userInfo);
        result.setLoginName(login);
        
        // Attribute must be serializable...
        ArrayList<String> groups = new ArrayList<>();
        userGroups.stream().map(String::valueOf).forEach(groups::add);
        result.setAttribute(new Attribute<ArrayList<String>>("groups", groups, true));
        
        List<String> roles = rolesFinder.find(groups, securityConfig.getRoles());
        result.setAttribute(new Attribute<ArrayList<String>>("roles", (ArrayList<String>) roles, true));
        return result;
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private DirContext connect(String login, Password password) {
        final Hashtable<String, String> connectorCtx = builder.buildUserBinding(login, new String(password.getValue()),
                                                                                connectorData);
        return new LdapConnector().connect(connectorCtx);
    }
    
    private void close(DirContext context) {
        if (context != null) {
            try {
                context.close();
            }
            catch (NamingException e) {
                Loggers.DEBUG.error(e.getMessage(), e);
            }
        }
    }
    
    private List<SearchResult> search(DirContext context, SearchControls searchControls, String rootDn,
                                      String searchFilter, Object[] objects) throws SecurityException {
        NamingEnumeration<SearchResult> searchResult = null;
        List<SearchResult> result = new ArrayList<>();
        try {
            searchResult = context.search(rootDn, searchFilter, objects, buildControls(searchControls));
            while (searchResult.hasMoreElements()) {
                SearchResult itemResult = (SearchResult) searchResult.nextElement();
                result.add(itemResult);
            }
        }
        catch (NamingException e) {
            throw new SecurityException(e.getMessage(), e);
        }
        closeEnumeration(searchResult);
        
        return result;
    }
    
    private SearchControls buildControls(SearchControls originalControls) {
        //@formatter:off
        return new SearchControls(originalControls.getSearchScope(),
                                  originalControls.getCountLimit(),
                                  originalControls.getTimeLimit(),
                                  originalControls.getReturningAttributes(),
                                  true,
                                  originalControls.getDerefLinkFlag());
        //@formatter:on
    }
    
    private void closeEnumeration(NamingEnumeration<?> ne) {
        try {
            if (ne != null) {
                ne.close();
            }
        }
        catch (NamingException e) {
            if (Loggers.SECURITY.isDebugEnabled()) {
                Loggers.SECURITY.error("Failed to close enumeration.", e);
            }
        }
    }
    
    @SuppressWarnings("restriction")
    private Name buildLdapDn(SearchResult userInfo) {
        String namespace = ((com.sun.jndi.ldap.LdapCtx) userInfo.getObject()).getNameInNamespace();
        Name result = null;
        try {
            result = new LdapName(namespace);
        }
        catch (InvalidNameException e) {
            if (Loggers.SECURITY.isDebugEnabled()) {
                Loggers.SECURITY.error(e.getMessage(), e);
            }
        }
        return result;
    }
    
}
