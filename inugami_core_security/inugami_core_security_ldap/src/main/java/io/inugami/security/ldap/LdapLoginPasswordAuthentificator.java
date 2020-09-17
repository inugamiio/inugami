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

import io.inugami.api.loggers.Loggers;
import io.inugami.configuration.models.app.SecurityConfiguration;
import io.inugami.core.security.commons.exceptions.SecurityException;
import io.inugami.core.security.commons.services.LoginPasswordAuthentificator;
import io.inugami.security.ldap.mapper.LdapMapper;
import org.picketlink.idm.credential.Credentials;
import org.picketlink.idm.credential.Credentials.Status;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.UsernamePasswordCredentials;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.User;

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
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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

    private final LdapBuilders builder = new LdapBuilders();

    private final LdapRolesFinder rolesFinder = new LdapRolesFinder();

    private LdapConnectorData connectorData;

    private SecurityConfiguration securityConfig;

    private LdapMapper mapper;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @PostConstruct
    private void init() {
        mapper         = ldapConfiguration.getMapper();
        connectorData  = ldapConfiguration.getConnectorData();
        securityConfig = ldapConfiguration.getSecurityConfig();
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public Credentials authentificate(final String login, final Password password) {
        Credentials result  = null;
        DirContext  context = null;
        try {
            context = connect(login, password);
            result  = loadUser(login, password, context);
        }
        catch (final SecurityException e) {
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
    protected Credentials loadUser(final String login, final Password password,
                                   final DirContext context) throws SecurityException {
        final SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        final List<SearchResult> userInformations = search(context, searchControls, connectorData.getRootDn(),
                                                           connectorData.getSearchFilter(), new Object[]{login});
        if (userInformations == null || userInformations.isEmpty() || userInformations.size() > 1) {
            throw new SecurityException("error on loading user information (" + login + ")");
        }

        final SearchResult userInfo   = userInformations.get(0);
        final Name         dn         = buildLdapDn(userInfo);
        final List<Object> userGroups = loadGroups(context, dn);

        final Account                     account = buildAccount(login, userInfo, userGroups);
        final UsernamePasswordCredentials result  = new UsernamePasswordCredentials(login, password);
        result.setValidatedAccount(account);
        result.setStatus(Status.VALID);
        return result;
    }

    protected List<Object> loadGroups(final DirContext context, final Name dn) throws SecurityException {
        final List<Object> result = new ArrayList<>();

        final SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        final String searchFilter = MessageFormat.format(connectorData.getRoleSearch(), dn);
        final Name   searchBase   = dn.getPrefix(2);

        final NamingEnumeration<SearchResult> answer;
        try {
            answer = context.search(searchBase, searchFilter, searchCtls);
            while (answer.hasMoreElements()) {
                final SearchResult sr = answer.next();
                result.add(sr.getNameInNamespace());
            }
        }
        catch (final NamingException e) {
            throw new SecurityException(e.getMessage(), e);
        }

        return result;
    }

    // =========================================================================
    // BUILD ACCOUNT
    // =========================================================================
    protected Account buildAccount(final String login, final SearchResult userInfo, final List<Object> userGroups) {
        final User result = mapper.mapping(userInfo);
        result.setLoginName(login);

        // Attribute must be serializable...
        final ArrayList<String> groups = new ArrayList<>();
        userGroups.stream().map(String::valueOf).forEach(groups::add);
        result.setAttribute(new Attribute<ArrayList<String>>("groups", groups, true));

        final List<String> roles = rolesFinder.find(groups, securityConfig.getRoles());
        result.setAttribute(new Attribute<ArrayList<String>>("roles", (ArrayList<String>) roles, true));
        return result;
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private DirContext connect(final String login, final Password password) {
        final Hashtable<String, String> connectorCtx = builder.buildUserBinding(login, new String(password.getValue()),
                                                                                connectorData);
        return new LdapConnector().connect(connectorCtx);
    }

    private void close(final DirContext context) {
        if (context != null) {
            try {
                context.close();
            }
            catch (final NamingException e) {
                Loggers.DEBUG.error(e.getMessage(), e);
            }
        }
    }

    private List<SearchResult> search(final DirContext context, final SearchControls searchControls,
                                      final String rootDn,
                                      final String searchFilter, final Object[] objects) throws SecurityException {
        NamingEnumeration<SearchResult> searchResult = null;
        final List<SearchResult>        result       = new ArrayList<>();
        try {
            searchResult = context.search(rootDn, searchFilter, objects, buildControls(searchControls));
            while (searchResult.hasMoreElements()) {
                final SearchResult itemResult = (SearchResult) searchResult.nextElement();
                result.add(itemResult);
            }
        }
        catch (final NamingException e) {
            throw new SecurityException(e.getMessage(), e);
        }
        closeEnumeration(searchResult);

        return result;
    }

    private SearchControls buildControls(final SearchControls originalControls) {
        //@formatter:off
        return new SearchControls(originalControls.getSearchScope(),
                                  originalControls.getCountLimit(),
                                  originalControls.getTimeLimit(),
                                  originalControls.getReturningAttributes(),
                                  true,
                                  originalControls.getDerefLinkFlag());
        //@formatter:on
    }

    private void closeEnumeration(final NamingEnumeration<?> ne) {
        try {
            if (ne != null) {
                ne.close();
            }
        }
        catch (final NamingException e) {
            if (Loggers.SECURITY.isDebugEnabled()) {
                Loggers.SECURITY.error("Failed to close enumeration.", e);
            }
        }
    }

    @SuppressWarnings("restriction")
    private Name buildLdapDn(final SearchResult userInfo) {

        final String namespace = invoke(userInfo.getObject(), "getNameInNamespace()");
        Name         result    = null;
        try {
            result = new LdapName(namespace);
        }
        catch (final InvalidNameException e) {
            if (Loggers.SECURITY.isDebugEnabled()) {
                Loggers.SECURITY.error(e.getMessage(), e);
            }
        }
        return result;
    }

    private String invoke(final Object object, final String methodName) {
        Object rawResult = null;
        Method method    = null;

        if (object != null) {
            try {
                method = object.getClass().getDeclaredMethod(methodName);
            }
            catch (final NoSuchMethodException e) {
                Loggers.DEBUG.error(e.getMessage(), e);
            }
        }

        if (method != null) {
            try {
                method.setAccessible(true);
                rawResult = method.invoke(object);
            }
            catch (final Exception e) {
                Loggers.DEBUG.error(e.getMessage(), e);
            }
        }
        return rawResult == null ? null : String.valueOf(rawResult);
    }

}
