package io.inugami.logs.obfuscator.appender.writer;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.Encoder;
import io.inugami.api.exceptions.FatalException;
import io.inugami.logs.obfuscator.appender.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class FileWriter implements AppenderWriterStrategy {

    private static final String LINE = "\n";
    private Configuration configuration = null;
    private Encoder<ILoggingEvent> encoder;
    private java.io.FileWriter     writer;

    @Override
    public boolean accept(final Configuration configuration) {
        if (configuration.getFile() != null) {
            this.configuration = configuration;
            return true;
        }
        return false;
    }

    @Override
    public void start(final Encoder<ILoggingEvent> encoder) {
        this.encoder = encoder;
        String filePath = configuration.getFile();

        try {
            File file = new File(filePath).getCanonicalFile();
            writer = new java.io.FileWriter(file);
        } catch (IOException e) {
            throw new FatalException(e);
        }
    }

    @Override
    public void stop() {
        try {
            writer.close();
        } catch (IOException e) {
            throw new FatalException(e);
        }
    }

    @Override
    public void write(final ILoggingEvent iLoggingEvent) {
        final byte[] encoded = encoder.encode(iLoggingEvent);

        try {
            writer.write(new String(encoded, StandardCharsets.UTF_8));
            writer.flush();
        } catch (IOException e) {
        }
    }
}
