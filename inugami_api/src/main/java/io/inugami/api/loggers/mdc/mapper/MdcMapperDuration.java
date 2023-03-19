package io.inugami.api.loggers.mdc.mapper;

import io.inugami.api.monitoring.MdcService;

import java.io.Serializable;

public class MdcMapperDuration implements LoggerMdcMappingSPI {

    @Override
    public boolean accept(final String key) {
        return MdcService.MDCKeys.duration.name().equalsIgnoreCase(key);
    }

    @Override
    public Serializable convert(final String value) {
        long result = 0;
        if (value != null) {
            try {
                result = Long.parseLong(value);
            } catch (Throwable e) {
            }

        }
        return result;
    }
}
