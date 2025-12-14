package io.inugami.logs.obfuscator.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import io.inugami.logs.obfuscator.appender.writer.AppenderWriterStrategy;
import io.inugami.logs.obfuscator.appender.writer.ElasticSearchWriter;
import io.inugami.logs.obfuscator.appender.writer.FileWriter;
import io.inugami.logs.obfuscator.appender.writer.LogstashWriter;
import io.inugami.logs.obfuscator.encoder.ObfuscatorEncoder;

import java.util.List;

import static io.inugami.framework.api.tools.RunSafeUtils.runSafeVoid;
import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;


@SuppressWarnings({"java:S108", "java:S1117", "java:S108", "java:S1181"})
public class JsonAppender extends ConsoleAppender<ILoggingEvent> {
    private       AppenderConfiguration        configuration;
    private final List<AppenderWriterStrategy> writers = List.of(
            new FileWriter(),
            new LogstashWriter(),
            new ElasticSearchWriter(),
            this::superWriteOut);
    private       AppenderWriterStrategy       writer  = null;

    @Override
    public void start() {
        configuration.init();
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
        applyIfNotNull(writer, AppenderWriterStrategy::stop);
    }


    @Override
    protected void append(final ILoggingEvent iLoggingEvent) {
        if (this.isStarted()) {
            runSafeVoid(() -> writer.write(iLoggingEvent));
        }
    }


    private AppenderWriterStrategy resolveWriter(final AppenderConfiguration configuration) {
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
        runSafeVoid(() -> writeOut(event));
    }

    public AppenderConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(final AppenderConfiguration configuration) {
        this.configuration = configuration;
    }
}
