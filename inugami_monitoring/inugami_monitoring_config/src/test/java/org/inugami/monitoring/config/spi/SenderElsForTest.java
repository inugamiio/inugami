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
package org.inugami.monitoring.config.spi;

import java.util.List;

import org.inugami.api.processors.ConfigHandler;
import org.inugami.monitoring.api.data.GenericMonitoringModel;
import org.inugami.monitoring.api.senders.MonitoringSender;
import org.inugami.monitoring.api.senders.MonitoringSenderException;

/**
 * SenderElsForTest
 * 
 * @author patrickguillerm
 * @since Jan 16, 2019
 */
public class SenderElsForTest implements MonitoringSender {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    
    // =========================================================================
    // SPI
    // =========================================================================
    @Override
    public MonitoringSender buildInstance(ConfigHandler<String, String> configuration) {
        return this;
    }
    
    @Override
    public String getName() {
        return "ELS";
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void process(List<GenericMonitoringModel> data) throws MonitoringSenderException {
        // TODO Auto-generated method stub
        
    }
}
