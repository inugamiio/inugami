package io.inugami.api.monitoring;

import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.models.Tuple;
import org.slf4j.MDC;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
        service,

        lifecycle,
        appService,
        appSubService,

        errorCode,
        errorStatus,
        errorMessage,
        errorType,
        errorMessageDetail,

        partner,
        partnerType,
        partnerService,
        partnerSubService,
        partnerUrl
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


    // =========================================================================
    // PUBLIC API
    // =========================================================================
    public static void setMdc(final String key, final String value) {
        MDC.put(key, value == null ? "" : value);
    }
    public static void remove(final MDCKeys... keys) {
        if(keys!=null){
            for(MDCKeys key : keys){
                MDC.remove(key.name());
            }
        }
    }
    public static void remove(final String... keys) {
        if(keys!=null){
            for(String key : keys){
                MDC.remove(key);
            }
        }
    }

    public static void addMdc(Tuple<String,String>... keys){
        if(keys!=null){
            for(Tuple<String,String> key : keys){
                setMdc(key.getKey(),key.getValue());
            }
        }
    }
    public static void addMdc(Collection<Tuple<String,String>> keys){
        if(keys!=null){
            for(Tuple<String,String> key : keys){
                setMdc(key.getKey(),key.getValue());
            }
        }
    }

    public static void lifecycleIn(){
        setMdc(MDCKeys.lifecycle, "in");
    }
    public static void lifecycleOut(){
        setMdc(MDCKeys.lifecycle, "out");
    }

    public static void lifecycleRemove(){
        remove(MDCKeys.lifecycle);
    }

    public static void applicationService(String name){
        setMdc(MDCKeys.appService,name);
    }
    public static void applicationSubService(String name){
        setMdc(MDCKeys.appSubService,name);
    }

    public static void errorCode(ErrorCode errorCode){
        if(errorCode==null){
            errorCodeRemove();
        }else{
            setMdc(MDCKeys.errorCode,errorCode.getErrorCode());
            setMdc(MDCKeys.errorMessage,errorCode.getMessage());
            setMdc(MDCKeys.errorType,errorCode.getErrorType());
            setMdc(MDCKeys.errorMessageDetail,errorCode.getMessageDetail());
            setMdc(MDCKeys.errorStatus,String.valueOf(errorCode.getStatusCode()));
        }
    }

    private static void errorCodeRemove() {
        remove(MDCKeys.errorCode,
               MDCKeys.errorMessage,
               MDCKeys.errorType,
               MDCKeys.errorMessageDetail,
               MDCKeys.errorStatus);
    }

    public static void partner(String name){
        setMdc(MDCKeys.partner,name);
    }
    public static void partnerType(String name){
        setMdc(MDCKeys.partnerType,name);
    }
    public static void partnerService(String name){
        setMdc(MDCKeys.partnerService,name);
    }
    public static void partnerSubService(String name){
        setMdc(MDCKeys.partnerSubService,name);
    }
    public static void partnerUrl(String url){
        setMdc(MDCKeys.partnerUrl,url);
    }

    public static void partnerRemove(){
        remove(MDCKeys.partner,
               MDCKeys.partnerType,
               MDCKeys.partnerService,
               MDCKeys.partnerSubService,
               MDCKeys.partnerUrl);
    }

}
