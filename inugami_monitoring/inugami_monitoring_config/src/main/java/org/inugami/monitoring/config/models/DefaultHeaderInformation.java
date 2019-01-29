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

import static org.inugami.monitoring.config.tools.ApplyStrategy.applyStrategy;

import org.inugami.api.constants.JvmKeyValues;
import org.inugami.api.exceptions.TechnicalException;
import org.inugami.api.functionnals.ApplyIfNull;
import org.inugami.api.functionnals.PostProcessing;
import org.inugami.api.processors.ConfigHandler;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * DefaultHeaderInformation
 * 
 * @author patrickguillerm
 * @since Jan 14, 2019
 */
public class DefaultHeaderInformation implements PostProcessing<ConfigHandler<String, String>>, ApplyIfNull {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @XStreamAsAttribute
    private String correlationId;
    
    @XStreamAsAttribute
    private String requestId;
    
    @XStreamAsAttribute
    private String conversationId;
    
    @XStreamAsAttribute
    private String token;
    
    @XStreamAsAttribute
    private String deviceIdentifier;
    
    @XStreamAsAttribute
    private String deviceType;
    
    @XStreamAsAttribute
    private String deviceClass;
    
    @XStreamAsAttribute
    private String deviceVersion;
    
    @XStreamAsAttribute
    private String deviceOsVersion;
    
    @XStreamAsAttribute
    private String deviceNetworkType;
    
    @XStreamAsAttribute
    private String deviceNetworkSpeedDown;
    
    @XStreamAsAttribute
    private String deviceNetworkSpeedUp;
    
    @XStreamAsAttribute
    private String deviceNetworkSpeedLatency;
    
    @XStreamAsAttribute
    private String deviceIp;
    
    @XStreamAsAttribute
    private String userAgent;
    
    @XStreamAsAttribute
    private String language;
    
    @XStreamAsAttribute
    private String country;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @Override
    public void postProcessing(final ConfigHandler<String, String> context) throws TechnicalException {
        
        //@formatter:off
        correlationId               = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_CORRELATION_ID,correlationId,"x-correlation-id"));
        requestId                   = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_REQUEST_ID,requestId,"x-request-id")); 
        conversationId              = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_CONVERSATION_ID,conversationId,"x-conversation-id")); 
        token                       = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_TOKEN,token,"Authorization")); 
        deviceIdentifier            = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_IDENTIFIER,deviceIdentifier,"x-device-identifier")); 
        deviceType                  = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_TYPE,deviceType,"x-device-type"));
        deviceClass                 = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_CLASS,deviceClass,"x-device-class"));
        deviceVersion               = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_VERSION,deviceVersion,"x-device-version"));
        deviceOsVersion             = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_OS_VERSION,deviceOsVersion,"x-device-os-version"));
        deviceNetworkType           = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_NETWORK_TYPE,deviceNetworkType,"x-device-network-type"));
        deviceNetworkSpeedDown      = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_NETWORK_SPEED_DOWN,deviceNetworkSpeedDown,"x-device-network-speed-down"));
        deviceNetworkSpeedUp        = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_NETWORK_SPEED_UP,deviceNetworkSpeedUp,"x-device-network-speed-up"));
        deviceNetworkSpeedLatency   = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_NETWORK_SPEED_LATENCY,deviceNetworkSpeedLatency,"x-device-network-speed-latency"));
        deviceIp                    = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_DEVICE_IP,deviceIp,"x-device-ip"));
        userAgent                   = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_USER_AGENT,userAgent,"user-agent"));
        language                    = context.applyProperties(applyStrategy(JvmKeyValues.HEADER_KEY_LANGUAGE,language,"accept-language"));
        //@formatter:on
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getCorrelationId() {
        return correlationId;
    }
    
    public void setCorrelationId(final String correlationId) {
        this.correlationId = correlationId;
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public void setRequestId(final String requestId) {
        this.requestId = requestId;
    }
    
    public String getConversationId() {
        return conversationId;
    }
    
    public void setConversationId(final String conversationId) {
        this.conversationId = conversationId;
    }
    
    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }
    
    public void setDeviceIdentifier(final String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }
    
    public String getDeviceType() {
        return deviceType;
    }
    
    public void setDeviceType(final String deviceType) {
        this.deviceType = deviceType;
    }
    
    public String getDeviceClass() {
        return deviceClass;
    }
    
    public void setDeviceClass(final String deviceClass) {
        this.deviceClass = deviceClass;
    }
    
    public String getDeviceVersion() {
        return deviceVersion;
    }
    
    public void setDeviceVersion(final String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }
    
    public String getDeviceOsVersion() {
        return deviceOsVersion;
    }
    
    public void setDeviceOsVersion(final String deviceOsVersion) {
        this.deviceOsVersion = deviceOsVersion;
    }
    
    public String getDeviceNetworkType() {
        return deviceNetworkType;
    }
    
    public void setDeviceNetworkType(final String deviceNetworkType) {
        this.deviceNetworkType = deviceNetworkType;
    }
    
    public String getDeviceNetworkSpeedDown() {
        return deviceNetworkSpeedDown;
    }
    
    public void setDeviceNetworkSpeedDown(final String deviceNetworkSpeedDown) {
        this.deviceNetworkSpeedDown = deviceNetworkSpeedDown;
    }
    
    public String getDeviceNetworkSpeedUp() {
        return deviceNetworkSpeedUp;
    }
    
    public void setDeviceNetworkSpeedUp(final String deviceNetworkSpeedUp) {
        this.deviceNetworkSpeedUp = deviceNetworkSpeedUp;
    }
    
    public String getDeviceNetworkSpeedLatency() {
        return deviceNetworkSpeedLatency;
    }
    
    public void setDeviceNetworkSpeedLatency(final String deviceNetworkSpeedLatency) {
        this.deviceNetworkSpeedLatency = deviceNetworkSpeedLatency;
    }
    
    public String getDeviceIp() {
        return deviceIp;
    }
    
    public void setDeviceIp(final String deviceIp) {
        this.deviceIp = deviceIp;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(final String userAgent) {
        this.userAgent = userAgent;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(final String language) {
        this.language = language;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(final String country) {
        this.country = country;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(final String token) {
        this.token = token;
    }
    
}
