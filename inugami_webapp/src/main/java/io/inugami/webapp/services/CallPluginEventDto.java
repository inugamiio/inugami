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
package io.inugami.webapp.services;

import io.inugami.api.models.Gav;
import lombok.*;

import java.io.Serializable;

/**
 * CallPluginEventDto
 *
 * @author patrick_guillerm
 * @since 29 janv. 2018
 */
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public final class CallPluginEventDto implements Serializable {

    private static final long serialVersionUID = 3358467013550180903L;

    private Gav    gav;
    private String excludeRegex;

}
