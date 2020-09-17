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

import java.util.Map;

/**
 * RequestInformationBuilder
 * 
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
public class RequestInformationBuilder {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    // server
    private String env;
    
    private String asset;
    
    private String hostname;
    
    private String instanceName;
    
    private String instanceNumber;
    
    // tracking
    private String correlationId;
    
    private String requestId;
    
    private String conversationId;
    
    private String sessionId;
    
    private String token;
    
    // application
    private String applicationVersion;
    
    private String service;
    
    // devices
    private String deviceIdentifier;
    
    private String deviceType;
    
    private String deviceClass;
    
    private String version;
    
    private String majorVersion;
    
    private String osVersion;
    
    private String deviceNetworkType;
    
    private double deviceNetworkSpeedDown;
    
    private double deviceNetworkSpeedUp;
    
    private double deviceNetworkSpeedLatency;
    
    // IPs
    private String remoteAddress;
    
    private String deviceIp;
    
    private String userAgent;
    
    // language
    private String language;
    
    private String country;
    
    private Map<String, String> specific;
    // =========================================================================
    // BUILD
    // =========================================================================
    public RequestInformation build() {
        return new RequestInformation(env, asset, hostname, instanceName, instanceNumber, correlationId, requestId,
                                      conversationId, sessionId, token, applicationVersion, service, deviceIdentifier,
                                      deviceType, deviceClass, version, majorVersion, osVersion, deviceNetworkType,
                                      deviceNetworkSpeedDown, deviceNetworkSpeedUp, deviceNetworkSpeedLatency,
                                      remoteAddress, deviceIp, userAgent, language, country,specific);
    }
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getAsset() {
        return asset;
    }
    
    public RequestInformationBuilder setEnv(String env) {
        this.env = env;
        return this;
    }
    
    public RequestInformationBuilder setAsset(String asset) {
        this.asset = asset;
        return this;
    }
    
    public String getHostname() {
        return hostname;
    }
    
    public RequestInformationBuilder setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }
    
    public String getInstanceName() {
        return instanceName;
    }
    
    public RequestInformationBuilder setInstanceName(String instanceName) {
        this.instanceName = instanceName;
        return this;
    }
    
    public String getInstanceNumber() {
        return instanceNumber;
    }
    
    public RequestInformationBuilder setInstanceNumber(String instanceNumber) {
        this.instanceNumber = instanceNumber;
        return this;
    }
    
    public String getCorrelationId() {
        return correlationId;
    }
    
    public RequestInformationBuilder setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public RequestInformationBuilder setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }
    
    public String getConversationId() {
        return conversationId;
    }
    
    public RequestInformationBuilder setConversationId(String conversationId) {
        this.conversationId = conversationId;
        return this;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public RequestInformationBuilder setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }
    
    public RequestInformationBuilder setToken(String token) {
        this.token = token;
        return this;
    }
    
    public String getApplicationVersion() {
        return applicationVersion;
    }
    
    public RequestInformationBuilder setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
        return this;
    }
    
    public String getService() {
        return service;
    }
    
    public RequestInformationBuilder setService(String service) {
        this.service = service;
        return this;
    }
    
    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }
    
    public RequestInformationBuilder setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
        return this;
    }
    
    public String getDeviceType() {
        return deviceType;
    }
    
    public RequestInformationBuilder setDeviceType(String deviceType) {
        this.deviceType = deviceType;
        return this;
    }
    
    public RequestInformationBuilder setDeviceClass(String deviceClass) {
        this.deviceClass = deviceClass;
        return this;
    }
    
    public String getVersion() {
        return version;
    }
    
    public RequestInformationBuilder setVersion(String version) {
        this.version = version;
        return this;
    }
    
    public String getMajorVersion() {
        return majorVersion;
    }
    
    public RequestInformationBuilder setMajorVersion(String majorVersion) {
        this.majorVersion = majorVersion;
        return this;
    }
    
    public String getOsVersion() {
        return osVersion;
    }
    
    public RequestInformationBuilder setOsVersion(String osVersion) {
        this.osVersion = osVersion;
        return this;
    }
    
    public String getRemoteAddress() {
        return remoteAddress;
    }
    
    public RequestInformationBuilder setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
        return this;
    }
    
    public String getDeviceIp() {
        return deviceIp;
    }
    
    public RequestInformationBuilder setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
        return this;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public RequestInformationBuilder setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public RequestInformationBuilder setLanguage(String language) {
        this.language = language;
        return this;
    }
    
    public String getCountry() {
        return country;
    }
    
    public RequestInformationBuilder setCountry(String country) {
        this.country = country;
        return this;
    }
    
    public RequestInformationBuilder setDeviceNetworkType(String value) {
        this.deviceNetworkType = value;
        return this;
        
    }
    
    public RequestInformationBuilder setDeviceNetworkSpeedDown(Double value) {
        this.deviceNetworkSpeedDown = value == null ? 0.0 : value;
        return this;
    }
    
    public RequestInformationBuilder setDeviceNetworkSpeedUp(Double value) {
        this.deviceNetworkSpeedUp = value == null ? 0.0 : value;
        return this;
    }
    
    public RequestInformationBuilder setDeviceNetworkSpeedLatency(Double value) {
        this.deviceNetworkSpeedLatency = value == null ? 0.0 : value;
        return this;
    }

    public RequestInformationBuilder setSpecific(Map<String, String> specific) {
        this.specific=specific;
        return this;
        
    }
    
}
