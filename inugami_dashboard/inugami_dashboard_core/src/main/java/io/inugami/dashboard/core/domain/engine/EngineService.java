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
package io.inugami.dashboard.core.domain.engine;

import io.inugami.dashboard.api.domain.engine.EngineListener;
import io.inugami.dashboard.api.domain.engine.IEngineService;
import io.inugami.dashboard.api.domain.engine.dto.EngineResultDTO;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.commons.threads.ThreadsExecutorService;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.interfaces.models.engine.Status;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Builder
public class EngineService implements IEngineService {

    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private final List<Plugin>               plugins;
    private final Collection<EngineListener> listeners;
    private final ThreadsExecutorService     threadsExecutor;
    private final Clock                      clock;

    //==================================================================================================================
    // INIT
    //==================================================================================================================


    //==================================================================================================================
    // RUN
    //==================================================================================================================
    @Override
    public void run() {
        final var mdc = MdcService.getInstance();
        mdc.processId(UUID.randomUUID().toString());

        final var result = EngineResultDTO.builder()
                                          .traceId(mdc.traceId())
                                          .processId(mdc.processId())
                                          .start(LocalDateTime.now(clock));



        result.end(LocalDateTime.now(clock));
        result.status(Status.SUCCESS);
        sendOnDone(result.build());
    }


    //==================================================================================================================
    // EVENT
    //==================================================================================================================
    private void sendOnDone(final EngineResultDTO engineResult) {
        listeners.forEach(listener -> listener.onDone(engineResult));
    }
}
