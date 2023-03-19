package io.inugami.logs.obfuscator.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import io.inugami.logs.obfuscator.encoder.ObfuscatorEncoder;

public class JsonAppender extends ConsoleAppender<ILoggingEvent> {
    private Configuration configuration;

    @Override
    public void start() {
        if (this.encoder == null) {
            this.encoder = new ObfuscatorEncoder(configuration);
        }
        super.start();
    }

    @Override
    protected void append(final ILoggingEvent iLoggingEvent) {

        if (this.isStarted()) {
            try {
                writeOut(iLoggingEvent);
            } catch (Throwable e) {
            }
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(final Configuration configuration) {
        this.configuration = configuration;
    }
}
