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
package io.inugami.configuration.models.app;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import io.inugami.api.constants.JvmKeyValues;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.functionnals.PostProcessing;
import io.inugami.api.processors.ConfigHandler;

import java.io.Serializable;
import java.util.List;

/**
 * UserConfig
 *
 * @author patrickguillerm
 * @since 16 déc. 2017
 */
@XStreamAlias("user")
public class UserConfig implements Serializable, PostProcessing<ConfigHandler<String, String>> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -1426894131168094022L;

    @XStreamAsAttribute
    private String login;

    @XStreamAsAttribute
    private String password;

    @XStreamAsAttribute
    private String token;

    @XStreamAsAttribute
    private String firstName;

    @XStreamAsAttribute
    private String lastName;

    private UserRoles userRoles;

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public void postProcessing(final ConfigHandler<String, String> ctx) throws TechnicalException {
        Asserts.assertNotNull("User login is mandatory!", login);
        // @formatter:off
        login = ctx.applyProperties(login);
        password = ctx.applyProperties(JvmKeyValues.SECURITY_USERS.or(login.concat(".password"), password));
        token = ctx.applyProperties(JvmKeyValues.SECURITY_USERS.or(login.concat(".token"), token));
        lastName = ctx.applyProperties(JvmKeyValues.SECURITY_USERS.or(login.concat(".lastName"), lastName));
        firstName = ctx.applyProperties(JvmKeyValues.SECURITY_USERS.or(login.concat(".firstName"), firstName));
        // @formatter:on
    }

    @Override
    public int hashCode() {
        final int prime  = 31;
        int       result = 1;
        result = prime * result + ((login == null) ? 0 : login.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;

        if (!result && obj != null && obj instanceof UserConfig) {
            final UserConfig other = (UserConfig) obj;
            result = login == null ? other.getLogin() == null : login.equals(other.getLogin());
        }

        return result;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UserConfig [login=");
        builder.append(login);
        builder.append(", firstName=");
        builder.append(firstName);
        builder.append(", lastName=");
        builder.append(lastName);
        builder.append("]");
        return builder.toString();
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public List<String> getUserRoles() {
        return userRoles == null ? null : userRoles.getUserRoles();
    }

    public void setUserRoles(final List<String> roles) {
        if (userRoles == null) {
            userRoles = new UserRoles();
        }
        this.userRoles.setUserRoles(roles);
    }

}
