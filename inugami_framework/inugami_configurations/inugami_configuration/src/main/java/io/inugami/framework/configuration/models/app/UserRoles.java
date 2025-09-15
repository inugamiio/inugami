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

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * UserRoles
 *
 * @author patrickguillerm
 * @since 18 déc. 2017
 */
@SuppressWarnings({"java:S1700"})
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class UserRoles implements Serializable {

    private static final long serialVersionUID = 1438748070951725838L;

    @Singular("userRoles")
    private List<String> userRoles;

}
