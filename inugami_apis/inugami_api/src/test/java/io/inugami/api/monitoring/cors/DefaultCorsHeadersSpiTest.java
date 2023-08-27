package io.inugami.api.monitoring.cors;

import io.inugami.api.monitoring.models.Headers;
import io.inugami.api.processors.DefaultConfigHandler;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DefaultCorsHeadersSpiTest {


    @Test
    void buildCorsHeaders_nominal() {
        final Headers headers = new Headers().refreshConfig(new DefaultConfigHandler());

        final List<String> corsHeaders = new DefaultCorsHeadersSpi().buildCorsHeaders(null, headers, null);
        Collections.sort(corsHeaders);

        assertThat(String.join("\n", corsHeaders))
                .isEqualTo(String.join("\n", List.of(
                        "Accept-Language",
                        "Allow",
                        "Authorization",
                        "Date",
                        "Early-Data",
                        "Forwarded",
                        "From",
                        "Host",
                        "Last-Event-ID",
                        "Link",
                        "Location",
                        "NEL",
                        "Ping-From",
                        "Ping-To",
                        "Referer",
                        "Referrer-Policy",
                        "Refresh",
                        "Report-To",
                        "Retry-After",
                        "Sec-WebSocket-Accept",
                        "Sec-WebSocket-Extensions",
                        "Sec-WebSocket-Key",
                        "Sec-WebSocket-Protocol",
                        "Sec-WebSocket-Version",
                        "Server",
                        "SourceMap",
                        "TE",
                        "Trailer",
                        "Transfer-Encoding",
                        "Upgrade",
                        "User-Agent",
                        "User-Agent",
                        "Via",
                        "Warning",
                        "X-Forwarded-For",
                        "X-Forwarded-Host",
                        "X-Forwarded-Proto",
                        "authorization",
                        "clientIp",
                        "content-type",
                        "country",
                        "errorCode",
                        "errorException",
                        "errorMessage",
                        "errorMessageDetail",
                        "x-application",
                        "x-b3-traceid",
                        "x-b3-traceid",
                        "x-conversation-id",
                        "x-correlation-id",
                        "x-device-class",
                        "x-device-identifier",
                        "x-device-network-speed-down",
                        "x-device-network-speed-latency",
                        "x-device-network-speed-up",
                        "x-device-network-type",
                        "x-device-os-version",
                        "x-device-system",
                        "x-device-type",
                        "x-device-version",
                        "x-front-version")));

    }
}