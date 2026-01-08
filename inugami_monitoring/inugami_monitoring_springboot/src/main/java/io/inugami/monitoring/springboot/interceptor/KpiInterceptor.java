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
package io.inugami.monitoring.springboot.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.inugami.framework.api.tools.RunSafeUtils;
import io.inugami.framework.commons.threads.ThreadsExecutorService;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.monitoring.ErrorResult;
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import io.inugami.framework.interfaces.monitoring.data.ResponseData;
import io.inugami.framework.interfaces.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.framework.interfaces.monitoring.kpi.KpiExtractorContext;
import io.inugami.framework.interfaces.monitoring.kpi.KpiExtractorSPI;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModelDTO;
import io.inugami.framework.interfaces.spi.SpiLoader;
import io.inugami.monitoring.core.sensors.ServicesSensor;
import io.inugami.monitoring.springboot.config.InugamiMonitoringProperties;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 * @since 2026-01-08
 */
@Builder
@RequiredArgsConstructor
@Component
public class KpiInterceptor implements MonitoringFilterInterceptor {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String APPLICATION_JSON = "application/json";
    public static final String GET              = "GET";
    public static final String POST             = "POST";
    public static final String PATCH            = "PATCH";
    public static final String PUT              = "PUT";
    public static final String THREAD_NAME      = "KpiInterceptor";
    public static final long   TIMEOUT          = 60000L;

    private final InugamiMonitoringProperties monitoringProperties;
    private final ObjectMapper                objectMapper;
    private final Clock                       clock;

    private final ThreadsExecutorService executor     = new ThreadsExecutorService(THREAD_NAME,
                                                                                   5,
                                                                                   false,
                                                                                   TIMEOUT);
    private final List<KpiExtractorSPI>  extractors   = new ArrayList<>();
    private final List<Pattern>          skipPatterns = new ArrayList<>();
    private final AtomicBoolean          enabled      = new AtomicBoolean(false);

    // =================================================================================================================
    // BUILDER
    // =================================================================================================================
    @Override
    public MonitoringFilterInterceptor buildInstance(final ConfigHandler<String, String> configuration) {
        final List<KpiExtractorSPI> providers = RunSafeUtils.runSafeOrElse(() -> SpiLoader.getInstance()
                                                                                          .loadSpiService(KpiExtractorSPI.class), new ArrayList<>());
        extractors.addAll(extractors);

        final var config = Optional.ofNullable(monitoringProperties)
                                   .map(InugamiMonitoringProperties::getInterceptors)
                                   .map(InugamiMonitoringProperties.InugamiMonitoringPropertiesInterceptors::getKpi)
                                   .orElse(InugamiMonitoringProperties.KpiInterceptors.builder()
                                                                                      .build());
        enabled.set(config.isEnabled());

        if (config.getSkipUrl() != null) {
            final String[] skipUrls = config.getSkipUrl().split(";");
            for (String skipUrl : skipUrls) {
                skipPatterns.add(Pattern.compile(skipUrl));
            }
        }
        return this;
    }
    // =================================================================================================================
    // BUILDER
    // =================================================================================================================

    @Override
    public List<GenericMonitoringModel> onBegin(final RequestData request) {
        if (skip(request) || extractors.isEmpty()) {
            return List.of();
        }


        final List<Callable<List<GenericMonitoringModelDTO>>> tasks = new ArrayList<>();
        final KpiExtractorContext kpiContext = KpiExtractorContext.builder()
                                                                  .request(request.toBuilder().build())
                                                                  .now(LocalDateTime.now(clock))
                                                                  .requestContent(extractRequestJsonNode(request))
                                                                  .build();
        for (KpiExtractorSPI extractor : extractors) {
            tasks.add(() -> extractor.extractFromRequest(kpiContext));
        }

        RunSafeUtils.runSafeVoid(() -> executor.run(tasks, this::onTaskDone));

        return List.of();
    }


    @Override
    public List<GenericMonitoringModel> onDone(final RequestData request,
                                               final ResponseData response,
                                               final ErrorResult error) {
        List<GenericMonitoringModel> result = new ArrayList<>();
        if (skip(request) || extractors.isEmpty()) {
            return result;
        }

        final List<Callable<List<GenericMonitoringModelDTO>>> tasks = new ArrayList<>();
        final KpiExtractorContext kpiContext = KpiExtractorContext.builder()
                                                                  .request(request.toBuilder().build())
                                                                  .now(LocalDateTime.now(clock))
                                                                  .requestContent(extractRequestJsonNode(request))
                                                                  .response(response.toBuilder().build())
                                                                  .responseContent(extractResponseJsonNode(response,request.getMethod()))
                                                                  .build();
        for (KpiExtractorSPI extractor : extractors) {
            tasks.add(() -> extractor.extractFromRequest(kpiContext));
        }

        RunSafeUtils.runSafeVoid(() -> executor.run(tasks, this::onTaskDone));

        return result;
    }


    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    private boolean skip(final RequestData request) {
        if (!enabled.get()) {
            return true;
        }


        if (skipPatterns.isEmpty()) {
            return false;
        }

        for (Pattern skipPattern : skipPatterns) {
            if (skipPattern.matcher(request.getUri()).matches()) {
                return false;
            }
        }
        return true;
    }


    protected JsonNode extractRequestJsonNode(final RequestData request) {
        JsonNode requestJson = null;
        if (!APPLICATION_JSON.equalsIgnoreCase(request.getContentType())) {
            return requestJson;
        }

        if (validRequestVerb(request.getMethod())) {
            requestJson = RunSafeUtils.runSafe(() -> objectMapper.readTree(request.getContent()));
        }
        return requestJson;
    }

    private JsonNode extractResponseJsonNode(final ResponseData response, final String method) {
        JsonNode json = null;
        if (!APPLICATION_JSON.equalsIgnoreCase(response.getContentType())) {
            return json;
        }

        if (validResponseVerb(method)) {
            json = RunSafeUtils.runSafe(() -> objectMapper.readTree(response.getContent()));
        }
        return json;

    }

    private boolean validRequestVerb(final String method) {
        return POST.equalsIgnoreCase(method)
               || PATCH.equalsIgnoreCase(method)
               || PUT.equalsIgnoreCase(method);
    }

    private boolean validResponseVerb(final String method) {
        return GET.equalsIgnoreCase(method)
               || POST.equalsIgnoreCase(method)
               || PATCH.equalsIgnoreCase(method)
               || PUT.equalsIgnoreCase(method);
    }

    protected void onTaskDone(final List<GenericMonitoringModelDTO> data,
                              final Callable<List<GenericMonitoringModelDTO>> listCallable) {
        ServicesSensor.addData(data);
    }
}
