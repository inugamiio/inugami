package io.inugami.framework.api.monitoring.cors;

import io.inugami.framework.interfaces.monitoring.core.CorsHeadersSpi;
import io.inugami.framework.interfaces.monitoring.data.ResquestData;
import io.inugami.framework.interfaces.monitoring.models.Headers;
import io.inugami.framework.interfaces.processors.ConfigHandler;

import java.util.ArrayList;
import java.util.List;

public class DefaultCorsHeadersSpi implements CorsHeadersSpi {


    @Override
    public List<String> buildCorsHeaders(final ResquestData request,
                                         final ConfigHandler<String, String> configuration) {
        final List<String> result = new ArrayList<>(List.of(
                Headers.X_DEVICE_IDENTIFIER,
                Headers.X_CORRELATION_ID,
                Headers.X_B_3_TRACEID,
                Headers.X_CONVERSATION_ID,

                Headers.X_DEVICE_TYPE,
                Headers.X_DEVICE_SYSTEM,
                Headers.X_DEVICE_CLASS,
                Headers.X_DEVICE_VERSION,
                Headers.X_DEVICE_OS_VERSION,
                Headers.X_DEVICE_NETWORK_TYPE,
                Headers.X_DEVICE_NETWORK_SPEED_DOWN,
                Headers.X_DEVICE_NETWORK_SPEED_UP,
                Headers.X_DEVICE_NETWORK_SPEED_LATENCY,
                Headers.CLIENT_IP,
                Headers.USER_AGENT,
                Headers.ACCEPT_LANGUAGE,
                Headers.COUNTRY,
                Headers.WARNING,
                Headers.ERROR_CODE,
                Headers.ERROR_EXCEPTION,
                Headers.ERROR_MESSAGE,
                Headers.ERROR_MESSAGE_DETAIL,
                Headers.X_FRONT_VERSION,
                Headers.X_APPLICATION,

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

        return result;
    }
}
