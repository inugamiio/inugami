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
package org.inugami.monitoring.api.data;

import java.io.Serializable;
import java.util.Map;

/**
 * RequestInformation
 * 
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
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
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    /* package */ RequestInformation(String env, String asset, String hostname, String instanceName,
                                     String instanceNumber, String correlationId, String requestId,
                                     String conversationId, String sessionId, String token, String applicationVersion,
                                     String service, String deviceIdentifier, String deviceType, String deviceClass,
                                     String version, String majorVersion, String osVersion, String deviceNetworkType,
                                     double deviceNetworkSpeedDown, double deviceNetworkSpeedUp,
                                     double deviceNetworkSpeedLatency, String remoteAddress, String deviceIp,
                                     String userAgent, String language, String country,
                                     final Map<String, String> specific) {
        this.env = env;
        this.asset = asset;
        this.hostname = hostname;
        this.instanceName = instanceName;
        this.instanceNumber = instanceNumber;
        
        this.correlationId = correlationId;
        this.requestId = requestId;
        this.conversationId = conversationId;
        this.sessionId = sessionId;
        this.token = token;
        
        this.applicationVersion = applicationVersion;
        this.service = service;
        this.deviceIdentifier = deviceIdentifier;
        this.deviceType = deviceType;
        this.deviceClass = deviceClass;
        this.version = version;
        this.majorVersion = majorVersion;
        this.osVersion = osVersion;
        this.deviceNetworkType = deviceNetworkType;
        this.deviceNetworkSpeedDown = deviceNetworkSpeedDown;
        this.deviceNetworkSpeedUp = deviceNetworkSpeedUp;
        this.deviceNetworkSpeedLatency = deviceNetworkSpeedLatency;
        this.remoteAddress = remoteAddress;
        this.deviceIp = deviceIp;
        this.userAgent = userAgent;
        this.language = language;
        this.country = country;
        this.specific = specific;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RequestInformation [");
        builder.append("env=");
        builder.append(env);
        builder.append(", correlationId=");
        builder.append(correlationId);
        builder.append(", requestId=");
        builder.append(requestId);
        builder.append(", conversationId=");
        builder.append(conversationId);
        builder.append(", sessionId=");
        builder.append(sessionId);
        builder.append(", token=");
        builder.append(token);
        
        builder.append(", applicationVersion=");
        builder.append(applicationVersion);
        builder.append(", service=");
        builder.append(service);
        builder.append(", deviceIdentifier=");
        builder.append(deviceIdentifier);
        builder.append(", deviceType=");
        builder.append(deviceType);
        builder.append(", deviceClass=");
        builder.append(deviceClass);
        builder.append(", version=");
        builder.append(version);
        builder.append(", majorVersion=");
        builder.append(majorVersion);
        builder.append(", osVersion=");
        builder.append(osVersion);
        builder.append(", deviceNetworkType=");
        builder.append(deviceNetworkType);
        
        builder.append(", deviceNetworkSpeedDown=");
        builder.append(deviceNetworkSpeedDown);
        builder.append(", deviceNetworkSpeedUp=");
        builder.append(deviceNetworkSpeedUp);
        builder.append(", deviceNetworkSpeedLatency=");
        builder.append(deviceNetworkSpeedLatency);
        
        builder.append(", remoteAddress=");
        builder.append(remoteAddress);
        builder.append(", deviceIp=");
        builder.append(deviceIp);
        builder.append(", userAgent=");
        builder.append(userAgent);
        builder.append(", language=");
        builder.append(language);
        builder.append(", country=");
        builder.append(country);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getEnv() {
        return env;
    }
    
    public String getAsset() {
        return asset;
    }
    
    public String getHostname() {
        return hostname;
    }
    
    public String getInstanceName() {
        return instanceName;
    }
    
    public String getInstanceNumber() {
        return instanceNumber;
    }
    
    public String getCorrelationId() {
        return correlationId;
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public String getConversationId() {
        return conversationId;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public String getToken() {
        return token;
    }
    
    public String getApplicationVersion() {
        return applicationVersion;
    }
    
    public String getService() {
        return service;
    }
    
    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }
    
    public String getDeviceType() {
        return deviceType;
    }
    
    public String getDeviceClass() {
        return deviceClass;
    }
    
    public String getVersion() {
        return version;
    }
    
    public String getMajorVersion() {
        return majorVersion;
    }
    
    public String getOsVersion() {
        return osVersion;
    }
    
    public String getRemoteAddress() {
        return remoteAddress;
    }
    
    public String getDeviceIp() {
        return deviceIp;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public String getCountry() {
        return country;
    }
    
    public String getDeviceNetworkType() {
        return deviceNetworkType;
    }
    
    public double getDeviceNetworkSpeedDown() {
        return deviceNetworkSpeedDown;
    }
    
    public double getDeviceNetworkSpeedUp() {
        return deviceNetworkSpeedUp;
    }
    
    public double getDeviceNetworkSpeedLatency() {
        return deviceNetworkSpeedLatency;
    }
    
    public Map<String, String> getSpecific() {
        return specific;
    }
    
}
