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
package io.inugami.framework.interfaces.dependency.dto;

import lombok.*;

import java.io.Serializable;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class VersionRules implements Serializable {
    private static final long serialVersionUID = 1004153560918528115L;
    private Rule major;
    private Rule minor;
    private Rule patch;
}
