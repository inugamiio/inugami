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

import java.io.Serializable;
import java.util.List;

import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.functionnals.PostProcessing;
import io.inugami.api.processors.ConfigHandler;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * RolesMappeurConfig
 * 
 * @author patrickguillerm
 * @since 16 déc. 2017
 */
@XStreamAlias("roles")
public class RolesMappeurConfig implements Serializable, PostProcessing<ConfigHandler<String, String>> {

    private static final long       serialVersionUID = -6254381791279607759L;

    @XStreamImplicit
    private List<RoleMappeurConfig> roles;

    @Override
    public void postProcessing(ConfigHandler<String, String> ctx) throws TechnicalException {
        if (roles != null) {
            for (RoleMappeurConfig role : roles) {
                role.postProcessing(ctx);
            }
        }
    }

    public List<RoleMappeurConfig> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleMappeurConfig> roles) {
        this.roles = roles;
    }
}
