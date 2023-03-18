package io.inugami.api.spi;

import io.inugami.api.loggers.Loggers;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;


public class JavaSpiLoaderServiceSPI implements SpiLoaderServiceSPI {
    @Override
    public <T> List<T> loadServices(final Class<?> type) {
        final List<T> result = new ArrayList<>();
        try {
            final ServiceLoader<T> servicesLoaders = (ServiceLoader<T>) ServiceLoader.load(type);
            servicesLoaders.forEach(result::add);
        } catch (Throwable e) {
            Loggers.DEBUG.error(e.getMessage(), e);
        }
        return result;
    }
}
