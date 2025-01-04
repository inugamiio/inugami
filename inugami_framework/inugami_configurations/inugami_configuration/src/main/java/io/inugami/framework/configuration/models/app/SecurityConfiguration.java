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

import java.io.Serializable;
import java.util.List;

import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.functionnals.PostProcessing;
import io.inugami.api.processors.Config;
import io.inugami.api.processors.ConfigHandler;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * SecurityConfiguration
 *
 * @author patrick_guillerm
 * @since 15 déc. 2017
 */
@XStreamAlias("security")
public class SecurityConfiguration implements Serializable, PostProcessing<ConfigHandler<String, String>> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 2251616016609037084L;

    @XStreamAsAttribute
    private String name;

    @XStreamImplicit
    private List<Config> configs;

    private UsersConfig users;

    private RolesMappeurConfig roles;


    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public void postProcessing(ConfigHandler<String, String> ctx) throws TechnicalException {
        //@formatter:off
        name            = ctx.applyProperties(name);
        //@formatter:on

    }

    @Override
    public int hashCode() {
        final int prime  = 31;
        int       result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;

        if (!result && obj != null && obj instanceof SecurityConfiguration) {
            final SecurityConfiguration other = (SecurityConfiguration) obj;
            result = name == null ? other.getName() == null : name.equals(other.getName());
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SecurityConfiguration [name=");
        builder.append(name);
        builder.append(", configs=");
        builder.append(configs);
        builder.append(", roles=");
        builder.append(roles);
        builder.append("]");
        return builder.toString();
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }

    public List<RoleMappeurConfig> getRoles() {
        return roles == null ? null : roles.getRoles();
    }

    public void setRoles(List<RoleMappeurConfig> roles) {
        if (this.roles == null) {
            this.roles = new RolesMappeurConfig();
        }
        this.roles.setRoles(roles);
    }

    public List<UserConfig> getUsers() {
        return users == null ? null : users.getUsers();
    }

    public void setUsers(List<UserConfig> users) {
        if (this.users == null) {
            this.users = new UsersConfig();
        }
        this.users.setUsers(users);
    }

}
