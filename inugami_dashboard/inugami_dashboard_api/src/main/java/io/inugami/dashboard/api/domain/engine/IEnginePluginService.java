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

import io.inugami.dashboard.api.domain.engine.dto.EnginePluginResultDTO;
import io.inugami.framework.commons.threads.ThreadsExecutorService;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.util.Collection;

public interface IEnginePluginService {
    boolean hasEventsToRun(final LocalDateTime now);

    EnginePluginResultDTO run(@NonNull final Collection<EngineListener> currentListeners,
                              @NonNull final LocalDateTime now);

    ThreadsExecutorService getThreadsExecutorService();
}
