package io.inugami.interfaces.monitoring.core;

import io.inugami.interfaces.monitoring.data.ResquestData;
import io.inugami.interfaces.monitoring.models.Headers;
import io.inugami.interfaces.processors.ConfigHandler;

import java.util.List;

public interface CorsHeadersSpi {

    List<String> buildCorsHeaders(final ResquestData request,
                                  final Headers headers,
                                  final ConfigHandler<String, String> configuration);
}
