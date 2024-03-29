package io.inugami.api.monitoring.logs;

import java.util.function.Consumer;
import java.util.regex.Pattern;

public class DefaultLogListener implements LogListener {

    private final Class<?>                logClass;
    private final Pattern                 regex;
    private final Consumer<BasicLogEvent> consumer;

    public DefaultLogListener(final Class<?> logClass,
                              final Consumer<BasicLogEvent> consumer) {
        this.logClass = logClass;
        this.consumer = consumer;
        this.regex = null;
    }

    public DefaultLogListener(final String pattern,
                              final Consumer<BasicLogEvent> consumer) {
        this.logClass = null;
        this.consumer = consumer;
        this.regex = Pattern.compile(pattern);
    }


    @Override
    public boolean accept(final String loggerName) {
        if (consumer == null) {
            return false;
        }

        return this.logClass == null ? matchPattern(loggerName) : matchClass(loggerName);
    }

    private boolean matchPattern(final String loggerName) {
        return regex.matcher(loggerName).matches();
    }

    private boolean matchClass(final String loggerName) {
        return this.logClass.getName().equals(loggerName);
    }


    @Override
    public void append(final BasicLogEvent event) {
        this.consumer.accept(event);
    }
}
