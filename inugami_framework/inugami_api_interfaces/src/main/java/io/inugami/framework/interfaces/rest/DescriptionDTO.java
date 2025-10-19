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
package io.inugami.framework.interfaces.rest;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DescriptionDTO implements Serializable {
    private static final long                    serialVersionUID = -8260338174990313946L;
    @EqualsAndHashCode.Include
    private              String                  content;
    private              String                  example;
    private              String                  url;
    private              List<PotentialErrorDTO> potentialErrors;
}
