package io.inugami.commons.test.logs;

public interface LogListener {
    boolean accept(final String loggerName);
    void append(final BasicLogEvent event);
}
