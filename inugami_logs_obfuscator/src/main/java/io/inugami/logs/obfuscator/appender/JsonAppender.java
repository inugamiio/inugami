package io.inugami.logs.obfuscator.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import io.inugami.logs.obfuscator.appender.writer.AppenderWriterStrategy;
import io.inugami.logs.obfuscator.appender.writer.FileWriter;
import io.inugami.logs.obfuscator.appender.writer.LogstashWriter;
import io.inugami.logs.obfuscator.encoder.ObfuscatorEncoder;

import java.io.IOException;
import java.util.List;

@SuppressWarnings({"java:S108", "java:S1117", "java:S108", "java:S1181"})
public class JsonAppender extends ConsoleAppender<ILoggingEvent> {
    private Configuration configuration;

    private final List<AppenderWriterStrategy> writers = List.of(
            new FileWriter(),
            new LogstashWriter(),
            this::superWriteOut
    );


    private AppenderWriterStrategy writer = null;

    @Override
    public void start() {
        if (this.encoder == null) {
            this.encoder = new ObfuscatorEncoder(configuration);
        }

        writer = resolveWriter(configuration);
        if (writer != null) {
            writer.start(encoder);
            super.start();
        }

    }

    @Override
    public void stop() {
        if (writer != null) {
            writer.stop();
        }
    }


    @Override
    protected void append(final ILoggingEvent iLoggingEvent) {

        if (this.isStarted()) {
            try {
                writer.write(iLoggingEvent);
            } catch (final Throwable e) {
            }
        }
    }


    private AppenderWriterStrategy resolveWriter(final Configuration configuration) {
        AppenderWriterStrategy result = null;
        for (final AppenderWriterStrategy writer : writers) {
            if (writer.accept(configuration)) {
                result = writer;
                break;
            }
        }
        return result;
    }

    private void superWriteOut(final ILoggingEvent event) {
        try {
            this.writeOut(event);
        } catch (final IOException e) {
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(final Configuration configuration) {
        this.configuration = configuration;
    }
}
