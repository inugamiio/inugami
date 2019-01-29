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
import org.inugami.api.functionnals.PostProcessing;
import org.inugami.api.processors.ConfigHandler;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * InterceptorConfig
 * 
 * @author patrickguillerm
 * @since Jan 18, 2019
 */
@XStreamAlias("interceptor")
public class InterceptorConfig implements PostProcessing<ConfigHandler<String, String>> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @XStreamAsAttribute
    private String           name;
    
    private PropertiesConfig properties;
    
    // =========================================================================
    // INIT
    // =========================================================================
    @Override
    public void postProcessing(ConfigHandler<String, String> context) throws TechnicalException {
    }
    
    
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
