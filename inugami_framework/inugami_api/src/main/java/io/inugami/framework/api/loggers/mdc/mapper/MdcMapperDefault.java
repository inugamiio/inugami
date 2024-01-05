package io.inugami.framework.api.loggers.mdc.mapper;

import io.inugami.framework.interfaces.monitoring.logger.mapper.LoggerMdcMappingSPI;
import io.inugami.framework.interfaces.spi.SpiPriority;

import java.io.Serializable;

@SpiPriority(Integer.MIN_VALUE)
public class MdcMapperDefault implements LoggerMdcMappingSPI {
    @Override
    public boolean accept(final String key) {
        return true;
    }

    @Override
    public Serializable convert(final String value) {
        return value;
    }
}
