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
package org.inugami.monitoring.core.interceptors.spi;

import java.util.Map;

import org.inugami.api.monitoring.RequestContext;
import org.inugami.api.monitoring.RequestInformation;
import org.inugami.api.monitoring.interceptors.MonitoringFilterInterceptor;
import org.inugami.api.processors.ConfigHandler;
import org.slf4j.MDC;

public class MdcInterceptor implements MonitoringFilterInterceptor {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private enum MDCKeys {
        env,
        asset,
        hostname,
        instanceName,
        instanceNumber,
        correlationId,
        requestId,
        conversationId,
        sessionId,
        applicationVersion,
        deviceIdentifier,
        deviceType,
        deviceClass,
        version,
        majorVersion,
        osVersion,
        deviceNetworkType,
        deviceNetworkSpeedDown,
        deviceNetworkSpeedUp,
        deviceNetworkSpeedLatency,
        remoteAddress,
        deviceIp,
        userAgent,
        language,
        country
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public MonitoringFilterInterceptor buildInstance(final ConfigHandler<String, String> configuration) {
        initialize();
        return this;
    }
    
    @Override
    public void initialize() {
        final RequestInformation requestContext = RequestContext.getInstance();
        
        setMdc(MDCKeys.env, requestContext.getEnv());
        setMdc(MDCKeys.asset, requestContext.getAsset());
        setMdc(MDCKeys.hostname, requestContext.getHostname());
        setMdc(MDCKeys.instanceName, requestContext.getInstanceName());
        setMdc(MDCKeys.instanceNumber, requestContext.getInstanceNumber());
        setMdc(MDCKeys.correlationId, requestContext.getConversationId());
        setMdc(MDCKeys.requestId, requestContext.getRequestId());
        setMdc(MDCKeys.conversationId, requestContext.getConversationId());
        setMdc(MDCKeys.sessionId, requestContext.getSessionId());
        setMdc(MDCKeys.applicationVersion, requestContext.getApplicationVersion());
        setMdc(MDCKeys.deviceIdentifier, requestContext.getDeviceIdentifier());
        setMdc(MDCKeys.deviceType, requestContext.getDeviceType());
        setMdc(MDCKeys.deviceClass, requestContext.getDeviceClass());
        setMdc(MDCKeys.version, requestContext.getVersion());
        setMdc(MDCKeys.majorVersion, requestContext.getMajorVersion());
        setMdc(MDCKeys.osVersion, requestContext.getOsVersion());
        setMdc(MDCKeys.deviceNetworkType, requestContext.getDeviceNetworkType());
        setMdc(MDCKeys.deviceNetworkSpeedDown, String.valueOf(requestContext.getDeviceNetworkSpeedDown()));
        setMdc(MDCKeys.deviceNetworkSpeedUp, String.valueOf(requestContext.getDeviceNetworkSpeedUp()));
        setMdc(MDCKeys.deviceNetworkSpeedLatency, String.valueOf(requestContext.getDeviceNetworkSpeedLatency()));
        setMdc(MDCKeys.remoteAddress, requestContext.getRemoteAddress());
        setMdc(MDCKeys.deviceIp, requestContext.getDeviceIp());
        setMdc(MDCKeys.userAgent, requestContext.getUserAgent());
        setMdc(MDCKeys.language, requestContext.getLanguage());
        setMdc(MDCKeys.country, requestContext.getCountry());
        
        if (requestContext.getSpecific() != null) {
            for (final Map.Entry<String, String> entry : requestContext.getSpecific().entrySet()) {
                if (entry.getKey() != null) {
                    MDC.put(entry.getKey(), entry.getValue());
                }
                
            }
        }
    }
    
    private void setMdc(final MDCKeys key, final String value) {
        MDC.put(key.name(), value == null ? "" : value);
    }
    
}
