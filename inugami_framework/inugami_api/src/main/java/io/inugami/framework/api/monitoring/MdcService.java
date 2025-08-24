package io.inugami.framework.api.monitoring;

import io.inugami.framework.api.listeners.DefaultApplicationLifecycleSPI;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.functionnals.VoidFunctionWithException;
import io.inugami.framework.interfaces.listeners.ApplicationLifecycleSPI;
import io.inugami.framework.interfaces.models.Tuple;
import io.inugami.framework.interfaces.monitoring.MdcServiceSpi;
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import io.inugami.framework.interfaces.monitoring.logger.MDCKeys;
import io.inugami.framework.interfaces.monitoring.logger.mapper.LoggerMdcMappingSPI;
import io.inugami.framework.interfaces.monitoring.models.Headers;
import io.inugami.framework.interfaces.monitoring.models.IoInfoDTO;
import io.inugami.framework.interfaces.spi.SpiLoader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

@SuppressWarnings({"java:S1845", "java:S115", "java:S1181", "java:S2386", "java:S1125"})
@Getter
@Slf4j
public class MdcService implements MdcServiceSpi {

    private static final String X_B_3_TRACE_ID       = "X-B3-TraceId";
    public static final  String ISO_DATE             = "yyyy-MM-dd'T'HH:mm:ss.sss";
    public static final  String CALL_TYPE_REST       = "REST";
    public static final  String CALL_TYPE_JMS        = "JMS";
    public static final  String CALL_TYPE_RABBITMQ   = "RABBITMQ";
    private static final String DEFAULT_STRING_VALUE = "xxxx";
    private static final String SEP                  = ";";
    private static final String IN                   = "in";
    private static final String OUT                  = "out";
    private static final String SUCCESS              = "success";
    private static final String ERROR                = "error";
    public static final  String NULL                 = "null";

    private List<LoggerMdcMappingSPI> mdcMappers = SpiLoader.getInstance()
                                                            .loadSpiServicesByPriority(LoggerMdcMappingSPI.class);

    private static final MdcService INSTANCE = new MdcService();

    public static MdcService getInstance() {
        return INSTANCE;
    }

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public MdcService() {
        final MdcService instance = this;
        DefaultApplicationLifecycleSPI.register(new ApplicationLifecycleSPI() {
            @Override
            public void onContextRefreshed(final Object event) {
                instance.onContextRefreshed(event);
            }
        });
        initialize();
    }

    public void onContextRefreshed(final Object event) {
        mdcMappers = SpiLoader.getInstance().loadSpiServicesByPriority(LoggerMdcMappingSPI.class);
    }


    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    // =========================================================================
    // METHODS
    // =========================================================================
    public void initialize() {
        final RequestData requestContext = RequestContext.getInstance();

        setMdc(MDCKeys.env, requestContext.getEnv());
        setMdc(MDCKeys.asset, requestContext.getAsset());
        setMdc(MDCKeys.hostname, requestContext.getHostname());
        setMdc(MDCKeys.instanceName, requestContext.getInstanceName());
        setMdc(MDCKeys.instanceNumber, requestContext.getInstanceNumber());
        setMdc(MDCKeys.correlation_id, requestContext.getCorrelationId());
        setMdc(MDCKeys.request_id, requestContext.getRequestId());
        setMdc(MDCKeys.traceId, requestContext.getTraceId());
        setMdc(MDCKeys.conversation_id, requestContext.getConversationId());
        setMdc(MDCKeys.sessionId, requestContext.getSessionId());
        setMdc(MDCKeys.applicationVersion, requestContext.getApplicationVersion());
        setMdc(MDCKeys.callFrom, requestContext.getCallFrom());
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
    // SET MDC
    // =========================================================================
    public MdcServiceSpi setMdc(final Map<String, Serializable> values) {
        if (values == null) {
            return this;
        }

        for (final Map.Entry<String, Serializable> value : values.entrySet()) {
            setMdc(value.getKey(), value.getValue());
        }
        return this;
    }

    public MdcServiceSpi setMdc(final MDCKeys key, final Serializable value) {
        if (key == null) {
            return this;
        }
        if (isNull(value)) {
            remove(key);
            return this;
        }

        return setMdc(key.getCurrentName(), value);
    }

    private static boolean isNull(final Serializable value) {
        return value == null || NULL.equals(value) || String.valueOf(value).isEmpty();
    }


    public MdcServiceSpi setMdc(final String key, final Serializable value) {
        if (isNull(value)) {
            remove(key);
            return this;
        }

        if (value instanceof Date) {
            MDC.put(key, new SimpleDateFormat(ISO_DATE).format((Date) value));
        } else if (value instanceof Calendar) {
            MDC.put(key, new SimpleDateFormat(ISO_DATE).format(((Calendar) value).getTime()));
        } else if (value instanceof LocalDateTime) {
            MDC.put(key, ((LocalDateTime) value).format(DateTimeFormatter.ISO_DATE_TIME));
        } else if (value instanceof LocalDate) {
            MDC.put(key, ((LocalDate) value).format(DateTimeFormatter.ISO_DATE));
        } else {
            MDC.put(key, String.valueOf(value));
        }

        return this;
    }


    public MdcServiceSpi addMdc(final Tuple<String, Serializable>... keys) {
        if (keys != null) {
            for (final Tuple<String, Serializable> key : keys) {
                setMdc(key.getKey(), key.getValue());
            }
        }
        return this;
    }

    public MdcServiceSpi addMdc(final Collection<Tuple<String, Serializable>> keys) {
        if (keys != null) {
            for (final Tuple<String, Serializable> key : keys) {
                setMdc(key.getKey(), key.getValue());
            }
        }
        return this;
    }

    // =========================================================================
    // GET MDC
    // =========================================================================
    public String getMdc(final MDCKeys key) {
        if (key == null) {
            return null;
        }
        return getMdc(key.getCurrentName());
    }


    public String getMdc(final String key) {
        if (key == null) {
            return null;
        }
        return MDC.get(key);
    }

    public boolean getBoolean(final MDCKeys key) {
        return key == null ? false : getBoolean(key.name());
    }

    public boolean getBoolean(final String key) {
        final String value = getMdc(key);
        return Boolean.parseBoolean(value);
    }

    public int getInt(final MDCKeys key) {
        return key == null ? 0 : getInt(key.name());
    }

    public int getInt(final String key) {
        try {
            final String value = getMdc(key);
            return value == null ? 0 : Integer.parseInt(value);
        } catch (final Throwable e) {
            return 0;
        }
    }

    public long getLong(final MDCKeys key) {
        return key == null ? 0L : getLong(key.name());
    }

    public long getLong(final String key) {
        try {
            final String value = getMdc(key);
            return value == null ? 0 : Long.parseLong(value);
        } catch (final Throwable e) {
            return 0;
        }
    }

    public double getDouble(final MDCKeys key) {
        return key == null ? 0.0 : getDouble(key.name());
    }

    public double getDouble(final String key) {
        if (key == null) {
            return 0.0;
        }
        try {
            final String value = getMdc(key);
            return value == null ? 0.0 : Double.parseDouble(value);
        } catch (final Throwable e) {
            return 0.0;
        }
    }

    public Charset getCharset(final MDCKeys key) {
        return key == null ? null : getCharset(key.name());
    }

    public Charset getCharset(final String key) {
        if (key == null) {
            return null;
        }
        final String value  = getMdc(key);
        Charset      result = null;

        if (value != null) {
            try {
                result = Charset.forName(value);
            } catch (final Throwable e) {
                result = StandardCharsets.UTF_8;
            }

        }

        return result;
    }


    public LocalDateTime getLocalDateTime(final MDCKeys key) {
        return key == null ? null : getLocalDateTime(key.name());
    }

    public LocalDateTime getLocalDateTime(final String key) {
        if (key == null) {
            return null;
        }
        LocalDateTime result = null;
        final String  value  = getMdc(key);
        if (value != null) {
            try {
                result = LocalDateTime.parse(value);
            } catch (final Throwable e) {
                log.error(e.getMessage(), e);
            }

        }

        return result;
    }

    public Map<String, String> getAllMdc() {
        final var result = MDC.getCopyOfContextMap();
        return result == null ? new LinkedHashMap<>() : result;
    }

    public Map<String, Serializable> getAllMdcExtended() {
        final Map<String, Serializable> result = new LinkedHashMap<>();

        final Map<String, String> standardMdc = getAllMdc();

        if (mdcMappers == null) {
            result.putAll(standardMdc);
        } else {
            for (final Map.Entry<String, String> entry : standardMdc.entrySet()) {
                for (final LoggerMdcMappingSPI mapper : mdcMappers) {
                    if (mapper.accept(entry.getKey())) {
                        result.put(entry.getKey(), mapper.convert(entry.getValue()));
                    }
                }
            }
        }
        return result;
    }


    // =========================================================================
    // REMOVE
    // =========================================================================
    public MdcServiceSpi remove(final MDCKeys... keys) {
        if (keys != null) {
            for (final MDCKeys key : keys) {
                MDC.remove(key.name());
            }
        }
        return this;
    }

    public MdcServiceSpi remove(final String... keys) {
        if (keys != null) {
            for (final String key : keys) {
                MDC.remove(key);
            }
        }
        return this;
    }

    public MdcServiceSpi clear() {
        MDC.clear();
        return this;
    }


    // =========================================================================
    // FIELDS
    // =========================================================================

    public MdcServiceSpi appClass(final String value) {
        setMdc(MDCKeys.appClass, value);
        return this;
    }

    public String appClass() {
        return getMdc(MDCKeys.appClass);
    }

    public MdcServiceSpi appClassShortName(final String value) {
        setMdc(MDCKeys.appClassShortName, value);
        return this;
    }

    public String appClassShortName() {
        return getMdc(MDCKeys.appClassShortName);
    }

    public MdcServiceSpi domain(final String value) {
        setMdc(MDCKeys.domain, value);
        return this;
    }

    public String domain() {
        return getMdc(MDCKeys.domain);
    }

    public MdcServiceSpi subDomain(final String value) {
        setMdc(MDCKeys.subDomain, value);
        return this;
    }

    public String subDomain() {
        return getMdc(MDCKeys.subDomain);
    }


    public MdcServiceSpi appMethod(final String value) {
        setMdc(MDCKeys.appMethod, value);
        return this;
    }

    public String appMethod() {
        return getMdc(MDCKeys.appMethod);
    }


    public MdcServiceSpi appService(final String value) {
        setMdc(MDCKeys.appService, value);
        return this;
    }

    public String appService() {
        return getMdc(MDCKeys.appService);
    }

    public MdcServiceSpi appSubService(final String value) {
        setMdc(MDCKeys.appSubService, value);
        return this;
    }

    public String appSubService() {
        return getMdc(MDCKeys.appSubService);
    }

    public MdcServiceSpi applicationVersion(final String value) {
        setMdc(MDCKeys.applicationVersion, value);
        return this;
    }

    public String applicationVersion() {
        return getMdc(MDCKeys.applicationVersion);
    }

    public MdcServiceSpi asset(final String value) {
        setMdc(MDCKeys.asset, value);
        return this;
    }

    public String asset() {
        return getMdc(MDCKeys.asset);
    }

    public MdcServiceSpi authProtocol(final String value) {
        setMdc(MDCKeys.authProtocol, value);
        return this;
    }

    public String authProtocol() {
        return getMdc(MDCKeys.authProtocol);
    }

    public MdcServiceSpi callFrom(final String value) {
        setMdc(MDCKeys.callFrom, value);
        return this;
    }

    public String callFrom() {
        return getMdc(MDCKeys.callFrom);
    }

    public MdcServiceSpi callType(final String value) {
        setMdc(MDCKeys.callType, value);
        return this;
    }

    public String callType() {
        return getMdc(MDCKeys.callType);
    }

    public MdcServiceSpi conversationId(final String value) {
        setMdc(MDCKeys.conversation_id, value);
        return this;
    }

    public String conversationId() {
        return getMdc(MDCKeys.conversation_id);
    }

    public MdcServiceSpi correlationId(final String value) {
        setMdc(MDCKeys.correlation_id, value);
        return this;
    }

    public String correlationId() {
        String value = getMdc(MDCKeys.correlation_id);
        if (value == null) {
            value = UUID.randomUUID().toString();
            correlationId(value);
        }
        return value;
    }

    public MdcServiceSpi country(final String value) {
        setMdc(MDCKeys.country, value);
        return this;
    }

    public String country() {
        return getMdc(MDCKeys.country);
    }

    public MdcServiceSpi customerId(final String value) {
        setMdc(MDCKeys.customerId, value);
        return this;
    }

    public String customerId() {
        return getMdc(MDCKeys.customerId);
    }

    public MdcServiceSpi deviceClass(final String value) {
        setMdc(MDCKeys.deviceClass, value);
        return this;
    }

    public String deviceClass() {
        return getMdc(MDCKeys.deviceClass);
    }

    public MdcServiceSpi deviceIdentifier(final String value) {
        setMdc(MDCKeys.deviceIdentifier, value);
        return this;
    }

    public String deviceIdentifier() {
        return getMdc(MDCKeys.deviceIdentifier);
    }

    public MdcServiceSpi deviceIp(final String value) {
        setMdc(MDCKeys.deviceIp, value);
        return this;
    }

    public String deviceIp() {
        return getMdc(MDCKeys.deviceIp);
    }

    public MdcServiceSpi deviceNetworkSpeedDown(final double value) {
        setMdc(MDCKeys.deviceNetworkSpeedDown, value);
        return this;
    }

    public double deviceNetworkSpeedDown() {
        return getDouble(MDCKeys.deviceNetworkSpeedDown);
    }

    public MdcServiceSpi deviceNetworkSpeedLatency(final double value) {
        setMdc(MDCKeys.deviceNetworkSpeedLatency, value);
        return this;
    }

    public double deviceNetworkSpeedLatency() {
        return getDouble(MDCKeys.deviceNetworkSpeedLatency);
    }

    public MdcServiceSpi deviceNetworkSpeedUp(final double value) {
        setMdc(MDCKeys.deviceNetworkSpeedUp, value);
        return this;
    }

    public double deviceNetworkSpeedUp() {
        return getDouble(MDCKeys.deviceNetworkSpeedUp);
    }

    public MdcServiceSpi deviceNetworkType(final String value) {
        setMdc(MDCKeys.deviceNetworkType, value);
        return this;
    }

    public String deviceNetworkType() {
        return getMdc(MDCKeys.deviceNetworkType);
    }

    public MdcServiceSpi deviceType(final String value) {
        setMdc(MDCKeys.deviceType, value);
        return this;
    }

    public String deviceType() {
        return getMdc(MDCKeys.deviceType);
    }

    public MdcServiceSpi duration(final long value) {
        setMdc(MDCKeys.duration, value);
        return this;
    }

    public long duration() {
        return getLong(MDCKeys.duration);
    }

    public MdcServiceSpi env(final String value) {
        setMdc(MDCKeys.env, value);
        return this;
    }

    public String env() {
        return getMdc(MDCKeys.env);
    }

    public MdcServiceSpi errorCategory(final String value) {
        setMdc(MDCKeys.errorCategory, value);
        return this;
    }

    public String errorCategory() {
        return getMdc(MDCKeys.errorCategory);
    }

    public MdcServiceSpi errorCode(final String value) {
        setMdc(MDCKeys.errorCode, value);
        return this;
    }


    public boolean hasError() {
        return errorCode() != null;
    }

    public MdcServiceSpi errorCode(final ErrorCode errorCode) {
        if (errorCode == null) {
            errorCodeRemove();
            return this;
        }

        for (final Map.Entry<String, Serializable> entry : errorCode.toMap().entrySet()) {
            if (ErrorCode.STATUS_CODE.equals(entry.getKey())) {
                setMdc(entry.getKey(), entry.getValue() == null ? 500 : entry.getValue());
            } else {
                setMdc(entry.getKey(), entry.getValue());
            }

        }
        return this;
    }

    public MdcServiceSpi errorCodeRemove() {
        for (final String key : ErrorCode.KEYS_SET) {
            remove(key);
        }
        return this;
    }

    public ErrorCode errorCode() {
        final String code = getMdc(MDCKeys.errorCode);
        if (code == null) {
            return null;
        }
        return DefaultErrorCode.buildUndefineErrorCode()
                               .errorCode(code)
                               .category(errorCategory())
                               .statusCode(errorStatus())
                               .message(errorMessage())
                               .messageDetail(errorMessageDetail())
                               .errorType(errorType())
                               .retryable(errorRetryable())
                               .exploitationError(errorExploitationError())
                               .field(errorField())
                               .rollback(errorRollback())
                               .url(errorUrl())
                               .domain(errorDomain())
                               .subDomain(errorSubDomain())
                               .build();
    }


    public MdcServiceSpi errorExploitationError(final boolean value) {
        setMdc(MDCKeys.errorExploitationError, value);
        return this;
    }

    public boolean errorExploitationError() {
        return getBoolean(MDCKeys.errorExploitationError);
    }

    public MdcServiceSpi errorField(final String... value) {
        if (value == null) {
            remove(MDCKeys.errorField);
        }
        setMdc(MDCKeys.errorField, String.join(SEP, value));
        return this;
    }

    public String errorField() {
        return getMdc(MDCKeys.errorField);
    }


    public MdcServiceSpi errorMessage(final String value) {
        setMdc(MDCKeys.errorMessage, value);
        return this;
    }

    public String errorMessage() {
        return getMdc(MDCKeys.errorMessage);
    }

    public MdcServiceSpi errorMessageDetail(final String value) {
        setMdc(MDCKeys.errorMessageDetail, value);
        return this;
    }

    public String errorMessageDetail() {
        return getMdc(MDCKeys.errorMessageDetail);
    }

    public MdcServiceSpi errorRetryable(final boolean value) {
        setMdc(MDCKeys.errorRetryable, value);
        return this;
    }

    public boolean errorRetryable() {
        return getBoolean(MDCKeys.errorRetryable);
    }

    public MdcServiceSpi errorRollback(final boolean value) {
        setMdc(MDCKeys.errorRollback, value);
        return this;
    }

    public boolean errorRollback() {
        return getBoolean(MDCKeys.errorRollback);
    }

    /**
     * errorStatus is <strong>int value</strong>
     */
    public MdcServiceSpi errorStatus(final int value) {
        setMdc(MDCKeys.errorStatus, value);
        return this;
    }

    public int errorStatus() {
        return getInt(MDCKeys.errorStatus);
    }

    public MdcServiceSpi errorType(final String value) {
        setMdc(MDCKeys.errorType, value);
        return this;
    }

    public String errorType() {
        return getMdc(MDCKeys.errorType);
    }

    public MdcServiceSpi errorUrl(final String value) {
        setMdc(MDCKeys.errorUrl, value);
        return this;
    }

    public String errorUrl() {
        return getMdc(MDCKeys.errorUrl);
    }

    public MdcServiceSpi exceptionName(final String value) {
        setMdc(MDCKeys.exceptionName, value);
        return this;
    }

    public MdcServiceSpi errorDomain(final String value) {
        setMdc(MDCKeys.errorDomain, value);
        return this;
    }

    public String errorDomain() {
        return getMdc(MDCKeys.errorDomain);
    }

    public MdcServiceSpi errorSubDomain(final String value) {
        setMdc(MDCKeys.errorSubDomain, value);
        return this;
    }

    public String errorSubDomain() {
        return getMdc(MDCKeys.errorSubDomain);
    }

    public String exceptionName() {
        return getMdc(MDCKeys.exceptionName);
    }

    public MdcServiceSpi flags(final String value) {
        setMdc(MDCKeys.flags, value);
        return this;
    }

    public String flags() {
        return getMdc(MDCKeys.flags);
    }

    public MdcServiceSpi from(final LocalDateTime value) {
        setMdc(MDCKeys.from, value);
        return this;
    }

    public LocalDateTime from() {
        return getLocalDateTime(MDCKeys.from);
    }


    public MdcServiceSpi groupId(final String value) {
        setMdc(MDCKeys.groupId, value);
        return this;
    }

    public String groupId() {
        return getMdc(MDCKeys.groupId);
    }


    public MdcServiceSpi artifactId(final String value) {
        setMdc(MDCKeys.artifactId, value);
        return this;
    }

    public String artifactId() {
        return getMdc(MDCKeys.artifactId);
    }

    public MdcServiceSpi commitId(final String value) {
        setMdc(MDCKeys.commitId, value);
        return this;
    }

    public String commitId() {
        return getMdc(MDCKeys.commitId);
    }


    public MdcServiceSpi commitDate(final String value) {
        setMdc(MDCKeys.commitDate, value);
        return this;
    }

    public String commitDate() {
        return getMdc(MDCKeys.commitDate);
    }


    public MdcServiceSpi fromTimestamp(final long value) {
        setMdc(MDCKeys.fromTimestamp, value);
        return this;
    }

    public long fromTimestamp() {
        return getLong(MDCKeys.fromTimestamp);
    }

    public MdcServiceSpi functionalUid(final String value) {
        setMdc(MDCKeys.functionalUid, value);
        return this;
    }

    public String functionalUid() {
        return getMdc(MDCKeys.functionalUid);
    }

    public MdcServiceSpi globalStatus(final String value) {
        setMdc(MDCKeys.globalStatus, value);
        return this;
    }

    public String globalStatus() {
        return getMdc(MDCKeys.globalStatus);
    }

    public void globalStatusSuccess() {
        setMdc(MDCKeys.globalStatus, SUCCESS);
    }

    public void globalStatusError() {
        setMdc(MDCKeys.globalStatus, ERROR);
    }

    public MdcServiceSpi removeGlobalStatus() {
        remove(MDCKeys.globalStatus);
        return this;
    }

    public MdcServiceSpi healthStatus(final String value) {
        setMdc(MDCKeys.healthStatus, value);
        return this;
    }

    public String healthStatus() {
        return getMdc(MDCKeys.healthStatus);
    }

    public MdcServiceSpi hostname(final String value) {
        setMdc(MDCKeys.hostname, value);
        return this;
    }

    public String hostname() {
        return getMdc(MDCKeys.hostname);
    }

    public MdcServiceSpi httpStatus(final int value) {
        setMdc(MDCKeys.httpStatus, value);
        return this;
    }

    public int httpStatus() {
        return getInt(MDCKeys.httpStatus);
    }

    public MdcServiceSpi instanceName(final String value) {
        setMdc(MDCKeys.instanceName, value);
        return this;
    }

    public String instanceName() {
        return getMdc(MDCKeys.instanceName);
    }

    public MdcServiceSpi instanceNumber(final String value) {
        setMdc(MDCKeys.instanceNumber, value);
        return this;
    }

    public String instanceNumber() {
        return getMdc(MDCKeys.instanceNumber);
    }

    public MdcServiceSpi ioinfoIoLog(final IoInfoDTO info) {
        if (info == null) {
            return removeIoinfoIoLog();
        }

        url(info.getUrl());
        verb(info.getMethod());
        service(info.getPartnerService());
        appSubService(info.getPartnerSubService());
        status(info.getStatus());
        duration(info.getDuration());
        return this;
    }

    public MdcServiceSpi removeIoinfoIoLog() {
        remove(MDCKeys.url,
               MDCKeys.verb,
               MDCKeys.service,
               MDCKeys.appSubService,
               MDCKeys.status,
               MDCKeys.duration
        );
        return this;
    }

    public MdcServiceSpi ioinfoPartner(final IoInfoDTO info) {
        if (info == null) {
            return removeIoinfoPartner();
        }
        partner(info.getPartnerName());
        partnerUrl(info.getUrl());
        partnerVerb(info.getMethod());
        partnerService(info.getPartnerService());
        partnerSubService(info.getPartnerSubService());
        partnerRequestCharset(info.getRequestCharset());
        partnerResponseStatus(info.getStatus());
        partnerResponseCharset(info.getResponseCharset());
        partnerResponseDuration(info.getDuration());
        partnerResponseMessage(info.getMessage());
        return this;
    }

    public MdcServiceSpi partnerRemove() {
        return removeIoinfoPartner();
    }

    public MdcServiceSpi removeIoinfoPartner() {
        remove(
                MDCKeys.errorCategory,
                MDCKeys.errorCode,
                MDCKeys.errorExploitationError,
                MDCKeys.errorField,
                MDCKeys.errorMessage,
                MDCKeys.errorMessageDetail,
                MDCKeys.errorRetryable,
                MDCKeys.errorRollback,
                MDCKeys.errorStatus,
                MDCKeys.errorType,
                MDCKeys.errorUrl,
                MDCKeys.exceptionName,
                MDCKeys.errorDomain,
                MDCKeys.errorSubDomain,
                MDCKeys.partner,
                MDCKeys.partnerRequestCharset,
                MDCKeys.partnerResponseCharset,
                MDCKeys.partnerResponseDuration,
                MDCKeys.partnerResponseMessage,
                MDCKeys.partnerResponseStatus,
                MDCKeys.partnerService,
                MDCKeys.partnerSubService,
                MDCKeys.partnerType,
                MDCKeys.partnerUrl,
                MDCKeys.partnerVerb,
                MDCKeys.lifecycle,
                MDCKeys.duration
        );
        return this;
    }

    public MdcServiceSpi language(final String value) {
        setMdc(MDCKeys.language, value);
        return this;
    }

    public String language() {
        return getMdc(MDCKeys.language);
    }

    public MdcServiceSpi lifecycle(final String value) {
        setMdc(MDCKeys.lifecycle, value);
        return this;
    }

    public String lifecycle() {
        return getMdc(MDCKeys.lifecycle);
    }

    public MdcServiceSpi lifecycleIn() {
        setMdc(MDCKeys.lifecycle, IN);
        return this;
    }

    public Exception lifecycleIn(final VoidFunctionWithException function) {
        Exception error = null;
        if (function != null) {
            lifecycleIn();
            try {
                function.process();
            } catch (final Exception e) {
                error = e;
            } finally {
                lifecycleRemove();
            }
        }

        return error;
    }

    public MdcServiceSpi lifecycleOut() {
        setMdc(MDCKeys.lifecycle, OUT);
        return this;
    }

    public Exception lifecycleOut(final VoidFunctionWithException function) {
        Exception error = null;
        if (function != null) {
            lifecycleOut();
            try {
                function.process();
            } catch (final Exception e) {
                error = e;
            } finally {
                lifecycleRemove();
            }
        }

        return error;
    }


    public MdcServiceSpi lifecycleRemove() {
        remove(MDCKeys.lifecycle);
        return this;
    }

    public MdcServiceSpi majorVersion(final String value) {
        setMdc(MDCKeys.majorVersion, value);
        return this;
    }

    public String majorVersion() {
        return getMdc(MDCKeys.majorVersion);
    }

    public MdcServiceSpi messageId(final String value) {
        setMdc(MDCKeys.messageId, value);
        return this;
    }

    public String messageId() {
        return getMdc(MDCKeys.messageId);
    }

    public MdcServiceSpi methodInCause(final String value) {
        setMdc(MDCKeys.methodInCause, value);
        return this;
    }

    public String methodInCause() {
        return getMdc(MDCKeys.methodInCause);
    }

    public Map<String, String> getTrackingInformation() {
        return getTrackingInformation(null);
    }

    public Map<String, String> getTrackingInformation(final Headers headers) {
        final Map<String, String> result = new LinkedHashMap<>();
        applyIfNotNull(deviceIdentifier(), value -> result.put(Headers.X_DEVICE_IDENTIFIER, value));
        applyIfNotNull(correlationId(), value -> result.put(Headers.X_CORRELATION_ID, value));
        applyIfNotNull(conversationId(), value -> result.put(Headers.X_CONVERSATION_ID, value));
        applyIfNotNull(traceId(), value -> result.put(Headers.X_B_3_TRACEID, value));

        return result;
    }

    public MdcServiceSpi orderId(final String value) {
        setMdc(MDCKeys.orderId, value);
        return this;
    }

    public String orderId() {
        return getMdc(MDCKeys.orderId);
    }

    public MdcServiceSpi osVersion(final String value) {
        setMdc(MDCKeys.osVersion, value);
        return this;
    }

    public String osVersion() {
        return getMdc(MDCKeys.osVersion);
    }

    public MdcServiceSpi parentSpanId(final String value) {
        setMdc(MDCKeys.parentSpanId, value);
        return this;
    }

    public String parentSpanId() {
        return getMdc(MDCKeys.parentSpanId);
    }

    public MdcServiceSpi partner(final String value) {
        setMdc(MDCKeys.partner, value);
        return this;
    }

    public String partner() {
        return getMdc(MDCKeys.partner);
    }

    public MdcServiceSpi partnerRequestCharset(final String value) {
        setMdc(MDCKeys.partnerRequestCharset, value);
        return this;
    }

    public MdcServiceSpi partnerRequestCharset(final Charset value) {
        if (value == null) {
            remove(MDCKeys.partnerRequestCharset);
        } else {
            setMdc(MDCKeys.partnerRequestCharset, value.name());
        }
        return this;
    }

    public Charset partnerRequestCharset() {
        return getCharset(MDCKeys.partnerRequestCharset);
    }


    public MdcServiceSpi partnerResponseCharset(final String value) {
        setMdc(MDCKeys.partnerResponseCharset, value);
        return this;
    }

    public MdcServiceSpi partnerResponseCharset(final Charset value) {
        if (value == null) {
            remove(MDCKeys.partnerResponseCharset);
        } else {
            setMdc(MDCKeys.partnerResponseCharset, value.name());
        }
        return this;
    }

    public Charset partnerResponseCharset() {
        return getCharset(MDCKeys.partnerResponseCharset);
    }

    public MdcServiceSpi partnerResponseDuration(final long value) {
        setMdc(MDCKeys.partnerResponseDuration, value);
        return this;
    }

    public long partnerResponseDuration() {
        return getLong(MDCKeys.partnerResponseDuration);
    }

    public MdcServiceSpi partnerResponseMessage(final String value) {
        setMdc(MDCKeys.partnerResponseMessage, value);
        return this;
    }

    public String partnerResponseMessage() {
        return getMdc(MDCKeys.partnerResponseMessage);
    }

    public MdcServiceSpi partnerResponseStatus(final int value) {
        setMdc(MDCKeys.partnerResponseStatus, value);
        return this;
    }

    public int partnerResponseStatus() {
        return getInt(MDCKeys.partnerResponseStatus);
    }

    public MdcServiceSpi partnerService(final String value) {
        setMdc(MDCKeys.partnerService, value);
        return this;
    }

    public String partnerService() {
        return getMdc(MDCKeys.partnerService);
    }

    public MdcServiceSpi partnerSubService(final String value) {
        setMdc(MDCKeys.partnerSubService, value);
        return this;
    }

    public String partnerSubService() {
        return getMdc(MDCKeys.partnerSubService);
    }

    public MdcServiceSpi partnerType(final String value) {
        setMdc(MDCKeys.partnerType, value);
        return this;
    }

    public String partnerType() {
        return getMdc(MDCKeys.partnerType);
    }

    public MdcServiceSpi partnerUrl(final String value) {
        setMdc(MDCKeys.partnerUrl, value);
        return this;
    }

    public String partnerUrl() {
        return getMdc(MDCKeys.partnerUrl);
    }

    public MdcServiceSpi partnerVerb(final String value) {
        setMdc(MDCKeys.partnerVerb, value);
        return this;
    }

    public String partnerVerb() {
        return getMdc(MDCKeys.partnerVerb);
    }

    public MdcServiceSpi price(final double value) {
        setMdc(MDCKeys.price, value);
        return this;
    }

    public double price() {
        return getDouble(MDCKeys.price);
    }

    public MdcServiceSpi principal(final String value) {
        setMdc(MDCKeys.principal, value);
        return this;
    }

    public String principal() {
        return getMdc(MDCKeys.principal);
    }

    public MdcServiceSpi productId(final String value) {
        setMdc(MDCKeys.productId, value);
        return this;
    }

    public String productId() {
        return getMdc(MDCKeys.productId);
    }

    public MdcServiceSpi processId(final String value) {
        setMdc(MDCKeys.processId, value);
        return this;
    }

    public String processId() {
        return getMdc(MDCKeys.processId);
    }

    public MdcServiceSpi processName(final String value) {
        setMdc(MDCKeys.processName, value);
        return this;
    }

    public String processName() {
        return getMdc(MDCKeys.processName);
    }

    public MdcServiceSpi processStatus(final String value) {
        setMdc(MDCKeys.processStatus, value);
        return this;
    }

    public String processStatus() {
        return getMdc(MDCKeys.processStatus);
    }

    public MdcServiceSpi quantity(final double value) {
        setMdc(MDCKeys.quantity, value);
        return this;
    }

    public double quantity() {
        return getDouble(MDCKeys.quantity);
    }

    public MdcServiceSpi remoteAddress(final String value) {
        setMdc(MDCKeys.remoteAddress, value);
        return this;
    }

    public String remoteAddress() {
        return getMdc(MDCKeys.remoteAddress);
    }

    public MdcServiceSpi requestHeaders(final String value) {
        setMdc(MDCKeys.requestHeaders, value);
        return this;
    }

    public String requestHeaders() {
        return getMdc(MDCKeys.requestHeaders);
    }

    public MdcServiceSpi requestId(final String value) {
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

    public MdcServiceSpi reservationNumber(final String value) {
        setMdc(MDCKeys.reservationNumber, value);
        return this;
    }

    public String reservationNumber() {
        return getMdc(MDCKeys.reservationNumber);
    }

    public MdcServiceSpi responseHeaders(final String value) {
        setMdc(MDCKeys.responseHeaders, value);
        return this;
    }

    public String responseHeaders() {
        return getMdc(MDCKeys.responseHeaders);
    }

    public MdcServiceSpi service(final String value) {
        setMdc(MDCKeys.service, value);
        return this;
    }

    public String service() {
        return getMdc(MDCKeys.service);
    }

    public MdcServiceSpi sessionId(final String value) {
        setMdc(MDCKeys.sessionId, value);
        return this;
    }

    public String sessionId() {
        return getMdc(MDCKeys.sessionId);
    }

    public MdcServiceSpi size(final double value) {
        setMdc(MDCKeys.size, value);
        return this;
    }

    public double size() {
        return getDouble(MDCKeys.size);
    }

    public MdcServiceSpi traceId(final String value) {
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

    public MdcServiceSpi until(final LocalDateTime value) {
        setMdc(MDCKeys.until, value);
        return this;
    }

    public LocalDateTime until() {
        return getLocalDateTime(MDCKeys.until);
    }

    public MdcServiceSpi untilTimestamp(final long value) {
        setMdc(MDCKeys.untilTimestamp, value);
        return this;
    }

    public long untilTimestamp() {
        return getLong(MDCKeys.untilTimestamp);
    }

    public MdcServiceSpi uri(final String value) {
        setMdc(MDCKeys.uri, value);
        return this;
    }

    public String uri() {
        return getMdc(MDCKeys.uri);
    }

    public MdcServiceSpi url(final String value) {
        setMdc(MDCKeys.url, value);
        return this;
    }

    public String url() {
        return getMdc(MDCKeys.url);
    }

    public MdcServiceSpi urlPattern(final String value) {
        setMdc(MDCKeys.urlPattern, value);
        return this;
    }

    public String urlPattern() {
        return getMdc(MDCKeys.urlPattern);
    }


    public MdcServiceSpi userAgent(final String value) {
        setMdc(MDCKeys.userAgent, value);
        return this;
    }

    public String userAgent() {
        return getMdc(MDCKeys.userAgent);
    }

    public MdcServiceSpi userId(final String value) {
        setMdc(MDCKeys.userId, value);
        return this;
    }

    public String userId() {
        return getMdc(MDCKeys.userId);
    }

    public MdcServiceSpi verb(final String value) {
        setMdc(MDCKeys.verb, value);
        return this;
    }

    public MdcServiceSpi status(final int status) {
        setMdc(MDCKeys.status, status);
        return this;
    }

    public int status() {
        final int status = getInt(MDCKeys.status);
        return status < 200 ? 200 : status;
    }

    public String verb() {
        return getMdc(MDCKeys.verb);
    }

    public MdcServiceSpi version(final String value) {
        setMdc(MDCKeys.version, value);
        return this;
    }

    public String version() {
        return getMdc(MDCKeys.version);
    }

    public MdcServiceSpi warning(final String value) {
        setMdc(MDCKeys.warning, value);
        return this;
    }

    public String warning() {
        return getMdc(MDCKeys.warning);
    }

}

