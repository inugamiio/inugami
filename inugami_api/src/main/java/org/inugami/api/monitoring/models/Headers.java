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
package org.inugami.api.monitoring.models;

import java.util.Set;

/**
 * Headers
 * 
 * @author patrickguillerm
 * @since Jan 16, 2019
 */
public class Headers {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String      correlationId;
    
    private final String      requestId;
    
    private final String      conversationId;
    
    private final String      token;
    
    private final String      deviceIdentifier;
    
    private final String      deviceType;
    
    private final String      deviceClass;
    
    private final String      deviceVersion;
    
    private final String      deviceOsVersion;
    
    private final String      deviceNetworkType;
    
    private final String      deviceNetworkSpeedDown;
    
    private final String      deviceNetworkSpeedUp;
    
    private final String      deviceNetworkSpeedLatency;
    
    private final String      deviceIp;
    
    private final String      userAgent;
    
    private final String      language;
    
    private final String      country;
    
    private final Set<String> specificHeaders;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public Headers(String correlationId, String requestId, String conversationId, String token, String deviceIdentifier,
                   String deviceType, String deviceClass, String deviceVersion, String deviceOsVersion,
                   String deviceNetworkType, String deviceNetworkSpeedDown, String deviceNetworkSpeedUp,
                   String deviceNetworkSpeedLatency, String deviceIp, String userAgent, String language, String country,
                   Set<String> specificHeaders) {
        super();
        this.correlationId = correlationId;
        this.requestId = requestId;
        this.conversationId = conversationId;
        this.token = token;
        this.deviceIdentifier = deviceIdentifier;
        this.deviceType = deviceType;
        this.deviceClass = deviceClass;
        this.deviceVersion = deviceVersion;
        this.deviceOsVersion = deviceOsVersion;
        this.deviceNetworkType = deviceNetworkType;
        this.deviceNetworkSpeedDown = deviceNetworkSpeedDown;
        this.deviceNetworkSpeedUp = deviceNetworkSpeedUp;
        this.deviceNetworkSpeedLatency = deviceNetworkSpeedLatency;
        this.deviceIp = deviceIp;
        this.userAgent = userAgent;
        this.language = language;
        this.country = country;
        this.specificHeaders = specificHeaders;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Headers [correlationId=");
        builder.append(correlationId);
        builder.append(", requestId=");
        builder.append(requestId);
        builder.append(", conversationId=");
        builder.append(conversationId);
        builder.append(", token=");
        builder.append(token);
        builder.append(", deviceIdentifier=");
        builder.append(deviceIdentifier);
        builder.append(", deviceType=");
        builder.append(deviceType);
        builder.append(", deviceClass=");
        builder.append(deviceClass);
        builder.append(", deviceVersion=");
        builder.append(deviceVersion);
        builder.append(", deviceOsVersion=");
        builder.append(deviceOsVersion);
        builder.append(", deviceNetworkType=");
        builder.append(deviceNetworkType);
        builder.append(", deviceNetworkSpeedDown=");
        builder.append(deviceNetworkSpeedDown);
        builder.append(", deviceNetworkSpeedUp=");
        builder.append(deviceNetworkSpeedUp);
        builder.append(", deviceNetworkSpeedLatency=");
        builder.append(deviceNetworkSpeedLatency);
        builder.append(", deviceIp=");
        builder.append(deviceIp);
        builder.append(", userAgent=");
        builder.append(userAgent);
        builder.append(", language=");
        builder.append(language);
        builder.append(", country=");
        builder.append(country);
        builder.append(", specificHeaders=");
        builder.append(specificHeaders);
        builder.append("]");
        return builder.toString();
    }
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getCorrelationId() {
        return correlationId;
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public String getConversationId() {
        return conversationId;
    }
    
    public String getToken() {
        return token;
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
    
    public String getDeviceVersion() {
        return deviceVersion;
    }
    
    public String getDeviceOsVersion() {
        return deviceOsVersion;
    }
    
    public String getDeviceNetworkType() {
        return deviceNetworkType;
    }
    
    public String getDeviceNetworkSpeedDown() {
        return deviceNetworkSpeedDown;
    }
    
    public String getDeviceNetworkSpeedUp() {
        return deviceNetworkSpeedUp;
    }
    
    public String getDeviceNetworkSpeedLatency() {
        return deviceNetworkSpeedLatency;
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
    
    public Set<String> getSpecificHeaders() {
        return specificHeaders;
    }
    
}
