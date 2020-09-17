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
package io.inugami.monitoring.config.models;

import java.util.ArrayList;
import java.util.List;

import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.functionnals.PostProcessing;
import io.inugami.api.processors.ConfigHandler;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * SensorsConfig
 * 
 * @author patrickguillerm
 * @since Jan 16, 2019
 */
public class SensorsConfig implements PostProcessing<ConfigHandler<String, String>> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @XStreamImplicit
    private List<MonitoringSensorConfig> sensors;
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public List<MonitoringSensorConfig> getSensors() {
        return sensors == null ? new ArrayList<>() : sensors;
    }
    
    public void setSensors(List<MonitoringSensorConfig> sensors) {
        this.sensors = sensors;
    }
    
    @Override
    public void postProcessing(ConfigHandler<String, String> context) throws TechnicalException {
        if (sensors != null) {
            for (MonitoringSensorConfig sensor : sensors) {
                sensor.postProcessing(context);
            }
        }
    }
    
}
