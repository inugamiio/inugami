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
package io.inugami.framework.configuration.models.plugins;

import lombok.*;

import java.io.Serializable;

/**
 * Resources
 *
 * @author patrick_guillerm
 * @since 22 déc. 2016
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Resource implements Serializable {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final long          serialVersionUID = -4852393784977428286L;
    private              String        path;
    private              String        name;
    private              RessourceType type;

    public String getFullPath() {
        return String.join("/", path, name);
    }
}
