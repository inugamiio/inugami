package io.inugami.logs.obfuscator.appender.writer;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.Encoder;
import io.inugami.framework.api.connectors.HttpBasicConnector;
import io.inugami.framework.interfaces.connectors.ConnectorConstants;
import io.inugami.framework.interfaces.connectors.ConnectorListener;
import io.inugami.framework.interfaces.connectors.HttpRequest;
import io.inugami.framework.interfaces.connectors.config.HttpBasicConnectorConfiguration;
import io.inugami.framework.interfaces.exceptions.services.ConnectorException;
import io.inugami.logs.obfuscator.appender.AppenderConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"java:S108", "java:S1450"})
public class LogstashWriter implements AppenderWriterStrategy, ConnectorListener {

    public static final String                         LOGSTASH                   = "logstash";
    public static final int                            DEFAULT_TIMEOUT            = 2000;
    public static final int                            DEFAULT_TIME_TO_LIVE       = 10000;
    public static final int                            DEFAULT_MAX_CONNECTIONS    = 50;
    public static final int                            DEFAULT_MAX_SOCKET_TIMEOUT = 1000;
    public static final String                         DEFAULT_HOST               = "http://localhost:5054";
    private             AppenderConfiguration          configuration              = null;
    private             Encoder<ILoggingEvent>         encoder;
    private             HttpBasicConnector             connector;
    private             String                         baseUrl;
    private             Map<String, String>            headers;
    private             HttpRequest.HttpRequestBuilder request;

    @Override
    public boolean accept(final AppenderConfiguration configuration) {
        if (LOGSTASH.equalsIgnoreCase(configuration.getMode())) {
            this.configuration = configuration;
            return true;
        }
        return false;
    }

    @Override
    public void start(final Encoder<ILoggingEvent> encoder) {
        this.encoder = encoder;

        final int timeout = configuration.getTimeout() == null ? DEFAULT_TIMEOUT : configuration.getTimeout();

        baseUrl = configuration.getHost() == null ? DEFAULT_HOST : configuration.getHost();
        connector = new HttpBasicConnector(HttpBasicConnectorConfiguration.builder()
                                                                          .timeoutReading(timeout)
                                                                          .build());
        headers = configuration.getHeadersMap() == null ? new HashMap<>() : configuration.getHeadersMap();

        request = HttpRequest.builder()
                             .verb(ConnectorConstants.HTTP_POST)
                             .url(baseUrl)
                             .headers(headers)
                             .disableListener(true)
                             .listener(this)
                             .addHeader("ContentType", "application/json");

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
