package io.inugami.framework.interfaces.spi;

import java.util.List;

public interface SpiLoaderServiceSPI {

    <T> List<T> loadServices(final Class<?> type);
}
