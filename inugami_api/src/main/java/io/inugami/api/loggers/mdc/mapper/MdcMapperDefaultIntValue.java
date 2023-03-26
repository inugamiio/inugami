package io.inugami.api.loggers.mdc.mapper;

import io.inugami.api.monitoring.MdcService;

import java.io.Serializable;
import java.util.List;

public class MdcMapperDefaultIntValue implements LoggerMdcMappingSPI {

    private static final List<String> MATCHING_KEYS = List.of(
            MdcService.MDCKeys.errorStatus.name(),
            MdcService.MDCKeys.httpStatus.name()
    );

    @Override
    public boolean accept(final String key) {
        return MATCHING_KEYS.contains(key);
    }

    @Override
    public Serializable convert(final String value) {
        return MdcMapperUtils.convertToInt(value);
    }
}
