package io.inugami.framework.interfaces.monitoring.core;

import io.inugami.framework.interfaces.monitoring.data.ResquestData;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;

import java.util.List;

public interface CorsHeadersSpi {

    List<String> buildCorsHeaders(final ResquestData request,
                                  final ConfigHandler<String, String> configuration);
}
