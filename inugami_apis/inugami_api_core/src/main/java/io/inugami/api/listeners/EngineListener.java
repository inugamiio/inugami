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
package io.inugami.api.listeners;

import java.util.Map;

import io.inugami.api.models.data.ClientSendData;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.models.tools.Chrono;

/**
 * EngineListener
 * 
 * @author patrick_guillerm
 * @since 15 déc. 2016
 */
public interface EngineListener {
    
    // =========================================================================
    // Change
    // =========================================================================
    default void onStartDayTimeChange(final String startDayTime) {
    }
    
    // =========================================================================
    // PROVIDER
    // =========================================================================
    default void onCallBeginProvider(final String providerType, final String request) {
    }
    
    default void onCallEndProvider(final String providerType, final String request, final int status,
                                   final String result, final long delayTime) {
    }
    
    // =========================================================================
    // Process Events
    // =========================================================================
    default void onStartEvent(final String gav, final GenericEvent event, final Map<String, Chrono> runningEvents) {
    }
    
    default void onStopEvent(final String gav, final GenericEvent event, final Map<String, Chrono> runningEvents) {
    }
    
    default void onStartJobExecution() {
    }
    
    default void onStopJobExecution() {
    }
    
    default void onProcessRunning(final int nbProcessed, final int nbEvents) {
    }
    
    default void onSendEventData(final String eventName, final ClientSendData data, final String json) {
    }
    
    // =========================================================================
    // ERRORS
    // =========================================================================
    default void onErrorInRunningJob(final String message, final Exception exception) {
    }
    
    default void onErrorInEvent(final GenericEvent event, final String fromBegin, final Exception exception) {
    }
    
    default void onErrorCallEndProvider(final String providerType, final String request, final int status,
                                        final String message, final long delayTime) {
    }
    
}
