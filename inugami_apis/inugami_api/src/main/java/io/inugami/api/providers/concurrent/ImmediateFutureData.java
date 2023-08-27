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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * ImmediateFutureData
 * 
 * @author patrick_guillerm
 * @since 9 août 2017
 */
public class ImmediateFutureData<T> implements Future<T> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final T data;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ImmediateFutureData(final T data) {
        super();
        this.data = data;
    }
    
    // =========================================================================
    // GET
    // =========================================================================
    @Override
    public T get() throws InterruptedException, ExecutionException {
        return data;
    }
    
    @Override
    public T get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException,
                                                          TimeoutException {
        return data;
    }
    
    // =========================================================================
    // OTHERS
    // =========================================================================
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return true;
    }
    
    @Override
    public boolean isCancelled() {
        return false;
    }
    
    @Override
    public boolean isDone() {
        return true;
    }
    
}
