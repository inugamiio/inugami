package io.inugami.api.monitoring;

import io.inugami.api.exceptions.DefaultErrorCode;
import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.functionnals.VoidFunctionWithException;
import io.inugami.api.listeners.ApplicationLifecycleSPI;
import io.inugami.api.listeners.DefaultApplicationLifecycleSPI;
import io.inugami.api.loggers.mdc.mapper.LoggerMdcMappingSPI;
import io.inugami.api.models.Tuple;
import io.inugami.api.monitoring.models.Headers;
import io.inugami.api.monitoring.models.IoInfoDTO;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.spi.SpiLoader;
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

import static io.inugami.api.functionnals.FunctionalUtils.applyIfNotNull;

@SuppressWarnings({"java:S1845"})
@Getter
@Slf4j
public class MdcService implements ApplicationLifecycleSPI {

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

    private Headers headers;

    private List<LoggerMdcMappingSPI> mdcMappers = SpiLoader.getInstance().loadSpiServicesByPriority(LoggerMdcMappingSPI.class);


    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    private MdcService() {
        DefaultApplicationLifecycleSPI.register(this);
        headers = new Headers().refreshConfig();
    }

    @Override
    public void onConfigurationReady(final ConfigHandler<String, String> configuration) {
        headers = new Headers().refreshConfig(configuration);
    }

    @Override
    public void onContextRefreshed(final Object event) {
        mdcMappers = SpiLoader.getInstance().loadSpiServicesByPriority(LoggerMdcMappingSPI.class);
    }


    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @Getter
    public enum MDCKeys {
        appClass,
        appClassShortName,
        appMethod,
        appService,
        appSubService,
        applicationVersion,
        asset,
        authProtocol,
        callFrom,
        callType,
        conversation_id,
        correlation_id,
        country,
        customerId,
        deviceClass,
        deviceIdentifier,
        deviceIp,
        deviceNetworkSpeedDown(Double.valueOf(0.0)),
        deviceNetworkSpeedLatency(Double.valueOf(0.0)),
        deviceNetworkSpeedUp(Double.valueOf(0.0)),
        deviceNetworkType,
        deviceType,
        domain("xxxx", "appDomain"),
        duration(Long.valueOf(0)),
        env,
        errorCategory,
        errorCode,
        errorExploitationError(Boolean.FALSE),
        errorField,
        errorMessage,
        errorMessageDetail,
        errorRetryable(Boolean.FALSE),
        errorRollback(Boolean.FALSE),
        /**
         * errorStatus is <strong>int value</strong>
         */
        errorStatus,
        errorType,
        errorUrl,
        exceptionName,
        errorDomain,
        errorSubDomain,
        flags,
        from(LocalDateTime.now()),
        fromTimestamp(Long.valueOf(0)),
        functionalUid,
        globalStatus,
        healthStatus("up"),
        hostname,
        httpStatus(Integer.valueOf(0)),
        instanceName,
        instanceNumber,
        language,
        lifecycle,
        majorVersion,
        messageId,
        methodInCause,
        orderId,
        osVersion,
        parentSpanId,
        partner,
        partnerRequestCharset,
        partnerResponseCharset,
        partnerResponseDuration(Long.valueOf(0)),
        partnerResponseMessage,
        partnerResponseStatus(Integer.valueOf(0)),
        partnerService,
        partnerSubService,
        partnerType,
        partnerUrl,
        partnerVerb,
        price(Double.valueOf(0.0)),
        principal,
        productId,
        processId,
        processName,
        processStatus,
        quantity(Double.valueOf(0.0)),
        remoteAddress,
        requestHeaders,
        request_id,
        reservationNumber,
        responseHeaders,
        service,
        sessionId,
        size(Double.valueOf(0.0)),
        traceId,
        until(LocalDateTime.now()),
        untilTimestamp(Long.valueOf(0)),
        uri("xxxx", "appUri"),
        url("xxxx", "appUrl"),
        urlPattern,
        userAgent,
        userId,
        status,
        subDomain("xxxx", "appSubDomain"),
        verb("xxxx", "appVerb"),
        version,
        warning;

        public static final MDCKeys[]                     VALUES = values();
        private final       Serializable                  defaultValue;
        private final       Class<? extends Serializable> type;

        private final String currentName;

        private MDCKeys() {
            this.defaultValue = DEFAULT_STRING_VALUE;
            type = String.class;
            currentName = this.name();
        }

        private MDCKeys(final Serializable defaultValue) {
            this.defaultValue = defaultValue;
            type = defaultValue.getClass();
            currentName = this.name();
        }

        private MDCKeys(final Serializable defaultValue, final String name) {
            this.defaultValue = defaultValue;
            type = defaultValue.getClass();
            currentName = name;
        }
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
    public MdcService setMdc(final Map<String, Serializable> values) {
        if (values == null) {
            return this;
        }

        for (final Map.Entry<String, Serializable> value : values.entrySet()) {
            setMdc(value.getKey(), value.getValue());
        }
        return this;
    }

    public MdcService setMdc(final MDCKeys key, final Serializable value) {
        if (key == null) {
            return this;
        }
        if (value == null) {
            remove(key);
            return this;
        }

        return setMdc(key.getCurrentName(), value);
    }


    public MdcService setMdc(final String key, final Serializable value) {
        if (key == null || (value instanceof String && "null".equalsIgnoreCase(String.valueOf(value)))) {
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


    public MdcService addMdc(final Tuple<String, Serializable>... keys) {
        if (keys != null) {
            for (final Tuple<String, Serializable> key : keys) {
                setMdc(key.getKey(), key.getValue());
            }
        }
        return this;
    }

    public MdcService addMdc(final Collection<Tuple<String, Serializable>> keys) {
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

    private int getInt(final MDCKeys key) {
        return key == null ? 0 : getInt(key.name());
    }

    private int getInt(final String key) {
        try {
            final String value = getMdc(key);
            return value == null ? 0 : Integer.parseInt(value);
        } catch (final Throwable e) {
            return 0;
        }
    }

    private long getLong(final MDCKeys key) {
        return key == null ? 0L : getLong(key.name());
    }

    private long getLong(final String key) {
        try {
            final String value = getMdc(key);
            return value == null ? 0 : Long.parseLong(value);
        } catch (final Throwable e) {
            return 0;
        }
    }

    private double getDouble(final MDCKeys key) {
        return key == null ? 0.0 : getDouble(key.name());
    }

    private double getDouble(final String key) {
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
        return MDC.getCopyOfContextMap();
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
    public MdcService remove(final MDCKeys... keys) {
        if (keys != null) {
            for (final MDCKeys key : keys) {
                MDC.remove(key.name());
            }
        }
        return this;
    }

    public MdcService remove(final String... keys) {
        if (keys != null) {
            for (final String key : keys) {
                MDC.remove(key);
            }
        }
        return this;
    }

    public MdcService clear() {
        MDC.clear();
        return this;
    }

    // =========================================================================
    // FIELDS
    // =========================================================================
    public MdcService appClass(final String value) {
        setMdc(MDCKeys.appClass, value);
        return this;
    }

    public String appClass() {
        return getMdc(MDCKeys.appClass);
    }

    public MdcService appClassShortName(final String value) {
        setMdc(MDCKeys.appClassShortName, value);
        return this;
    }

    public String appClassShortName() {
        return getMdc(MDCKeys.appClassShortName);
    }

    public MdcService domain(final String value) {
        setMdc(MDCKeys.domain, value);
        return this;
    }

    public String domain() {
        return getMdc(MDCKeys.domain);
    }

    public MdcService subDomain(final String value) {
        setMdc(MDCKeys.subDomain, value);
        return this;
    }

    public String subDomain() {
        return getMdc(MDCKeys.subDomain);
    }


    public MdcService appMethod(final String value) {
        setMdc(MDCKeys.appMethod, value);
        return this;
    }

    public String appMethod() {
        return getMdc(MDCKeys.appMethod);
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

    public MdcService applicationVersion(final String value) {
        setMdc(MDCKeys.applicationVersion, value);
        return this;
    }

    public String applicationVersion() {
        return getMdc(MDCKeys.applicationVersion);
    }

    public MdcService asset(final String value) {
        setMdc(MDCKeys.asset, value);
        return this;
    }

    public String asset() {
        return getMdc(MDCKeys.asset);
    }

    public MdcService authProtocol(final String value) {
        setMdc(MDCKeys.authProtocol, value);
        return this;
    }

    public String authProtocol() {
        return getMdc(MDCKeys.authProtocol);
    }

    public MdcService callFrom(final String value) {
        setMdc(MDCKeys.callFrom, value);
        return this;
    }

    public String callFrom() {
        return getMdc(MDCKeys.callFrom);
    }

    public MdcService callType(final String value) {
        setMdc(MDCKeys.callType, value);
        return this;
    }

    public String callType() {
        return getMdc(MDCKeys.callType);
    }

    public MdcService conversationId(final String value) {
        setMdc(MDCKeys.conversation_id, value);
        return this;
    }

    public String conversationId() {
        return getMdc(MDCKeys.conversation_id);
    }

    public MdcService correlationId(final String value) {
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

    public MdcService country(final String value) {
        setMdc(MDCKeys.country, value);
        return this;
    }

    public String country() {
        return getMdc(MDCKeys.country);
    }

    public MdcService customerId(final String value) {
        setMdc(MDCKeys.customerId, value);
        return this;
    }

    public String customerId() {
        return getMdc(MDCKeys.customerId);
    }

    public MdcService deviceClass(final String value) {
        setMdc(MDCKeys.deviceClass, value);
        return this;
    }

    public String deviceClass() {
        return getMdc(MDCKeys.deviceClass);
    }

    public MdcService deviceIdentifier(final String value) {
        setMdc(MDCKeys.deviceIdentifier, value);
        return this;
    }

    public String deviceIdentifier() {
        return getMdc(MDCKeys.deviceIdentifier);
    }

    public MdcService deviceIp(final String value) {
        setMdc(MDCKeys.deviceIp, value);
        return this;
    }

    public String deviceIp() {
        return getMdc(MDCKeys.deviceIp);
    }

    public MdcService deviceNetworkSpeedDown(final double value) {
        setMdc(MDCKeys.deviceNetworkSpeedDown, value);
        return this;
    }

    public double deviceNetworkSpeedDown() {
        return getDouble(MDCKeys.deviceNetworkSpeedDown);
    }

    public MdcService deviceNetworkSpeedLatency(final double value) {
        setMdc(MDCKeys.deviceNetworkSpeedLatency, value);
        return this;
    }

    public double deviceNetworkSpeedLatency() {
        return getDouble(MDCKeys.deviceNetworkSpeedLatency);
    }

    public MdcService deviceNetworkSpeedUp(final double value) {
        setMdc(MDCKeys.deviceNetworkSpeedUp, value);
        return this;
    }

    public double deviceNetworkSpeedUp() {
        return getDouble(MDCKeys.deviceNetworkSpeedUp);
    }

    public MdcService deviceNetworkType(final String value) {
        setMdc(MDCKeys.deviceNetworkType, value);
        return this;
    }

    public String deviceNetworkType() {
        return getMdc(MDCKeys.deviceNetworkType);
    }

    public MdcService deviceType(final String value) {
        setMdc(MDCKeys.deviceType, value);
        return this;
    }

    public String deviceType() {
        return getMdc(MDCKeys.deviceType);
    }

    public MdcService duration(final long value) {
        setMdc(MDCKeys.duration, value);
        return this;
    }

    public long duration() {
        return getLong(MDCKeys.duration);
    }

    public MdcService env(final String value) {
        setMdc(MDCKeys.env, value);
        return this;
    }

    public String env() {
        return getMdc(MDCKeys.env);
    }

    public MdcService errorCategory(final String value) {
        setMdc(MDCKeys.errorCategory, value);
        return this;
    }

    public String errorCategory() {
        return getMdc(MDCKeys.errorCategory);
    }

    public MdcService errorCode(final String value) {
        setMdc(MDCKeys.errorCode, value);
        return this;
    }


    public boolean hasError() {
        return errorCode() != null;
    }

    public MdcService errorCode(final ErrorCode errorCode) {
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

    public MdcService errorCodeRemove() {
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


    public MdcService errorExploitationError(final boolean value) {
        setMdc(MDCKeys.errorExploitationError, value);
        return this;
    }

    public boolean errorExploitationError() {
        return getBoolean(MDCKeys.errorExploitationError);
    }

    public MdcService errorField(final String... value) {
        if (value == null) {
            remove(MDCKeys.errorField);
        }
        setMdc(MDCKeys.errorField, String.join(SEP, value));
        return this;
    }

    public String errorField() {
        return getMdc(MDCKeys.errorField);
    }


    public MdcService errorMessage(final String value) {
        setMdc(MDCKeys.errorMessage, value);
        return this;
    }

    public String errorMessage() {
        return getMdc(MDCKeys.errorMessage);
    }

    public MdcService errorMessageDetail(final String value) {
        setMdc(MDCKeys.errorMessageDetail, value);
        return this;
    }

    public String errorMessageDetail() {
        return getMdc(MDCKeys.errorMessageDetail);
    }

    public MdcService errorRetryable(final boolean value) {
        setMdc(MDCKeys.errorRetryable, value);
        return this;
    }

    public boolean errorRetryable() {
        return getBoolean(MDCKeys.errorRetryable);
    }

    public MdcService errorRollback(final boolean value) {
        setMdc(MDCKeys.errorRollback, value);
        return this;
    }

    public boolean errorRollback() {
        return getBoolean(MDCKeys.errorRollback);
    }

    /**
     * errorStatus is <strong>int value</strong>
     */
    public MdcService errorStatus(final int value) {
        setMdc(MDCKeys.errorStatus, value);
        return this;
    }

    public int errorStatus() {
        return getInt(MDCKeys.errorStatus);
    }

    public MdcService errorType(final String value) {
        setMdc(MDCKeys.errorType, value);
        return this;
    }

    public String errorType() {
        return getMdc(MDCKeys.errorType);
    }

    public MdcService errorUrl(final String value) {
        setMdc(MDCKeys.errorUrl, value);
        return this;
    }

    public String errorUrl() {
        return getMdc(MDCKeys.errorUrl);
    }

    public MdcService exceptionName(final String value) {
        setMdc(MDCKeys.exceptionName, value);
        return this;
    }

    public MdcService errorDomain(final String value) {
        setMdc(MDCKeys.errorDomain, value);
        return this;
    }

    public String errorDomain() {
        return getMdc(MDCKeys.errorDomain);
    }

    public MdcService errorSubDomain(final String value) {
        setMdc(MDCKeys.errorSubDomain, value);
        return this;
    }

    public String errorSubDomain() {
        return getMdc(MDCKeys.errorSubDomain);
    }

    public String exceptionName() {
        return getMdc(MDCKeys.exceptionName);
    }

    public MdcService flags(final String value) {
        setMdc(MDCKeys.flags, value);
        return this;
    }

    public String flags() {
        return getMdc(MDCKeys.flags);
    }

    public MdcService from(final LocalDateTime value) {
        setMdc(MDCKeys.from, value);
        return this;
    }

    public LocalDateTime from() {
        return getLocalDateTime(MDCKeys.from);
    }

    public MdcService fromTimestamp(final long value) {
        setMdc(MDCKeys.fromTimestamp, value);
        return this;
    }

    public long fromTimestamp() {
        return getLong(MDCKeys.fromTimestamp);
    }

    public MdcService functionalUid(final String value) {
        setMdc(MDCKeys.functionalUid, value);
        return this;
    }

    public String functionalUid() {
        return getMdc(MDCKeys.functionalUid);
    }

    public MdcService globalStatus(final String value) {
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

    public MdcService removeGlobalStatus() {
        remove(MDCKeys.globalStatus);
        return this;
    }

    public MdcService healthStatus(final String value) {
        setMdc(MDCKeys.healthStatus, value);
        return this;
    }

    public String healthStatus() {
        return getMdc(MDCKeys.healthStatus);
    }

    public MdcService hostname(final String value) {
        setMdc(MDCKeys.hostname, value);
        return this;
    }

    public String hostname() {
        return getMdc(MDCKeys.hostname);
    }

    public MdcService httpStatus(final int value) {
        setMdc(MDCKeys.httpStatus, value);
        return this;
    }

    public int httpStatus() {
        return getInt(MDCKeys.httpStatus);
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

    public MdcService ioinfoIoLog(final IoInfoDTO info) {
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

    private MdcService removeIoinfoIoLog() {
        remove(MDCKeys.url,
               MDCKeys.verb,
               MDCKeys.service,
               MDCKeys.appSubService,
               MDCKeys.status,
               MDCKeys.duration
        );
        return this;
    }

    public MdcService ioinfoPartner(final IoInfoDTO info) {
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

    public MdcService partnerRemove() {
        return removeIoinfoPartner();
    }

    public MdcService removeIoinfoPartner() {
        remove(MDCKeys.partnerUrl,
               MDCKeys.partnerVerb,
               MDCKeys.partner,
               MDCKeys.partnerService,
               MDCKeys.partnerSubService,
               MDCKeys.partnerRequestCharset,
               MDCKeys.partnerResponseStatus,
               MDCKeys.partnerResponseDuration,
               MDCKeys.partnerResponseMessage,
               MDCKeys.partnerResponseCharset
        );
        return this;
    }

    public MdcService language(final String value) {
        setMdc(MDCKeys.language, value);
        return this;
    }

    public String language() {
        return getMdc(MDCKeys.language);
    }

    public MdcService lifecycle(final String value) {
        setMdc(MDCKeys.lifecycle, value);
        return this;
    }

    public String lifecycle() {
        return getMdc(MDCKeys.lifecycle);
    }

    public MdcService lifecycleIn() {
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

    public MdcService lifecycleOut() {
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


    public MdcService lifecycleRemove() {
        remove(MDCKeys.lifecycle);
        return this;
    }

    public MdcService majorVersion(final String value) {
        setMdc(MDCKeys.majorVersion, value);
        return this;
    }

    public String majorVersion() {
        return getMdc(MDCKeys.majorVersion);
    }

    public MdcService messageId(final String value) {
        setMdc(MDCKeys.messageId, value);
        return this;
    }

    public String messageId() {
        return getMdc(MDCKeys.messageId);
    }

    public MdcService methodInCause(final String value) {
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
        final Map<String, String> result         = new LinkedHashMap<>();
        final Headers             currentHeaders = headers == null ? this.headers : headers;

        applyIfNotNull(deviceIdentifier(), value -> result.put(currentHeaders.getDeviceIdentifier(), value));
        applyIfNotNull(correlationId(), value -> result.put(currentHeaders.getCorrelationId(), value));
        applyIfNotNull(conversationId(), value -> result.put(currentHeaders.getConversationId(), value));
        applyIfNotNull(traceId(), value -> result.put(currentHeaders.getRequestId(), value));

        return result;
    }

    public MdcService orderId(final String value) {
        setMdc(MDCKeys.orderId, value);
        return this;
    }

    public String orderId() {
        return getMdc(MDCKeys.orderId);
    }

    public MdcService osVersion(final String value) {
        setMdc(MDCKeys.osVersion, value);
        return this;
    }

    public String osVersion() {
        return getMdc(MDCKeys.osVersion);
    }

    public MdcService parentSpanId(final String value) {
        setMdc(MDCKeys.parentSpanId, value);
        return this;
    }

    public String parentSpanId() {
        return getMdc(MDCKeys.parentSpanId);
    }

    public MdcService partner(final String value) {
        setMdc(MDCKeys.partner, value);
        return this;
    }

    public String partner() {
        return getMdc(MDCKeys.partner);
    }

    public MdcService partnerRequestCharset(final String value) {
        setMdc(MDCKeys.partnerRequestCharset, value);
        return this;
    }

    public MdcService partnerRequestCharset(final Charset value) {
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


    public MdcService partnerResponseCharset(final String value) {
        setMdc(MDCKeys.partnerRequestCharset, value);
        return this;
    }

    public MdcService partnerResponseCharset(final Charset value) {
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

    public MdcService partnerResponseDuration(final long value) {
        setMdc(MDCKeys.partnerResponseDuration, value);
        return this;
    }

    public long partnerResponseDuration() {
        return getLong(MDCKeys.partnerResponseDuration);
    }

    public MdcService partnerResponseMessage(final String value) {
        setMdc(MDCKeys.partnerResponseMessage, value);
        return this;
    }

    public String partnerResponseMessage() {
        return getMdc(MDCKeys.partnerResponseMessage);
    }

    public MdcService partnerResponseStatus(final int value) {
        setMdc(MDCKeys.partnerResponseStatus, value);
        return this;
    }

    public int partnerResponseStatus() {
        return getInt(MDCKeys.partnerResponseStatus);
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

    public MdcService partnerType(final String value) {
        setMdc(MDCKeys.partnerType, value);
        return this;
    }

    public String partnerType() {
        return getMdc(MDCKeys.partnerType);
    }

    public MdcService partnerUrl(final String value) {
        setMdc(MDCKeys.partnerUrl, value);
        return this;
    }

    public String partnerUrl() {
        return getMdc(MDCKeys.partnerUrl);
    }

    public MdcService partnerVerb(final String value) {
        setMdc(MDCKeys.partnerVerb, value);
        return this;
    }

    public String partnerVerb() {
        return getMdc(MDCKeys.partnerVerb);
    }

    public MdcService price(final double value) {
        setMdc(MDCKeys.price, value);
        return this;
    }

    public double price() {
        return getDouble(MDCKeys.price);
    }

    public MdcService principal(final String value) {
        setMdc(MDCKeys.principal, value);
        return this;
    }

    public String principal() {
        return getMdc(MDCKeys.principal);
    }

    public MdcService productId(final String value) {
        setMdc(MDCKeys.productId, value);
        return this;
    }

    public String productId() {
        return getMdc(MDCKeys.productId);
    }

    public MdcService processId(final String value) {
        setMdc(MDCKeys.processId, value);
        return this;
    }

    public String processId() {
        return getMdc(MDCKeys.processId);
    }

    public MdcService processName(final String value) {
        setMdc(MDCKeys.processName, value);
        return this;
    }

    public String processName() {
        return getMdc(MDCKeys.processName);
    }

    public MdcService processStatus(final String value) {
        setMdc(MDCKeys.processStatus, value);
        return this;
    }

    public String processStatus() {
        return getMdc(MDCKeys.processStatus);
    }

    public MdcService quantity(final double value) {
        setMdc(MDCKeys.quantity, value);
        return this;
    }

    public double quantity() {
        return getDouble(MDCKeys.quantity);
    }

    public MdcService remoteAddress(final String value) {
        setMdc(MDCKeys.remoteAddress, value);
        return this;
    }

    public String remoteAddress() {
        return getMdc(MDCKeys.remoteAddress);
    }

    public MdcService requestHeaders(final String value) {
        setMdc(MDCKeys.requestHeaders, value);
        return this;
    }

    public String requestHeaders() {
        return getMdc(MDCKeys.requestHeaders);
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

    public MdcService reservationNumber(final String value) {
        setMdc(MDCKeys.reservationNumber, value);
        return this;
    }

    public String reservationNumber() {
        return getMdc(MDCKeys.reservationNumber);
    }

    public MdcService responseHeaders(final String value) {
        setMdc(MDCKeys.responseHeaders, value);
        return this;
    }

    public String responseHeaders() {
        return getMdc(MDCKeys.responseHeaders);
    }

    public MdcService service(final String value) {
        setMdc(MDCKeys.service, value);
        return this;
    }

    public String service() {
        return getMdc(MDCKeys.service);
    }

    public MdcService sessionId(final String value) {
        setMdc(MDCKeys.sessionId, value);
        return this;
    }

    public String sessionId() {
        return getMdc(MDCKeys.sessionId);
    }

    public MdcService size(final double value) {
        setMdc(MDCKeys.size, value);
        return this;
    }

    public double size() {
        return getDouble(MDCKeys.size);
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

    public MdcService until(final LocalDateTime value) {
        setMdc(MDCKeys.until, value);
        return this;
    }

    public LocalDateTime until() {
        return getLocalDateTime(MDCKeys.until);
    }

    public MdcService untilTimestamp(final long value) {
        setMdc(MDCKeys.untilTimestamp, value);
        return this;
    }

    public long untilTimestamp() {
        return getLong(MDCKeys.untilTimestamp);
    }

    public MdcService uri(final String value) {
        setMdc(MDCKeys.uri, value);
        return this;
    }

    public String uri() {
        return getMdc(MDCKeys.uri);
    }

    public MdcService url(final String value) {
        setMdc(MDCKeys.url, value);
        return this;
    }

    public String url() {
        return getMdc(MDCKeys.url);
    }

    public MdcService urlPattern(final String value) {
        setMdc(MDCKeys.urlPattern, value);
        return this;
    }

    public String urlPattern() {
        return getMdc(MDCKeys.urlPattern);
    }


    public MdcService userAgent(final String value) {
        setMdc(MDCKeys.userAgent, value);
        return this;
    }

    public String userAgent() {
        return getMdc(MDCKeys.userAgent);
    }

    public MdcService userId(final String value) {
        setMdc(MDCKeys.userId, value);
        return this;
    }

    public String userId() {
        return getMdc(MDCKeys.userId);
    }

    public MdcService verb(final String value) {
        setMdc(MDCKeys.verb, value);
        return this;
    }

    public MdcService status(final int status) {
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

    public MdcService version(final String value) {
        setMdc(MDCKeys.version, value);
        return this;
    }

    public String version() {
        return getMdc(MDCKeys.version);
    }

    public MdcService warning(final String value) {
        setMdc(MDCKeys.warning, value);
        return this;
    }

    public String warning() {
        return getMdc(MDCKeys.warning);
    }


}

