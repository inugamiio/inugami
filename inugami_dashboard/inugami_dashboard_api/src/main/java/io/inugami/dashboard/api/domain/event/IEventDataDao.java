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
package io.inugami.dashboard.api.domain.event;

import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.dashboard.api.domain.engine.dto.EventDoneDTO;
import io.inugami.framework.interfaces.models.maven.Gav;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.Map;

public interface IEventDataDao {
    void updateEventsData(final Collection<EventDoneDTO> eventsResult);

    Map<String, EnginePluginEventResultDTO> findPluginDataByGav(@NonNull final Gav gav);
}
