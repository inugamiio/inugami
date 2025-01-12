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
package io.inugami.framework.configuration.models.app;

import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.configurtation.JvmKeyValues;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.functionnals.PostProcessing;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * UserConfig
 *
 * @author patrickguillerm
 * @since 16 déc. 2017
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserConfig implements Serializable, PostProcessing<ConfigHandler<String, String>> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long            serialVersionUID = -1426894131168094022L;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              String          login;
    private              String          password;
    private              String          token;
    @ToString.Include
    private              String          firstName;
    @ToString.Include
    private              String          lastName;
    @Singular("userRoles")
    private              List<UserRoles> userRoles;

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public void postProcessing(final ConfigHandler<String, String> ctx) throws TechnicalException {
        Asserts.assertNotNull("User login is mandatory!", login);
        login = ctx.applyProperties(login);
        password = ctx.applyProperties(JvmKeyValues.SECURITY_USERS.or(login.concat(".password"), password));
        token = ctx.applyProperties(JvmKeyValues.SECURITY_USERS.or(login.concat(".token"), token));
        lastName = ctx.applyProperties(JvmKeyValues.SECURITY_USERS.or(login.concat(".lastName"), lastName));
        firstName = ctx.applyProperties(JvmKeyValues.SECURITY_USERS.or(login.concat(".firstName"), firstName));
    }

}
