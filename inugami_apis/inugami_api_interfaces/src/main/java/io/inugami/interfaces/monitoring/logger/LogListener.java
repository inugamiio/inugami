package io.inugami.interfaces.monitoring.logger;

public interface LogListener {
    boolean accept(final String loggerName);

    void append(final BasicLogEvent event);
}
