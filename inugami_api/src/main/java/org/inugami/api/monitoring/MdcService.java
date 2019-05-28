package org.inugami.api.monitoring;

import java.util.Map;

import org.slf4j.MDC;

public class MdcService {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private enum MDCKeys {
        env,
        asset,
        hostname,
        instanceName,
        instanceNumber,
        correlation_id,
        request_id,
        conversation_id,
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
        country,
        service
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static void initialize() {
        final RequestInformation requestContext = RequestContext.getInstance();
        
        setMdc(MDCKeys.env, requestContext.getEnv());
        setMdc(MDCKeys.asset, requestContext.getAsset());
        setMdc(MDCKeys.hostname, requestContext.getHostname());
        setMdc(MDCKeys.instanceName, requestContext.getInstanceName());
        setMdc(MDCKeys.instanceNumber, requestContext.getInstanceNumber());
        setMdc(MDCKeys.correlation_id, requestContext.getCorrelationId());
        setMdc(MDCKeys.request_id, requestContext.getRequestId());
        setMdc(MDCKeys.conversation_id, requestContext.getConversationId());
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
        setMdc(MDCKeys.service, requestContext.getService());
        
        if (requestContext.getSpecific() != null) {
            for (final Map.Entry<String, String> entry : requestContext.getSpecific().entrySet()) {
                if (entry.getKey() != null) {
                    MDC.put(entry.getKey(), entry.getValue());
                }
                
            }
        }
    }
    
    private static void setMdc(final MDCKeys key, final String value) {
        MDC.put(key.name(), value == null ? "" : value);
    }
    
}
