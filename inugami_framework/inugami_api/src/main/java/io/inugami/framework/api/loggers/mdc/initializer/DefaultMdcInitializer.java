package io.inugami.framework.api.loggers.mdc.initializer;

import io.inugami.framework.interfaces.monitoring.logger.MDCKeys;
import io.inugami.framework.interfaces.monitoring.logger.initializer.MdcInitializerSpi;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultMdcInitializer implements MdcInitializerSpi {
    @Override
    public Map<String, Serializable> getDefaultValue() {
        final Map<String, Serializable> result = new LinkedHashMap<>();

        for (MDCKeys key : MDCKeys.VALUES) {
            result.put(key.name(), key.getDefaultValue());
        }
        return result;
    }
}
