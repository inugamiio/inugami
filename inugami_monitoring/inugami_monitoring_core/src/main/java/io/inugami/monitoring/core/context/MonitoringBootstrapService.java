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
package io.inugami.monitoring.core.context;

import io.inugami.framework.interfaces.monitoring.MonitoringLoaderSpi;
import io.inugami.framework.interfaces.monitoring.models.Monitoring;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

/**
 * MonitoringBootstrap
 *
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
@RequiredArgsConstructor
@Builder
public class MonitoringBootstrapService {

    // =================================================================================================================
    // ATTRIBUTE
    // =================================================================================================================
    public static final AtomicReference<MonitoringContext> CONTEXT = new AtomicReference<>();
    private final       MonitoringLoaderSpi                loader;


    // =================================================================================================================
    // INITIALIZE
    // =================================================================================================================
    public MonitoringBootstrapService initialize() {
        final Monitoring monitoring = loader.load();

        final MonitoringContext monitoringContext = MonitoringContext.builder()
                                                                     .config(monitoring)
                                                                     .build();
        monitoringContext.initialize(null);
        CONTEXT.set(monitoringContext);
        return this;
    }


    public void shutdown() {
        final var context = getContext();
        if (context != null) {
            context.shutdown(null);
        }
    }


    // =================================================================================================================
    // GETTER
    // =================================================================================================================
    public static MonitoringContext getContext() {
        return CONTEXT.get();
    }
}
