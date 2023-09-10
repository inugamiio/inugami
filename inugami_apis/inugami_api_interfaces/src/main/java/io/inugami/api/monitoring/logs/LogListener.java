package io.inugami.api.monitoring.logs;

public interface LogListener {
    boolean accept(final String loggerName);

    void append(final BasicLogEvent event);
}
