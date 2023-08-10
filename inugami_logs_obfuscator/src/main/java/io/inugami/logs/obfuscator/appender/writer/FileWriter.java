package io.inugami.logs.obfuscator.appender.writer;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.Encoder;
import io.inugami.api.exceptions.FatalException;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.processors.DefaultConfigHandler;
import io.inugami.logs.obfuscator.appender.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"java:S5852", "java:S899", "java:S6395", "java:S108"})
public class FileWriter implements AppenderWriterStrategy {

    private              Configuration             configuration    = null;
    private              Encoder<ILoggingEvent>    encoder;
    private              java.io.FileWriter        writer;
    private static final AtomicReference<String>   FILE_PATH        = new AtomicReference<>();
    private static final AtomicReference<Calendar> LAST_PATH_CHANGE = new AtomicReference<>();

    private static final long MINUTE = 60000;

    private static final String                        DATE_REGEX   = "(?:\\%d\\{)(yyyy[^}]+)(\\})";
    private static final Pattern                       DATE_PATTERN = Pattern.compile(".*" + DATE_REGEX + ".*");
    private static final ConfigHandler<String, String> CONFIG       = refreshConfig();


    @Override
    public boolean accept(final Configuration configuration) {
        if (configuration.getFile() != null) {
            this.configuration = configuration;
            refreshConfig();
            return true;
        }
        return false;
    }

    private static ConfigHandler<String, String> refreshConfig() {
        return new DefaultConfigHandler();
    }

    @Override
    public void start(final Encoder<ILoggingEvent> encoder) {
        this.encoder = encoder;
        processCreateWriter();
    }

    private synchronized void processCreateWriter() {
        if (writer == null) {
            createWriter();
        } else if (filePathHasChange()) {
            closeWriter();
            createWriter();
        }
    }


    private synchronized void createWriter() {
        final String filePath = resolveFilePath();
        FILE_PATH.set(filePath);
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        LAST_PATH_CHANGE.set(calendar);
        try {
            final File file = createFileIfNotExists(filePath);
            writer = new java.io.FileWriter(file);
        } catch (final IOException e) {
            throw new FatalException(e);
        }
    }

    private boolean filePathHasChange() {
        final Calendar lastChange = LAST_PATH_CHANGE.get();
        if (lastChange == null) {
            return false;
        }

        final Calendar now          = Calendar.getInstance();
        final long     diff         = now.getTimeInMillis() - lastChange.getTimeInMillis();
        final boolean  minuteChange = diff - MINUTE >= 0;

        if (!minuteChange) {
            return false;
        }

        return !FILE_PATH.get().equals(resolveFilePath());
    }

    protected String resolveFilePath() {
        String filePath = configuration.getFile();

        final Matcher matcher = DATE_PATTERN.matcher(filePath);
        if (matcher.matches()) {
            final String date        = matcher.group(1);
            final String currentDate = new SimpleDateFormat(date).format(new Date());
            filePath = filePath.replaceAll(DATE_REGEX, currentDate);
        }

        return CONFIG.applyProperties(filePath);
    }

    protected File createFileIfNotExists(final String filePath) {
        File file = null;
        try {
            file = new File(filePath).getCanonicalFile();
            if (!file.exists()) {
                final File parent = file.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                file.createNewFile();
            }
            return file;
        } catch (final IOException e) {
            return null;
        }
    }

    @Override
    public void stop() {
        closeWriter();
    }


    private void closeWriter() {
        try {
            writer.flush();
        } catch (final IOException e) {
        } finally {
            try {
                writer.close();
            } catch (final IOException e) {
            }
        }
    }

    @Override
    public void write(final ILoggingEvent iLoggingEvent) {
        processCreateWriter();
        final byte[] encoded = encoder.encode(iLoggingEvent);

        try {
            writer.write(new String(encoded, StandardCharsets.UTF_8));
            writer.flush();
        } catch (final IOException e) {
        }
    }
}
