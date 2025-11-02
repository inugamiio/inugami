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

import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import io.inugami.framework.interfaces.tools.StringComparator;
import lombok.*;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class EnginePluginEventResultDTO implements Serializable, Comparable<EnginePluginEventResultDTO> {
    @EqualsAndHashCode.Include
    private String               name;
    @ToString.Include
    @EqualsAndHashCode.Include
    private ErrorCode            errorCode;
    @ToString.Include
    @EqualsAndHashCode.Include
    private Status               status;
    private String               message;
    private Throwable            error;
    private ProviderFutureResult data;

    @Override
    public int compareTo(@NonNull final EnginePluginEventResultDTO other) {
        return StringComparator.compareTo(name, other == null ? null : other.getName());
    }
}
