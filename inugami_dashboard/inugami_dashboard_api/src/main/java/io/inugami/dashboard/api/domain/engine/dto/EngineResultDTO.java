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
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class EngineResultDTO implements Serializable {
    private static final long                              serialVersionUID = -2891977091971159911L;
    private              String                            traceId;
    private              String                            processId;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              LocalDateTime                     start;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              LocalDateTime                     end;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              Status                            status;
    @Singular("plugins")
    private              Collection<EnginePluginResultDTO> plugins;
}
