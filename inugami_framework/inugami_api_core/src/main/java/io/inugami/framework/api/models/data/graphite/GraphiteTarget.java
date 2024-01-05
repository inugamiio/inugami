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
package io.inugami.framework.api.models.data.graphite;

import io.inugami.framework.interfaces.models.basic.Dto;
import io.inugami.framework.interfaces.models.number.DataPoint;
import lombok.*;

import java.util.List;
import java.util.Optional;

/**
 * GraphiteValues
 *
 * @author patrick_guillerm
 * @since 23 sept. 2016
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class GraphiteTarget implements Dto<GraphiteTarget> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long            serialVersionUID = -6818032035661421722L;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              String          target;
    private              List<DataPoint> datapoints;


    @Override
    public GraphiteTarget cloneObj() {
        final var builder = toBuilder();

        builder.datapoints(Optional.ofNullable(datapoints)
                                   .orElse(List.of())
                                   .stream()
                                   .map(DataPoint::cloneObj)
                                   .toList());

        return builder.build();
    }
}
