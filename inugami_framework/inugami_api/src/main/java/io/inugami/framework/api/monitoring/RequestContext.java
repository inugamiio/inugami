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
package io.inugami.framework.api.monitoring;

import io.inugami.framework.interfaces.monitoring.MonitoringLoaderSpi;
import io.inugami.framework.interfaces.monitoring.RequestInformation;
import io.inugami.framework.interfaces.monitoring.models.Monitoring;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.UUID;

/**
 * RequestContext
 *
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
@UtilityClass
public final class RequestContext {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static Monitoring config = loadConfig();

    private static final ThreadLocal<RequestInformation> INSTANCE = new ThreadLocal<>();


    // =========================================================================
    // METHODS
    // =========================================================================
    public static synchronized void clean() {
        INSTANCE.remove();
    }

    public static synchronized Monitoring loadConfig() {
        if (config == null) {
            final List<MonitoringLoaderSpi>          services       = new ArrayList<>();
            final ServiceLoader<MonitoringLoaderSpi> servicesLoader = ServiceLoader.load(MonitoringLoaderSpi.class);
            servicesLoader.forEach(services::add);

            if (!services.isEmpty()) {
                config = services.get(0).load();
            }

        }
        return config;
    }

    public static synchronized RequestInformation getInstance() {
        RequestInformation result = INSTANCE.get();
        if (result == null) {
            result = initializeTechnicalRequest();
        }
        return result;
    }

    public static synchronized void setInstance(final RequestInformation instance) {
        INSTANCE.set(instance);
    }

    // =========================================================================
    // INIT
    // =========================================================================
    public static RequestInformation initializeTechnicalRequest() {
        final RequestInformation.RequestInformationBuilder builder = RequestInformation.builder();


        if (config != null) {
            builder.env(config.getEnv());
            builder.asset(config.getAsset());
            builder.hostname(config.getHostname());
            builder.instanceName(config.getInstanceName());
            builder.instanceNumber(config.getInstanceNumber());
            builder.applicationVersion(config.getApplicationVersion());
        }
        builder.deviceIdentifier("system");
        builder.correlationId(UUID.randomUUID().toString());
        final String traceId = UUID.randomUUID().toString();
        builder.requestId(traceId);
        builder.traceId(traceId);
        builder.service(String.join("_", "technical", Thread.currentThread().getName()));

        final RequestInformation result = builder.build();
        RequestContext.setInstance(result);
        return result;
    }
}
