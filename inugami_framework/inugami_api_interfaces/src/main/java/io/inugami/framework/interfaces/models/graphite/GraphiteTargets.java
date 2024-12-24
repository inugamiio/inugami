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
import lombok.*;

import java.util.List;
import java.util.Optional;

/**
 * GraphiteTargets
 *
 * @author patrick_guillerm
 * @since 12 janv. 2017
 */
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GraphiteTargets implements Dto<GraphiteTargets> {

    private static final long serialVersionUID = -732597415422973512L;

    private List<GraphiteTarget> targets;


    @Override
    public GraphiteTargets cloneObj() {
        final var builder = toBuilder();

        builder.targets(Optional.ofNullable(targets)
                                .orElse(List.of())
                                .stream()
                                .map(GraphiteTarget::cloneObj)
                                .toList());

        return builder.build();
    }
}
