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
package io.inugami.core.context.runner;

import java.util.List;
import java.util.concurrent.Future;

import io.inugami.api.providers.concurrent.FutureData;
import io.inugami.api.providers.task.ProviderFutureResult;

/**
 * EventRunnerFuture
 * 
 * @author patrick_guillerm
 * @since 16 janv. 2017
 */
public class EventRunnerFuture {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String                                         hash;
    
    private final List<Future<FutureData<ProviderFutureResult>>> futures;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public EventRunnerFuture(final String hash, final List<Future<FutureData<ProviderFutureResult>>> futures) {
        this.hash = hash;
        this.futures = futures;
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public boolean isDone() {
        boolean allDone = true;
        if (futures != null) {
            for (final Future<FutureData<ProviderFutureResult>> future : futures) {
                allDone = future.isDone();
                if (!allDone) {
                    break;
                }
            }
        }
        return allDone;
    }
    
    public List<Future<FutureData<ProviderFutureResult>>> getFutures() {
        return futures;
    }
    
    public String getHash() {
        return hash;
    }
    
}
