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

import java.util.ArrayList;
import java.util.List;

import org.inugami.api.exceptions.TechnicalException;
import org.inugami.api.functionnals.PostProcessing;
import org.inugami.api.processors.ConfigHandler;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * SensorsConfig
 * 
 * @author patrickguillerm
 * @since Jan 16, 2019
 */
public class InterceptorsConfig implements PostProcessing<ConfigHandler<String, String>> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @XStreamImplicit
    private List<InterceptorConfig> interceptors;
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public List<InterceptorConfig> getInterceptors() {
        return interceptors == null ? new ArrayList<>() : interceptors;
    }
    
    public void setInterceptors(List<InterceptorConfig> interceptors) {
        this.interceptors = interceptors;
    }
    
    @Override
    public void postProcessing(ConfigHandler<String, String> context) throws TechnicalException {
        if (interceptors != null) {
            for (InterceptorConfig interceptor : interceptors) {
                interceptor.postProcessing(context);
            }
        }
    }
    
}
