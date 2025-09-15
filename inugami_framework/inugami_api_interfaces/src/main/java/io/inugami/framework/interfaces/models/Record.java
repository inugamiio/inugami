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
package io.inugami.framework.interfaces.models;

import lombok.Builder;
import lombok.*;

import java.io.Serializable;

/**
 * Record
 *
 * @author patrick_guillerm
 * @since 10 oct. 2017
 */
@ToString
@EqualsAndHashCode
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Record implements Serializable {

    private static final long serialVersionUID = 789112101362883558L;

    private long time;

    private double value;

    private String unit;

}
