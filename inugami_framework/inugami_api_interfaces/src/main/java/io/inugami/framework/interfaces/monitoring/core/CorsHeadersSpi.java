package io.inugami.framework.interfaces.monitoring.core;

import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.monitoring.data.RequestData;

import java.util.List;

public interface CorsHeadersSpi {

    List<String> buildCorsHeaders(final RequestData request,
                                  final ConfigHandler<String, String> configuration);
}
