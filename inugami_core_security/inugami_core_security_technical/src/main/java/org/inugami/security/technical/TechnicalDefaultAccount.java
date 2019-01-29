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
package org.inugami.security.technical;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.inugami.api.exceptions.FatalException;
import org.inugami.commons.security.EncryptionUtils;
import org.inugami.configuration.models.app.SecurityConfiguration;
import org.inugami.configuration.models.app.UserConfig;
import org.inugami.core.context.ApplicationContext;
import org.inugami.core.security.commons.services.SecurityTokenService;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.User;

/**
 * DevDefaultAccount
 * 
 * @author patrick_guillerm
 * @since 8 déc. 2017
 */
@Named
@ApplicationScoped
public class TechnicalDefaultAccount {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @Inject
    private PartitionManager      partitionManager;

    @Inject
    private ApplicationContext    applicationCtx;

    @Inject
    private SecurityTokenService  securityTokenSerivce;

    private final EncryptionUtils encryptionUtils = new EncryptionUtils();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public void load(@Observes @Initialized(ApplicationScoped.class) Object init) {
        IdentityManager identityManager = this.partitionManager.createIdentityManager();

        final List<SecurityConfiguration> technicalUsers = applicationCtx.getApplicationConfiguration().getSecurityTechnicalConfig();
        if (technicalUsers != null) {
            for (int i = 0; i < technicalUsers.size(); i++) {
                processInitSecurityConfig(identityManager, technicalUsers.get(i));
            }
        }
    }


    private void processInitSecurityConfig(final IdentityManager identityManager, final SecurityConfiguration config) {
        final List<UserConfig> users = config.getUsers() == null ? new ArrayList<>() : config.getUsers();
        for (int i = 0; i < users.size(); i++) {
            initializeUsers(users.get(i), identityManager);
        }
    }

    private void initializeUsers(UserConfig config, IdentityManager identityManager) {
        final User user = new User(config.getLogin());
        user.setFirstName(config.getFirstName());
        user.setLastName(config.getLastName());

        Attribute<ArrayList<String>> rolesUser = new Attribute<>("roles", (ArrayList<String>) config.getUserRoles());
        user.setAttribute(rolesUser);

        identityManager.add(user);
        String password = null;
        try {
            password = encryptionUtils.decodeAES(config.getPassword());
        } catch (SecurityException e) {
            throw new FatalException("Invalide user password ({0}) : {1}", config.getLogin(), e.getMessage());
        }

        identityManager.updateCredential(user, new Password(password));

        securityTokenSerivce.register(config.getLogin(), user, config.getToken());
    }

}
