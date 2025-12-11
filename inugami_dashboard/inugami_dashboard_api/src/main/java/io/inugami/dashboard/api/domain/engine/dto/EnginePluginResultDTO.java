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
package io.inugami.dashboard.api.domain.engine.dto;

import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.tools.StringComparator;
import lombok.*;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;
import java.util.Collection;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EnginePluginResultDTO implements Serializable, Comparable<EnginePluginResultDTO> {
    private static final long                                   serialVersionUID = -4426380938842629284L;
    @Singular("events")
    private              Collection<EnginePluginEventResultDTO> events;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              Gav                                    gav;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              Status                                 status;
    private              String                                 message;

    @Override
    public int compareTo(@NonNull final EnginePluginResultDTO other) {
        final String currentGav = gav == null ? null : gav.getHash();
        final String otherGav   = other == null || other.getGav() == null ? null : other.getGav().getHash();
        return StringComparator.compareTo(currentGav, otherGav);
    }
}
