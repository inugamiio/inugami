package io.inugami.api.exceptions;

import java.util.List;

/**
 * The <strong>WarningTracker</strong> interface allows to track warning information.
 * It's SPI interface. To add new implementation you should add your implementation
 * in the file <strong>/META-INF/services/io.inugami.api.exceptions.WarningTracker</strong>
 */
@FunctionalInterface
public interface WarningTracker {
    void track(final List<Warning> warning);
}
