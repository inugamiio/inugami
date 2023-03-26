package io.inugami.api.loggers.mdc.mapper;

import io.inugami.api.monitoring.MdcService;

import java.io.Serializable;
import java.util.List;

public class MdcMapperDefaultLongValue implements LoggerMdcMappingSPI {

    private static final List<String> MATCHING_KEYS = List.of(
            MdcService.MDCKeys.duration.name(),
            MdcService.MDCKeys.fromTimestamp.name(),
            MdcService.MDCKeys.unitlTimestamp.name()
    );

    @Override
    public boolean accept(final String key) {
        return MATCHING_KEYS.contains(key);
    }

    @Override
    public Serializable convert(final String value) {
        return MdcMapperUtils.convertToLong(value);
    }
}
