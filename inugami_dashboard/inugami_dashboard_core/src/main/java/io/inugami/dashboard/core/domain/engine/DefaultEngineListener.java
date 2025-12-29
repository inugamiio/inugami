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
package io.inugami.dashboard.core.domain.engine;

import io.inugami.dashboard.api.domain.engine.EngineListener;
import io.inugami.dashboard.api.domain.engine.dto.EnginePluginEventResultDTO;
import io.inugami.dashboard.api.domain.engine.dto.EnginePluginResultDTO;
import io.inugami.dashboard.api.domain.engine.dto.EngineResultDTO;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.models.event.GenericEvent;
import io.inugami.framework.interfaces.monitoring.logger.LogInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

@SuppressWarnings({"java:S1186"})
@Slf4j
@Component
public class DefaultEngineListener implements EngineListener {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public static final String EVENT      = "event";
    public static final String STATUS     = "status";
    public static final String MESSAGE    = "message";
    public static final String ERROR      = "error";
    public static final String ERROR_CODE = "errorCode";
    public static final String EMPTY      = "";

    //==================================================================================================================
    // ON DONE
    //==================================================================================================================
    @Override
    public void onDone(final EngineResultDTO engineResult) {
        if (Status.SUCCESS == engineResult.getStatus()) {
            log.info("successful engine running (starting:{} |finish : {})", engineResult.getStart(), engineResult.getEnd());
        } else {
            final var info = buildErrorInfo(engineResult);
            log.error("error on engine running (starting:{} |finish : {}) \n{}", engineResult.getStart(), engineResult.getEnd(), info);
        }
    }

    @Override
    public void onEventDone(final Plugin plugin, final GenericEvent<?> event, final EnginePluginEventResultDTO data) {
        if (Status.SUCCESS == data.getStatus()) {
            log.debug("successful event running ({}:{})", plugin.getGav().getHash(), event.getName());
        } else {

            log.debug("error on event running (starting:{} |finish : {}) \n{}", plugin.getGav()
                                                                                      .getHash(), event.getName(), buildEventErrorInfo(data));
        }
    }


    protected static LogInfoDTO buildErrorInfo(final EngineResultDTO engineResult) {
        final var result = LogInfoDTO.builder();
        result.with(STATUS, engineResult.getStatus());

        for (EnginePluginResultDTO plugin : Optional.ofNullable(engineResult.getPlugins()).orElse(List.of())) {
            final var pluginResult = LogInfoDTO.builder();
            pluginResult.with(STATUS, plugin.getStatus());

            for (final var event : Optional.ofNullable(plugin.getEvents()).orElse(List.of())) {
                final var eventResult = LogInfoDTO.builder();
                eventResult.with(STATUS, event.getStatus());
                applyIfNotNull(event.getErrorCode(), errorCode -> eventResult.with(ERROR_CODE, errorCode.getErrorCode()));
                pluginResult.with(event.getName(), eventResult.build());
            }
            result.with(plugin.getGav().getHash(), pluginResult.build());
        }
        return result.build();
    }

    protected static LogInfoDTO buildEventErrorInfo(final EnginePluginEventResultDTO eventData) {
        final var result = LogInfoDTO.builder();
        result.with(STATUS, eventData.getStatus());
        result.with(MESSAGE, eventData.getMessage());
        applyIfNotNull(eventData.getErrorCode(), errorCode -> result.with(ERROR_CODE, errorCode.getErrorCode()));
        result.with(ERROR, Optional.ofNullable(eventData)
                                   .map(EnginePluginEventResultDTO::getError)
                                   .map(Throwable::getMessage)
                                   .orElse(EMPTY));

        return result.build();
    }
}
