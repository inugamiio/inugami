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
package org.inugami.monitoring.api.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.inugami.api.ctx.BootstrapContext;
import org.inugami.api.exceptions.Asserts;
import org.inugami.api.loggers.Loggers;

/**
 * IntervalValuesScheduled
 * 
 * @author patrickguillerm
 * @since Jan 17, 2019
 */
public class IntervalValues<T> implements BootstrapContext<Void> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final Queue<T>                 values = new LinkedBlockingQueue<T>();
    
    private final ScheduledExecutorService executor;
    
    private final Supplier<T>              handler;
    
    private final Consumer<Queue<T>>       consumer;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public IntervalValues(Supplier<T> handler, final long interval) {
        this(handler, null, interval);
    }
    
    public IntervalValues(Consumer<Queue<T>> consumer, final long interval) {
        this(null, consumer, interval);
    }
    
    public IntervalValues(Supplier<T> handler, Consumer<Queue<T>> consumer, final long interval) {
        super();
        Asserts.isTrue("interval delais must be equals or bigger than 100ms!", interval >= 100);
        this.handler = handler;
        this.consumer = consumer;
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.executor.scheduleAtFixedRate(new IntervalValuesTask(), 0, interval, TimeUnit.MILLISECONDS);
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void shutdown(Void ctx) {
        executor.shutdown();
        if (!executor.isShutdown()) {
            try {
                executor.awaitTermination(0, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) {
                Loggers.DEBUG.error(e.getMessage(), e);
            }
        }
    }
    
    public List<T> poll() {
        final List<T> result = new ArrayList<>();
        
        while (!values.isEmpty()) {
            result.add(values.poll());
        }
        return result;
    }
    
    public void addValues(List<T> values) {
        if (values != null) {
            this.values.addAll(values);
        }
    }
    
    public void addValue(T value) {
        if (value != null) {
            this.values.add(value);
        }
    }
    
    // =========================================================================
    // THREAD
    // =========================================================================
    private class IntervalValuesTask implements Runnable {
        @Override
        public void run() {
            if (handler != null) {
                values.add(handler.get());
            }
            if (consumer != null) {
                consumer.accept(values);
            }
        }
    }
}
