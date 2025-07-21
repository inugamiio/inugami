package io.inugami.monitoring.core.spi;

import io.inugami.framework.interfaces.monitoring.data.ResquestData;
import io.inugami.monitoring.api.resolvers.Interceptable;

public class H2Interceptable implements Interceptable {

    private static final String H2_CONSOLE = "h2-console";


    @Override
    public boolean isInterceptable(final ResquestData request) {
        return !request.getUri().contains(H2_CONSOLE);
    }
}
