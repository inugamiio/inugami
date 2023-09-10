package io.inugami.api.spi;

import java.util.List;

public interface SpiLoaderServiceSPI {

    <T> List<T> loadServices(final Class<?> type);
}
