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
package io.inugami.core.security.commons.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.inugami.core.security.commons.models.SecurityToken;
import io.inugami.core.security.commons.models.UserModel;
import io.inugami.core.security.commons.services.SecurityTokenService;
import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.model.Account;

/**
 * SecurityRest
 * 
 * @author patrick_guillerm
 * @since 11 déc. 2017
 */
@Named
@Path("security/authenticate")
public class SecurityAuthenticateRest {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final String      CREDENTIAL_CONTENT_TYPE = "application/x-authc-username-password+json";
    
    public static final String      TOKEN_CONTENT_CREDENTIAL_TYPE             = "application/x-authc-token";
    
    @Inject
    private DefaultLoginCredentials credentials;
    
    @Inject
    private SecurityTokenService    securityTokenService;
    
    @Inject
    private Identity                identity;
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({ CREDENTIAL_CONTENT_TYPE })
    public Response authenticate(DefaultLoginCredentials credential) {
        return authenticateLoginPassword(credential.getUserId(), credential.getPassword());
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    public Response basicFormAuthenticate(@FormParam("j_username") String jlogin, @FormParam("username") String login,
                                          @FormParam("j_password") String jpassword,
                                          @FormParam("password") String password) {
        return authenticateLoginPassword(chooseFormData(jlogin, login), chooseFormData(jpassword, password));
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({ TOKEN_CONTENT_CREDENTIAL_TYPE })
    public Response authenticate(@HeaderParam("token") String token) {
        Response result = Response.status(401).type(MediaType.APPLICATION_JSON_TYPE).build();
        AuthenticationResult loginResult = AuthenticationResult.FAILED;
        if (!identity.isLoggedIn()) {
            SecurityToken credential = new SecurityToken(token);
            this.credentials.setCredential(credential);
            loginResult = this.identity.login();
        }
        
        if (AuthenticationResult.SUCCESS == loginResult) {
            UserModel user = loadUser(token);
            result = Response.ok(user).type(MediaType.APPLICATION_JSON_TYPE).build();
        }
        
        return result;
    }
    
    @POST
    @Path("logout")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({ TOKEN_CONTENT_CREDENTIAL_TYPE })
    public Response logout(@HeaderParam("token") String token) {
        securityTokenService.logout(token);
        return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).build();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({ "*/*" })
    public Response basicFormAuthenticate() {
        return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE).build();
    }
    
    // =========================================================================
    // PRIVATE
    // =========================================================================
    private Response authenticateLoginPassword(final String login, final String password) {
        Response result = Response.status(401).type(MediaType.APPLICATION_JSON_TYPE).build();
        
        final boolean alreadyLoggedIn = identity.isLoggedIn();
        
        AuthenticationResult loginResult = alreadyLoggedIn ? AuthenticationResult.SUCCESS : AuthenticationResult.FAILED;
        
        if (!alreadyLoggedIn) {
            loginResult = checkInputPassword(login, password);
        }
        
        if (AuthenticationResult.SUCCESS == loginResult) {
            final String token = securityTokenService.register(login, credentials);
            final UserModel user = new UserModel(token, credentials.getValidatedAccount());
            result = Response.ok(user).type(MediaType.APPLICATION_JSON_TYPE).build();
        }
        
        return result;
    }
    
    private AuthenticationResult checkInputPassword(final String login, final String password) {
        AuthenticationResult loginResult;
        checkInput(login, password);
        
        credentials.setUserId(login);
        credentials.setPassword(password);
        
        loginResult = this.identity.login();
        return loginResult;
    }
    
    // =========================================================================
    // LOAD USER
    // =========================================================================
    private UserModel loadUser(String token) {
        final Account user = securityTokenService.getUser(token);
        return new UserModel(token, user);
    }
    
    // =========================================================================
    // EXCEPTION
    // =========================================================================
    private void checkInput(String userId, String password) throws BadRequestException {
        if (isEmpty(userId) || isEmpty(password)) {
            throw new BadRequestException();
        }
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private boolean isEmpty(final String value) {
        return value == null || value.trim().isEmpty();
    }
    
    private String chooseFormData(String mainValue, String otherValue) {
        return mainValue != null ? mainValue : otherValue;
    }
}
