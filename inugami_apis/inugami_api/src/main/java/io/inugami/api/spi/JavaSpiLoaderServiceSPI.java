package io.inugami.api.spi;

import io.inugami.api.loggers.Loggers;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

@SuppressWarnings({"java:S1181"})
public class JavaSpiLoaderServiceSPI implements SpiLoaderServiceSPI {
    @Override
    public <T> List<T> loadServices(final Class<?> type) {
        final List<T> result = new ArrayList<>();

        ServiceLoader<T> servicesLoaders = null;
        try {
            servicesLoaders = (ServiceLoader<T>) ServiceLoader.load(type);
        } catch (Throwable e) {
            traceExcetion(e);
        }
        if (servicesLoaders == null) {
            return result;
        }

        for (T service : servicesLoaders) {
            try {
                result.add(service);
            } catch (Throwable e) {
                traceExcetion(e);
            }
        }

        return result;
    }

    private void traceExcetion(final Throwable e) {
        if (Loggers.DEBUG.isTraceEnabled()) {
            Loggers.DEBUG.error(e.getMessage(), e);
        }
    }
}
