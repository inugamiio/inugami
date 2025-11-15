package io.inugami.framework.interfaces.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

@SuppressWarnings({"java:S1181", "java:S1141"})
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

        try {
            for (T service : servicesLoaders) {
                try {
                    result.add(service);
                } catch (Throwable e) {
                    traceExcetion(e);
                }
            }
        } catch (Throwable e) {
            traceExcetion(e);
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

    private void traceExcetion(final Throwable e) {
        System.err.println(e.getMessage());
        e.printStackTrace();
    }
}
