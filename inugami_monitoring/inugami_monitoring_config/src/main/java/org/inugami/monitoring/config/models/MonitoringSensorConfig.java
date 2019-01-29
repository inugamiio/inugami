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
package org.inugami.monitoring.config.models;

import org.inugami.api.exceptions.TechnicalException;
import org.inugami.api.functionnals.ApplyIfNull;
import org.inugami.api.functionnals.PostProcessing;
import org.inugami.api.processors.ConfigHandler;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * MonitoringSensorConfig
 * 
 * @author patrickguillerm
 * @since Jan 15, 2019
 */
@XStreamAlias("sensor")
public class MonitoringSensorConfig implements PostProcessing<ConfigHandler<String, String>>, ApplyIfNull {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @XStreamAsAttribute
    private String           name;
    
    @XStreamAsAttribute
    private String           interval;
    
    @XStreamAsAttribute
    private String           query;
    
    private PropertiesConfig properties;
    
    // =========================================================================
    // INIT
    // =========================================================================
    @Override
    public void postProcessing(final ConfigHandler<String, String> context) throws TechnicalException {
        interval = applyIfNull(interval, () -> "60000");
    }
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getName() {
        return name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getInterval() {
        return interval;
    }
    
    public void setInterval(final String interval) {
        this.interval = interval;
    }
    
    public String getQuery() {
        return query;
    }
    
    public void setQuery(final String query) {
        this.query = query;
    }
    
    public PropertiesConfig getProperties() {
        return properties;
    }
    
    public void setProperties(final PropertiesConfig properties) {
        this.properties = properties;
    }
    
}
