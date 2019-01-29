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
package org.inugami.commons.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;

import org.inugami.api.providers.concurrent.FutureData;

/**
 * WaittingFutureData
 * 
 * @author patrick_guillerm
 * @since 29 sept. 2017
 */
public class WaittingFutureData<T> {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public List<T> wait(final List<FutureData<T>> futures) {
        return wait(futures, null, null);
    }
    
    public List<T> wait(final List<FutureData<T>> futures, final BiConsumer<T, FutureData<T>> onDone) {
        return wait(futures, onDone, null);
    }
    
    public List<T> wait(final List<FutureData<T>> futures, final BiConsumer<T, FutureData<T>> onDone,
                        final BiConsumer<Exception, FutureData<T>> onError) {
        final List<T> result = new ArrayList<>();
        //@formatter:off
        final BiConsumer<T,FutureData<T>>           functionOnDone  = onDone  != null ? onDone  : (data,task) -> {};
        final BiConsumer<Exception, FutureData<T>>  functionOnError = onError != null ? onError : (error,task) -> {};
        //@formatter:on
        
        final List<CompletableFuture<T>> completableFutures = new ArrayList<>();
        for (final FutureData<T> futureData : futures) {
            completableFutures.add(buildFuture(futureData, functionOnDone, functionOnError));
        }
        
        final CompletableFuture<?>[] type = {};
        CompletableFuture.allOf(completableFutures.toArray(type));
        
        return result;
    }
    
    // =========================================================================
    // BUILDER
    // =========================================================================
    private CompletableFuture<T> buildFuture(final FutureData<T> futureData, final BiConsumer<T, FutureData<T>> onDone,
                                             final BiConsumer<Exception, FutureData<T>> onError) {
        final CompletableFuture<T> result = CompletableFuture.supplyAsync(() -> {
            T returnResult = null;
            
            try {
                final T futureResult = futureData.getFuture().get(futureData.getTimeout(), futureData.getTimeUnit());
                returnResult = futureResult;
                onDone.accept(futureResult, futureData);
                if (futureData.onDone() != null) {
                    futureData.onDone().forEach(func -> func.onDone(futureResult, futureData.getEvent(),
                                                                    futureData.getChannel()));
                }
                
            }
            catch (InterruptedException | ExecutionException | TimeoutException e) {
                onError.accept(e, futureData);
                if (futureData.onError() != null) {
                    futureData.onError().forEach(func -> func.onError(futureData.getEvent(), futureData.getChannel(),
                                                                      futureData.getTask(), e));
                }
            }
            
            return returnResult;
        });
        
        return result;
    }
    
}
