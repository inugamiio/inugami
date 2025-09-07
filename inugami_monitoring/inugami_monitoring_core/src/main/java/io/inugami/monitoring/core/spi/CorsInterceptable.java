package io.inugami.monitoring.core.spi;

import io.inugami.framework.api.configurtation.ConfigurationSpiFactory;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.monitoring.core.CorsHeadersSpi;
import io.inugami.framework.interfaces.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;
import io.inugami.framework.interfaces.spi.SpiLoader;
import lombok.NoArgsConstructor;
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"java:S5122", "java:S1168"})
public class CorsInterceptable implements MonitoringFilterInterceptor {
    private static final String                        SEP = ", ";
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private              ConfigHandler<String, String> configuration;
    private List<CorsHeadersSpi> corsHeaders;
    private Boolean              enabled;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    @Override
    public MonitoringFilterInterceptor buildInstance(final ConfigHandler<String, String> configuration) {
        return new CorsInterceptable(configuration);
    }
    public CorsInterceptable() {
        enabled=true;
    }
    public CorsInterceptable(final ConfigHandler<String, String> configuration) {
    }

    // =========================================================================
    // API
    // =========================================================================
    @Override
    public List<GenericMonitoringModel> onBegin(final RequestData request) {
        if (enabled == null) {
            corsHeaders = SpiLoader.getInstance().loadSpiServicesByPriority(CorsHeadersSpi.class);
            enabled = ConfigurationSpiFactory.INSTANCE.getBooleanProperty("inugami.monitoring.cors.enabled", true);
        }

        if (enabled) {
            final List<String> currentHeaders = resolveHeaders(request);
            final String       headerStr      = String.join(SEP, currentHeaders);

            request.getResponse().setHeader("Access-Control-Allow-Origin", "*");
            request.getResponse()
                   .setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
            request.getResponse().setHeader("Access-Control-Allow-Headers", headerStr);
            request.getResponse().setHeader("Access-Control-Expose-Headers", headerStr);
            request.getResponse().setHeader("Access-Control-Allow-Credentials", "true");
        }
        return null;
    }

    private List<String> resolveHeaders(final RequestData request) {
        final Set<String> result = new LinkedHashSet<>();

        if (corsHeaders != null && request.getHeaders() != null) {
            for (final CorsHeadersSpi resolver : corsHeaders) {
                final List<String> resultSet = resolver.buildCorsHeaders(request,  configuration);
                if (resultSet != null) {
                    result.addAll(resultSet);
                }
            }
        }
        return new ArrayList<>(result);
    }
}
