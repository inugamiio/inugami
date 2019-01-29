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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * MonitoringProviderConfig
 * 
 * @author patrickguillerm
 * @since Jan 15, 2019
 */
@XStreamAlias("sender")
public class MonitoringSenderConfig {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @XStreamAsAttribute
    private String     name;
    
    private PropertiesConfig properties;
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public PropertiesConfig getProperties() {
        return properties;
    }
    
    public void setProperties(PropertiesConfig properties) {
        this.properties = properties;
    }
    
}
