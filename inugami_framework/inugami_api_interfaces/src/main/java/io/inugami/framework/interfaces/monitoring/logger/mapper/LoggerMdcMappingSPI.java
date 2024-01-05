package io.inugami.framework.interfaces.monitoring.logger.mapper;

import java.io.Serializable;

public interface LoggerMdcMappingSPI {
    boolean accept(final String key);

    Serializable convert(final String value);
}
