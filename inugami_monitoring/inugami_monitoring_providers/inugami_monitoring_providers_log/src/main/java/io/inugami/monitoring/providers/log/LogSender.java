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
package io.inugami.monitoring.providers.log;


import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;
import io.inugami.framework.interfaces.monitoring.models.MonitoringContextDTO;
import io.inugami.framework.interfaces.monitoring.senders.MonitoringSender;
import io.inugami.framework.interfaces.monitoring.senders.MonitoringSenderException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * LogSender
 *
 * @author patrickguillerm
 * @since Jan 17, 2019
 */
@SuppressWarnings({"java:S2629"})
@Slf4j
public class LogSender implements MonitoringSender {


    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final String METRICS_LOG_SENDER = "METRICS_LOG_SENDER";
    private final Logger logger;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public LogSender() {
        logger = LoggerFactory.getLogger(METRICS_LOG_SENDER);
    }

    public LogSender(final ConfigHandler<String, String> configuration) {
        logger = LoggerFactory.getLogger(configuration.grabOrDefault("logName", METRICS_LOG_SENDER));
    }

    @Override
    public MonitoringSender buildInstance(final ConfigHandler<String, String> configuration,
                                          final MonitoringContextDTO contextDTO) {
        return new LogSender(configuration);
    }
    // =========================================================================
    // METHODS
    // =========================================================================

    @Override
    public void process(final List<GenericMonitoringModel> data) throws MonitoringSenderException {
        long counter = 0;
        for (final GenericMonitoringModel item : data) {
            final String json = convertToJson(item);
            if (json == null) {
                continue;
            }
            logger.info(json);
            counter++;
        }
        log.info("send {} metrics", counter);
    }

    private String convertToJson(final GenericMonitoringModel value) {
        try {
            return JsonMarshaller.getInstance().getDefaultObjectMapper().writeValueAsString(value);
        } catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.error(e.getMessage(), e);
            }
            return null;
        }


    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public String getName() {
        return "log";
    }

}
