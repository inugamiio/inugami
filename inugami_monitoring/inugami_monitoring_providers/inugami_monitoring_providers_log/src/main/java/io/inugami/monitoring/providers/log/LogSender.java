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

import java.util.List;

import io.inugami.api.monitoring.models.GenericMonitoringModel;
import io.inugami.api.monitoring.senders.MonitoringSender;
import io.inugami.api.monitoring.senders.MonitoringSenderException;
import io.inugami.api.processors.ConfigHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LogSender
 * 
 * @author patrickguillerm
 * @since Jan 17, 2019
 */
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
            logger.info(item.convertToJson());
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
