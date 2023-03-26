package io.inugami.api.loggers.mdc.initializer;

import io.inugami.api.monitoring.MdcService;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultMdcInitializer implements MdcInitializerSpi {
    @Override
    public Map<String, Serializable> getDefaultValue() {
        final Map<String, Serializable> result = new LinkedHashMap<>();

        for (MdcService.MDCKeys key : MdcService.MDCKeys.VALUES) {
            result.put(key.name(), key.getDefaultValue());
        }
        return result;
    }
}
