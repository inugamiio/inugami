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
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;
import ch.qos.logback.core.spi.ContextAware;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.inugami.api.listeners.ApplicationLifecycleSPI;
import io.inugami.api.listeners.DefaultApplicationLifecycleSPI;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.loggers.mdc.mapper.LoggerMdcMappingSPI;
import io.inugami.api.loggers.mdc.mapper.MdcDynamicFieldSPI;
import io.inugami.api.models.JsonBuilder;
import io.inugami.api.spi.SpiLoader;
import io.inugami.commons.marshaling.JsonMarshaller;
import io.inugami.logs.obfuscator.api.LogEventDto;
import io.inugami.logs.obfuscator.api.ObfuscatorSpi;
import io.inugami.logs.obfuscator.appender.Configuration;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ObfuscatorEncoder extends PatternLayoutEncoderBase<ILoggingEvent> implements ContextAware, ApplicationLifecycleSPI {

    private static final String                    MESSAGE        = "message";
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private              List<ObfuscatorSpi>       obfuscators;
    private              List<LoggerMdcMappingSPI> mdcMappers;
    private              List<MdcDynamicFieldSPI>  mdcDynamicFields;
    private final static byte[]                    EMPTY          = "".getBytes(StandardCharsets.UTF_8);
    private static final String                    THREAD_NAME    = "threadName";
    private static final String                    LOGGER_NAME    = "loggerName";
    private static final String                    LEVEL          = "level";
    private static final String                    LEVEL_PRIORITY = "levelPriority";
    private static final String                    TIMESTAMP      = "timestamp";
    private static final String                    DATE           = "date";
    private static final String                    STACKTRACE     = "stacktrace";
    private              Configuration             configuration;


    private Map<String, Serializable> additionalFieldsData = null;

    public ObfuscatorEncoder() {
        onContextRefreshed(null);
        DefaultApplicationLifecycleSPI.register(this);
    }


    public ObfuscatorEncoder(Configuration configuration) {
        this.configuration = configuration;
        onContextRefreshed(null);
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
        LogEventDto currentEvent = buildEvent(event);
        String      message      = encodeMessage(currentEvent);

        byte[] result = null;

        if (this.configuration != null && this.configuration.isEncodeAsJson()) {
            result = renderAsJson(message, event);
        } else {
            result = message.getBytes(StandardCharsets.UTF_8);
        }

        return result == null ? EMPTY : result;
    }


    // =========================================================================
    // OVERRIDES
    // =========================================================================

    private LogEventDto buildEvent(final ILoggingEvent event) {
        final LogEventDto.LogEventDtoBuilder builder = LogEventDto.builder();

        builder.timestamp(event.getTimeStamp())
               .threadName(event.getThreadName())
               .loggerName(event.getLoggerName())
               .message(event.getFormattedMessage())
               .stacktrace(event.getCallerData())
               .mdc(cloneMap(event.getMDCPropertyMap(), event.getMdc()));


        return builder.build();
    }

    private Map<String, Serializable> cloneMap(final Map<String, String> mdcPropertyMap,
                                               final Map<String, String> mdc) {
        final Map<String, Serializable> result = new LinkedHashMap<>();

        if (mdc != null) {
            for (Map.Entry<String, String> entry : mdc.entrySet()) {
                if (entry.getValue() != null) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }

        if (mdcPropertyMap != null) {
            for (Map.Entry<String, String> entry : mdcPropertyMap.entrySet()) {
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
        for (ObfuscatorSpi obfuscator : obfuscators) {
            LogEventDto currentEvent = builder.build();
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
        String json = renderJson(message, event);
        return json == null ? EMPTY : json.getBytes(StandardCharsets.UTF_8);
    }

    private String renderJson(final String message, final ILoggingEvent event) {
        Map<String, Serializable> result = buildBaseData(event);

        result.put(THREAD_NAME, event.getThreadName());
        result.put(LOGGER_NAME, event.getLoggerName());
        result.put(LEVEL, event.getLevel().toString());
        result.put(LEVEL_PRIORITY, event.getLevel().toInt());
        result.put(TIMESTAMP, event.getTimeStamp());
        result.put(DATE, DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now()));
        result.put(MESSAGE, message);

        if (event.getLevel().toInt() >= Level.ERROR.toInt()) {
            result.put(STACKTRACE, convertStackTrace(event.getCallerData()));
        }


        try {
            return JsonMarshaller.getInstance().getDefaultObjectMapper().writeValueAsString(result);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private Serializable convertStackTrace(final StackTraceElement[] callerData) {
        List<String> result = new ArrayList<>();
        if (callerData != null) {

            for (StackTraceElement stack : callerData) {
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
        return String.join("\n", result);
    }

    private Map<String, Serializable> buildBaseData(final ILoggingEvent event) {
        final Map<String, String>       mdc    = new LinkedHashMap<>();
        final Map<String, Serializable> result = new LinkedHashMap<>();


        if (event.getMDCPropertyMap() != null) {
            mdc.putAll(event.getMDCPropertyMap());
        }

        if (additionalFieldsData != null) {
            result.putAll(additionalFieldsData);
        } else if (additionalFieldsData == null && this.configuration.getAdditionalFields() != null) {
            initAdditonnalFieldDate();
            result.putAll(additionalFieldsData);
        }

        if (mdcDynamicFields != null) {
            for (MdcDynamicFieldSPI mdcDynamicField : mdcDynamicFields) {
                try {
                    final Map<String, Serializable> data = mdcDynamicField.generate();
                    if (data != null) {
                        result.putAll(data);
                    }
                } catch (Throwable e) {
                }
            }
        }


        if (mdc != null) {
            for (Map.Entry<String, String> entry : mdc.entrySet()) {
                Serializable mdcValue = convertMdcValue(entry.getKey(), entry.getValue());
                if (mdcValue != null) {
                    result.put(entry.getKey(), mdcValue);
                }
            }
        }


        return result;
    }

    private Serializable convertMdcValue(final String key, final String value) {
        LoggerMdcMappingSPI strategy = null;

        for (LoggerMdcMappingSPI registredStrategy : mdcMappers) {
            if (registredStrategy.accept(key)) {
                strategy = registredStrategy;
                break;
            }
        }

        return strategy.convert(value);
    }

    private synchronized void initAdditonnalFieldDate() {
        final ObjectMapper marshaller = JsonMarshaller.getInstance().getDefaultObjectMapper();

        Map<String, Serializable> result = null;
        try {
            result = marshaller.readValue(this.configuration.getAdditionalFields(), new TypeReference<Map<String, Serializable>>() {
            });
        } catch (Throwable e) {
            Loggers.DEBUG.error(e.getMessage(), e);
        }

        this.additionalFieldsData = result == null ? new LinkedHashMap<>() : result;
    }

    // =========================================================================
    // GETTERS / SETTERS
    // =========================================================================

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(final Configuration configuration) {
        this.configuration = configuration;
    }
}
