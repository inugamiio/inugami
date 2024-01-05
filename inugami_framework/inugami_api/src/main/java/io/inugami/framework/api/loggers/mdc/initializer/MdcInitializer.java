package io.inugami.framework.api.loggers.mdc.initializer;

import io.inugami.framework.interfaces.monitoring.MdcServiceSpi;
import io.inugami.framework.interfaces.monitoring.MdcServiceSpiFactory;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.monitoring.logger.initializer.MdcInitializerSpi;
import io.inugami.framework.interfaces.spi.SpiLoader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MdcInitializer {

    public static void initialize() {
        final List<MdcInitializerSpi> initializers = SpiLoader.getInstance()
                                                              .loadSpiServicesByPriority(MdcInitializerSpi.class);

        final Map<String, Serializable> mdcContext = loadValues(initializers);

        final MdcServiceSpi mdcService = MdcServiceSpiFactory.getInstance();
        for (Map.Entry<String, Serializable> entry : mdcContext.entrySet()) {
            mdcService.setMdc(entry.getKey(), entry.getValue());
        }
        Loggers.LOG_INITIALIZER.info("initialize log MDC");
        mdcService.clear();
    }

    private static Map<String, Serializable> loadValues(final List<MdcInitializerSpi> initializers) {
        final Map<String, Serializable> result = new LinkedHashMap<>();

        for (MdcInitializerSpi initializer : initializers) {
            result.putAll(initializer.getDefaultValue());
        }
        return result;
    }
}
