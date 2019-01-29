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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringEscapeUtils;
import org.inugami.api.constants.JvmKeyValues;
import org.inugami.commons.files.FilesUtils;
import org.inugami.commons.security.TokenBuilder;
import org.inugami.configuration.models.app.SecurityConfiguration;
import org.inugami.configuration.models.app.UserConfig;
import org.inugami.core.context.ApplicationContext;
import org.inugami.core.context.Context;
import org.inugami.core.security.commons.models.SecurityContext;
import org.inugami.core.security.commons.services.producers.TechnicalAccountInitializer;
import org.picketlink.authentication.Authenticator.AuthenticationStatus;
import org.picketlink.idm.credential.Credentials;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.Attribute;

/**
 * SecurityTokenService
 * 
 * @author patrick_guillerm
 * @since 11 déc. 2017
 */
@Named
@ApplicationScoped
public class SecurityTokenService implements TechnicalAccountInitializer {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    //@formatter:off
    private final static File HOME       = Context.getInstance().getHome();
    private final static File FILE_TOKEN = buildFileToken("users.token",JvmKeyValues.SECURITY_FILE_TOKEN);
    private final static File FILE_USERS = buildFileToken("users.refs",JvmKeyValues.SECURITY_FILE_USER);
    
    private final static Map<String, String>  TOKEN_REFS = initMap(FILE_TOKEN);
    private final static Map<String, Account> USERS_REFS = FilesUtils.readFromBinary(FILE_USERS,new ConcurrentHashMap<>());
    //@formatter:on
    
    private final TokenBuilder        tokenBuilder = new TokenBuilder();
    
    @Inject
    private transient SecurityContext securityContext;
    
    @Inject
    private ApplicationContext        appContext;
    
    private static File buildFileToken(final String defaultFileName, final JvmKeyValues jvmConfig) {
        File result = FilesUtils.buildFile(HOME, defaultFileName + ".bin");
        final String config = jvmConfig.get();
        if (config != null) {
            result = new File(config);
        }
        
        final File folder = result.getParentFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return result;
    }
    
    private static Map<String, String> initMap(final File fileToken) {
        final Map<String, String> result = FilesUtils.readFromBinary(fileToken, new ConcurrentHashMap<>());
        return result;
    }
    
    // =========================================================================
    // METHODS IDENTIFY & REGISTER
    // =========================================================================
    public AuthenticationStatus identify(final String token) {
        boolean result = TOKEN_REFS.containsKey(token);
        if (result) {
            final String login = TOKEN_REFS.get(token);
            
            final String serurityToken = buildSecurityToken(login, securityContext);
            result = token.startsWith(tokenBuilder.buildUserToken(login, serurityToken));
        }
        
        return result ? AuthenticationStatus.SUCCESS : AuthenticationStatus.FAILURE;
    }
    
    public void logout(final String token) {
        final AuthenticationStatus status = identify(token);
        if (AuthenticationStatus.SUCCESS == status) {
            final String login = TOKEN_REFS.get(token);
            TOKEN_REFS.remove(token);
            
            if (hasNoMoreTokenForLogin(login)) {
                USERS_REFS.remove(login);
            }
            backupFiles();
        }
    }
    
    public void register(final String login, final Account account, final String token) {
        processRegister(login, token, account);
    }
    
    public String register(final String loginName, final Credentials credentials) {
        final String serurityToken = buildSecurityToken(loginName, securityContext);
        final String token = tokenBuilder.buildToken(loginName, serurityToken);
        
        if (!appContext.isTechnicalUser(loginName)) {
            final Account account = credentials.getValidatedAccount();
            processRegister(loginName, token, account);
        }
        else {
            for (final Map.Entry<String, String> entry : TOKEN_REFS.entrySet()) {
                if (entry.getValue().equals(loginName)) {
                    TOKEN_REFS.put(token, loginName);
                    USERS_REFS.put(loginName, credentials.getValidatedAccount());
                    backupFiles();
                    break;
                }
            }
        }
        return StringEscapeUtils.escapeJavaScript(token);
    }
    
    private synchronized void processRegister(final String loginName, final String token, final Account account) {
        TOKEN_REFS.put(token, loginName);
        account.setAttribute(new Attribute<Serializable>("groups", new ArrayList<>()));
        USERS_REFS.put(loginName, account);
        backupFiles();
    }
    
    private void backupFiles() {
        FilesUtils.writeToBinary(FILE_USERS, (ConcurrentHashMap<String, Account>) USERS_REFS);
        FilesUtils.writeToBinary(FILE_TOKEN, (ConcurrentHashMap<String, String>) TOKEN_REFS);
    }
    
    public Account getUser(final String token) {
        final String login = TOKEN_REFS.get(token);
        return login == null ? null : USERS_REFS.get(login);
    }
    
    private boolean hasNoMoreTokenForLogin(final String login) {
        boolean found = false;
        for (final Entry<String, String> entry : TOKEN_REFS.entrySet()) {
            found = login.equals(entry.getValue());
            if (found) {
                break;
            }
        }
        return !found;
    }
    
    private String buildSecurityToken(final String login, final SecurityContext context) {
        return isTechnicalUser(login) ? TokenBuilder.TECHNICAL_CONTEXT : context.convertToJson();
    }
    
    private boolean isTechnicalUser(final String login) {
        //@formatter:off
        return appContext.getApplicationConfiguration()
                         .getSecurityTechnicalConfig()
                         .stream()
                         .map(SecurityConfiguration::getUsers)
                         .filter(Objects::nonNull)
                         .flatMap(List::stream)
                         .map(UserConfig::getLogin)
                         .filter(userLogin -> userLogin.equals(login))
                         .findFirst()
                         .isPresent();
        //@formatter:on
    }
    
}
