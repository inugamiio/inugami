package io.inugami.logs.obfuscator.appender.writer;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.Encoder;
import io.inugami.framework.api.connectors.HttpBasicConnector;
import io.inugami.framework.interfaces.connectors.ConnectorConstants;
import io.inugami.framework.interfaces.connectors.ConnectorListener;
import io.inugami.framework.interfaces.connectors.HttpRequest;
import io.inugami.framework.interfaces.exceptions.services.ConnectorException;
import io.inugami.framework.interfaces.models.JsonBuilder;
import io.inugami.logs.obfuscator.appender.AppenderConfiguration;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"java:S108", "java:S1450"})
public class ElasticSearchWriter implements AppenderWriterStrategy, ConnectorListener, Runnable {

    public static final String ELASTIC_SEARCH             = "elasticSearch";
    public static final int    DEFAULT_TIMEOUT            = 2000;
    public static final int    DEFAULT_TIME_TO_LIVE       = 10000;
    public static final int    DEFAULT_MAX_CONNECTIONS    = 50;
    public static final int    DEFAULT_MAX_SOCKET_TIMEOUT = 1000;
    public static final String DEFAULT_HOST               = "http://localhost:9000";
    public static final String DEFAULT_INDEX              = "application";
    public static final String DEFAULT_DATE               = "yyyy-MM-dd";
    public static final String EMPTY                      = "";

    private       AppenderConfiguration          configuration = null;
    private       Encoder<ILoggingEvent>         encoder;
    private       HttpBasicConnector             connector;
    private       String                         baseUrl;
    private       Map<String, String>            headers;
    private       String                         index;
    private       String                         indexPattern;
    private       HttpRequest.HttpRequestBuilder request;
    private final ScheduledExecutorService       executor      = Executors.newSingleThreadScheduledExecutor();
    private final Queue<String>                  values        = new LinkedBlockingQueue<>();

    @Override
    public boolean accept(final AppenderConfiguration configuration) {
        if (ELASTIC_SEARCH.equalsIgnoreCase(configuration.getMode())) {
            this.configuration = configuration;

            return true;
        }
        return false;
    }

    @Override
    public void start(final Encoder<ILoggingEvent> encoder) {
        this.encoder = encoder;

        executor.scheduleAtFixedRate(this, 0, 500, TimeUnit.MILLISECONDS);

        final int timeout = configuration.getTimeout() == null ? DEFAULT_TIMEOUT : configuration.getTimeout();
        final int timeToLive =
                configuration.getTimeout() == null ? DEFAULT_TIME_TO_LIVE : configuration.getTimeToLive();
        final int maxConnections =
                configuration.getTimeout() == null ? DEFAULT_MAX_CONNECTIONS : configuration.getMaxConnections();
        final int maxPerRoute =
                configuration.getTimeout() == null ? DEFAULT_MAX_CONNECTIONS : configuration.getMaxPerRoute();
        final int socketTimeout =
                configuration.getTimeout() == null ? DEFAULT_MAX_SOCKET_TIMEOUT : configuration.getSocketTimeout();

        baseUrl = configuration.getHost() == null ? DEFAULT_HOST : configuration.getHost();
        connector = new HttpBasicConnector(timeout, timeToLive, maxConnections, maxPerRoute, socketTimeout);
        headers = configuration.getHeadersMap() == null ? new HashMap<>() : configuration.getHeadersMap();
        index = configuration.getIndex() == null ? DEFAULT_INDEX : this.configuration.getIndex();
        indexPattern =
                configuration.getIndexDatePattern() == null ? DEFAULT_DATE : this.configuration.getIndexDatePattern();

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
        run();
        connector.close();
    }

    @Override
    public void write(final ILoggingEvent iLoggingEvent) {
        final byte[] encoded = encoder.encode(iLoggingEvent);
        if (encoded != null) {
            values.add(new JsonBuilder().write("{ \"index\" : { \"_index\" : \"" + index + "-" +
                                               new SimpleDateFormat(indexPattern).format(new Date()) + "\"} }")
                                        .line()
                                        .write(new String(encoded, StandardCharsets.UTF_8))
                                        .toString());
        }
    }

    @Override
    public void run() {
        final List<String>     result   = new ArrayList<>();
        final Iterator<String> iterator = values.iterator();
        while (iterator.hasNext()) {
            result.add(iterator.next());
            result.add("\n");
        }

        if (result.isEmpty()) {
            return;
        }

        try {
            final String payload = String.join(EMPTY, result);
            connector.post(request.body(payload).build());
        } catch (final ConnectorException e) {
        }
    }

}
