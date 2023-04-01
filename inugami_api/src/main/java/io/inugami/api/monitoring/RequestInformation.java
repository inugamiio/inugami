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
package io.inugami.api.monitoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * RequestInformation
 * 
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@ToString
@Getter
public class RequestInformation implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -7805638130853062376L;
    
    // server
    private final String env;
    
    private final String asset;
    
    private final String hostname;
    
    private final String instanceName;
    
    private final String instanceNumber;
    
    // tracking
    private final String correlationId;
    
    private final String requestId;
    private final String traceId;
    
    private final String conversationId;
    
    private final String sessionId;
    
    private final String token;
    
    // application
    private final String applicationVersion;
    
    private final String service;
    
    // devices
    private final String deviceIdentifier;
    
    private final String deviceType;
    
    private final String deviceClass;
    
    private final String version;
    
    private final String majorVersion;
    
    private final String osVersion;
    
    private final String deviceNetworkType;
    
    private final double deviceNetworkSpeedDown;
    
    private final double deviceNetworkSpeedUp;
    
    private final double deviceNetworkSpeedLatency;
    
    // IPs
    private final String remoteAddress;
    
    private final String deviceIp;
    
    private final String userAgent;
    
    // language
    private final String              language;
    
    private final String              country;
    
    private final Map<String, String> specific;

}
