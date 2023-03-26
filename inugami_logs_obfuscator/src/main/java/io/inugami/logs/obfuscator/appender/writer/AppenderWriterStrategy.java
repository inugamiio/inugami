package io.inugami.logs.obfuscator.appender.writer;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.Encoder;
import io.inugami.logs.obfuscator.appender.Configuration;

@FunctionalInterface
public interface AppenderWriterStrategy {


    void write(final ILoggingEvent iLoggingEvent);

    default boolean accept(final Configuration configuration) {
        return true;
    }
    default void start(final Encoder<ILoggingEvent> encoder) {
    }

    default void stop() {
    }
}
