package io.inugami.api.loggers.mdc.mapper;

import io.inugami.api.monitoring.MdcService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MdcMapperDefaultIntValue implements LoggerMdcMappingSPI {

    private static final List<String> MATCHING_KEYS = initializeKeys();

    private static List<String> initializeKeys() {
        List<String> result = new ArrayList<>();

        for (MdcService.MDCKeys key : MdcService.MDCKeys.VALUES) {
            if (key.getDefaultValue() instanceof Integer) {
                result.add(key.name());
            }
        }

        return result;
    }
    @Override
    public boolean accept(final String key) {
        return MATCHING_KEYS.contains(key);
    }

    @Override
    public Serializable convert(final String value) {
        return MdcMapperUtils.convertToInt(value);
    }
}
