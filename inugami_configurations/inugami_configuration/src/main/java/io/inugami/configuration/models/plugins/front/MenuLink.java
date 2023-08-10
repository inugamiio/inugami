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
package io.inugami.configuration.models.plugins.front;

import lombok.*;

import java.io.Serializable;

/**
 * MenuLink
 *
 * @author patrick_guillerm
 * @since 19 janv. 2017
 */
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MenuLink implements Serializable {
    
    private static final long serialVersionUID = 8016174687361342718L;

    @EqualsAndHashCode.Include
    private String path;

    private String title;

    private String styleClass;

}
