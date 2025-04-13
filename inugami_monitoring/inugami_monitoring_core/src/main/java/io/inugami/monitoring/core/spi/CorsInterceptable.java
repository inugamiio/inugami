package io.inugami.monitoring.core.spi;

import io.inugami.api.configurtation.ConfigurationSpiFactory;
import io.inugami.api.monitoring.cors.CorsHeadersSpi;
import io.inugami.api.monitoring.data.ResquestData;
import io.inugami.api.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.api.monitoring.models.GenericMonitoringModel;
import io.inugami.api.monitoring.models.Headers;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.spi.SpiLoader;
import io.inugami.monitoring.core.context.MonitoringBootstrapService;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"java:S5122", "java:S1168"})
@NoArgsConstructor
public class CorsInterceptable implements MonitoringFilterInterceptor {
    private static final String                        SEP = ", ";
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private              ConfigHandler<String, String> configuration;
    private              List<CorsHeadersSpi>          corsHeaders;
    private              Headers                       headers;
    private              Boolean                       enabled;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    @Override
    public MonitoringFilterInterceptor buildInstance(final ConfigHandler<String, String> configuration) {
        return new CorsInterceptable(configuration);
    }

    public CorsInterceptable(final ConfigHandler<String, String> configuration) {
    }

    // =========================================================================
    // API
    // =========================================================================
    @Override
    public List<GenericMonitoringModel> onBegin(final ResquestData request) {
        if (enabled == null) {
            corsHeaders = SpiLoader.getInstance().loadSpiServicesByPriority(CorsHeadersSpi.class);
            headers = MonitoringBootstrapService.getContext().getConfig().getHeaders();
            enabled = ConfigurationSpiFactory.INSTANCE.getBooleanProperty("inugami.monitoring.cors.enabled", true);
        }

        if (enabled) {
            final List<String> currentHeaders = resolveHeaders(request);
            final String       headerStr      = String.join(SEP, currentHeaders);

            request.getHttpResponse().setHeader("Access-Control-Allow-Origin", "*");
            request.getHttpResponse()
                   .setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
            request.getHttpResponse().setHeader("Access-Control-Allow-Headers", headerStr);
            request.getHttpResponse().setHeader("Access-Control-Expose-Headers", headerStr);
            request.getHttpResponse().setHeader("Access-Control-Allow-Credentials", "true");
        }
        return null;
    }

    private List<String> resolveHeaders(final ResquestData request) {
        final Set<String> result = new LinkedHashSet<>();

        if (corsHeaders != null && headers != null) {
            for (final CorsHeadersSpi resolver : corsHeaders) {
                final List<String> resultSet = resolver.buildCorsHeaders(request, headers, configuration);
                if (resultSet != null) {
                    result.addAll(resultSet);
                }
            }
        }
        return new ArrayList<>(result);
    }
}
