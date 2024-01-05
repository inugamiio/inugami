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
package io.inugami.framework.interfaces.models.number;

import io.inugami.framework.interfaces.models.basic.Dto;
import lombok.*;

/**
 * DataPoint
 *
 * @author patrick_guillerm
 * @since 23 sept. 2016
 */
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public final class DataPoint implements Dto<DataPoint> {

    private static final long serialVersionUID = -6203322773055819499L;

    private Double value;
    private long   timestamp;

    @Override
    public DataPoint cloneObj() {
        return toBuilder().build();
    }
}
