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

import java.util.List;

import org.inugami.api.models.events.GenericEvent;
import org.inugami.api.providers.task.ProviderTask;

/**
 * FutureData
 * 
 * @author patrick_guillerm
 * @since 9 août 2017
 */
public class FutureDataResult<T> {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String                   channel;
    
    private final T                        data;
    
    private final GenericEvent             evente;
    
    private final List<OnDoneFunction<T>>  onDone;
    
    private final List<OnErrorFunction<T>> onError;
    
    private final ProviderTask             task;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public FutureDataResult(final String channel, final T data, final GenericEvent evente,
                            final List<OnDoneFunction<T>> onDone, final List<OnErrorFunction<T>> onError,
                            final ProviderTask task) {
        super();
        this.channel = channel;
        this.data = data;
        this.evente = evente;
        this.onDone = onDone;
        this.onError = onError;
        this.task = task;
    }
    
    // =========================================================================
    // GETTERS
    // =========================================================================
    
    public String getChannel() {
        return channel;
    }
    
    public T getData() {
        return data;
    }
    
    public GenericEvent getEvente() {
        return evente;
    }
    
    public List<OnDoneFunction<T>> getOnDone() {
        return onDone;
    }
    
    public List<OnErrorFunction<T>> getOnError() {
        return onError;
    }
    
    public ProviderTask getTask() {
        return task;
    }
}
