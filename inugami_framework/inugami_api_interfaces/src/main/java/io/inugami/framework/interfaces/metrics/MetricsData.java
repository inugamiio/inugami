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
package io.inugami.framework.interfaces.metrics;

import io.inugami.framework.interfaces.models.basic.Dto;
import lombok.*;

/**
 * MetricsData
 *
 * @author patrick_guillerm
 * @since 6 juin 2017
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MetricsData implements Dto<MetricsData> {

    private static final long serialVersionUID = 1097665514789847818L;

    private String          path;
    private Double          value;
    private MetricsDataType type;


    @Override
    public MetricsData cloneObj() {
        return toBuilder().build();
    }
}
