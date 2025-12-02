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

import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.interfaces.models.event.GenericEvent;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = false)
public class EventDoneDTO implements Serializable {
    private static final long                       serialVersionUID = 1549708365269970176L;
    @EqualsAndHashCode.Include
    private              Plugin                     plugin;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              GenericEvent<?>            event;
    private              EnginePluginEventResultDTO data;
    @ToString.Include
    @EqualsAndHashCode.Include
    private              LocalDateTime              date;
}
