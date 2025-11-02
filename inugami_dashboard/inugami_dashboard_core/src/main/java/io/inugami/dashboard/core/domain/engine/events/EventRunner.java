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
package io.inugami.dashboard.core.domain.engine.events;

import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.dashboard.api.domain.event.EventErrors;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.interfaces.concurrent.FutureData;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.ExceptionWithErrorCode;
import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.models.event.Event;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.providers.Provider;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
@Builder
@RequiredArgsConstructor
public class EventRunner {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private final Event           event;
    private final LocalDateTime   now;
    private final Plugin          plugin;
    private final List<Provider>  providers;
    private final List<Processor> processors;
    private final ZoneOffset      zoneOffset;

    // =================================================================================================================
    // RUN EVENTS
    // =================================================================================================================
    public Callable<EnginePluginEventResultDTO> run() {
        return this::runEvent;
    }

    protected EnginePluginEventResultDTO runEvent() {
        try {
            return EnginePluginEventResultDTO.builder()
                                             .name(event.getName())
                                             .data(processRunEvent())
                                             .status(Status.SUCCESS)
                                             .build();
        } catch (Throwable e) {
            return EnginePluginEventResultDTO.builder()
                                             .name(event.getName())
                                             .status(Status.ERROR)
                                             .message(e.getMessage())
                                             .error(e)
                                             .errorCode(extractErrorCode(e))
                                             .build();
        }
    }


    protected ProviderFutureResult processRunEvent() throws Throwable {
        //TODO:implement
        log.info("TODO :implement");
        return null;
    }

    // =================================================================================================================
    // INTERNAL
    // =================================================================================================================
    protected ErrorCode extractErrorCode(final Throwable error) {
        if (error instanceof ExceptionWithErrorCode e) {
            return e.getErrorCode();
        }
        return EventErrors.UNDEFINED;
    }
}
