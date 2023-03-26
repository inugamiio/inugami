package io.inugami.api.loggers.mdc.mapper;

import io.inugami.api.monitoring.MdcService;

import java.io.Serializable;
import java.util.List;

public class MdcMapperDefaultDoubleValue implements LoggerMdcMappingSPI {

    private static final List<String> MATCHING_KEYS = List.of(
            MdcService.MDCKeys.price.name(),
            MdcService.MDCKeys.deviceNetworkSpeedDown.name(),
            MdcService.MDCKeys.deviceNetworkSpeedUp.name(),
            MdcService.MDCKeys.deviceNetworkSpeedLatency.name(),
            MdcService.MDCKeys.size.name(),
            MdcService.MDCKeys.quantity.name()
    );

    @Override
    public boolean accept(final String key) {
        return MATCHING_KEYS.contains(key);
    }

    @Override
    public Serializable convert(final String value) {
        return MdcMapperUtils.convertToDouble(value);
    }
}
