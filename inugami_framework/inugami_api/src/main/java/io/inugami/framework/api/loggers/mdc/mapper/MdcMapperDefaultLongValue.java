package io.inugami.framework.api.loggers.mdc.mapper;

import io.inugami.framework.interfaces.monitoring.logger.MDCKeys;
import io.inugami.framework.interfaces.monitoring.logger.mapper.LoggerMdcMappingSPI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MdcMapperDefaultLongValue implements LoggerMdcMappingSPI {

    private static final List<String> MATCHING_KEYS = initializeKeys();

    private static List<String> initializeKeys() {
        List<String> result = new ArrayList<>();

        for (MDCKeys key : MDCKeys.VALUES) {
            if (key.getDefaultValue() instanceof Long) {
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
        return MdcMapperUtils.convertToLong(value);
    }
}
