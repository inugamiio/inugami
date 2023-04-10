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

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.metrics.events.MetricsEvents;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.providers.Provider;
import io.inugami.api.providers.concurrent.FutureData;
import io.inugami.api.providers.task.EventCompositeResult;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.configuration.models.plugins.Plugin;
import io.inugami.core.context.Context;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * CallableSimpleEvent
 *
 * @author patrick_guillerm
 * @since 9 août 2017
 */
class CallableSimpleEventProcessor {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final Context context;

    private final Plugin plugin;

    private final long timeout;

    private final AlertingsProcessor alertingProcessor;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    CallableSimpleEventProcessor(final Plugin plugin, final Context context, final long timeout) {
        this.plugin = plugin;
        this.context = context;
        this.timeout = timeout;
        this.alertingProcessor = new AlertingsProcessor();
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    ProviderFutureResult process(final SimpleEvent localEvent) {
        if (!localEvent.getProvider().isPresent()) {
            throw new CallableEventException("provider is mandatory for event : {0}", localEvent.getName());
        }
        final Provider provider = context.getProvider(localEvent.getProvider().get());
        return callProvider(localEvent, provider);
    }

    ProviderFutureResult callProvider(final SimpleEvent localEvent, final Provider provider) {
        Asserts.assertNotNull(localEvent);
        MetricsEvents.onStart(plugin.getGav(), localEvent.getName());
        ProviderFutureResult result = null;

        final FutureData<ProviderFutureResult> future = provider.callEvent(localEvent, plugin.getGav());

        final ProviderFutureResult data;
        try {
            data = future.getFuture().get(timeout, TimeUnit.MILLISECONDS);
            result = this.callEventDone(data, localEvent);
        } catch (final InterruptedException | ExecutionException | TimeoutException e) {
            Loggers.PLUGINS.error("{} : {}", localEvent.getName(), e.getMessage());
            Loggers.DEBUG.error(e.getMessage(), e);
        } finally {
            MetricsEvents.onStop(plugin.getGav(), localEvent.getName());
        }

        JsonObject dataResult = null;
        if ((result != null) && result.getData().isPresent()) {
            dataResult = result.getData().get();
        }

        final List<AlertingResult> alerts = alertingProcessor.computeAlerts(plugin.getGav(), localEvent, result);

        return new EventCompositeResult(null, null, dataResult, localEvent, localEvent.getScheduler(),
                                        localEvent.getScheduler(), alerts);
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    private ProviderFutureResult callEventDone(final ProviderFutureResult data, final GenericEvent currentEvent) {
        ProviderFutureResult result = data;
        if (currentEvent.getProcessors().isPresent()) {
            result = context.applyProcessors(data, currentEvent);
        }
        return result;
    }

}
