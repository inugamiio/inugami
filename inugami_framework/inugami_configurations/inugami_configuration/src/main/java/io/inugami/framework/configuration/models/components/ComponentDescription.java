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
package io.inugami.framework.configuration.models.components;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * ComponentDescription
 *
 * @author patrickguillerm
 * @since 30 août 2018
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ComponentDescription implements Serializable {


    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final long                                   serialVersionUID = -1483992189580390931L;
    @ToString.Include
    @EqualsAndHashCode.Include
    @Singular("descriptions")
    private              List<ComponentDescriptionContentModel> descriptions;
    @Singular("screenshots")
    private              List<ComponentScreenshot>              screenshots;
}
