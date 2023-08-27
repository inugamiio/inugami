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
package io.inugami.core.context.scripts;

import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.data.graphite.DataPoint;
import io.inugami.api.models.data.graphite.GraphiteTarget;
import io.inugami.api.models.data.graphite.GraphiteTargets;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.providers.Provider;
import io.inugami.api.providers.ProviderForce;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.core.context.Context;
import lombok.experimental.UtilityClass;

import java.util.concurrent.*;

import static io.inugami.core.context.scripts.JsCommonsHelper.convertStrValue;
import static io.inugami.core.context.scripts.JsCommonsHelper.isNotNull;

/**
 * JsProviderHelper
 *
 * @author patrick_guillerm
 * @since 27 déc. 2017
 */
@UtilityClass
@SuppressWarnings({"java:S2142", "java:S3655"})
public class JsProviderHelper {

    // =========================================================================
    // METHODS
    // =========================================================================
    public static JsonObject callProvider(final String providerName, final String query, final String from,
                                          final String until) {
        JsonObject     result   = null;
        final Provider provider = Context.getInstance().getProvider(providerName);

        if (provider instanceof ProviderForce && isNotNull(query)) {
            //@formatter:off
            final SimpleEvent event = SimpleEvent.simpleEventBuilder()
                                                .name("callGraphiteProvider")
                                                .from(convertStrValue(from))
                                                .until(convertStrValue(until))
                                                .query(convertStrValue(query))
                                                .build();
            //@formatter:on
            final Future<ProviderFutureResult> providerResultFuture = CompletableFuture.supplyAsync(() -> ((ProviderForce) provider).callEvent(event));

            final long timeout = Context.getInstance()
                                        .getApplicationConfiguration()
                                        .getScriptTimeout();
            ProviderFutureResult providerResult = null;

            try {
                providerResult = providerResultFuture.get(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                Loggers.DEBUG.error(e.getMessage());
            }

            if ((providerResult != null) && providerResult.getData().isPresent()) {
                result = providerResult.getData().get();
            }
        }
        return result;
    }

    @SuppressWarnings({"java:S4201"})
    public static JsonObject callGraphiteProvider(final String providerName, final String query, final String from,
                                                  final String until) {

        final JsonObject rawResult = callProvider(convertStrValue(providerName), convertStrValue(query),
                                                  convertStrValue(from), convertStrValue(until));

        if (rawResult == null || !(rawResult instanceof GraphiteTargets)) {
            return rawResult;
        }

        final GraphiteTargets localResult = new GraphiteTargets();

        for (final GraphiteTarget target : ((GraphiteTargets) rawResult).getTargets()) {
            final GraphiteTarget newTarget = new GraphiteTarget();
            newTarget.setTarget(resolveTarget(target));

            for (final DataPoint dataPoint : target.getDatapoints()) {
                if (dataPoint.getValue() == null) {
                    continue;
                }
                newTarget.addDatapoint(dataPoint.getValue(), dataPoint.getTimestamp());
            }
            if (!newTarget.getDatapoints().isEmpty()) {
                localResult.addTarget(newTarget);
            }
        }
        return localResult;
    }

    private static String resolveTarget(final GraphiteTarget target) {
        return target.getTarget() == null ? "unknowTargetName" : target.getTarget();
    }

}
