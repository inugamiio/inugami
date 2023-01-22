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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.inugami.api.ctx.BootstrapContext;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.tools.Chrono;
import io.inugami.api.monitoring.MdcService;
import io.inugami.api.monitoring.models.GenericMonitoringModel;
import io.inugami.api.monitoring.senders.MonitoringSender;
import io.inugami.api.monitoring.sensors.MonitoringSensor;
import io.inugami.api.tools.CalendarTools;
import io.inugami.commons.threads.MonitoredThreadFactory;
import io.inugami.commons.threads.RunAndCloseService;
import io.inugami.monitoring.core.context.MonitoringContext;

/**
 * SensorsIntervalManagerTask
 * 
 * @author patrickguillerm
 * @since Jan 17, 2019
 */
public class SensorsIntervalManagerTask implements BootstrapContext<MonitoringContext> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final int                                          maxTheads;
    
    private final long                                         interval;
    
    private final long                                         timeout;
    
    private final ScheduledExecutorService                     executor;
    
    private final List<Callable<List<GenericMonitoringModel>>> tasks = new ArrayList<>();
    
    private final String                                       nameSensor;
    
    private final String                                       nameSender;
    
    private final List<MonitoringSender>                       senders;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SensorsIntervalManagerTask(final int maxTheads, final long interval, final List<MonitoringSender> senders) {
        super();
        this.maxTheads = maxTheads;
        this.interval = interval;
        timeout = (long) (interval * 0.9);
        executor = Executors.newSingleThreadScheduledExecutor(new MonitoredThreadFactory(getClass().getSimpleName(),
                                                                                         false));
        final String name = String.join("_", SensorsIntervalManagerTask.class.getSimpleName(),
                                        String.valueOf(interval) + "ms");
        this.nameSensor = name + "_sensor";
        this.nameSender = name + "_sender";
        this.senders = senders;
    }
    
    // =========================================================================
    // LIFECYCLE
    // =========================================================================
    @Override
    public void bootrap(final MonitoringContext ctx) {
        final Calendar calendar = CalendarTools.buildCalendarByMin();
        calendar.add(Calendar.MINUTE, 1);
        
        final long now = CalendarTools.buildCalendar().getTimeInMillis();
        final long nextMin = calendar.getTimeInMillis();
        executor.scheduleAtFixedRate(new SensorsIntervalTask(), nextMin - now, interval, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void shutdown(final MonitoringContext ctx) {
        executor.shutdown();
        if (!executor.isShutdown()) {
            try {
                executor.awaitTermination(0, TimeUnit.MILLISECONDS);
            }
            catch (final InterruptedException e) {
                Loggers.DEBUG.error(e.getMessage(), e);
            }
        }
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public void add(final MonitoringSensor sensor) {
        this.tasks.add(new SensorTask(sensor));
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
            final Chrono chrono = Chrono.startChrono();
            final List<List<GenericMonitoringModel>> rawData = sensorThreads.run();
            sensorThreads.forceShutdown();
            chrono.stop();
            final List<GenericMonitoringModel> data = new ArrayList<>();
            
            if (rawData != null) {
                rawData.forEach(data::addAll);
            }
            
            final List<Callable<Void>> senderTasks = new ArrayList<>();
            if (!data.isEmpty() && (senders != null)) {
                for (final MonitoringSender sender : senders) {
                    senderTasks.add(new SenderTask(sender, data));
                }
                
            }
            
            if (!senderTasks.isEmpty()) {
                long timeoutSender = timeout - chrono.getDelaisInMillis();
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
    }
    
    private class SensorTask implements Callable<List<GenericMonitoringModel>> {
        private final MonitoringSensor sensor;
        
        public SensorTask(final MonitoringSensor sensor) {
            this.sensor = sensor;
        }
        
        @Override
        public List<GenericMonitoringModel> call() throws Exception {
            return sensor.process();
        }
    }
    
    private class SenderTask implements Callable<Void> {
        private final MonitoringSender             sender;
        
        private final List<GenericMonitoringModel> data;
        
        public SenderTask(final MonitoringSender sender, final List<GenericMonitoringModel> data) {
            this.sender = sender;
            this.data = data;
        }
        
        @Override
        public Void call() throws Exception {
            sender.process(data);
            return null;
        }
        
    }
    
}
