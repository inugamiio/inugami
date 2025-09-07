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
package io.inugami.monitoring.core.spi;


import io.inugami.framework.api.exceptions.WarningContext;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.exceptions.Warning;
import io.inugami.framework.interfaces.models.JsonBuilder;
import io.inugami.framework.interfaces.monitoring.ErrorResult;
import io.inugami.framework.interfaces.monitoring.IoLogContentDisplayResolverSPI;
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import io.inugami.framework.interfaces.monitoring.data.ResponseData;
import io.inugami.framework.interfaces.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;
import io.inugami.framework.interfaces.spi.SpiLoader;
import io.inugami.monitoring.api.obfuscators.ObfuscatorTools;
import org.springframework.web.servlet.HandlerMapping;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * LogInterceptor
 *
 * @author patrickguillerm
 * @since Jan 7, 2019
 */
@SuppressWarnings({"java:S3457", "java:S2629", "java:S1168"})
public class IoLogInterceptor implements MonitoringFilterInterceptor {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final AtomicReference<List<IoLogContentDisplayResolverSPI>> DISPLAY_RESOLVERS = new AtomicReference<>();
    private static final String                                                EMPTY             = "";
    public static final  String                                                URL_SEPARATOR     = "/";
    private final        String                                                inputDecorator;
    private final        String                                                outputDecorator;
    private final        boolean                                               enableDecorator;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public IoLogInterceptor() {
        enableDecorator = true;
        inputDecorator = "[IN] ";
        outputDecorator = "[OUT] ";
    }
    public IoLogInterceptor(final List<IoLogContentDisplayResolverSPI> resolvers) {
        this();
        DISPLAY_RESOLVERS.set(resolvers);
    }

    public IoLogInterceptor(final ConfigHandler<String, String> configuration) {
        enableDecorator = configuration.grabBoolean("enableDecorator", true);
        inputDecorator = configuration.grabOrDefault("inputDecorator", "[IN] ");
        outputDecorator = configuration.grabOrDefault("outputDecorator", "[OUT] ");
    }

    @Override
    public MonitoringFilterInterceptor buildInstance(final ConfigHandler<String, String> configuration) {
        return new IoLogInterceptor(configuration);
    }

    @Override
    public String getName() {
        return "iolog";
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public List<GenericMonitoringModel> onBegin(final RequestData request) {
        initDisplayResolvers();

        final JsonBuilder msg = buildIologIn(request);
        MdcService.getInstance().lifecycleIn();
        Loggers.IOLOG.info(
                (enableDecorator ? inputDecorator : EMPTY) + ObfuscatorTools.applyObfuscators(msg.toString()));
        MdcService.getInstance().lifecycleRemove();
        return null;
    }

    private void initDisplayResolvers() {
        if (DISPLAY_RESOLVERS.get() == null) {
            DISPLAY_RESOLVERS.set(SpiLoader.getInstance()
                                           .loadSpiServicesByPriority(IoLogContentDisplayResolverSPI.class));
        }
    }

    protected JsonBuilder buildIologIn(final RequestData request) {
        final JsonBuilder result   = new JsonBuilder();
        final String      fullPath = resolveFullPath(request);
        result.openList().write(request.getMethod()).closeList();
        result.writeSpace().write(fullPath).line();
        result.write("headers :").line();

        for (Map.Entry<String, String> header : Optional.ofNullable(request.getHeaders())
                                                        .orElse(new LinkedHashMap<>())
                                                        .entrySet()) {
            result.tab().write(header.getKey()).write(":").writeSpace().write(header.getValue()).line();
        }

        result.write("payload :").line();
        if (shouldDisplayContent(request)) {
            result.write(request.getContent() == null ? JsonBuilder.VALUE_NULL : request.getContent());
        }
        return result;
    }

    private boolean shouldDisplayContent(final RequestData request) {
        for (final IoLogContentDisplayResolverSPI resolver : Optional.ofNullable(DISPLAY_RESOLVERS.get())
                                                                     .orElse(List.of())) {
            if (!resolver.display(request)) {
                return false;
            }
        }
        return true;
    }

    protected String resolveFullPath(final RequestData request) {
        final StringBuilder result = new StringBuilder();
        if (request.getContextPath() != null && !request.getUri().startsWith(request.getContextPath())) {
            result.append(request.getContextPath());
            if (!request.getUri().startsWith(URL_SEPARATOR)) {
                result.append(URL_SEPARATOR);
            }
        }
        result.append(request.getUri());
        return result.toString();
    }

    @SuppressWarnings({"java:S3776"})
    @Override
    public List<GenericMonitoringModel> onDone(final RequestData request,
                                               final ResponseData httpResponse,
                                               final ErrorResult error) {
        final JsonBuilder msg = buildIologIn(request);

        msg.addLine();
        msg.write("response:").line();
        msg.tab().write("status:").write(httpResponse.getCode()).line();
        msg.tab().write("datetime:").write(httpResponse.getDatetime()).line();
        msg.tab().write("duration:").write(httpResponse.getDuration()).line();
        msg.tab().write("contentType:").write(httpResponse.getContentType()).line();
        msg.tab().write("headers:").line();
        for (final Map.Entry<String, String> entry : Optional.ofNullable(httpResponse.getHearder())
                                                             .orElse(new LinkedHashMap<>())
                                                             .entrySet()) {
            msg.tab().tab().write(entry.getKey()).write(":").writeSpace().write(entry.getValue()).line();
        }


        final List<Warning> warns = WarningContext.getInstance().getWarnings();
        if (!warns.isEmpty()) {
            msg.tab().write("warning:").line();
            for (final Warning warning : warns) {
                msg.tab().tab().write(warning.getWarningCode()).write(":").line();
                msg.tab().tab().tab().write(warning.getMessage());
                if (warning.getMessageDetail() != null) {
                    msg.write(":").write(warning.getMessageDetail());
                }
                msg.line();
            }
        }
        if (shouldDisplayContent(request)) {
            msg.tab().write("payload:").write(httpResponse.getContent());
        }

        //@formatter:on
        MdcService.getInstance().lifecycleOut();
        if (error != null || httpResponse.getCode() >= 400) {
            MdcService.getInstance().globalStatusError();
            ErrorResult errorResult = error != null ? error : ErrorResult.builder()
                                                                         .errorCode("ERR-0000")
                                                                         .httpCode(httpResponse.getCode())
                                                                         .message("undefined error")
                                                                         .build();
            if (errorResult.isExploitationError()) {
                Loggers.XLLOG.error("[HTTP-{}:{}] {} : {}", errorResult.getHttpCode(), errorResult.getErrorCode(), errorResult.getMessage(), errorResult.getCause());
            }
            Loggers.IOLOG.error((enableDecorator ? outputDecorator : EMPTY) + msg);
        } else {
            MdcService.getInstance().globalStatusSuccess();
            Loggers.IOLOG.info((enableDecorator ? outputDecorator : EMPTY) + msg);

        }
        MdcService.getInstance().lifecycleRemove().removeGlobalStatus();
        return null;
    }


}
