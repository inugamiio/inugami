package io.inugami.api.monitoring;

import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.functionnals.VoidFunctionWithException;
import io.inugami.api.models.Tuple;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static io.inugami.api.functionnals.FunctionalUtils.applyIfNotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MdcService {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public enum MDCKeys {
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
        partnerUrl,

        exceptionName,
        duration,
        functionalUid,
        methodInCause,
        uri,
        requestHeaders,
        responseHeaders,
        parentSpanId,
        traceId,
        flags,

        customerId,
        userId,
        productId,
        orderId,
        reservationNumber;

    }

    private static final MdcService INSTANCE = new MdcService();

    public static MdcService getInstance() {
        return INSTANCE;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public void initialize() {
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


    // =========================================================================
    // PUBLIC API
    // =========================================================================
    public MdcService setMdc(final Map<String, String> values) {
        if (values == null) {
            return this;
        }

        for (Map.Entry<String, String> value : values.entrySet()) {
            setMdc(value.getKey(), value.getValue());
        }
        return this;
    }

    public MdcService setMdc(final MDCKeys key, final String value) {
        if (key == null) {
            return this;
        }

        return setMdc(key.name(), value);
    }

    public MdcService setMdc(final String key, final String value) {
        if (key == null) {
            return this;
        }

        if (value == null) {
            remove(key);
        }
        else {
            MDC.put(key, value);
        }

        return this;
    }

    public String getMdc(final MDCKeys key) {
        if (key == null) {
            return null;
        }
        return getMdc(key.name());
    }


    public String getMdc(final String key) {
        if (key == null) {
            return null;
        }
        return MDC.get(key);
    }

    public Map<String, String> getAllMdc() {
        return MDC.getCopyOfContextMap();
    }

    public MdcService remove(final MDCKeys... keys) {
        if (keys != null) {
            for (MDCKeys key : keys) {
                MDC.remove(key.name());
            }
        }
        return this;
    }

    public MdcService remove(final String... keys) {
        if (keys != null) {
            for (String key : keys) {
                MDC.remove(key);
            }
        }
        return this;
    }

    public MdcService addMdc(Tuple<String, String>... keys) {
        if (keys != null) {
            for (Tuple<String, String> key : keys) {
                setMdc(key.getKey(), key.getValue());
            }
        }
        return this;
    }

    public MdcService addMdc(Collection<Tuple<String, String>> keys) {
        if (keys != null) {
            for (Tuple<String, String> key : keys) {
                setMdc(key.getKey(), key.getValue());
            }
        }
        return this;
    }


    public MdcService applicationService(String name) {
        setMdc(MDCKeys.appService, name);
        return this;
    }

    public MdcService applicationSubService(String name) {
        setMdc(MDCKeys.appSubService, name);
        return this;
    }


    public boolean hasError() {
        return errorCode() != null;
    }

    public MdcService errorCode(ErrorCode errorCode) {
        if (errorCode == null) {
            errorCodeRemove();
        }
        else {
            setMdc(MDCKeys.errorCode, errorCode.getErrorCode());
            setMdc(MDCKeys.errorMessage, errorCode.getMessage());
            setMdc(MDCKeys.errorType, errorCode.getErrorType());
            setMdc(MDCKeys.errorMessageDetail, errorCode.getMessageDetail());
            setMdc(MDCKeys.errorStatus, String.valueOf(errorCode.getStatusCode()));
        }
        return this;
    }

    public MdcService errorCodeRemove() {
        remove(MDCKeys.errorCode,
               MDCKeys.errorMessage,
               MDCKeys.errorType,
               MDCKeys.errorMessageDetail,
               MDCKeys.errorStatus);
        return this;
    }


    public MdcService partnerRemove() {
        remove(MDCKeys.partner,
               MDCKeys.partnerType,
               MDCKeys.partnerService,
               MDCKeys.partnerSubService,
               MDCKeys.partnerUrl);
        return this;
    }


    // =========================================================================
    // BUILDER
    // =========================================================================
    public MdcService env(final String value) {
        setMdc(MDCKeys.env, value);
        return this;
    }

    public String env() {
        return getMdc(MDCKeys.env);
    }


    public MdcService asset(final String value) {
        setMdc(MDCKeys.asset, value);
        return this;
    }

    public String asset() {
        return getMdc(MDCKeys.asset);
    }

    public MdcService hostname(final String value) {
        setMdc(MDCKeys.hostname, value);
        return this;
    }

    public String hostname() {
        return getMdc(MDCKeys.hostname);
    }

    public MdcService instanceName(final String value) {
        setMdc(MDCKeys.instanceName, value);
        return this;
    }

    public String instanceName() {
        return getMdc(MDCKeys.instanceName);
    }

    public MdcService instanceNumber(final String value) {
        setMdc(MDCKeys.instanceNumber, value);
        return this;
    }

    public String instanceNumber() {
        return getMdc(MDCKeys.instanceNumber);
    }

    public MdcService correlationId(final String value) {
        setMdc(MDCKeys.correlation_id, value);
        return this;
    }

    public Map<String, String> getTrackingInformation() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put(MDCKeys.deviceIdentifier.name(), deviceIdentifier());
        result.put(MDCKeys.correlation_id.name(), correlationId());
        result.put(MDCKeys.request_id.name(), requestId());
        result.put(MDCKeys.traceId.name(), traceId());

        applyIfNotNull(conversationId(), value -> result.put(MDCKeys.conversation_id.name(), value));
        applyIfNotNull(functionalUid(), value -> result.put(MDCKeys.functionalUid.name(), value));


        return result;
    }

    public String correlationId() {
        String value = getMdc(MDCKeys.correlation_id);
        if (value == null) {
            value = UUID.randomUUID().toString();
            correlationId(value);
        }
        return value;
    }

    public MdcService requestId(final String value) {
        setMdc(MDCKeys.request_id, value);
        return this;
    }

    public String requestId() {
        String value = getMdc(MDCKeys.request_id);
        if (value == null) {
            value = UUID.randomUUID().toString();
            requestId(value);
        }
        return value;
    }

    public MdcService conversationId(final String value) {
        setMdc(MDCKeys.conversation_id, value);
        return this;
    }

    public String conversationId() {
        return getMdc(MDCKeys.conversation_id);
    }

    public MdcService sessionId(final String value) {
        setMdc(MDCKeys.sessionId, value);
        return this;
    }

    public String sessionId() {
        return getMdc(MDCKeys.sessionId);
    }

    public MdcService applicationVersion(final String value) {
        setMdc(MDCKeys.applicationVersion, value);
        return this;
    }

    public String applicationVersion() {
        return getMdc(MDCKeys.applicationVersion);
    }

    public MdcService deviceIdentifier(final String value) {
        setMdc(MDCKeys.deviceIdentifier, value);
        return this;
    }

    public String deviceIdentifier() {
        String value = getMdc(MDCKeys.deviceIdentifier);
        if (value == null) {
            value = UUID.randomUUID().toString();
            deviceIdentifier(value);
        }
        return value;
    }

    public MdcService deviceType(final String value) {
        setMdc(MDCKeys.deviceType, value);
        return this;
    }

    public String deviceType() {
        return getMdc(MDCKeys.deviceType);
    }

    public MdcService deviceClass(final String value) {
        setMdc(MDCKeys.deviceClass, value);
        return this;
    }

    public String deviceClass() {
        return getMdc(MDCKeys.deviceClass);
    }

    public MdcService version(final String value) {
        setMdc(MDCKeys.version, value);
        return this;
    }

    public String version() {
        return getMdc(MDCKeys.version);
    }

    public MdcService majorVersion(final String value) {
        setMdc(MDCKeys.majorVersion, value);
        return this;
    }

    public String majorVersion() {
        return getMdc(MDCKeys.majorVersion);
    }

    public MdcService osVersion(final String value) {
        setMdc(MDCKeys.osVersion, value);
        return this;
    }

    public String osVersion() {
        return getMdc(MDCKeys.osVersion);
    }

    public MdcService deviceNetworkType(final String value) {
        setMdc(MDCKeys.deviceNetworkType, value);
        return this;
    }

    public String deviceNetworkType() {
        return getMdc(MDCKeys.deviceNetworkType);
    }


    public MdcService deviceNetworkSpeedDown(final String value) {
        setMdc(MDCKeys.deviceNetworkSpeedDown, value);
        return this;
    }

    public String deviceNetworkSpeedDown() {
        return getMdc(MDCKeys.deviceNetworkSpeedDown);
    }

    public MdcService deviceNetworkSpeedUp(final String value) {
        setMdc(MDCKeys.deviceNetworkSpeedUp, value);
        return this;
    }

    public String deviceNetworkSpeedUp() {
        return getMdc(MDCKeys.deviceNetworkSpeedUp);
    }

    public MdcService deviceNetworkSpeedLatency(final String value) {
        setMdc(MDCKeys.deviceNetworkSpeedLatency, value);
        return this;
    }

    public String deviceNetworkSpeedLatency() {
        return getMdc(MDCKeys.deviceNetworkSpeedLatency);
    }

    public MdcService remoteAddress(final String value) {
        setMdc(MDCKeys.remoteAddress, value);
        return this;
    }

    public String remoteAddress() {
        return getMdc(MDCKeys.remoteAddress);
    }

    public MdcService deviceIp(final String value) {
        setMdc(MDCKeys.deviceIp, value);
        return this;
    }

    public String deviceIp() {
        return getMdc(MDCKeys.deviceIp);
    }

    public MdcService userAgent(final String value) {
        setMdc(MDCKeys.userAgent, value);
        return this;
    }

    public String userAgent() {
        return getMdc(MDCKeys.userAgent);
    }

    public MdcService language(final String value) {
        setMdc(MDCKeys.language, value);
        return this;
    }

    public String language() {
        return getMdc(MDCKeys.language);
    }


    public MdcService country(final String value) {
        setMdc(MDCKeys.country, value);
        return this;
    }

    public String country() {
        return getMdc(MDCKeys.country);
    }

    public MdcService service(final String value) {
        setMdc(MDCKeys.service, value);
        return this;
    }

    public String service() {
        return getMdc(MDCKeys.service);
    }


    public MdcService lifecycleIn() {
        setMdc(MDCKeys.lifecycle, "in");
        return this;
    }

    public Exception lifecycleIn(final VoidFunctionWithException function) {
        Exception error = null;
        if (function != null) {
            lifecycleIn();
            try {
                function.process();
            }
            catch (Exception e) {
                error = e;
            }
            finally {
                lifecycleRemove();
            }
        }

        return error;
    }

    public MdcService lifecycleOut() {
        setMdc(MDCKeys.lifecycle, "out");
        return this;
    }

    public Exception lifecycleOut(final VoidFunctionWithException function) {
        Exception error = null;
        if (function != null) {
            lifecycleOut();
            try {
                function.process();
            }
            catch (Exception e) {
                error = e;
            }
            finally {
                lifecycleRemove();
            }
        }

        return error;
    }


    public MdcService lifecycleRemove() {
        remove(MDCKeys.lifecycle);
        return this;
    }

    public MdcService appService(final String value) {
        setMdc(MDCKeys.appService, value);
        return this;
    }

    public String appService() {
        return getMdc(MDCKeys.appService);
    }

    public MdcService appSubService(final String value) {
        setMdc(MDCKeys.appSubService, value);
        return this;
    }

    public String appSubService() {
        return getMdc(MDCKeys.appSubService);
    }

    public MdcService errorCode(final String value) {
        setMdc(MDCKeys.errorCode, value);
        return this;
    }

    public String errorCode() {
        return getMdc(MDCKeys.errorCode);
    }

    public MdcService errorStatus(final String value) {
        setMdc(MDCKeys.errorStatus, value);
        return this;
    }

    public String errorStatus() {
        return getMdc(MDCKeys.errorStatus);
    }

    public MdcService errorMessage(final String value) {
        setMdc(MDCKeys.errorMessage, value);
        return this;
    }

    public String errorMessage() {
        return getMdc(MDCKeys.errorMessage);
    }

    public MdcService errorType(final String value) {
        setMdc(MDCKeys.errorType, value);
        return this;
    }

    public String errorType() {
        return getMdc(MDCKeys.errorType);
    }

    public MdcService errorMessageDetail(final String value) {
        setMdc(MDCKeys.errorMessageDetail, value);
        return this;
    }

    public String errorMessageDetail() {
        return getMdc(MDCKeys.errorMessageDetail);
    }

    public MdcService partner(final String value) {
        setMdc(MDCKeys.partner, value);
        return this;
    }

    public String partner() {
        return getMdc(MDCKeys.partner);
    }

    public MdcService partnerType(final String value) {
        setMdc(MDCKeys.partnerType, value);
        return this;
    }

    public String partnerType() {
        return getMdc(MDCKeys.partnerType);
    }

    public MdcService partnerService(final String value) {
        setMdc(MDCKeys.partnerService, value);
        return this;
    }

    public String partnerService() {
        return getMdc(MDCKeys.partnerService);
    }

    public MdcService partnerSubService(final String value) {
        setMdc(MDCKeys.partnerSubService, value);
        return this;
    }

    public String partnerSubService() {
        return getMdc(MDCKeys.partnerSubService);
    }

    public MdcService partnerUrl(final String value) {
        setMdc(MDCKeys.partnerUrl, value);
        return this;
    }

    public String partnerUrl() {
        return getMdc(MDCKeys.partnerUrl);
    }

    public MdcService exceptionName(final String value) {
        setMdc(MDCKeys.exceptionName, value);
        return this;
    }

    public String exceptionName() {
        return getMdc(MDCKeys.exceptionName);
    }

    public MdcService duration(final long value) {
        setMdc(MDCKeys.duration, String.valueOf(value));
        return this;
    }

    public String duration() {
        return getMdc(MDCKeys.duration);
    }


    public MdcService functionalUid(final String value) {
        setMdc(MDCKeys.functionalUid, value);
        return this;
    }

    public String functionalUid() {
        return getMdc(MDCKeys.functionalUid);
    }

    public MdcService methodInCause(final String value) {
        setMdc(MDCKeys.methodInCause, value);
        return this;
    }

    public String methodInCause() {
        return getMdc(MDCKeys.methodInCause);
    }

    public MdcService uri(final String value) {
        setMdc(MDCKeys.uri, value);
        return this;
    }

    public String uri() {
        return getMdc(MDCKeys.uri);
    }

    public MdcService requestHeaders(final String value) {
        setMdc(MDCKeys.requestHeaders, value);
        return this;
    }

    public String requestHeaders() {
        return getMdc(MDCKeys.requestHeaders);
    }

    public MdcService responseHeaders(final String value) {
        setMdc(MDCKeys.responseHeaders, value);
        return this;
    }

    public String responseHeaders() {
        return getMdc(MDCKeys.responseHeaders);
    }

    public MdcService parentSpanId(final String value) {
        setMdc(MDCKeys.parentSpanId, value);
        return this;
    }

    public String parentSpanId() {
        return getMdc(MDCKeys.parentSpanId);
    }

    public MdcService traceId(final String value) {
        setMdc(MDCKeys.traceId, value);
        return this;
    }

    public String traceId() {
        String value = getMdc(MDCKeys.traceId);
        if (value == null) {
            value = UUID.randomUUID().toString();
            traceId(value);
        }
        return value;
    }

    public MdcService flags(final String value) {
        setMdc(MDCKeys.flags, value);
        return this;
    }

    public String flags() {
        return getMdc(MDCKeys.flags);
    }

    public MdcService customerId(final String value) {
        setMdc(MDCKeys.customerId, value);
        return this;
    }

    public String customerId() {
        return getMdc(MDCKeys.customerId);
    }

    public MdcService userId(final String value) {
        setMdc(MDCKeys.userId, value);
        return this;
    }

    public String userId() {
        return getMdc(MDCKeys.userId);
    }

    public MdcService productId(final String value) {
        setMdc(MDCKeys.productId, value);
        return this;
    }

    public String useproductIdrId() {
        return getMdc(MDCKeys.productId);
    }

    public MdcService orderId(final String value) {
        setMdc(MDCKeys.orderId, value);
        return this;
    }

    public String orderId() {
        return getMdc(MDCKeys.orderId);
    }

    public MdcService reservationNumber(final String value) {
        setMdc(MDCKeys.reservationNumber, value);
        return this;
    }

    public String reservationNumber() {
        return getMdc(MDCKeys.reservationNumber);
    }
}
