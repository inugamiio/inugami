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
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.functionnals.PostProcessing;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * RolesMappeurConfig
 *
 * @author patrickguillerm
 * @since 16 déc. 2017
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class RolesMappeurConfig implements Serializable, PostProcessing<ConfigHandler<String, String>> {

    private static final long                    serialVersionUID = -6254381791279607759L;
    @Singular("roles")
    private              List<RoleMappeurConfig> roles;

    @Override
    public void postProcessing(ConfigHandler<String, String> ctx) throws TechnicalException {
        if (roles != null) {
            for (RoleMappeurConfig role : roles) {
                role.postProcessing(ctx);
            }
        }
    }

}
