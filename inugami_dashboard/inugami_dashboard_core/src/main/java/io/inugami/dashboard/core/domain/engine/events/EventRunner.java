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
import io.inugami.framework.commons.threads.ThreadsExecutorService;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.interfaces.concurrent.FutureData;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.ExceptionWithErrorCode;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.exceptions.services.ProviderException;
import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.models.event.Event;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.models.event.TargetConfig;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.providers.Provider;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static io.inugami.dashboard.core.domain.engine.events.EventRunnerUtils.resolveErrorCode;
import static io.inugami.dashboard.core.domain.engine.events.EventRunnerUtils.selectProcessor;
import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;
@SuppressWarnings({"java:S1172"})
@Slf4j
@Builder
@RequiredArgsConstructor
public class EventRunner {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private final Event                      event;
    private final LocalDateTime              now;
    private final Plugin                     plugin;
    private final List<Provider>             providers;
    private final List<Processor>            processors;
    private final ZoneOffset                 zoneOffset;
    private final long                       timeout;
    private final ThreadsExecutorService     threadsExecutorService;
    private final Collection<EngineListener> listeners;

    // =================================================================================================================
    // RUN EVENTS
    // =================================================================================================================
    public Callable<EnginePluginEventResultDTO> run() {
        return this::runEvent;
    }

    protected EnginePluginEventResultDTO runEvent() {
        if (event.getTargets() == null || event.getTargets().isEmpty()) {
            return EnginePluginEventResultDTO.builder().name(event.getName()).status(Status.NOTHING_TO_DO).build();
        }
        return processRunEvent();
    }


    protected EnginePluginEventResultDTO processRunEvent() {
        var mainProvider = selectProvider(event.getProvider(), providers, null);


        final List<Callable<EnginePluginEventResultDTO>> tasks = new ArrayList<>();

        for (final TargetConfig target : event.getTargets()) {
            final var provider = selectProvider(event.getProvider(), providers, mainProvider);
            if (mainProvider == null && provider != null) {
                mainProvider = provider;
            }
            if (provider == null) {
                log.warn("[{}] {} : no provider define for target {}", plugin.getGav(), event.getName(), target.getName());
                continue;
            }

            tasks.add(() -> processRunTarget(target, provider, now));
        }

        if (tasks.isEmpty()) {
            return EnginePluginEventResultDTO.builder()
                                             .name(event.getName())
                                             .message("no provider found")
                                             .status(Status.NOTHING_TO_DO)
                                             .build();
        }

        final List<EnginePluginEventResultDTO> data         = new ArrayList<>();
        final List<EnginePluginEventResultDTO> targetStatus = new ArrayList<>();
        try {
            final List<EnginePluginEventResultDTO> resultData = threadsExecutorService.runAndGrab(tasks,
                                                                                                  (value, task) -> onDone(value, task, targetStatus),
                                                                                                  (error, task) -> onError(error, task, targetStatus), timeout);
            applyIfNotNull(resultData, data::addAll);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

        if (hasError(targetStatus)) {
            final var result = EnginePluginEventResultDTO.builder()
                                                         .name(event.getName())
                                                         .message(buildErrorMessage(targetStatus))
                                                         .status(Status.ERROR)
                                                         .build();
            callListeners(result);
            return result;
        }

        if (mainProvider == null) {
            return EnginePluginEventResultDTO.builder()
                                             .name(event.getName())
                                             .message("no provider defined")
                                             .status(Status.ERROR)
                                             .build();
        }

        try {
            final var result = EnginePluginEventResultDTO.builder()
                                                         .name(event.getName())
                                                         .data(buildData(data, mainProvider))
                                                         .status(Status.SUCCESS)
                                                         .build();
            callListeners(result);
            return result;
        } catch (ProviderException e) {
            final var result = EnginePluginEventResultDTO.builder()
                                                         .name(event.getName())
                                                         .message(e.getMessage())
                                                         .error(e)
                                                         .status(Status.ERROR)
                                                         .build();
            callListeners(result);
            return result;
        }
    }

    @SuppressWarnings({"java:S2142"})
    protected EnginePluginEventResultDTO processRunTarget(final TargetConfig target,
                                                          final Provider provider,
                                                          final LocalDateTime now) {
        try {
            final FutureData<ProviderFutureResult> future = provider.callEvent(buildEvent(target), plugin.getGav(), this.now);
            ProviderFutureResult                   result = future.getFuture().get(timeout, TimeUnit.MILLISECONDS);

            for (Processor processor : selectProcessor(event.getProcessors(), processors)) {
                result = processor.process(event, result);
            }

            return EnginePluginEventResultDTO.builder().data(result).status(Status.SUCCESS).build();
        } catch (Throwable e) {
            if (log.isDebugEnabled()) {
                log.error(e.getMessage(), e);
            }
            return EnginePluginEventResultDTO.builder()
                                             .message(e.getMessage())
                                             .status(Status.ERROR)
                                             .error(e)
                                             .errorCode(resolveErrorCode(e))
                                             .build();
        }

    }


    // =================================================================================================================
    // EVENTS
    // =================================================================================================================
    protected void onDone(@NonNull final EnginePluginEventResultDTO result,
                          @NonNull final Callable<EnginePluginEventResultDTO> task,
                          @NonNull final List<EnginePluginEventResultDTO> status) {
        log.debug("target done");
        status.add(result);
    }

    protected void onError(@NonNull final Exception error,
                           @NonNull final Callable<EnginePluginEventResultDTO> task,
                           @NonNull final List<EnginePluginEventResultDTO> status) {
        log.error("target done");
        status.add(EnginePluginEventResultDTO.builder()
                                             .message(error.getMessage())
                                             .error(error)
                                             .status(Status.ERROR)
                                             .build());
    }

    // =================================================================================================================
    // INTERNAL
    // =================================================================================================================
    protected @Nullable Provider selectProvider(final String provider,
                                                final List<Provider> providers,
                                                final Provider defaultProvider) {
        return providers.stream()
                        .filter(p -> p.getName().equalsIgnoreCase(provider) ||
                                     p.getClass().getName().equalsIgnoreCase(provider))
                        .findFirst()
                        .orElse(defaultProvider);
    }

    protected @NonNull SimpleEvent buildEvent(final TargetConfig target) {
        return SimpleEvent.builder()
                          .from(target.getFrom())
                          .fromFirstTime(target.getFromFirstTime())
                          .name(target.getName())
                          .parent(target.getParent())
                          .processors(Optional.ofNullable(target.getProcessors()).orElse(List.of()))
                          .provider(target.getProvider())
                          .query(target.getQuery())
                          .scheduler(target.getScheduler())
                          .until(target.getUntil())
                          .alertings(Optional.ofNullable(target.getAlertings()).orElse(List.of()))
                          .build();
    }

    private @NonNull String buildErrorMessage(@NonNull final List<EnginePluginEventResultDTO> targetStatus) {
        return String.join("\n", targetStatus.stream().map(EnginePluginEventResultDTO::getMessage).toList());
    }

    private boolean hasError(@NonNull final List<EnginePluginEventResultDTO> targetStatus) {
        return targetStatus.stream().anyMatch(t -> Status.ERROR == t.getStatus());
    }

    private ProviderFutureResult buildData(@NonNull final List<EnginePluginEventResultDTO> data,
                                           @NonNull final Provider mainProvider) throws ProviderException {
        return mainProvider.aggregate(data.stream()
                                          .filter(d -> Status.ERROR != d.getStatus())
                                          .map(EnginePluginEventResultDTO::getData)
                                          .filter(Objects::nonNull)
                                          .toList());
    }

    protected void callListeners(final EnginePluginEventResultDTO result) {
        listeners.forEach(l -> l.onEventDone(plugin, event, result));
    }
}
