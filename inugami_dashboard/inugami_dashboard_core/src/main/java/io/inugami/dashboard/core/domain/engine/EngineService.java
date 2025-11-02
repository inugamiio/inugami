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
import io.inugami.dashboard.api.domain.engine.IEngineService;
import io.inugami.dashboard.api.domain.engine.dto.EnginePluginResultDTO;
import io.inugami.dashboard.api.domain.engine.dto.EngineResultDTO;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.commons.threads.ThreadsExecutorService;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.processors.Processor;
import io.inugami.framework.interfaces.providers.Provider;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Builder
public class EngineService implements IEngineService {


    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public static final String                            MAX_THREADS          = "maxThreads";
    public static final String                            TIMEOUT              = "timeout";
    public static final int                               DEFAULT_TIMEOUT      = 60_000;
    public static final int                               MIN_TIMEOUT          = 1000;
    private final       Map<String, IEnginePluginService> enginePluginServices = new LinkedHashMap<>();
    private final       List<Provider>                    providers            = new ArrayList<>();
    private final       List<Processor>                   processors           = new ArrayList<>();
    private final       Clock                             clock;
    private final       ZoneOffset                        zoneOffset;
    private final       Collection<EngineListener>        listeners;
    private final       List<Plugin>                      plugins;
    private final       ThreadsExecutorService            threadsExecutorInternal;
    private final       ThreadsExecutorService            threadsExecutor;
    private final       long                              timeout;


    //==================================================================================================================
    // INIT
    //==================================================================================================================
    public EngineService init() {
        for (Plugin plugin : getPlugins()) {
            providers.addAll(Optional.ofNullable(plugin.getProviders()).orElse(List.of()));
            processors.addAll(Optional.ofNullable(plugin.getProcessors()).orElse(List.of()));
        }

        for (Plugin plugin : getPlugins()) {
            final var timeout    = getPluginTimeout(plugin.getConfig().getProperties());
            final var maxThreads = getPluginMaxThread(plugin.getConfig().getProperties());
            final var threadPool = new ThreadsExecutorService("ENGINE_PLUGIN_" + plugin.getGav().getHash(),
                                                              maxThreads,
                                                              false,
                                                              timeout);

            enginePluginServices.put(plugin.getGav().getHash(),
                                     EnginePluginService.builder()
                                                        .plugin(plugin)
                                                        .threadsExecutorService(threadPool)
                                                        .zoneOffset(zoneOffset)
                                                        .providers(providers)
                                                        .processors(processors)
                                                        .timeout(timeout)
                                                        .build());
        }
        return this;
    }


    //==================================================================================================================
    // RUN
    //==================================================================================================================
    @Override
    public void run() {
        threadsExecutorInternal.submit(UUID.randomUUID().toString(), () -> processRun());
    }

    protected EngineResultDTO processRun() {
        final var mdc = MdcService.getInstance();
        mdc.processId(UUID.randomUUID().toString());
        final Collection<EngineListener>            currentListeners = getListeners();
        final List<Callable<EnginePluginResultDTO>> tasks            = new ArrayList<>();
        final Map<String, EnginePluginResultDTO>    pluginStatus     = new ConcurrentHashMap<>();
        final LocalDateTime                         now              = LocalDateTime.now(clock);

        for (Plugin plugin : getPlugins()) {
            final IEnginePluginService enginePluginService = getPluginEngine(plugin);
            if (enginePluginService.hasEventsToRun(now)) {
                tasks.add(PluginCallable.builder()
                                        .plugin(plugin)
                                        .callable(() -> enginePluginService.run(currentListeners, now))
                                        .build());

                pluginStatus.put(plugin.getGav().getHash(),
                                 EnginePluginResultDTO.builder()
                                                      .gav(plugin.getGav())
                                                      .status(Status.RUNNING)
                                                      .build());
            }
        }

        if (tasks.isEmpty()) {
            log.debug("no task to run");
            return EngineResultDTO.builder().status(Status.NOTHING_TO_DO).build();
        }

        try {
            log.debug("executing plugins");
            threadsExecutor.runAndGrab(tasks,
                                       (value, task) -> onDone(value, task, pluginStatus),
                                       (error, task) -> onError(error, task, pluginStatus),
                                       timeout);
        } catch (TechnicalException e) {
            log.error(e.getMessage(), e);
        }

        log.debug("computing status");
        final var builder = EngineResultDTO.builder()
                                           .traceId(mdc.traceId())
                                           .processId(mdc.processId())
                                           .start(LocalDateTime.now(clock));


        builder.end(LocalDateTime.now(clock));
        builder.status(computStatus(pluginStatus));

        final var result = builder.build();
        sendOnDone(result);
        return result;
    }

    protected Status computStatus(final Map<String, EnginePluginResultDTO> pluginStatus) {
        for (Map.Entry<String, EnginePluginResultDTO> status : pluginStatus.entrySet()) {
            if (Status.ERROR == status.getValue().getStatus() || Status.RUNNING == status.getValue().getStatus()) {
                return Status.ERROR;
            }
            if (Status.WARN == status.getValue().getStatus()) {
                return Status.WARN;
            }
        }
        return Status.SUCCESS;
    }


    //==================================================================================================================
    // EVENT
    //==================================================================================================================
    protected void onDone(final EnginePluginResultDTO value,
                          final Callable<EnginePluginResultDTO> task,
                          final Map<String, EnginePluginResultDTO> pluginStatus) {
        if (task instanceof PluginCallable pluginTask) {
            EnginePluginResultDTO status = pluginStatus.get(pluginTask.getPlugin().getGav().getHash());
            if (status == null) {
                status = EnginePluginResultDTO.builder()
                                              .gav(pluginTask.getPlugin().getGav())
                                              .status(value.getStatus())
                                              .build();
                pluginStatus.put(pluginTask.getPlugin().getGav().getHash(), status);
            } else {
                status.setEvents(value.getEvents());
                status.setStatus(value.getStatus());
            }
        }
    }

    protected void onError(final Exception error,
                           final Callable<EnginePluginResultDTO> task,
                           final Map<String, EnginePluginResultDTO> pluginStatus) {
        log.error(error.getMessage(), error);
        if (task instanceof PluginCallable pluginTask) {
            EnginePluginResultDTO status = pluginStatus.get(pluginTask.getPlugin().getGav().getHash());
            if (status == null) {
                status = EnginePluginResultDTO.builder()
                                              .gav(pluginTask.getPlugin().getGav())
                                              .status(Status.ERROR)
                                              .build();
                pluginStatus.put(pluginTask.getPlugin().getGav().getHash(), status);
            } else {
                status.setStatus(Status.ERROR);
            }

        }
    }

    protected void sendOnDone(@NonNull final EngineResultDTO engineResult) {
        listeners.forEach(listener -> listener.onDone(engineResult));
    }


    //==================================================================================================================
    // GETTERS
    //==================================================================================================================
    protected @NonNull Collection<EngineListener> getListeners() {
        return Optional.ofNullable(listeners).orElse(List.of());
    }

    protected @NonNull Collection<Plugin> getPlugins() {
        return Optional.ofNullable(plugins).orElse(List.of());
    }

    protected long getPluginTimeout(@Nullable final Map<String, String> properties) {
        final String timeoutStr = Optional.ofNullable(properties).orElse(Map.of()).get(TIMEOUT);
        final var    timeout    = timeoutStr == null ? DEFAULT_TIMEOUT : Long.parseLong(timeoutStr);
        return timeout < MIN_TIMEOUT ? DEFAULT_TIMEOUT : timeout;
    }

    protected int getPluginMaxThread(@Nullable final Map<String, String> properties) {
        final String maxThreads = Optional.ofNullable(properties).orElse(Map.of()).get(MAX_THREADS);
        return maxThreads == null ? 20 : Integer.parseInt(maxThreads);
    }

    protected @NonNull IEnginePluginService getPluginEngine(@NonNull final Plugin plugin) {
        return enginePluginServices.get(plugin.getGav().getHash());
    }

    public Collection<ThreadsExecutorService> getPluginsThreadPool() {
        return enginePluginServices.entrySet()
                                   .stream()
                                   .map(Map.Entry::getValue)
                                   .map(IEnginePluginService::getThreadsExecutorService)
                                   .toList();
    }
}
