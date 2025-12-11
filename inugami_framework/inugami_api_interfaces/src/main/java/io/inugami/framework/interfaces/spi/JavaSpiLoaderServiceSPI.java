package io.inugami.framework.interfaces.spi;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

@SuppressWarnings({"java:S1181", "java:S1141"})
@Slf4j
public class JavaSpiLoaderServiceSPI implements SpiLoaderServiceSPI {
    @Override
    public <T> List<T> loadServices(final Class<?> type) {
        final List<T> result = new ArrayList<>();

        ServiceLoader<T> servicesLoaders = null;
        try {
            servicesLoaders = (ServiceLoader<T>) ServiceLoader.load(type);
        } catch (Throwable e) {
            traceException(e);
        }
        if (servicesLoaders == null) {
            return result;
        }

        try {
            for (T service : servicesLoaders) {
                applyIfNotNull(service, result::add);
            }
        } catch (Throwable e) {
            traceException(e);
        }

        return result;
    }


    @Override
    public <T> List<T> loadSpiServicesByPriority(final Class<?> type, final T defaultImplementation) {
        final List<T> result = loadServices(type);
        if (defaultImplementation != null) {
            result.add(defaultImplementation);
        }

        result.sort(new PriorityComparator<>());
        return result;
    }

    private void traceException(final Throwable e) {
        log.error(e.getMessage(), e);
    }
}
