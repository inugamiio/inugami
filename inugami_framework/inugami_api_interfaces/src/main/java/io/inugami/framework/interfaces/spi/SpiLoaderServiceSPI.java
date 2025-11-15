package io.inugami.framework.interfaces.spi;

import java.util.List;

public interface SpiLoaderServiceSPI {

    <T> List<T> loadServices(final Class<?> type);

    default <T> List<T> loadSpiServicesByPriority(final Class<?> type) {
        return loadSpiServicesByPriority(type, null);
    }

    <T> List<T> loadSpiServicesByPriority(final Class<?> type, final T defaultImplementation);
}
