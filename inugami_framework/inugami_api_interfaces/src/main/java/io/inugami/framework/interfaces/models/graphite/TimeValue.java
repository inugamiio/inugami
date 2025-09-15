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
package io.inugami.framework.interfaces.models.graphite;

import io.inugami.framework.interfaces.models.basic.Dto;
import io.inugami.framework.interfaces.models.number.GraphiteNumber;
import lombok.*;

/**
 * TimeValue
 *
 * @author patrick_guillerm
 * @since 14 mai 2018
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TimeValue implements Dto<TimeValue> {

    private static final long serialVersionUID = -4766022961962855510L;

    private String         path;
    private GraphiteNumber value;
    private long           time;

    @Override
    public TimeValue cloneObj() {
        return null;
    }
}
