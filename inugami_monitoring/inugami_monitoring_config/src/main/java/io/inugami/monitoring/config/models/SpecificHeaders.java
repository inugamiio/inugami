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
package io.inugami.monitoring.config.models;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * CustomsHeaders
 *
 * @author patrickguillerm
 * @since Jan 16, 2019
 */
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SpecificHeaders implements Serializable {
    private static final long serialVersionUID = 5978552740744597100L;

    @Singular("specificHeader")
    private List<SpecificHeader> specificHeader;

}
