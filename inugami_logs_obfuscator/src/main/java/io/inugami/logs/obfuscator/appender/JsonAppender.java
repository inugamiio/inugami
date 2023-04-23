package io.inugami.logs.obfuscator.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import io.inugami.logs.obfuscator.appender.writer.AppenderWriterStrategy;
import io.inugami.logs.obfuscator.appender.writer.FileWriter;
import io.inugami.logs.obfuscator.appender.writer.LogstashWriter;
import io.inugami.logs.obfuscator.encoder.ObfuscatorEncoder;

import java.io.IOException;
import java.util.List;

public class JsonAppender extends ConsoleAppender<ILoggingEvent> {
    private Configuration configuration;

    private final List<AppenderWriterStrategy> WRITERS = List.of(
            new FileWriter(),
            new LogstashWriter(),
            (event) -> this.superWriteOut(event)
    );


    private AppenderWriterStrategy writer = null;

    @Override
    public void start() {
        if (this.encoder == null) {
            this.encoder = new ObfuscatorEncoder(configuration);
        }

        writer = resolveWriter(configuration);
        writer.start(encoder);
        super.start();
    }

    @Override
    public void stop() {
        writer.stop();
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
        for (final AppenderWriterStrategy writer : WRITERS) {
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
