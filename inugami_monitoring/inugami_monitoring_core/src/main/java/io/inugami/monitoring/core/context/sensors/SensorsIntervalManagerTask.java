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
import io.inugami.framework.commons.threads.MonitoredThreadFactory;
import io.inugami.framework.commons.threads.RunAndCloseService;
import io.inugami.framework.interfaces.ctx.BootstrapContext;
import io.inugami.framework.interfaces.models.tools.Chrono;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;
import io.inugami.framework.interfaces.monitoring.senders.MonitoringSender;
import io.inugami.framework.interfaces.monitoring.sensors.MonitoringSensor;
import io.inugami.monitoring.core.context.MonitoringContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SensorsIntervalManagerTask(final int maxTheads, final long interval, final List<MonitoringSender> senders) {
        this.maxTheads = maxTheads;
        this.interval  = interval;
        timeout        = (long) (interval * 0.9);
        executor       =
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
            MdcService.getInstance().initialize();
            final int nbThreads = tasks.size() < maxTheads ? tasks.size() : maxTheads;
            final RunAndCloseService<List<GenericMonitoringModel>> sensorThreads = new RunAndCloseService<>(nameSensor,
                                                                                                            timeout,
                                                                                                            nbThreads,
                                                                                                            tasks);
            final Chrono                             chrono  = Chrono.startChrono();
            final List<List<GenericMonitoringModel>> rawData = sensorThreads.run();
            sensorThreads.forceShutdown();
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
                long timeoutSender = timeout - chrono.getDuration();
                if (timeoutSender < 300) {
                    Loggers.METRICS.warn("no enough time for processing metrics senders : {}ms", timeoutSender);
                    timeoutSender = 300;
                }
                final int maxSenderThreads = senderTasks.size() < maxTheads ? senderTasks.size() : maxTheads;
                final RunAndCloseService<Void> sendersThreads = new RunAndCloseService<>(nameSender, timeoutSender,
                                                                                         maxSenderThreads, senderTasks);

                sendersThreads.run();
                sendersThreads.forceShutdown();
            }
        }

        @Builder
        @AllArgsConstructor
        public static class SensorTask implements Callable<List<GenericMonitoringModel>> {
            private final MonitoringSensor                                sensor;
            private final Map<String, String>                             mdc;
            private final CompletableFuture<List<GenericMonitoringModel>> future;

            @Override
            public List<GenericMonitoringModel> call() throws Exception {
                final var result = sensor.process();
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
