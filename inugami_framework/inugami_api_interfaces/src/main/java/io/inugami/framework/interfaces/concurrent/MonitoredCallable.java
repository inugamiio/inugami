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
package io.inugami.framework.interfaces.concurrent;


import io.inugami.framework.interfaces.models.tools.Chrono;
import io.inugami.framework.interfaces.monitoring.MdcServiceSpiFactory;
import io.inugami.framework.interfaces.monitoring.threads.MonitoredCallableListener;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
@Builder
public class MonitoredCallable<T> implements Callable<T> {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private final Map<String, Serializable>       mdc;
    private final List<MonitoredCallableListener> listeners;
    private final Callable<T>                     callable;

    // =================================================================================================================
    // CALL
    // =================================================================================================================
    @Override
    public T call() throws Exception {
        if (mdc != null) {
            MdcServiceSpiFactory.getInstance().setMdc(mdc);
        }

        invokeListenerOnStart();

        final Chrono chrono = Chrono.startChrono();
        Throwable    error  = null;
        T            result = null;
        try {
            result = callable == null ? null : callable.call();
        } catch (Throwable e) {
            error = e;
            throw e;
        } finally {
            chrono.stop();
            invokeListenerOnDone(result, chrono, error);
        }
        return result;
    }

    // =================================================================================================================
    // EVENTS
    // =================================================================================================================
    private void invokeListenerOnStart() {
        if (listeners != null) {
            for (MonitoredCallableListener listener : listeners) {
                try {
                    listener.start(this);
                } catch (Throwable e) {
                }
            }
        }
    }

    private void invokeListenerOnDone(final T currentResult, final Chrono chrono, final Throwable error) {
        if (listeners != null) {
            for (MonitoredCallableListener listener : listeners) {
                try {
                    listener.done(this, currentResult, chrono, error);
                } catch (Throwable e) {
                }
            }
        }
    }

    // =================================================================================================================
    // BUILDER
    // =================================================================================================================
    public static class MonitoredCallableBuilder<T> {
        public MonitoredCallable<T> build() {

            final MonitoredCallable<T> result = new MonitoredCallable<>(MdcServiceSpiFactory.getInstance()
                                                                                            .getAllMdcExtended(), listeners, callable);
            if (listeners != null) {
                for (MonitoredCallableListener listener : listeners) {
                    try {
                        listener.created(result, System.currentTimeMillis());
                    } catch (Throwable e) {
                    }
                }
            }
            return result;
        }

        public MonitoredCallableBuilder<T> addListener(MonitoredCallableListener listener) {
            if (listeners == null) {
                listeners = new ArrayList<>();
            }
            if (listener != null) {
                listeners.add(listener);
            }
            return this;
        }
    }
}
