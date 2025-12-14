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

import io.inugami.dashboard.api.domain.engine.EngineListener;
import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.dashboard.api.domain.event.EventErrors;
import io.inugami.dashboard.api.domain.sender.ISSESender;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.interfaces.concurrent.FutureData;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.ExceptionWithErrorCode;
import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.providers.Provider;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static io.inugami.dashboard.core.domain.engine.events.EventRunnerUtils.selectProcessor;
@SuppressWarnings({"java:S112"})
@Slf4j
@Builder
@RequiredArgsConstructor
public class SimpleEventRunner {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private final SimpleEvent                event;
    private final LocalDateTime              now;
    private final Plugin                     plugin;
    private final Provider                   provider;
    private final List<Processor>            processors;
    private final long                       timeout;
    private final ZoneOffset                 zoneOffset;
    private final Collection<EngineListener> listeners;


    // =================================================================================================================
    // RUN EVENTS
    // =================================================================================================================
    public Callable<EnginePluginEventResultDTO> run() {
        return this::runEvent;
    }

    protected EnginePluginEventResultDTO runEvent() {
        try {
            final var result = EnginePluginEventResultDTO.builder()
                                                         .name(event.getName())
                                                         .data(processRunEvent())
                                                         .status(Status.SUCCESS)
                                                         .build();
            callListeners(result);
            return result;
        } catch (Throwable e) {
            if (log.isDebugEnabled()) {
                log.error(e.getMessage(), e);
            }
            final var result = EnginePluginEventResultDTO.builder()
                                                         .name(event.getName())
                                                         .status(Status.ERROR)
                                                         .message(e.getMessage())
                                                         .error(e)
                                                         .errorCode(extractErrorCode(e))
                                                         .build();
            callListeners(result);
            return result;
        }
    }


    protected ProviderFutureResult processRunEvent() throws Throwable {
        final FutureData<ProviderFutureResult> future = provider.callEvent(event, plugin.getGav(), now);

        ProviderFutureResult result = future.getFuture().get(timeout, TimeUnit.MILLISECONDS);

        for (Processor processor : selectProcessor(event.getProcessors(), processors)) {
            result = processor.process(event, result);
        }

        return result;
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

    protected void callListeners(final EnginePluginEventResultDTO result) {
        listeners.forEach(l -> l.onEventDone(plugin, event, result));
    }
}
