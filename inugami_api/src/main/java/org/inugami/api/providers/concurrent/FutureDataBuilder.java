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
package org.inugami.api.providers.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.inugami.api.models.events.GenericEvent;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.providers.task.ProviderTask;

/**
 * FutureDataBuilder
 * 
 * @author patrick_guillerm
 * @since 9 août 2017
 */
public class FutureDataBuilder<T> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private transient Future<T>                future;
    
    private T                                  data;
    
    private long                               timeout;
    
    private TimeUnit                           timeUnit;
    
    private transient List<OnErrorFunction<T>> onError = new ArrayList<>();
    
    private transient List<OnDoneFunction<T>>  onDone  = new ArrayList<>();
    
    private GenericEvent                       event;
    
    private transient ProviderTask             task;
    
    private String                             channel;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public FutureDataBuilder() {
        super();
    }
    
    public FutureDataBuilder(final FutureData<T> futureData) {
        if (futureData != null) {
            future = futureData.getFuture();
            task = futureData.getTask();
            timeout = futureData.getTimeout();
            timeUnit = futureData.getTimeUnit();
            event = futureData.getEvent();
            //
            onError = futureData.onError();
            onDone = futureData.onDone();
            channel = futureData.getChannel();
            data = futureData.getData();
        }
    }
    
    public FutureData<T> build() {
        return new FutureDataModel<T>(future, task, timeout, timeUnit, event, channel, onDone, onError, data);
    }
    
    public static FutureData<ProviderFutureResult> buildDefaultFuture(final long timeout) {
        return new FutureDataBuilder<ProviderFutureResult>().addTimeout(timeout).build();
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public FutureDataBuilder<T> addFuture(final Future<T> future) {
        this.future = future;
        return this;
    }
    
    public FutureDataBuilder<T> addImmediateFuture(final T data) {
        this.future = new ImmediateFutureData<T>(data);
        return this;
    }
    
    public FutureDataBuilder<T> addTimeout(final long timeout) {
        this.timeout = timeout;
        return this;
    }
    
    public FutureDataBuilder<T> addOnDone(final OnDoneFunction<T> onDone) {
        this.onDone.add(onDone);
        return this;
    }
    
    public FutureDataBuilder<T> addOnError(final OnErrorFunction<T> onError) {
        this.onError.add(onError);
        return this;
    }
    
    public FutureDataBuilder<T> addTimeUnit(final TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }
    
    public FutureDataBuilder<T> addEvent(final GenericEvent event) {
        this.event = event;
        return this;
    }
    
    public FutureDataBuilder<T> addTask(final ProviderTask task) {
        this.task = task;
        return this;
    }
    
    public FutureDataBuilder<T> addChannel(final String channel) {
        this.channel = channel;
        return this;
    }
    
    public FutureDataBuilder<T> addData(final T data) {
        this.data = data;
        return this;
    }
}
