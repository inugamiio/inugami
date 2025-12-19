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
import io.inugami.dashboard.api.domain.engine.IEnginePluginService;
import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.dashboard.api.domain.engine.dto.EnginePluginResultDTO;
import io.inugami.dashboard.core.domain.engine.events.EventRunner;
import io.inugami.dashboard.core.domain.engine.events.SimpleEventRunner;
import io.inugami.dashboard.core.domain.engine.plugin.PluginEventCron;
import io.inugami.framework.commons.cron.CronResolver;
import io.inugami.framework.commons.threads.ThreadsExecutorService;
import io.inugami.framework.configuration.models.EventConfig;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.configuration.models.plugins.PluginConfiguration;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.models.event.Event;
import io.inugami.framework.interfaces.models.event.GenericEvent;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.models.event.TargetConfig;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.processors.ProcessorModel;
import io.inugami.framework.interfaces.providers.Provider;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.Callable;

@SuppressWarnings({"java:S2153"})
@Slf4j
public class EnginePluginService implements IEnginePluginService {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String                               DEFAULT_CRON        = "0 * * * * ?";
    public static final String                               EMPTY               = "";
    public static final Callable<EnginePluginEventResultDTO> NO_PROVIDER_DEFINED =
            () -> EnginePluginEventResultDTO.builder()
                                            .status(Status.ERROR)
                                            .message("no provider defined")
                                            .build();
    private final       Plugin                               plugin;
    private final       ZoneOffset                           zoneOffset;
    @Getter
    private final       ThreadsExecutorService               threadsExecutorService;
    private final       List<PluginEventCron>                events              = new ArrayList<>();
    private final       List<Provider>                       providers;
    private final       List<Processor>                      processors;
    private final       long                                 timeout;
    private final       Collection<EngineListener>           listeners;

    // =================================================================================================================
    // INIT
    // =================================================================================================================
    @Builder
    public EnginePluginService(final Plugin plugin,
                               final List<Provider> providers,
                               final List<Processor> processors,
                               final ZoneOffset zoneOffset,
                               final long timeout,
                               final ThreadsExecutorService threadsExecutorService,
                               final Collection<EngineListener> listeners) {
        this.providers              = providers;
        this.processors             = processors;
        this.plugin                 = plugin;
        this.zoneOffset             = zoneOffset;
        this.threadsExecutorService = threadsExecutorService;
        this.timeout                = timeout < 1000
                                      ? Double.valueOf(EngineService.DEFAULT_TIMEOUT * 0.9).longValue()
                                      : timeout;
        this.listeners              = listeners;
        initializeEvents();
    }


    protected void initializeEvents() {
        if (!isEnabled()) {
            return;
        }
        for (EventConfig eventConfig : Optional.ofNullable(plugin.getEvents()).orElse(List.of())) {
            final String defaultCron = Optional.ofNullable(eventConfig.getScheduler()).orElse(DEFAULT_CRON);

            for (Event event : Optional.ofNullable(eventConfig.getEvents()).orElse(List.of())) {
                events.add(PluginEventCron.builder()
                                          .event(event)
                                          .eventConfig(eventConfig)
                                          .cron(CronResolver.of(Optional.ofNullable(event.getScheduler())
                                                                        .orElse(defaultCron)))
                                          .build());
            }

            for (SimpleEvent event : Optional.ofNullable(eventConfig.getSimpleEvents()).orElse(List.of())) {
                events.add(PluginEventCron.builder()
                                          .simpleEvent(event)
                                          .eventConfig(eventConfig)
                                          .cron(CronResolver.of(Optional.ofNullable(event.getScheduler())
                                                                        .orElse(defaultCron)))
                                          .build());
            }
        }
    }

    protected boolean isEnabled() {
        return Optional.ofNullable(plugin)
                       .map(Plugin::getConfig)
                       .map(PluginConfiguration::getEnable)
                       .orElse(false);
    }

    // =================================================================================================================
    // ACCEPT
    // =================================================================================================================
    @Override
    public boolean hasEventsToRun(@NonNull final LocalDateTime now) {
        for (PluginEventCron event : Optional.ofNullable(events).orElse(List.of())) {
            if (event.getCron().willFire(now, zoneOffset)) {
                return true;
            }
        }
        return false;
    }

    // =================================================================================================================
    // RUN
    // =================================================================================================================
    @Override
    public EnginePluginResultDTO run(final @NonNull Collection<EngineListener> inputListeners,
                                     final @NonNull LocalDateTime now) {

        final List<EnginePluginEventResultDTO> eventsDone       = new ArrayList<>();
        final List<EngineListener>             currentListeners = new ArrayList<>();
        currentListeners.addAll(Optional.ofNullable(listeners).orElse(List.of()));
        currentListeners.addAll(Optional.ofNullable(inputListeners).orElse(List.of()));
        currentListeners.add(new EngineListener() {
            @Override
            public void onEventDone(final Plugin plugin,
                                    final GenericEvent<?> event,
                                    final EnginePluginEventResultDTO data) {
                eventsDone.add(data);
            }
        });


        final List<PluginEventCron> eventsToRun = events.stream()
                                                        .filter(eventCron -> eventCron.getCron()
                                                                                      .willFire(now, zoneOffset))
                                                        .toList();

        if (eventsToRun.isEmpty()) {
            return EnginePluginResultDTO.builder().gav(plugin.getGav()).status(Status.SUCCESS).build();
        }

        final List<Callable<EnginePluginEventResultDTO>> tasks = new ArrayList<>();
        for (PluginEventCron eventCron : eventsToRun) {
            if (eventCron.getSimpleEvent() != null) {
                tasks.add(runSimpleEvent(eventCron.getSimpleEvent(), now, currentListeners));
            }
            if (eventCron.getEvent() != null) {
                tasks.add(runEvent(eventCron.getEvent(), now, currentListeners));
            }
        }

        try {
            log.debug("executing plugin event");
            threadsExecutorService.runAndGrab(tasks, timeout);
        } catch (TechnicalException e) {
            log.error(e.getMessage(), e);
        }

        Collections.sort(eventsDone);
        return EnginePluginResultDTO.builder()
                                    .gav(plugin.getGav())
                                    .status(resolveStatus(eventsDone))
                                    .events(eventsDone)
                                    .build();
    }

    private Status resolveStatus(final List<EnginePluginEventResultDTO> eventsDone) {
        Status result = Status.SUCCESS;
        for (EnginePluginEventResultDTO eventDone : eventsDone) {
            final Status itemStatus = Optional.ofNullable(eventDone.getStatus()).orElse(Status.SUCCESS);
            if (itemStatus.ordinal() > result.ordinal()) {
                result = itemStatus;
            }
        }
        return result;
    }


    // =================================================================================================================
    // RUN EVENTS
    // =================================================================================================================
    protected Callable<EnginePluginEventResultDTO> runSimpleEvent(@NonNull final SimpleEvent simpleEvent,
                                                                  @NonNull final LocalDateTime now,
                                                                  @NonNull final Collection<EngineListener> currentListeners) {
        final Optional<Provider> provider = getProvider(simpleEvent.getProvider());
        if (provider.isEmpty()) {
            return NO_PROVIDER_DEFINED;
        }


        return SimpleEventRunner.builder()
                                .event(simpleEvent)
                                .now(now)
                                .plugin(plugin)
                                .processors(getProcessors(simpleEvent.getProcessors()))
                                .provider(provider.get())
                                .listeners(currentListeners)
                                .timeout(Double.valueOf(timeout * 0.9).longValue())
                                .zoneOffset(zoneOffset)
                                .build()
                                .run();
    }

    private Callable<EnginePluginEventResultDTO> runEvent(@NonNull final Event event,
                                                          @NonNull final LocalDateTime now,
                                                          @NonNull final Collection<EngineListener> currentListeners) {
        final Set<String>         providers  = new LinkedHashSet<>();
        final Set<ProcessorModel> processors = new LinkedHashSet<>();

        Optional.ofNullable(event.getProvider()).ifPresent(providers::add);
        Optional.ofNullable(event.getProcessors()).ifPresent(processors::addAll);

        Optional.ofNullable(event.getTargets())
                .orElse(List.of())
                .stream()
                .map(TargetConfig::getProvider)
                .filter(Objects::nonNull)
                .forEach(providers::add);

        Optional.ofNullable(event.getTargets())
                .orElse(List.of())
                .stream()
                .map(TargetConfig::getProcessors)
                .filter(Objects::nonNull)
                .forEach(processors::addAll);


        return EventRunner.builder()
                          .event(event)
                          .listeners(currentListeners)
                          .now(now)
                          .timeout(Double.valueOf(timeout * 0.9).longValue())
                          .threadsExecutorService(threadsExecutorService)
                          .providers(providers.stream()
                                              .map(this::getProvider)
                                              .filter(Optional::isPresent)
                                              .map(Optional::get)
                                              .toList())
                          .plugin(plugin)
                          .processors(getProcessors(new ArrayList<>(processors)))
                          .zoneOffset(zoneOffset)
                          .build()
                          .run();
    }

    // =================================================================================================================
    // GETTERS
    // =================================================================================================================
    protected Optional<Provider> getProvider(final String providerName) {
        final var name = providerName == null ? EMPTY : providerName;
        return providers.stream().filter(provider -> name.equalsIgnoreCase(provider.getName())).findFirst();
    }

    protected List<Processor> getProcessors(final List<ProcessorModel> processorNames) {
        final List<String> names = Optional.ofNullable(processorNames)
                                           .orElse(List.of())
                                           .stream()
                                           .map(ProcessorModel::getName)
                                           .filter(Objects::nonNull)
                                           .map(String::toUpperCase)
                                           .toList();
        return processors.stream()
                         .filter(processor -> names.contains(Optional.ofNullable(processor.getName())
                                                                     .orElse(EMPTY)
                                                                     .toUpperCase()))
                         .toList();
    }
}
