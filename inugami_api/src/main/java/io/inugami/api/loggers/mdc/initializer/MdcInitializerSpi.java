package io.inugami.api.loggers.mdc.initializer;

import io.inugami.api.monitoring.MdcService;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

/**
 * The <strong>MdcInitializerSpi</strong> is designed to initialize all MDC field in a single log line.
 * This approach can initialize automatic index
 */
@FunctionalInterface
public interface MdcInitializerSpi {
    String  DEFAULT_STRING  = "xxxx";
    long    DEFAULT_LONG    = 0L;
    double  DEFAULT_DOUBLE  = 0.0;
    boolean DEFAULT_BOOLEAN = false;

    Map<String, Serializable> getDefaultValue();

    default Map.Entry<String, Serializable> buildEntry(final MdcService.MDCKeys key, Serializable value) {
        return Map.entry(key.name(), value);
    }

    default Map.Entry<String, Serializable> buildEntry(final String key, Serializable value) {
        return Map.entry(key, value);
    }

    default LocalDate now() {
        return LocalDate.now();
    }
}
