package io.inugami.api.monitoring.cors;

import io.inugami.api.monitoring.data.ResquestData;
import io.inugami.api.monitoring.models.Headers;
import io.inugami.api.processors.ConfigHandler;

import java.util.ArrayList;
import java.util.List;

public class DefaultCorsHeadersSpi implements CorsHeadersSpi {


    @Override
    public List<String> buildCorsHeaders(final ResquestData request, final Headers headers, final ConfigHandler<String, String> configuration) {
        List<String> result = new ArrayList<>(List.of(headers.getConversationId(),
                                                      headers.getRequestId(),
                                                      headers.getTraceId(),
                                                      headers.getConversationId(),
                                                      headers.getToken(),
                                                      headers.getDeviceIdentifier(),
                                                      headers.getDeviceType(),
                                                      headers.getDeviceSystem(),
                                                      headers.getDeviceClass(),
                                                      headers.getDeviceVersion(),
                                                      headers.getDeviceNetworkType(),
                                                      headers.getDeviceNetworkSpeedDown(),
                                                      headers.getDeviceNetworkSpeedUp(),
                                                      headers.getDeviceNetworkSpeedLatency(),
                                                      headers.getDeviceNetworkType(),
                                                      headers.getDeviceIp(),
                                                      headers.getUserAgent(),
                                                      headers.getLanguage(),
                                                      headers.getCountry(),
                                                      "content-type",
                                                      "authorization",
                                                      "x-warnings",
                                                      "errorCode",
                                                      "errorException",
                                                      "errorMessage",
                                                      "errorMessageDetail",
                                                      "errorException",
                                                      "x-front-version"
        ));

        if (headers.getSpecificHeaders() != null) {
            for (String specificHeader : headers.getSpecificHeaders()) {
                result.add(specificHeader);
            }
        }
        return result;
    }
}
