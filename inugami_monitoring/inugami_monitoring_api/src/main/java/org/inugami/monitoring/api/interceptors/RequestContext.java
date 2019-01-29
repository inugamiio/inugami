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
package org.inugami.monitoring.api.interceptors;

import java.util.UUID;

import org.inugami.monitoring.api.data.RequestInformation;
import org.inugami.monitoring.api.data.RequestInformationBuilder;
import org.inugami.monitoring.api.data.config.Monitoring;

/**
 * RequestContext
 * 
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
public final class RequestContext {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static Monitoring                      config;
    
    private static ThreadLocal<RequestInformation> INSTANCE = new ThreadLocal<>();
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static void initializeConfig(Monitoring inputConfig) {
        config = inputConfig;
    }
    
    public static RequestInformation getInstance() {
        RequestInformation result = INSTANCE.get();
        if (result == null) {
            result = initializeTechnicalRequest();
        }
        return result;
    }
    
    /* package */ static synchronized void setInstance(RequestInformation instance) {
        INSTANCE.set(instance);
    }
    
    // =========================================================================
    // INIT
    // =========================================================================
    private static RequestInformation initializeTechnicalRequest() {
        RequestInformationBuilder builder = new RequestInformationBuilder();
        
        if (config != null) {
            builder.setEnv(config.getEnv());
            builder.setAsset(config.getAsset());
            builder.setHostname(config.getHostname());
            builder.setInstanceName(config.getInstanceName());
            builder.setInstanceNumber(config.getInstanceNumber());
            builder.setApplicationVersion(config.getApplicationVersion());
        }
        
        builder.setCorrelationId(UUID.randomUUID().toString());
        builder.setRequestId(UUID.randomUUID().toString());
        builder.setService(String.join("_", "technical", Thread.currentThread().getName()));
        
        final RequestInformation result = builder.build();
        RequestContext.setInstance(result);
        return result;
    }
}
