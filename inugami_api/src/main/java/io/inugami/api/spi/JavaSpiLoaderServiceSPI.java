package io.inugami.api.spi;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

@Slf4j
public class JavaSpiLoaderServiceSPI implements SpiLoaderServiceSPI {
    @Override
    public <T> List<T> loadServices(final Class<?> type) {
        final List<T> result = new ArrayList<>();
        try {
            final ServiceLoader<T> servicesLoaders = (ServiceLoader<T>) ServiceLoader.load(type);
            servicesLoaders.forEach(result::add);
        } catch (Throwable e) {
            log.error(e.getMessage(), e
            );
        }
        return result;
    }
}
