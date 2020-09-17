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
package io.inugami.api.providers.concurrent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.providers.task.ProviderTask;

/**
 * FutureDataModel
 * 
 * @author patrick_guillerm
 * @since 9 août 2017
 */
public class FutureDataModel<T> implements FutureData<T> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final Future<T>                future;
    
    private final T                        data;
    
    private final ProviderTask             task;
    
    private final GenericEvent             event;
    
    private final String                   channel;
    
    private final long                     timeout;
    
    private final TimeUnit                 timeUnit;
    
    private final List<OnErrorFunction<T>> onError;
    
    private final List<OnDoneFunction<T>>  onDone;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    FutureDataModel(final Future<T> future, final ProviderTask task, final long timeout, final TimeUnit timeUnit,
                    final GenericEvent event, final String channel, final List<OnDoneFunction<T>> onDone,
                    final List<OnErrorFunction<T>> onError, final T data) {
        super();
        this.future = future;
        this.task = task;
        this.event = event;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.channel = channel;
        this.onDone = onDone;
        this.onError = onError;
        this.data = data;
    }
    
    @Override
    public String toString() {
        //@formatter:off
        return new StringBuilder(this.getClass().getSimpleName())
                .append('@')
                .append(System.identityHashCode(this))
                .append('[')
                .append("done=").append(future.isDone())
                .append(", event=").append(event == null ? null : event.getName())
                .append(", channel=").append(channel)
                .append(", timeout=").append(timeout)
                .append(", timeUnit=").append(timeUnit)
                .append(']')
                .toString();
        //@formatter:on
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public Future<T> getFuture() {
        return future;
    }
    
    @Override
    public GenericEvent getEvent() {
        return event;
    }
    
    @Override
    public String getChannel() {
        return channel;
    }
    
    @Override
    public long getTimeout() {
        return timeout;
    }
    
    @Override
    public List<OnDoneFunction<T>> onDone() {
        return Collections.synchronizedList(onDone);
    }
    
    @Override
    public List<OnErrorFunction<T>> onError() {
        return Collections.synchronizedList(onError);
    }
    
    @Override
    public TimeUnit getTimeUnit() {
        return timeUnit == null ? TimeUnit.MILLISECONDS : timeUnit;
    }
    
    @Override
    public ProviderTask getTask() {
        return task;
    }
    
    @Override
    public T getData() {
        return data;
    }
    
    public List<OnErrorFunction<T>> getOnError() {
        return onError;
    }
    
    public List<OnDoneFunction<T>> getOnDone() {
        return onDone;
    }
    
}
