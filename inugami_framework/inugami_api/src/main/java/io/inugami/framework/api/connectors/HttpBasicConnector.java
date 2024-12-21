/* --------------------------------------------------------------------
 *  Inugami
 * --------------------------------------------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.framework.api.connectors;

import io.inugami.framework.interfaces.connectors.ConnectorListener;
import io.inugami.framework.interfaces.connectors.HttpRequest;
import io.inugami.framework.interfaces.connectors.IHttpBasicConnector;
import io.inugami.framework.interfaces.connectors.config.HttpBasicConnectorConfiguration;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.services.ConnectorException;
import io.inugami.framework.interfaces.functionnals.ConsumerWithException;
import io.inugami.framework.interfaces.functionnals.FunctionWithException;
import io.inugami.framework.interfaces.models.tools.Chrono;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static io.inugami.framework.interfaces.connectors.ConnectorConstants.*;
import static io.inugami.framework.interfaces.connectors.exceptions.HttpError.REQUEST_REQUIRE;


/**
 * HttpConnector.
 *
 * @author patrick_guillerm
 * @since 26 oct. 2016
 */
@Slf4j
public class HttpBasicConnector implements IHttpBasicConnector {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String                          URL_SEPARATOR    = "/";
    public static final String                          GET              = "GET";
    public static final String                          EMPTY            = "";
    public static final String                          OPTION_VALUE_SEP = "=";
    public static final String                          OPTION_SEP       = ",";
    private final       HttpBasicConnectorConfiguration configuration;
    private final       Duration                        timeout;
    private final       OkHttpClient                    client;
    private final       int                             retry;

    // =================================================================================================================
    // CONSTRUCTOR
    // =================================================================================================================
    public HttpBasicConnector(final HttpBasicConnectorConfiguration configuration) {
        this.configuration = configuration == null ? HttpBasicConnectorConfiguration.builder().build() : configuration;
        this.timeout = Duration.ofMillis(this.configuration.getTimeout());
        this.client = new OkHttpClient();
        this.retry = Math.max(this.configuration.getRetry(), 0);
    }

    // =================================================================================================================
    // GET
    // =================================================================================================================
    @Override
    public HttpConnectorResult get(final HttpRequest request) throws ConnectorException {
        Asserts.assertNotNull(REQUEST_REQUIRE, request);
        return processGenericRequest(request.toBuilder()
                                            .verb(HTTP_GET)
                                            .body(null)
                                            .build(), () -> new Request.Builder().get());
    }


    // =================================================================================================================
    // POST
    // =================================================================================================================
    @Override
    public HttpConnectorResult post(final HttpRequest request) throws ConnectorException {
        Asserts.assertNotNull(REQUEST_REQUIRE, request);
        return processGenericRequest(request.toBuilder()
                                            .verb(HTTP_POST)
                                            .build(), () -> new Request.Builder().post(buildBody(request)));
    }


    // =================================================================================================================
    // PUT
    // =================================================================================================================
    @Override
    public HttpConnectorResult put(final HttpRequest request) throws ConnectorException {
        Asserts.assertNotNull(REQUEST_REQUIRE, request);
        return processGenericRequest(request.toBuilder()
                                            .verb(PUT)
                                            .build(), () -> new Request.Builder().put(buildBody(request)));
    }


    // =================================================================================================================
    // PATCH
    // =================================================================================================================
    @Override
    public HttpConnectorResult patch(final HttpRequest request) throws ConnectorException {
        Asserts.assertNotNull(REQUEST_REQUIRE, request);
        return processGenericRequest(request.toBuilder()
                                            .verb(PATCH)
                                            .build(), () -> new Request.Builder().patch(buildBody(request)));
    }


    // =================================================================================================================
    // DELETE
    // =================================================================================================================
    @Override
    public HttpConnectorResult delete(final HttpRequest request) throws ConnectorException {
        Asserts.assertNotNull(REQUEST_REQUIRE, request);
        return processGenericRequest(request.toBuilder().verb(DELETE).build(), () -> new Request.Builder().delete());
    }


    // =================================================================================================================
    // OPTION
    // =================================================================================================================
    @Override
    public HttpConnectorResult option(final HttpRequest request) throws ConnectorException {
        Asserts.assertNotNull(REQUEST_REQUIRE, request);
        return processGenericRequest(request.toBuilder()
                                            .verb(OPTION)
                                            .body(null)
                                            .build(), () -> new Request.Builder().method(OPTION, null));
    }


    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    private HttpConnectorResult processGenericRequest(final HttpRequest request,
                                                      final Supplier<Request.Builder> requestClientBuilder) throws ConnectorException {

        HttpRequest currentRequest = request;
        if (!request.isDisableListener()) {
            currentRequest = callListenersOnBeforeCalling(currentRequest, currentRequest.getListener());
        }

        final var httpClientBuilder = requestClientBuilder.get();
        httpClientBuilder.url(buildFullUrl(currentRequest));

        final Call          call   = this.client.newCall(httpClientBuilder.build());
        HttpConnectorResult result = null;

        for (int i = retry; i >= 0; i--) {
            final Chrono chrono = Chrono.startChrono();

            try {
                final Response      response   = call.execute();
                HttpConnectorResult stepResult = convertToResult(response, chrono.getDuration());
                if (!request.isDisableListener()) {
                    if (response.code() < 400) {
                        callListenerOnDone(request.getListener(), stepResult);
                        result = stepResult;
                        break;
                    } else {
                        callListenerOnError(request.getListener(), stepResult);
                    }
                }
            } catch (IOException e) {
                if (i < retry) {
                    throw new ConnectorException(e);
                }
            }

        }

        return result;
    }

    private HttpConnectorResult convertToResult(final Response response, final long duration) {
        final var builder = HttpConnectorResult.builder()
                                               .message(response.message())
                                               .statusCode(response.code())
                                               .delay(duration)
                                               .verb(response.request().method())
                                               .url(response.request().url().toString());

        if (response.headers() != null) {
            final Map<String, List<String>> headers = response.headers().toMultimap();

            for (final String header : headers.keySet()) {
                builder.addResponseHeader(header, String.join(";", headers.get(header)));
            }
        }

        return builder.build();
    }


    // =================================================================================================================
    // LISTENER
    // =================================================================================================================
    private <T> T invokeListener(final List<ConnectorListener> listeners,
                                 final FunctionWithException<ConnectorListener, T, Exception> action) {
        T result = null;

        for (ConnectorListener listener : listeners) {
            result = invokeListener(listener, action);
        }
        return result;
    }

    private <T> T invokeListener(final ConnectorListener listener,
                                 final FunctionWithException<ConnectorListener, T, Exception> action) {
        T result = null;
        if (listener != null) {
            try {
                result = action.process(listener);
            } catch (final Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return result;
    }

    private void invokeVoidListener(final List<ConnectorListener> listeners,
                                    final ConsumerWithException<ConnectorListener> action) {
        for (ConnectorListener listener : listeners) {
            invokeVoidListener(listener, action);
        }
    }

    private void invokeVoidListener(final ConnectorListener listener,
                                    final ConsumerWithException<ConnectorListener> action) {
        if (listener != null) {
            try {
                action.process(listener);
            } catch (final Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    private HttpRequest callListenersOnBeforeCalling(final HttpRequest currentRequest,
                                                     final List<ConnectorListener> listener) {
        HttpRequest result = currentRequest;


        for (final ConnectorListener listenerSpi : Optional.ofNullable(configuration.getListeners())
                                                           .orElse(List.of())) {
            try {
                final HttpRequest newRequest = listenerSpi.beforeCalling(result);
                if (newRequest != null) {
                    result = newRequest;
                }
            } catch (final Throwable e) {
                log.error(e.getMessage(), e);
            }

        }
        HttpRequest newRequest = invokeListener(listener, l -> l.beforeCalling(currentRequest));
        if (newRequest != null) {
            result = newRequest;
        }

        return result;
    }


    private String callListenerSerializeToJson(final Object body, final ConnectorListener listener) {
        String result = invokeListener(listener, l -> l.serializeToJson(body));

        if (result == null) {
            for (final ConnectorListener listenerSpi : Optional.ofNullable(configuration.getListeners())
                                                               .orElse(List.of())) {
                try {
                    result = listenerSpi.serializeToJson(body);
                    if (result != null) {
                        break;
                    }
                } catch (final Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        return result;
    }

    private void invokeListenersOnProcessCalling(final List<ConnectorListener> listeners,
                                                 final HttpRequest request,
                                                 final HttpConnectorResult result) {
        if (request.isDisableListener()) {
            return;
        }

        for (final ConnectorListener listenerSpi : getListeners()) {
            invokeListener(listenerSpi, l -> l.processCalling(request, result));
        }
        invokeListener(listeners, l -> l.processCalling(request, result));
    }

    private void callListenerOnDone(final List<ConnectorListener> listeners, final HttpConnectorResult stepResult) {
        for (final ConnectorListener defaultListener : getListeners()) {
            invokeVoidListener(defaultListener, l -> l.onDone(stepResult));
        }
        if (listeners != null) {
            invokeVoidListener(listeners, l -> l.onDone(stepResult));
        }
    }

    private void callListenerOnError(final List<ConnectorListener> listeners, final HttpConnectorResult stepResult) {
        for (final ConnectorListener listenerSpi : getListeners()) {
            invokeVoidListener(listenerSpi, l -> l.onError(stepResult));
        }
        invokeVoidListener(listeners, l -> l.onError(stepResult));
    }

    private void callListenerOnError(final List<ConnectorListener> listeners, final ConnectorException error) {
        for (final ConnectorListener listenerSpi : getListeners()) {
            invokeVoidListener(listenerSpi, l -> l.onError(error));
        }
        invokeVoidListener(listeners, l -> l.onError(error));
    }


    // =========================================================================
    // CLOSE
    // =========================================================================

    @Override
    public void close() {

    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================

    @Override
    public String toString() {
        return new StringBuilder().append("HttpBasicConnector [")
                                  .append(configuration.getPartnerName())
                                  .append("]")
                                  .toString();
    }

    protected List<ConnectorListener> getListeners() {
        return Optional.ofNullable(configuration.getListeners()).orElse(List.of());
    }

    protected String buildFullUrl(final HttpRequest request) {
        StringBuilder result = new StringBuilder();
        if (configuration.getBaseUrl() != null) {
            if (configuration.getBaseUrl().endsWith(URL_SEPARATOR)) {
                result.append(configuration.getBaseUrl().substring(0, configuration.getBaseUrl().length() - 1));
            } else {
                result.append(configuration.getBaseUrl());
            }
        }
        if (request.getUrl() != null) {
            result.append(URL_SEPARATOR);
            if (request.getUrl().startsWith(URL_SEPARATOR)) {
                result.append(request.getUrl().substring(1));
            } else {
                result.append(request.getUrl());
            }
        }

        if (request.getOptions() != null) {
            result.append("?");

            final List<String> options = new ArrayList<>();
            for (String key : request.getOptions().keySet()) {
                final String value = request.getOptions().get(key);
                options.add(key + OPTION_VALUE_SEP + (value == null ? EMPTY : value));
            }
            result.append(String.join(OPTION_SEP, options));
        }
        return result.toString();
    }

    private RequestBody buildBody(final HttpRequest request) {
        return null;
    }


}
