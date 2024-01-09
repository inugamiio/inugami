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

import com.fasterxml.jackson.core.JsonProcessingException;
import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.interfaces.connectors.ConnectorListener;
import io.inugami.framework.interfaces.connectors.HttpProxy;
import io.inugami.framework.interfaces.connectors.HttpRequest;
import io.inugami.framework.interfaces.connectors.IHttpBasicConnector;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.connector.ConnectorNonSerializableBodyException;
import io.inugami.framework.interfaces.exceptions.connector.ConnectorRetryableException;
import io.inugami.framework.interfaces.exceptions.connector.ConnectorUndefinedCallException;
import io.inugami.framework.interfaces.exceptions.services.ConnectorException;
import io.inugami.framework.interfaces.functionnals.ConsumerWithException;
import io.inugami.framework.interfaces.functionnals.FunctionWithException;
import io.inugami.framework.interfaces.models.Tuple;
import io.inugami.framework.interfaces.models.tools.Chrono;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.spi.SpiLoader;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

import static io.inugami.framework.interfaces.connectors.ConnectorConstants.*;
import static io.inugami.framework.interfaces.connectors.exceptions.HttpError.REQUEST_REQUIRE;


/**
 * HttpConnector.
 *
 * @author patrick_guillerm
 * @since 26 oct. 2016
 */
@SuppressWarnings({"java:S3776", "java:S6355", "java:S1123", "java:S1168", "java:S108", "java:S1068", "java:S6353", "java:S135", "java:S1133", "java:S1450", "java:S1905"})
@Slf4j
public class HttpBasicConnector implements IHttpBasicConnector {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final List<ConnectorListener>            CONNECTOR_LISTENERS = SpiLoader.getInstance()
                                                                                           .loadSpiService(
                                                                                                   ConnectorListener.class);
    private final        PoolingHttpClientConnectionManager connectionManager   = new PoolingHttpClientConnectionManager();
    private final        int                                timeout;
    private final        String                             toStr;
    private final        int                                timeToLive;
    private final        int                                maxPerRoute;
    private final        int                                socketTimeout;
    private final        CloseableHttpClient                httpClient;
    private              RequestConfig                      requestConfig;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public HttpBasicConnector() {
        this(20000, 500, 50, 2, 60000);
    }

    public HttpBasicConnector(final int maxConnections) {
        this(20000, 500, maxConnections, 2, 60000);
    }

    public HttpBasicConnector(final int timeToLive, final int maxConnections) {
        this(20000, timeToLive, maxConnections, 2, 60000);
    }

    public HttpBasicConnector(final int timeout, final int timeToLive, final int maxConnections, final int maxPerRoute,
                              final int socketTimeout) {
        this.timeout = initIntValue(TIME_OUT_CONFIG, timeout);
        this.timeToLive = initIntValue(TIME_TO_LIVE_CONFIG, timeToLive);
        final int localmaxConnections = initIntValue(MAX_CONNEXIONS, maxConnections);
        this.maxPerRoute = initIntValue(MAX_PER_ROUTE, maxPerRoute);
        this.socketTimeout = initIntValue(SOCKET_TIMEOUT, socketTimeout);


        final StringBuilder str = new StringBuilder();
        str.append("timeout=").append(timeout);
        str.append(", timeToLive=").append(this.timeToLive);
        str.append(", maxConnections=").append(localmaxConnections);
        str.append(", maxPerRoute=").append(this.maxPerRoute);
        toStr = str.toString();

        connectionManager.setMaxTotal(localmaxConnections);
        connectionManager.setDefaultMaxPerRoute(maxPerRoute);
        httpClient = HttpBasicConnectorDelegateUtils.buildClient(timeout, socketTimeout);
    }


    private int initIntValue(final String timeOutConfig, final int defaultValue) {
        final int    result;
        final String value = System.getProperty(timeOutConfig);

        if ((value == null) || !Pattern.compile("[0-9]+").matcher(value).matches()) {
            result = defaultValue;
        } else {
            result = Integer.parseInt(value);
        }
        return result;
    }


    // =========================================================================
    // GET
    // =========================================================================
    @Override
    public HttpConnectorResult get(final HttpRequest request) throws ConnectorException {
        Asserts.assertNotNull(REQUEST_REQUIRE, request);
        return processGenericRequest(request.toBuilder().verb(HTTP_GET).body(null).build(), this::processGet);
    }

    private HttpConnectorResult processGet(final GenericRequestContext context) throws ConnectorException {
        final HttpConnectorResult.HttpConnectorResultBuilder resultBuilder = HttpConnectorResult.builder();
        resultBuilder.verb(HTTP_GET);
        resultBuilder.url(context.getRequest().getUrl());


        final HttpGet request = new HttpGet(context.getRequest().getUrl());
        HttpBasicConnectorDelegateUtils.defineRequestConfig(request, context.getRequest().getRequestConfig(),
                                                            request::setConfig);

        HttpBasicConnectorDelegateUtils.defineHearders(context.getRequest(),
                                                       (key, value) -> {
                                                           request.setHeader(key, value);
                                                           resultBuilder.addRequestHeader(key, value);
                                                       });

        invokeListenersOnProcessCalling(context.getRequest().getListener(), context.getRequest(),
                                        resultBuilder.build());
        final CloseableHttpResponse response = HttpBasicConnectorDelegateUtils.execute(request, context.getHttpclient(),
                                                                                       resultBuilder.build());

        resultBuilder.statusCode(response.getStatusLine().getStatusCode());
        resultBuilder.message(response.getStatusLine().getReasonPhrase());

        HttpBasicConnectorDelegateUtils.ReadResponseResultDTO responsePayload = null;
        try {
            responsePayload = HttpBasicConnectorDelegateUtils.readResponse(
                    response, context.getRequest().getUrl());
        } finally {
            HttpBasicConnectorDelegateUtils.close(response);
        }

        if (responsePayload != null) {
            resultBuilder.contentType(responsePayload.getContentType());
            resultBuilder.data(responsePayload.getData());
        }

        return resultBuilder.build();
    }

    // =========================================================================
    // POST
    // =========================================================================
    @Override
    public HttpConnectorResult post(final HttpRequest request) throws ConnectorException {
        Asserts.assertNotNull(REQUEST_REQUIRE, request);
        return processGenericRequest(request.toBuilder().verb(HTTP_POST).build(), this::processPost);
    }

    private HttpConnectorResult processPost(final GenericRequestContext context) throws ConnectorException {
        final var resultBuilder = HttpConnectorResult.builder();
        resultBuilder.addVerb(HTTP_POST).addUrl(context.getRequest().getUrl());

        final HttpPost request = new HttpPost(context.getRequest().getUrl());
        HttpBasicConnectorDelegateUtils.defineRequestConfig(request, context.getRequest().getRequestConfig(),
                                                            request::setConfig);
        HttpBasicConnectorDelegateUtils.defineHearders(context.getRequest(),
                                                       (key, value) -> {
                                                           request.setHeader(key, value);
                                                           resultBuilder.addRequestHeader(key, value);
                                                       });

        final HttpEntity bodyPayload = buildBodyPayload(context.getRequest());
        request.setEntity(bodyPayload);
        resultBuilder.addBodyData(convertToJson(bodyPayload));

        invokeListenersOnProcessCalling(context.getRequest().getListener(), context.getRequest(),
                                        resultBuilder.build());
        final CloseableHttpResponse response = HttpBasicConnectorDelegateUtils.execute(request, context.getHttpclient(),
                                                                                       resultBuilder.build());

        resultBuilder.addStatusCode(response.getStatusLine().getStatusCode());
        resultBuilder.addMessage(response.getStatusLine().getReasonPhrase());

        HttpBasicConnectorDelegateUtils.ReadResponseResultDTO responsePayload = null;
        try {
            responsePayload = HttpBasicConnectorDelegateUtils.readResponse(
                    response, context.getRequest().getUrl());
        } finally {
            HttpBasicConnectorDelegateUtils.close(response);
        }

        if (responsePayload != null) {
            resultBuilder.addContentType(responsePayload.getContentType());
            resultBuilder.addData(responsePayload.getData());
        }

        return resultBuilder.build();
    }


    private HttpEntity buildBodyPayload(final HttpRequest request) throws ConnectorNonSerializableBodyException {
        if (request.getHttpBody() != null) {
            return request.getHttpBody();
        } else if (request.getBody() != null) {
            final String json = serializeBodyToJson(request.getBody(), request.getListener());
            return new StringEntity(json, StandardCharsets.UTF_8);
        } else {
            return null;
        }
    }

    private String serializeBodyToJson(final Object body,
                                       final ConnectorListener listener) throws ConnectorNonSerializableBodyException {
        String result = callListenerSerializeToJson(body, listener);


        if (result == null) {
            try {
                result = JsonMarshaller.getInstance().getDefaultObjectMapper().writeValueAsString(body);
            } catch (final JsonProcessingException e) {
                throw new ConnectorNonSerializableBodyException(e.getMessage(), e);
            }
        }
        return result;
    }


    // =========================================================================
    // PUT
    // =========================================================================
    @Override
    public HttpConnectorResult put(final HttpRequest request) throws ConnectorException {
        Asserts.assertNotNull(REQUEST_REQUIRE, request);
        return processGenericRequest(request.toBuilder().verb(PUT).build(), this::processPut);
    }

    private HttpConnectorResult processPut(final GenericRequestContext context) throws ConnectorException {
        final var resultBuilder = HttpConnectorResult.builder();
        resultBuilder.addVerb(PUT).addUrl(context.getRequest().getUrl());

        final HttpPut request = new HttpPut(context.getRequest().getUrl());
        HttpBasicConnectorDelegateUtils.defineRequestConfig(request, context.getRequest().getRequestConfig(),
                                                            request::setConfig);
        HttpBasicConnectorDelegateUtils.defineHearders(context.getRequest(),
                                                       (key, value) -> {
                                                           request.setHeader(key, value);
                                                           resultBuilder.addRequestHeader(key, value);
                                                       });

        final HttpEntity bodyPayload = buildBodyPayload(context.getRequest());
        request.setEntity(bodyPayload);
        resultBuilder.addBodyData(convertToJson(bodyPayload));

        invokeListenersOnProcessCalling(context.getRequest().getListener(), context.getRequest(),
                                        resultBuilder.build());
        final CloseableHttpResponse response = HttpBasicConnectorDelegateUtils.execute(request, context.getHttpclient(),
                                                                                       resultBuilder.build());

        resultBuilder.addStatusCode(response.getStatusLine().getStatusCode());
        resultBuilder.addMessage(response.getStatusLine().getReasonPhrase());

        HttpBasicConnectorDelegateUtils.ReadResponseResultDTO responsePayload = null;
        try {
            responsePayload = HttpBasicConnectorDelegateUtils.readResponse(
                    response, context.getRequest().getUrl());
        } finally {
            HttpBasicConnectorDelegateUtils.close(response);
        }

        if (responsePayload != null) {
            resultBuilder.addContentType(responsePayload.getContentType());
            resultBuilder.addData(responsePayload.getData());
        }

        return resultBuilder.build();
    }


    private byte[] convertToJson(final HttpEntity bodyPayload) {
        if (bodyPayload == null) {
            return null;
        }

        if (bodyPayload instanceof StringEntity) {
            try {
                final StringWriter writer  = new StringWriter();
                final InputStream  content = ((StringEntity) bodyPayload).getContent();
                IOUtils.copy(content, writer, StandardCharsets.UTF_8);
                return writer.toString().getBytes(StandardCharsets.UTF_8);
            } catch (final IOException e) {
            }
        }

        return null;
    }

    // =========================================================================
    // PATCH
    // =========================================================================
    @Override
    public HttpConnectorResult patch(final HttpRequest request) throws ConnectorException {
        Asserts.assertNotNull(REQUEST_REQUIRE, request);
        return processGenericRequest(request.toBuilder().verb(PATCH).build(), this::processPatch);
    }

    private HttpConnectorResult processPatch(final GenericRequestContext context) throws ConnectorException {
        final var resultBuilder = HttpConnectorResult.builder();
        resultBuilder.addVerb(PATCH).addUrl(context.getRequest().getUrl());

        final HttpPatch request = new HttpPatch(context.getRequest().getUrl());
        HttpBasicConnectorDelegateUtils.defineRequestConfig(request, context.getRequest().getRequestConfig(),
                                                            request::setConfig);
        HttpBasicConnectorDelegateUtils.defineHearders(context.getRequest(),
                                                       (key, value) -> {
                                                           request.setHeader(key, value);
                                                           resultBuilder.addRequestHeader(key, value);
                                                       });

        final HttpEntity bodyPayload = buildBodyPayload(context.getRequest());
        request.setEntity(bodyPayload);
        resultBuilder.addBodyData(convertToJson(bodyPayload));

        invokeListenersOnProcessCalling(context.getRequest().getListener(), context.getRequest(),
                                        resultBuilder.build());
        final CloseableHttpResponse response = HttpBasicConnectorDelegateUtils.execute(request, context.getHttpclient(),
                                                                                       resultBuilder.build());

        resultBuilder.addStatusCode(response.getStatusLine().getStatusCode());
        resultBuilder.addMessage(response.getStatusLine().getReasonPhrase());

        HttpBasicConnectorDelegateUtils.ReadResponseResultDTO responsePayload = null;
        try {
            responsePayload = HttpBasicConnectorDelegateUtils.readResponse(
                    response, context.getRequest().getUrl());
        } finally {
            HttpBasicConnectorDelegateUtils.close(response);
        }

        if (responsePayload != null) {
            resultBuilder.addContentType(responsePayload.getContentType());
            resultBuilder.addData(responsePayload.getData());
        }

        return resultBuilder.build();
    }


    // =========================================================================
    // DELETE
    // =========================================================================
    @Override
    public HttpConnectorResult delete(final HttpRequest request) throws ConnectorException {
        Asserts.assertNotNull(REQUEST_REQUIRE, request);
        return processGenericRequest(request.toBuilder().verb(DELETE).build(), this::processDelete);
    }

    private HttpConnectorResult processDelete(final GenericRequestContext context) throws ConnectorException {
        final var resultBuilder = HttpConnectorResult.builder();
        resultBuilder.addVerb(DELETE).addUrl(context.getRequest().getUrl());

        final HttpDelete request = new HttpDelete(context.getRequest().getUrl());
        HttpBasicConnectorDelegateUtils.defineRequestConfig(request, context.getRequest().getRequestConfig(),
                                                            request::setConfig);
        HttpBasicConnectorDelegateUtils.defineHearders(context.getRequest(),
                                                       (key, value) -> request.setHeader(key, value));

        invokeListenersOnProcessCalling(context.getRequest().getListener(), context.getRequest(),
                                        resultBuilder.build());
        final CloseableHttpResponse response = HttpBasicConnectorDelegateUtils.execute(request, context.getHttpclient(),
                                                                                       resultBuilder.build());

        resultBuilder.addStatusCode(response.getStatusLine().getStatusCode());
        resultBuilder.addMessage(response.getStatusLine().getReasonPhrase());

        HttpBasicConnectorDelegateUtils.ReadResponseResultDTO responsePayload = null;
        try {
            responsePayload = HttpBasicConnectorDelegateUtils.readResponse(
                    response, context.getRequest().getUrl());
        } finally {
            HttpBasicConnectorDelegateUtils.close(response);
        }

        if (responsePayload != null) {
            resultBuilder.addContentType(responsePayload.getContentType());
            resultBuilder.addData(responsePayload.getData());
        }

        return resultBuilder.build();
    }


    // =========================================================================
    // OPTION
    // =========================================================================
    @Override
    public HttpConnectorResult option(final HttpRequest request) throws ConnectorException {
        Asserts.assertNotNull(REQUEST_REQUIRE, request);
        return processGenericRequest(request.toBuilder().verb(OPTION).body(null).build(), this::processOption);
    }

    private HttpConnectorResult processOption(final GenericRequestContext context) throws ConnectorException {
        final HttpConnectorResult.HttpConnectorResultBuilder resultBuilder = HttpConnectorResult.builder();
        resultBuilder.verb(DELETE).url(context.getRequest().getUrl());

        final HttpOptions request = new HttpOptions(context.getRequest().getUrl());
        HttpBasicConnectorDelegateUtils.defineRequestConfig(request, context.getRequest().getRequestConfig(),
                                                            request::setConfig);
        HttpBasicConnectorDelegateUtils.defineHearders(context.getRequest(),
                                                       (key, value) -> request.setHeader(key, value));

        invokeListenersOnProcessCalling(context.getRequest().getListener(), context.getRequest(),
                                        resultBuilder.build());
        final CloseableHttpResponse response = HttpBasicConnectorDelegateUtils.execute(request, context.getHttpclient(),
                                                                                       resultBuilder.build());

        resultBuilder.statusCode(response.getStatusLine().getStatusCode());
        resultBuilder.message(response.getStatusLine().getReasonPhrase());

        HttpBasicConnectorDelegateUtils.ReadResponseResultDTO responsePayload = null;
        try {
            responsePayload = HttpBasicConnectorDelegateUtils.readResponse(
                    response, context.getRequest().getUrl());
        } finally {
            HttpBasicConnectorDelegateUtils.close(response);
        }

        if (responsePayload != null) {
            resultBuilder.contentType(responsePayload.getContentType());
            resultBuilder.data(responsePayload.getData());
        }

        return resultBuilder.build();
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    @Getter
    @Builder(toBuilder = true)
    private static class GenericRequestContext {
        final HttpRequest         request;
        final CloseableHttpClient httpclient;
    }

    private HttpConnectorResult processGenericRequest(final HttpRequest request,
                                                      final FunctionWithException<GenericRequestContext, HttpConnectorResult, ConnectorException> function)
            throws ConnectorException {

        Asserts.assertNotNull(REQUEST_REQUIRE, request);

        final CloseableHttpClient httpclient =
                request.getHttpclient() == null ? HttpBasicConnectorDelegateUtils.buildClient(
                        timeout, socketTimeout) : request.getHttpclient();

        HttpConnectorResult stepResult = null;
        ConnectorException  exception  = null;
        final int           retry      = request.getNbRetry() <= 0 ? 0 : request.getNbRetry();
        Chrono              chrono     = null;

        HttpRequest currentRequest = request.toBuilder()
                                            .url(HttpBasicConnectorDelegateUtils.buildFullUrl(request))
                                            .httpBody(HttpBasicConnectorDelegateUtils.buildPayload(
                                                    request.getBody()))
                                            .build();

        if (!request.isDisableListener()) {
            currentRequest = callListenersOnBeforeCalling(currentRequest, request.getListener());
        }


        for (int i = retry; i >= 0; i--) {
            chrono = Chrono.startChrono();
            try {
                stepResult = function.process(GenericRequestContext.builder()
                                                                   .request(currentRequest)
                                                                   .httpclient(httpclient)
                                                                   .build());
                exception = null;
                if (!request.isDisableListener()) {
                    if (stepResult.getStatusCode() < 400) {
                        callListenerOnDone(request.getListener(), stepResult);
                        break;
                    } else {
                        callListenerOnError(request.getListener(), stepResult);
                    }
                }


            } catch (final ConnectorException error) {
                if (!request.isDisableListener()) {
                    callListenerOnError(request.getListener(), error);
                }

                if (error instanceof ConnectorRetryableException) {
                    exception = error;
                } else {
                    exception = error;
                    break;
                }
            } finally {
                chrono.stop();
            }
        }

        HttpConnectorResult.HttpConnectorResultBuilder resultBuilder = HttpConnectorResult.builder();
        if (stepResult != null) {
            resultBuilder = stepResult.toBuilder();
            resultBuilder.delay(chrono.getDuration());
        }


        if (exception != null || (exception == null && stepResult != null && stepResult.getStatusCode() >= 400)) {
            if (exception != null && stepResult != null && stepResult.getStatusCode() >= 400) {
                exception = new ConnectorException(stepResult.getStatusCode(), stepResult.getMessage(), exception);
            }

            if (request.isThrowable()) {
                throw exception == null ? new ConnectorException(500, "undefined error occurs") : exception;
            } else {
                resultBuilder.statusCode(exception instanceof ConnectorException
                                                 ? ((ConnectorException) exception).getCode()
                                                 : 500);
                resultBuilder.error(exception);
            }
        }
        return resultBuilder.build();
    }


    public void close(final CloseableHttpClient httpclient) throws ConnectorException {
        try {
            httpclient.close();
        } catch (final IOException e) {
            throw new ConnectorUndefinedCallException(e);
        }
    }


    // =========================================================================
    // LISTENER
    // =========================================================================
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
                                                     final ConnectorListener listener) {
        HttpRequest result = currentRequest;


        HttpRequest newRequest = invokeListener(listener, l -> l.beforeCalling(currentRequest));
        if (newRequest != null) {
            result = newRequest;
        }


        for (final ConnectorListener listenerSpi : CONNECTOR_LISTENERS) {
            try {
                newRequest = listenerSpi.beforeCalling(result);
                if (newRequest != null) {
                    result = newRequest;
                }
            } catch (final Throwable e) {
                log.error(e.getMessage(), e);
            }

        }

        return result;
    }


    private String callListenerSerializeToJson(final Object body, final ConnectorListener listener) {
        String result = invokeListener(listener, l -> l.serializeToJson(body));

        if (result == null) {
            for (final ConnectorListener listenerSpi : CONNECTOR_LISTENERS) {
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

    private void invokeListenersOnProcessCalling(final ConnectorListener listener,
                                                 final HttpRequest request,
                                                 final HttpConnectorResult result) {
        if (request.isDisableListener()) {
            return;
        }
        invokeListener(listener, l -> l.processCalling(request, result));
        for (final ConnectorListener listenerSpi : CONNECTOR_LISTENERS) {
            invokeListener(listenerSpi, l -> l.processCalling(request, result));
        }
    }

    private void callListenerOnDone(final ConnectorListener listener, final HttpConnectorResult stepResult) {

        if (listener != null) {
            invokeVoidListener(listener, l -> l.onDone(stepResult));
        }
        for (final ConnectorListener listenerSpi : CONNECTOR_LISTENERS) {
            invokeVoidListener(listenerSpi, l -> l.onDone(stepResult));
        }
    }

    private void callListenerOnError(final ConnectorListener listener, final HttpConnectorResult stepResult) {
        invokeVoidListener(listener, l -> l.onError(stepResult));
        for (final ConnectorListener listenerSpi : CONNECTOR_LISTENERS) {
            invokeVoidListener(listenerSpi, l -> l.onError(stepResult));
        }
    }

    private void callListenerOnError(final ConnectorListener listener, final ConnectorException error) {
        invokeVoidListener(listener, l -> l.onError(error));
        for (final ConnectorListener listenerSpi : CONNECTOR_LISTENERS) {
            invokeVoidListener(listenerSpi, l -> l.onError(error));
        }
    }

    // =========================================================================
    // EXCEPTION
    // =========================================================================
    public void setProxy(final HttpProxy proxyConfig) {
        if (proxyConfig != null) {
            final HttpHost proxy = new HttpHost(proxyConfig.getHost(), proxyConfig.getPort(),
                                                proxyConfig.getProtocol());
            // org.apache.http.client.config.RequestConfig
            requestConfig = RequestConfig.custom().setProxy(proxy).build();
        }
    }


    // =========================================================================
    // CLOSE
    // =========================================================================

    @Override
    public void close() {
        try {
            httpClient.close();
        } catch (final IOException e) {
            Loggers.IO.error(e.getMessage());
        }
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    @Override
    public String toString() {
        return new StringBuilder()
                .append("HttpBasicConnector [")
                .append(toStr)
                .append("]")
                .toString();
    }

    public static String buildRequest(final String baseUrl, final Tuple<String, String>[] parameters) {
        try {
            return HttpBasicConnectorDelegateUtils.buildRequest(baseUrl, parameters);
        } catch (final ConnectorException e) {
            throw new ConnectorFatalException(new ConnectorBadUrException(e));
        }
    }

    public static String buildRequest(final String baseUrl, final List<Tuple<String, String>> parameters) {
        return HttpBasicConnectorDelegateUtils.buildRequest(baseUrl, parameters);
    }


}
