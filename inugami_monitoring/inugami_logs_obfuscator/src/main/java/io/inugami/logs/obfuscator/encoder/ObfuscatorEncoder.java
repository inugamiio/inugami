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
package io.inugami.logs.obfuscator.encoder;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;
import ch.qos.logback.core.spi.ContextAware;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.inugami.framework.api.listeners.DefaultApplicationLifecycleSPI;
import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.interfaces.listeners.ApplicationLifecycleSPI;
import io.inugami.framework.interfaces.models.JsonBuilder;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.monitoring.logger.mapper.LoggerMdcMappingSPI;
import io.inugami.framework.interfaces.monitoring.logger.mapper.MdcDynamicFieldSPI;
import io.inugami.framework.interfaces.spi.SpiLoader;
import io.inugami.logs.obfuscator.api.LogEventDto;
import io.inugami.logs.obfuscator.api.ObfuscatorSpi;
import io.inugami.logs.obfuscator.appender.AppenderConfiguration;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

@SuppressWarnings({"java:S1181", "java:S108", "java:S1185", "java:S1185", "java:S1874", "java:S1125"})
public class ObfuscatorEncoder extends PatternLayoutEncoderBase<ILoggingEvent> implements ContextAware, ApplicationLifecycleSPI {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String MESSAGE      = "message";
    private static final String LINE         = "\n";
    private static final Clock  CLOCK        = Clock.systemUTC();
    public static final  String NO_FORMATTER = "%m";
    public static final  String DATA         = "data";

    private              List<ObfuscatorSpi>       obfuscators;
    private              List<LoggerMdcMappingSPI> mdcMappers;
    private              List<MdcDynamicFieldSPI>  mdcDynamicFields;
    public static final  String                    EMPTY_STR      = "";
    private static final byte[]                    EMPTY          = EMPTY_STR.getBytes(StandardCharsets.UTF_8);
    private static final String                    THREAD_NAME    = "threadName";
    private static final String                    LOGGER_NAME    = "loggerName";
    private static final String                    LEVEL          = "level";
    private static final String                    LEVEL_PRIORITY = "levelPriority";
    private static final String                    TIMESTAMP      = "timestamp";
    private static final String                    DATE           = "date";
    private static final String                    STACKTRACE     = "stacktrace";
    private              AppenderConfiguration     configuration;

    private Function<ILoggingEvent, String> messageEncoder = null;
    private PatternLayout                   patternLayout  = null;

    private Map<String, Serializable> additionalFieldsData = null;
    private String                    pattern;
    private boolean                   forceNewLine         = true;

    public ObfuscatorEncoder() {
        onContextRefreshed(null);
        DefaultApplicationLifecycleSPI.register(this);
    }


    public ObfuscatorEncoder(final AppenderConfiguration configuration) {
        this.configuration = configuration;
        onContextRefreshed(null);
        forceNewLine = this.configuration.getForceNewLine() == null
                ? true
                : Boolean.parseBoolean(this.configuration.getForceNewLine());

    }

    @Override
    public void onContextRefreshed(final Object event) {
        obfuscators = SpiLoader.getInstance().loadSpiServicesByPriority(ObfuscatorSpi.class);
        mdcMappers = SpiLoader.getInstance().loadSpiServicesByPriority(LoggerMdcMappingSPI.class);
        mdcDynamicFields = SpiLoader.getInstance().loadSpiServicesByPriority(MdcDynamicFieldSPI.class);
    }

    @Override
    public void setContext(final Context context) {
        super.setContext(context);
    }
// =========================================================================
    // API
    // =========================================================================

    @Override
    public byte[] encode(final ILoggingEvent event) {
        final LogEventDto currentEvent = buildEvent(event);
        String            message      = encodeMessage(currentEvent);
        if (message == null) {
            message = EMPTY_STR;
        }

        byte[] result = null;

        if (this.configuration != null && this.configuration.isEncodeAsJson()) {
            result = renderAsJson(message, event);
        } else {
            result = renderPlainText(message);
        }

        return result == null ? EMPTY : result;
    }

    private byte[] renderPlainText(final String message) {
        String currentMessage = message == null ? EMPTY_STR : message;
        if (forceNewLine && !currentMessage.endsWith(LINE)) {
            currentMessage = currentMessage + LINE;
        }
        return currentMessage.getBytes(StandardCharsets.UTF_8);
    }


    // =========================================================================
    // OVERRIDES
    // =========================================================================

    private LogEventDto buildEvent(final ILoggingEvent event) {
        final LogEventDto.LogEventDtoBuilder builder = LogEventDto.builder();

        if (messageEncoder == null) {
            initMessageEncoder();
        }
        String message = messageEncoder.apply(event);
        if (message != null) {
            message = message.trim();
        }
        builder.timestamp(event.getTimeStamp())
               .threadName(event.getThreadName())
               .loggerName(event.getLoggerName())
               .message(message)
               .stacktrace(event.getCallerData())
               .mdc(cloneMap(event.getMDCPropertyMap(), event.getMdc()));


        return builder.build();
    }


    private Map<String, Serializable> cloneMap(final Map<String, String> mdcPropertyMap,
                                               final Map<String, String> mdc) {
        final Map<String, Serializable> result = new LinkedHashMap<>();

        if (mdc != null) {
            for (final Map.Entry<String, String> entry : mdc.entrySet()) {
                if (entry.getValue() != null) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }

        if (mdcPropertyMap != null) {
            for (final Map.Entry<String, String> entry : mdcPropertyMap.entrySet()) {
                if (entry.getValue() != null) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return result;
    }

    private String encodeMessage(final LogEventDto event) {
        final LogEventDto.LogEventDtoBuilder builder = event.toBuilder();

        String result = event.getMessage();
        for (final ObfuscatorSpi obfuscator : obfuscators) {
            final LogEventDto currentEvent = builder.build();
            if (obfuscator.isEnabled() && obfuscator.accept(currentEvent)) {
                result = obfuscator.obfuscate(currentEvent);

                if (result != null) {
                    builder.message(result);
                    if (obfuscator.shouldStop(builder.build())) {
                        break;
                    }
                }
            }
        }

        return result;
    }


    // =========================================================================
    // RENDER AS JSON
    // =========================================================================
    private byte[] renderAsJson(final String message, final ILoggingEvent event) {
        String json = EMPTY_STR + renderJson(message, event) + ObfuscatorEncoder.LINE;
        if (forceNewLine && !json.endsWith(ObfuscatorEncoder.LINE)) {
            json = json + ObfuscatorEncoder.LINE;
        }

        return json.getBytes(StandardCharsets.UTF_8);
    }

    private String renderJson(final String message, final ILoggingEvent event) {
        final Map<String, Object> result = buildBaseData(event);

        result.put(THREAD_NAME, event.getThreadName());
        result.put(LOGGER_NAME, event.getLoggerName());
        result.put(LEVEL, event.getLevel().toString());
        result.put(LEVEL_PRIORITY, event.getLevel().toInt());
        result.put(TIMESTAMP, event.getTimeStamp());
        result.put(DATE, DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now(CLOCK)));

        if (configuration.isMessageAsJson()) {
            final Object data = convertToData(message);
            if (data != null) {
                result.put(DATA, data);
            }
        } else {
            result.put(MESSAGE, message);
        }


        if (event.getLevel().toInt() >= Level.ERROR.toInt()) {
            result.put(STACKTRACE, convertStackTrace(event.getCallerData()));
        }


        try {
            return JsonMarshaller.getInstance().getDefaultObjectMapper().writeValueAsString(result);
        } catch (final JsonProcessingException e) {
            return EMPTY_STR;
        }
    }

    private Object convertToData(final String message) {
        try {
            final Object result = JsonMarshaller.getInstance()
                                                .getDefaultObjectMapper()
                                                .readValue(message, Object.class);
            return result;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private Serializable convertStackTrace(final StackTraceElement[] callerData) {
        final List<String> result = new ArrayList<>();
        if (callerData != null) {

            for (final StackTraceElement stack : callerData) {
                final JsonBuilder buffer = new JsonBuilder();

                result.add(buffer.write(stack.getClassName())
                                 .dot()
                                 .write(stack.getMethodName())
                                 .openTuple()
                                 .write(stack.getFileName())
                                 .dot()
                                 .write(stack.getLineNumber())
                                 .closeTuple()
                                 .toString());
            }
        }
        return String.join(LINE, result);
    }

    private Map<String, Object> buildBaseData(final ILoggingEvent event) {
        final Map<String, String> mdc    = new LinkedHashMap<>();
        final Map<String, Object> result = new LinkedHashMap<>();


        if (event.getMDCPropertyMap() != null) {
            mdc.putAll(event.getMDCPropertyMap());
        }

        if (additionalFieldsData != null) {
            result.putAll(additionalFieldsData);
        } else if (this.configuration.getAdditionalFields() != null) {
            initAdditonnalFieldDate();
            result.putAll(additionalFieldsData);
        }

        result.putAll(extractMdcDynamicFieldsSpiData());
        result.putAll(extractMdcData(mdc));

        return result;
    }


    private Map<String, Serializable> extractMdcDynamicFieldsSpiData() {
        final Map<String, Serializable> result = new LinkedHashMap<>();
        if (mdcDynamicFields == null) {
            return result;
        }
        for (final MdcDynamicFieldSPI mdcDynamicField : mdcDynamicFields) {
            try {
                final Map<String, Serializable> data = mdcDynamicField.generate();
                applyIfNotNull(data, result::putAll);
            } catch (final Throwable e) {
            }
        }
        return result;
    }

    private Map<String, Serializable> extractMdcData(final Map<String, String> mdc) {
        final Map<String, Serializable> result = new LinkedHashMap<>();
        if (mdc == null) {
            return result;
        }

        for (final Map.Entry<String, String> entry : mdc.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }
            final Serializable mdcValue = convertMdcValue(entry.getKey(), entry.getValue());
            applyIfNotNull(mdcValue, value -> result.put(entry.getKey(), value));
        }

        return result;
    }


    private Serializable convertMdcValue(final String key, final String value) {
        LoggerMdcMappingSPI strategy = null;

        for (final LoggerMdcMappingSPI registredStrategy : mdcMappers) {
            if (registredStrategy.accept(key)) {
                strategy = registredStrategy;
                break;
            }
        }

        return strategy == null ? null : strategy.convert(value);
    }

    private synchronized void initAdditonnalFieldDate() {
        final ObjectMapper marshaller = JsonMarshaller.getInstance().getDefaultObjectMapper();

        Map<String, Serializable> result = null;
        try {
            result = marshaller.readValue(this.configuration.getAdditionalFields(), new TypeReference<Map<String, Serializable>>() {
            });
        } catch (final Throwable e) {
            Loggers.DEBUG.error(e.getMessage(), e);
        }

        this.additionalFieldsData = result == null ? new LinkedHashMap<>() : result;
    }

    private void initMessageEncoder() {
        if (layout != null) {
            messageEncoder = (event) -> getLayout().doLayout(event);
        } else if (pattern != null && !(NO_FORMATTER.equals(pattern))) {
            patternLayout = new PatternLayout();
            patternLayout.setPattern(pattern);
            patternLayout.setContext(this.getContext());
            patternLayout.start();
            messageEncoder = (event) -> patternLayout.doLayout(event);
        } else {
            messageEncoder = (event) -> event.getFormattedMessage();
        }
    }
    // =========================================================================
    // GETTERS / SETTERS
    // =========================================================================

    public AppenderConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(final AppenderConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public void setPattern(final String pattern) {
        this.pattern = pattern;
    }

    @Override
    public Layout<ILoggingEvent> getLayout() {
        return layout;
    }

    @Override
    public void setLayout(final Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }
}
