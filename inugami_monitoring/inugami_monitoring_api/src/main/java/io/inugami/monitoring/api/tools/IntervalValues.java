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
package io.inugami.monitoring.api.tools;

import io.inugami.api.ctx.BootstrapContext;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.loggers.Loggers;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * IntervalValuesScheduled
 *
 * @author patrickguillerm
 * @since Jan 17, 2019
 */
@SuppressWarnings({"java:S2142", "java:S1181"})
public class IntervalValues<T> implements BootstrapContext<Void> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final Queue<T> values = new LinkedBlockingQueue<>();

    private final ScheduledExecutorService executor;

    private final Supplier<T> handler;

    private final Consumer<Queue<T>> consumer;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public IntervalValues(final Supplier<T> handler, final long interval) {
        this(handler, null, interval);
    }

    public IntervalValues(final Consumer<Queue<T>> consumer, final long interval) {
        this(null, consumer, interval);
    }

    public IntervalValues(final Supplier<T> handler, final Consumer<Queue<T>> consumer, final long interval) {
        super();
        Asserts.assertTrue("interval delais must be equals or bigger than 100ms!", interval >= 100);
        this.handler = handler;
        this.consumer = consumer;
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.executor.scheduleAtFixedRate(new IntervalValuesTask(), 0, interval, TimeUnit.MILLISECONDS);
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void shutdown(final Void ctx) {
        executor.shutdown();
        if (!executor.isShutdown()) {
            try {
                executor.awaitTermination(0, TimeUnit.MILLISECONDS);
            } catch (final Throwable e) {
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

    public void addValues(final List<T> values) {
        if (values != null) {
            this.values.addAll(values);
        }
    }

    public void addValue(final T value) {
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
