package io.inugami.interfaces.spi;

import java.util.List;

public interface SpiLoaderServiceSPI {

    <T> List<T> loadServices(final Class<?> type);
}
