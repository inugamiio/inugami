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
package io.inugami.monitoring.core.interceptors.spi;

import io.inugami.api.exceptions.Warning;
import io.inugami.api.exceptions.WarningContext;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.JsonBuilder;
import io.inugami.api.monitoring.MdcService;
import io.inugami.api.monitoring.data.ResponseData;
import io.inugami.api.monitoring.data.ResquestData;
import io.inugami.api.monitoring.exceptions.ErrorResult;
import io.inugami.api.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.api.monitoring.models.GenericMonitoringModel;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.monitoring.api.obfuscators.ObfuscatorTools;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

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
    private static final Logger  LOGGER        = Loggers.IOLOG;
    private static final String  EMPTY         = "";
    public static final  String  URL_SEPARATOR = "/";
    private final        String  inputDecorator;
    private final        String  outputDecorator;
    private final        boolean enableDecorator;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public IoLogInterceptor() {
        enableDecorator = true;
        inputDecorator = "[IN] ";
        outputDecorator = "[OUT] ";
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
    public List<GenericMonitoringModel> onBegin(final ResquestData request) {
        final JsonBuilder msg = buildIologIn(request);
        MdcService.getInstance().lifecycleIn();
        LOGGER.info((enableDecorator ? inputDecorator : EMPTY) + ObfuscatorTools.applyObfuscators(msg.toString()));
        MdcService.getInstance().lifecycleRemove();
        return null;
    }

    protected JsonBuilder buildIologIn(final ResquestData request) {
        final JsonBuilder result   = new JsonBuilder();
        final String      fullPath = resolveFullPath(request);
        result.openList().write(request.getMethod()).closeList();
        result.writeSpace().write(fullPath).line();
        result.write("headers :").line();
        for (final Map.Entry<String, String> entry : request.getHearder().entrySet()) {
            result.tab().write(entry.getKey()).write(":").writeSpace().write(entry.getValue()).line();
        }

        result.write("payload :").line();
        result.write(request.getContent() == null ? JsonBuilder.VALUE_NULL : request.getContent());
        return result;
    }

    protected String resolveFullPath(final ResquestData request) {
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
    public List<GenericMonitoringModel> onDone(final ResquestData request,
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
        for (final Map.Entry<String, String> entry : httpResponse.getHearder().entrySet()) {
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
        msg.tab().write("payload:").write(httpResponse.getContent());


        //@formatter:on
        MdcService.getInstance().lifecycleOut();
        if (error == null) {
            MdcService.getInstance().globalStatusSuccess();
            LOGGER.info((enableDecorator ? outputDecorator : EMPTY) + msg);
        } else {
            if (error.isExploitationError()) {
                MdcService.getInstance().globalStatusError();
                Loggers.XLLOG.error("[HTTP-{}:{}] {} : {}", error.getHttpCode(), error.getErrorCode(), error.getMessage(), error.getCause());
            }
            LOGGER.error((enableDecorator ? outputDecorator : EMPTY) + msg);
        }
        MdcService.getInstance()
                  .lifecycleRemove()
                  .removeGlobalStatus();
        return null;
    }


}
