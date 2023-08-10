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
package io.inugami.core.listerners;

import io.inugami.api.exceptions.services.EventProcessException;
import io.inugami.api.listeners.EngineListener;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.data.ClientSendData;
import io.inugami.api.models.events.GenericEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * CoreListerner
 *
 * @author patrick_guillerm
 * @since 20 déc. 2016
 */
@SuppressWarnings({"java:S3655"})
@Slf4j
public class CoreListerner implements EngineListener {


    // =========================================================================
    // PROVIDER
    // =========================================================================
    @Override
    public void onCallBeginProvider(final String providerType, final String request) {
        if (Loggers.PROVIDER.isDebugEnabled()) {
            Loggers.PROVIDER.info("[{}] calling... ({})", providerType, request);
        } else {
            Loggers.PROVIDER.info("[{}] calling...", providerType);
        }
    }

    @Override
    public void onCallEndProvider(final String providerType, final String request, final int status,
                                  final String result, final long delay) {
        Loggers.PROVIDER.info("[{}][{}ms] calling done with code {}", providerType, delay, status);
    }

    @Override
    public void onErrorCallEndProvider(final String providerType, final String request, final int status,
                                       final String message, final long delay) {
        Loggers.PROVIDER.error("[{}][{}ms] calling done with code {} - {}\n request:{}", providerType, delay, status,
                               message, request);
    }

    // =========================================================================
    // EVENTS
    // =========================================================================

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStartDayTimeChange(final String startDayTime) {
        log.info("start time change for : {}", startDayTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStartJobExecution() {
        Loggers.EVENTS.info("start processing events...");
    }

    @Override
    public void onStopJobExecution() {
        Loggers.EVENTS.info("stop processing events");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onProcessRunning(final int nbProcessed, final int nbEvents) {
        Loggers.EVENTS.info("runing event process : {}/{}", nbProcessed, nbEvents);
    }

    @Override
    public void onSendEventData(final String eventName, final ClientSendData data, final String json) {
        if (Loggers.SSE.isDebugEnabled()) {
            Loggers.SSE.info("send data to \"{}\" event\n\tdata={}", eventName, json);
        } else {
            Loggers.SSE.info("send data to \"{}\" event", eventName);
        }
    }

    // =========================================================================
    // ERRORS
    // =========================================================================
    @Override
    public void onErrorInEvent(final GenericEvent event, final String fromBegin, final Exception exception) {
        final EventProcessException except = new EventProcessException(event, null, exception.getMessage(), exception);
        onErrorInRunningJob(exception.getMessage(), except);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onErrorInRunningJob(final String message, final Exception exception) {
        if (exception instanceof EventProcessException) {
            final String fullMessage = renderMsgEventProcessExcept((EventProcessException) exception);
            Loggers.DEBUG.error(fullMessage, exception);

        } else {
            Loggers.DEBUG.error(message, exception);
        }
    }

    // =========================================================================
    // render
    // =========================================================================
    private String renderMsgEventProcessExcept(final EventProcessException exception) {
        final StringBuilder result = new StringBuilder();
        result.append("Error on event:");

        if (exception.getEvent().isPresent()) {
            result.append(exception.getEvent().get().getName());
            result.append('\n');
            result.append('\t').append("event:").append(exception.getEvent().get());
        } else {
            result.append("null");
        }
        result.append('\n');
        result.append('\t').append("target:");
        if (exception.getTarget().isPresent()) {
            result.append(exception.getTarget().get());
        } else {
            result.append("null");
        }
        result.append('\n');
        return result.toString();
    }

}
