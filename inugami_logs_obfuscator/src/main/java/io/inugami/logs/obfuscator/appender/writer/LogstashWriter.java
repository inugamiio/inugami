package io.inugami.logs.obfuscator.appender.writer;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.Encoder;
import io.inugami.api.exceptions.services.ConnectorException;
import io.inugami.commons.connectors.ConnectorConstants;
import io.inugami.commons.connectors.ConnectorListener;
import io.inugami.commons.connectors.HttpBasicConnector;
import io.inugami.commons.connectors.HttpRequest;
import io.inugami.logs.obfuscator.appender.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LogstashWriter implements AppenderWriterStrategy, ConnectorListener {

    private static final String                         LINE                       = "\n";
    public static final  String                         LOGSTASH                   = "logstash";
    public static final  int                            DEFAULT_TIMEOUT            = 2000;
    public static final  int                            DEFAULT_TIME_TO_LIVE       = 10000;
    public static final  int                            DEFAULT_MAX_CONNECTIONS    = 50;
    public static final  int                            DEFAULT_MAX_SOCKET_TIMEOUT = 1000;
    public static final  String                         DEFAULT_HOST               = "http://localhost:5054";
    private              Configuration                  configuration              = null;
    private              Encoder<ILoggingEvent>         encoder;
    private              HttpBasicConnector             connector;
    private              String                         baseUrl;
    private              Map<String, String>            headers;
    private              HttpRequest.HttpRequestBuilder request;

    @Override
    public boolean accept(final Configuration configuration) {
        if (LOGSTASH.equalsIgnoreCase(configuration.getMode())) {
            this.configuration = configuration;
            return true;
        }
        return false;
    }

    @Override
    public void start(final Encoder<ILoggingEvent> encoder) {
        this.encoder = encoder;

        final int timeout        = configuration.getTimeout() == null ? DEFAULT_TIMEOUT : configuration.getTimeout();
        final int timeToLive     = configuration.getTimeout() == null ? DEFAULT_TIME_TO_LIVE : configuration.getTimeToLive();
        final int maxConnections = configuration.getTimeout() == null ? DEFAULT_MAX_CONNECTIONS : configuration.getMaxConnections();
        final int maxPerRoute    = configuration.getTimeout() == null ? DEFAULT_MAX_CONNECTIONS : configuration.getMaxPerRoute();
        final int socketTimeout  = configuration.getTimeout() == null ? DEFAULT_MAX_SOCKET_TIMEOUT : configuration.getSocketTimeout();

        baseUrl = configuration.getHost() == null ? DEFAULT_HOST : configuration.getHost();
        connector = new HttpBasicConnector(timeout, timeToLive, maxConnections, maxPerRoute, socketTimeout);
        headers = configuration.getHeaders() == null ? new HashMap<>() : headers;

        request = HttpRequest.builder()
                             .verb(ConnectorConstants.HTTP_POST)
                             .url(baseUrl)
                             .headers(headers)
                             .disableListener(true)
                             .listener(this)
                             .addHeader("ContentType", "application/json");

    }


    @Override
    public String serializeToJson(final Object body) {
        return body == null ? null : String.valueOf(body);
    }

    @Override
    public void stop() {
        connector.close();
    }

    @Override
    public void write(final ILoggingEvent iLoggingEvent) {
        final byte[] encoded = encoder.encode(iLoggingEvent);
        if (encoded == null) {
            return;
        }

        try {
            final String body = new String(encoded, StandardCharsets.UTF_8);
            connector.post(request.body(body).build());
        } catch (final ConnectorException e) {
        }

    }
}