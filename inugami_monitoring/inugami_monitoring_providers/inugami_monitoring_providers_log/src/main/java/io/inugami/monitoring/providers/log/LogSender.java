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
import io.inugami.framework.interfaces.monitoring.senders.MonitoringSender;
import io.inugami.framework.interfaces.monitoring.senders.MonitoringSenderException;
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
public class LogSender implements MonitoringSender {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final Logger logger;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public LogSender() {
        logger = LoggerFactory.getLogger(LogSender.class.getSimpleName());
    }

    public LogSender(final ConfigHandler<String, String> configuration) {
        logger = LoggerFactory.getLogger(configuration.grabOrDefault("logName", LogSender.class.getSimpleName()));
    }

    @Override
    public MonitoringSender buildInstance(final ConfigHandler<String, String> configuration) {
        return new LogSender(configuration);
    }
    // =========================================================================
    // METHODS
    // =========================================================================

    @Override
    public void process(final List<GenericMonitoringModel> data) throws MonitoringSenderException {
        for (final GenericMonitoringModel item : data) {
            final String json = convertToJson(item);
            if (json == null) {
                continue;
            }
            logger.info(json);
        }
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
