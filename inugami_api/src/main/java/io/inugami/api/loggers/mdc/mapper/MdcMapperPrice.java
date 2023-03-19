package io.inugami.api.loggers.mdc.mapper;

import java.io.Serializable;

public class MdcMapperPrice implements LoggerMdcMappingSPI {

    private static final String PRICE = "price";

    @Override
    public boolean accept(final String key) {
        return PRICE.equalsIgnoreCase(key);
    }

    @Override
    public Serializable convert(final String value) {
        double result = 0;
        if (value != null) {
            try {
                result = Double.parseDouble(value);
            } catch (Throwable e) {
            }

        }
        return result;
    }
}
