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
package io.inugami.dashboard.api.domain.engine;

import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.dashboard.api.domain.engine.dto.EngineResultDTO;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.interfaces.models.event.GenericEvent;

public interface EngineListener {

    default void onDone(final EngineResultDTO engineResult) {
    }

    default void onEventDone(final Plugin plugin, final GenericEvent<?> event, final EnginePluginEventResultDTO data) {
    }
}
