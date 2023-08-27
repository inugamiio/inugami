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
package io.inugami.core.security.commons.services;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;

import io.inugami.api.loggers.Loggers;
import io.inugami.core.security.commons.models.SecurityToken;
import io.inugami.core.security.commons.services.producers.TechnicalAccountInitializer;
import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.BaseAuthenticator;
import org.picketlink.authentication.UnexpectedCredentialException;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.credential.Credentials;
import org.picketlink.idm.credential.Credentials.Status;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.Token;
import org.picketlink.idm.credential.TokenCredential;
import org.picketlink.idm.model.Account;

/**
 * TokenAuthenticator
 * 
 * @author patrick_guillerm
 * @since 11 déc. 2017
 */
@Named
@Default
@RequestScoped
@PicketLink
public class TokenAndLoginPasswordAuthenticator extends BaseAuthenticator {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @Inject
    private SecurityTokenService               securityTokenService;
    
    @Inject
    private DefaultLoginCredentials            credentials;
    
    @Inject
    private List<LoginPasswordAuthentificator> authentificators;
    
    // init all default account
    @Inject
    private List<TechnicalAccountInitializer> technicalAccountInitializer;
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void authenticate() {
        if (credentials.getCredential() == null) {
            return;
        }
        
        if (isUsernamePasswordCredential()) {
            identifyWithLoginPassword();
        }
        else if (isTokenCredential()) {
            identifyByToken();
        }
        
        else {
            throwUnexpectedCredential(credentials.getCredential());
        }
        
        postProcessIdentify();
    }
    
    private void identifyByToken() {
        final SecurityToken token = credentials.getCredential() instanceof SecurityToken ? (SecurityToken) credentials.getCredential()
                                                                                         : (SecurityToken) ((TokenCredential) credentials.getCredential()).getToken();
        final AuthenticationStatus status = securityTokenService.identify(token.getToken());
        credentials.setStatus(AuthenticationStatus.SUCCESS == status ? Credentials.Status.VALID
                                                                     : Credentials.Status.INVALID);
        
        if (AuthenticationStatus.SUCCESS != status) {
            credentials.setStatus(Status.UNVALIDATED);
        }
        else {
            final Account user = securityTokenService.getUser(token.getToken());
            credentials.setValidatedAccount(user);
        }
        
    }
    
    private void identifyWithLoginPassword() {
        final Password password = (Password) credentials.getCredential();
        final String userId = credentials.getUserId();
        
        final Credentials result = processIdentifyWithPassword(userId, password);
        final Status status = result == null ? Status.INVALID : result.getStatus();
        
        credentials.setStatus(status);
        
        if (result != null) {
            credentials.setValidatedAccount(result.getValidatedAccount());
        }
    }
    
    public Credentials processIdentifyWithPassword(final String userId, final Password password) {
        Credentials result = null;
        for (final LoginPasswordAuthentificator authentificator : authentificators) {
            
            try {
                result = authentificator.authentificate(userId, password);
                if ((result != null) && (Status.VALID == result.getStatus())) {
                    break;
                }
            }
            catch (final Exception e) {
                Loggers.SECURITY.debug(e.getMessage());
            }
        }
        
        return result;
    }
    
    private void postProcessIdentify() {
        credentials.setStatus(credentials.getStatus());
        
        if (Credentials.Status.VALID.equals(credentials.getStatus())) {
            setAccount(credentials.getValidatedAccount());
        }
    }
    
    @Override
    public AuthenticationStatus getStatus() {
        AuthenticationStatus status = AuthenticationStatus.FAILURE;
        if (Status.VALID == credentials.getStatus()) {
            status = AuthenticationStatus.SUCCESS;
        }
        return status;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    private boolean isUsernamePasswordCredential() {
        return Password.class.equals(credentials.getCredential().getClass()) && (credentials.getUserId() != null);
    }
    
    private boolean isTokenCredential() {
        return (credentials.getCredential() instanceof Token)
               || (credentials.getCredential() instanceof TokenCredential);
    }
    
    // =========================================================================
    // EXCEPTIONS
    // =========================================================================
    private void throwUnexpectedCredential(final Object credential) {
        throw new UnexpectedCredentialException("Unsupported credential type [" + credential.getClass() + "].");
    }
}
