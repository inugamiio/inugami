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
package io.inugami.monitoring.core.context.sensors;

import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.api.tools.RunSafeUtils;
import io.inugami.framework.commons.threads.MonitoredThreadFactory;
import io.inugami.framework.commons.threads.ThreadsExecutorService;
import io.inugami.framework.interfaces.ctx.BootstrapContext;
import io.inugami.framework.interfaces.models.CurrentApplicationDTO;
import io.inugami.framework.interfaces.models.tools.Chrono;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;
import io.inugami.framework.interfaces.monitoring.senders.MonitoringSender;
import io.inugami.framework.interfaces.monitoring.sensors.MonitoringSensor;
import io.inugami.monitoring.core.context.MonitoringContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

import static io.inugami.framework.api.tools.RunSafeUtils.runSafeVoid;
import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

/**
 * SensorsIntervalManagerTask
 *
 * @author patrickguillerm
 * @since Jan 17, 2019
 */
@Slf4j
@SuppressWarnings({"java:S2142", "java:S1181"})
public class SensorsIntervalManagerTask implements BootstrapContext<MonitoringContext> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final int maxTheads;

    private final long interval;

    private final long timeout;

    private final ScheduledExecutorService executor;

    private final List<Callable<List<GenericMonitoringModel>>> tasks = new ArrayList<>();

    private final String nameSensor;

    private final String nameSender;

    private final List<MonitoringSender> senders;
    private final CurrentApplicationDTO  currentApplication;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @Builder
    public SensorsIntervalManagerTask(final int maxTheads,
                                      final long interval,
                                      final List<MonitoringSender> senders,
                                      final CurrentApplicationDTO currentApplication) {
        this.currentApplication = currentApplication;
        this.maxTheads          = maxTheads;
        this.interval           = interval;
        timeout                 = (long) (interval * 0.9);
        executor                =
                Executors.newSingleThreadScheduledExecutor(new MonitoredThreadFactory(getClass().getSimpleName(),
                                                                                      false));
        final String name = String.join("_", SensorsIntervalManagerTask.class.getSimpleName(),
                                        String.valueOf(interval) + "ms");
        this.nameSensor = name + "_sensor";
        this.nameSender = name + "_sender";
        this.senders    = senders;
    }

    // =========================================================================
    // LIFECYCLE
    // =========================================================================
    @Override
    public void initialize(final MonitoringContext ctx) {
        executor.scheduleAtFixedRate(new SensorsIntervalTask(), 0, interval, TimeUnit.MILLISECONDS);
    }

    @Override
    public void shutdown(final MonitoringContext ctx) {
        executor.shutdown();
        if (!executor.isShutdown()) {
            runSafeVoid(() -> executor.awaitTermination(0, TimeUnit.MILLISECONDS), log);
        }
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public CompletableFuture<List<GenericMonitoringModel>> add(final MonitoringSensor sensor) {
        final CompletableFuture<List<GenericMonitoringModel>> future = new CompletableFuture<>();
        this.tasks.add(SensorsIntervalTask.SensorTask.builder()
                                                     .mdc(MdcService.getInstance().getAllMdc())
                                                     .currentApplication(currentApplication)
                                                     .sensor(sensor)
                                                     .future(future)
                                                     .build());
        return future;
    }

    // =========================================================================
    // THREAD
    // =========================================================================
    private class SensorsIntervalTask implements Runnable {

        @Override
        public void run() {
            RunSafeUtils.runSafeVoid(this::process);
        }

        private void process() {
            MdcService.getInstance().initialize();
            int nbThreads = tasks.size() < maxTheads ? tasks.size() : maxTheads;
            if (nbThreads < 1) {
                nbThreads = 10;
            }

            final var engineThreadsExecutorService = new ThreadsExecutorService(
                    "SensorsIntervalTask_" + UUID.randomUUID(),
                    nbThreads,
                    false,
                    timeout);

            final Chrono chrono = Chrono.startChrono();
            final List<List<GenericMonitoringModel>> rawData =
                    RunSafeUtils.runSafeOrElse(() -> engineThreadsExecutorService.runAndGrab(tasks), List.of());


            chrono.stop();
            final List<GenericMonitoringModel> data = new ArrayList<>();
            applyIfNotNull(rawData, v -> v.forEach(data::addAll));

            final List<Callable<Void>> senderTasks = new ArrayList<>();
            if (data.isEmpty()) {
                return;
            }
            for (final MonitoringSender sender : Optional.ofNullable(senders).orElse(List.of())) {
                senderTasks.add(SenderTask.builder()
                                          .sender(sender)
                                          .data(data)
                                          .build());
            }

            if (!senderTasks.isEmpty()) {
                RunSafeUtils.runSafeVoid(() -> {
                    engineThreadsExecutorService.runAndGrab(senderTasks);
                });
            }

            engineThreadsExecutorService.shutdown();
        }

        @Builder
        @AllArgsConstructor
        public static class SensorTask implements Callable<List<GenericMonitoringModel>> {
            private final MonitoringSensor                                sensor;
            private final Map<String, String>                             mdc;
            private final CompletableFuture<List<GenericMonitoringModel>> future;
            private final CurrentApplicationDTO                           currentApplication;

            @Override
            public List<GenericMonitoringModel> call() throws Exception {
                final var result = Optional.ofNullable(sensor.process()).orElse(List.of());

                for (GenericMonitoringModel data : result) {
                    data.setDate(LocalDateTime.now(Clock.systemUTC()));
                    if(currentApplication!=null){
                        data.setGroupId(currentApplication.getGroupId());
                        data.setArtifactId(currentApplication.getArtifactId());
                        data.setVersion(currentApplication.getVersion());
                        data.setCommitId(currentApplication.getCommitId());
                        data.setCommitDate(currentApplication.getCommitDate());
                    }
                }
                future.complete(result);
                return result;
            }
        }

        @Builder
        @AllArgsConstructor
        public static class SenderTask implements Callable<Void> {
            private final MonitoringSender             sender;
            private final List<GenericMonitoringModel> data;


            @Override
            public Void call() throws Exception {
                sender.process(data);
                return null;
            }

        }

    }
}
