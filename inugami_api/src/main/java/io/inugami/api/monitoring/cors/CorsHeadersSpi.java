package io.inugami.api.monitoring.cors;

import io.inugami.api.monitoring.data.ResquestData;
import io.inugami.api.monitoring.models.Headers;
import io.inugami.api.processors.ConfigHandler;

import java.util.List;

public interface CorsHeadersSpi {

    List<String> buildCorsHeaders(final ResquestData request, final Headers headers, final ConfigHandler<String, String> configuration);
}
