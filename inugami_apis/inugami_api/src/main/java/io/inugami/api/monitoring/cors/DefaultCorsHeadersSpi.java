package io.inugami.api.monitoring.cors;

import io.inugami.interfaces.monitoring.core.CorsHeadersSpi;
import io.inugami.interfaces.monitoring.data.ResquestData;
import io.inugami.interfaces.monitoring.models.Headers;
import io.inugami.interfaces.processors.ConfigHandler;

import java.util.ArrayList;
import java.util.List;

public class DefaultCorsHeadersSpi implements CorsHeadersSpi {


    @Override
    public List<String> buildCorsHeaders(final ResquestData request,
                                         final Headers headers,
                                         final ConfigHandler<String, String> configuration) {
        final List<String> result = new ArrayList<>(List.of(
                headers.getDeviceIdentifier(),
                headers.getCorrelationId(),
                headers.getRequestId(),
                headers.getTraceId(),
                headers.getConversationId(),
                headers.getToken(),

                headers.getDeviceType(),
                headers.getDeviceSystem(),
                headers.getDeviceClass(),
                headers.getDeviceVersion(),
                headers.getDeviceOsVersion(),
                headers.getDeviceNetworkType(),
                headers.getDeviceNetworkSpeedDown(),
                headers.getDeviceNetworkSpeedUp(),
                headers.getDeviceNetworkSpeedLatency(),
                headers.getDeviceIp(),
                headers.getUserAgent(),
                headers.getLanguage(),
                headers.getCountry(),
                headers.getWarning(),
                headers.getErrorCode(),
                headers.getErrorException(),
                headers.getErrorMessage(),
                headers.getErrorMessageDetail(),
                headers.getFrontVersion(),
                headers.getCallFrom(),

                // Proxies
                "Forwarded",
                "X-Forwarded-For",
                "X-Forwarded-Host",
                "X-Forwarded-Proto",
                "Via",
                // Redirect
                "Location",
                "Refresh",
                // Request Context
                "From",
                "Host",
                "Referer",
                "Referrer-Policy",
                "User-Agent",

                // Response context
                "Allow",
                "Server",

                // SSE
                "Last-Event-ID",
                "NEL",
                "Ping-From",
                "Ping-To",
                "Report-To",

                // Transfer
                "Transfer-Encoding",
                "TE",
                "Trailer",

                // WEB socket
                "Sec-WebSocket-Key",
                "Sec-WebSocket-Extensions",
                "Sec-WebSocket-Accept",
                "Sec-WebSocket-Protocol",
                "Sec-WebSocket-Version",

                // Other
                "Date",
                "Early-Data",
                "Link",
                "Retry-After",
                "SourceMap",
                "Upgrade",
                "content-type",
                "authorization"
        ));

        if (headers.getSpecificHeaders() != null) {
            for (final String specificHeader : headers.getSpecificHeaders()) {
                result.add(specificHeader);
            }
        }
        return result;
    }
}
