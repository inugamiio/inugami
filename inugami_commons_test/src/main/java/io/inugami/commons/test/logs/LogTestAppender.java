package io.inugami.commons.test.logs;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.Encoder;
import io.inugami.api.monitoring.logs.BasicLogEvent;
import io.inugami.api.monitoring.logs.LogListener;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"java:S1186"})
public class LogTestAppender extends OutputStreamAppender<ILoggingEvent> {

    public static final Map<Integer, LogListener> LISTENERS = new ConcurrentHashMap<>();

    @Override
    public void start() {
    }


    public static synchronized void register(final LogListener listener) {
        if (listener != null) {
            LISTENERS.put(listener.hashCode(), listener);
        }
    }

    public static synchronized void removeListener(final LogListener listener) {
        if (listener != null && LISTENERS.containsKey(listener.hashCode())) {
            LISTENERS.remove(listener.hashCode());
        }
    }

    @Override
    public void doAppend(final ILoggingEvent eventObject) {
        append(eventObject);
    }

    @Override
    protected void append(final ILoggingEvent iLoggingEvent) {
        final BasicLogEvent event = buildEvent(iLoggingEvent);

        for (final Map.Entry<Integer, LogListener> listener : LISTENERS.entrySet()) {
            if (listener.getValue().accept(event.getLoggerName())) {
                listener.getValue().append(event);
            }
        }
    }

    private BasicLogEvent buildEvent(final ILoggingEvent event) {

        String                       message        = null;
        final Encoder<ILoggingEvent> currentEncoder = getEncoder();
        if (currentEncoder == null) {
            message = event.getFormattedMessage();
        } else {
            final byte[] rawMessage = currentEncoder.encode(event);
            message = new String(rawMessage, StandardCharsets.UTF_8);
        }
        return BasicLogEvent.builder()
                            .loggerName(event.getLoggerName())
                            .mdc(cloneMap(event.getMDCPropertyMap(), event.getMdc()))
                            .message(message)
                            .level(event.getLevel().toString())
                            .build();
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
}
