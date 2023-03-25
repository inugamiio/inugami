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
package io.inugami.commons.connectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.services.ConnectorException;
import io.inugami.api.exceptions.services.exceptions.*;
import io.inugami.api.functionnals.ConsumerWithException;
import io.inugami.api.functionnals.FunctionWithException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.Tuple;
import io.inugami.api.models.tools.Chrono;
import io.inugami.api.spi.SpiLoader;
import io.inugami.commons.marshaling.JsonMarshaller;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static io.inugami.commons.connectors.ConnectorConstants.*;
import static io.inugami.commons.connectors.exceptions.HttpError.REQUEST_REQUIRE;

/**
 * HttpConnector.
 *
 * @author patrick_guillerm
 * @since 26 oct. 2016
 */
@Slf4j
public class HttpBasicConnector {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final List<ConnectorListener> CONNECTOR_LISTENERS = SpiLoader.getInstance().loadSpiService(
            ConnectorListener.class);

    private final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

    private final int timeout;

    private final String toStr;

    private final int timeToLive;

    private final Map<String, HttpRoute> routesUrl = new ConcurrentHashMap<>();

    private final int maxPerRoute;

    private final int socketTimeout;


    private final CloseableHttpClient httpClient;

    private RequestConfig requestConfig;


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
        this.timeout    = initIntValue(TIME_OUT_CONFIG, timeout);
        this.timeToLive = initIntValue(TIME_TO_LIVE_CONFIG, timeToLive);
        final int localmaxConnections = initIntValue(MAX_CONNEXIONS, maxConnections);
        this.maxPerRoute   = initIntValue(MAX_PER_ROUTE, maxPerRoute);
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
        }
        else {
            result = Integer.parseInt(value);
        }
        return result;
    }


    // =========================================================================
    // GET LEGACY
    // =========================================================================
    @Deprecated
    public HttpConnectorResult get(final String url, final int retry) throws ConnectorException {
        return HttpBasicConnectorLegacy.get(httpClient, url, retry, null, null);
    }

    @Deprecated
    public HttpConnectorResult get(final String url, final int retry,
                                   final CredentialsProvider credentialsProvider) throws ConnectorException {

        return HttpBasicConnectorLegacy.get(httpClient, url, retry, credentialsProvider, null);
    }


    @Deprecated
    public HttpConnectorResult get(final String url, final int retry, final CredentialsProvider credentialsProvider,
                                   final Map<String, String> header) throws ConnectorException {
        return HttpBasicConnectorLegacy.get(httpClient, url, retry, credentialsProvider, header);
    }

    @Deprecated
    public HttpConnectorResult get(final String url,
                                   final CredentialsProvider credentialsProvider) throws ConnectorException {
        return HttpBasicConnectorLegacy.get(httpClient, url, 0, credentialsProvider, null);
    }

    @Deprecated
    public HttpConnectorResult get(final String url, final CredentialsProvider credentialsProvider,
                                   final Map<String, String> header) throws ConnectorException {
        return HttpBasicConnectorLegacy.get(httpClient, url, 0, credentialsProvider, header);
    }

    @Deprecated
    public HttpConnectorResult get(final String url) throws ConnectorException {
        return HttpBasicConnectorLegacy.get(httpClient, url, 0, null, null);
    }

    @Deprecated
    public HttpConnectorResult get(final String url, final Map<String, String> header) throws ConnectorException {
        return HttpBasicConnectorLegacy.get(httpClient, url, 0, null, header);
    }

    @Deprecated
    protected HttpConnectorResult processGet(final URL url, final CredentialsProvider credentialsProvider,
                                             final Map<String, String> headers) throws ConnectorException {

        return HttpBasicConnectorLegacy.processGet(httpClient, url, credentialsProvider, headers);
    }


    // =========================================================================
    // POST
    // =========================================================================
    @Deprecated
    public HttpConnectorResult post(final String url, final String jsonData) throws ConnectorException {
        return HttpBasicConnectorLegacy.post(httpClient, url, jsonData, null, null, timeout, socketTimeout, null);
    }

    @Deprecated
    public HttpConnectorResult post(final String url, final String jsonData,
                                    final Map<String, String> header) throws ConnectorException {
        return HttpBasicConnectorLegacy.post(httpClient, url, jsonData, null, header, timeout, socketTimeout, null);
    }

    @Deprecated
    public HttpConnectorResult post(final String url,
                                    final Map<String, String> urlEncodedData) throws ConnectorException {
        return HttpBasicConnectorLegacy.post(httpClient, url, urlEncodedData, null, null, timeout, socketTimeout, null);
    }

    @Deprecated
    public HttpConnectorResult post(final String url, final Map<String, String> urlEncodedData,
                                    final Map<String, String> header) throws ConnectorException {
        return HttpBasicConnectorLegacy.post(httpClient, url, urlEncodedData, null, header, timeout, socketTimeout,
                                             null);
    }

    @Deprecated
    public HttpConnectorResult post(final String url, final String jsonData,
                                    final CredentialsProvider credentialsProvider,
                                    final Map<String, String> header) throws ConnectorException {
        return HttpBasicConnectorLegacy.post(httpClient, url, jsonData, credentialsProvider, header, timeout,
                                             socketTimeout, null);
    }

    @Deprecated
    public HttpConnectorResult post(final String url, final Map<String, String> urlEncodedData,
                                    final CredentialsProvider credentialsProvider,
                                    final Map<String, String> header) throws ConnectorException {
        return HttpBasicConnectorLegacy.post(httpClient, url, urlEncodedData, credentialsProvider, header, timeout,
                                             socketTimeout, null);
    }


    // =========================================================================
    // GET
    // =========================================================================
    public HttpConnectorResult get(final HttpRequest request) throws ConnectorException {
        Asserts.notNull(REQUEST_REQUIRE, request);
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
        CloseableHttpResponse response = HttpBasicConnectorDelegateUtils.execute(request, context.getHttpclient(),
                                                                                 resultBuilder.build());

        resultBuilder.statusCode(response.getStatusLine().getStatusCode());
        resultBuilder.message(response.getStatusLine().getReasonPhrase());

        HttpBasicConnectorDelegateUtils.ReadResponseResultDTO responsePayload = null;
        try {
            responsePayload = HttpBasicConnectorDelegateUtils.readResponse(
                    response, context.getRequest().getUrl());
        }
        finally {
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
    public HttpConnectorResult post(final HttpRequest request) throws ConnectorException {
        Asserts.notNull(REQUEST_REQUIRE, request);
        return processGenericRequest(request.toBuilder().verb(HTTP_POST).build(), this::processPost);
    }

    private HttpConnectorResult processPost(final GenericRequestContext context) throws ConnectorException {
        final HttpConnectorResultBuilder resultBuilder = new HttpConnectorResultBuilder();
        resultBuilder.addVerb(HTTP_POST).addUrl(context.getRequest().getUrl());

        final HttpPost request = new HttpPost(context.getRequest().getUrl());
        HttpBasicConnectorDelegateUtils.defineRequestConfig(request, context.getRequest().getRequestConfig(),
                                                            request::setConfig);
        HttpBasicConnectorDelegateUtils.defineHearders(context.getRequest(),
                                                       (key, value) -> {
                                                           request.setHeader(key, value);
                                                           resultBuilder.addRequestHeader(key, value);
                                                       });

        HttpEntity bodyPayload = buildBodyPayload(context.getRequest());
        request.setEntity(bodyPayload);
        resultBuilder.addBodyData(convertToJson(bodyPayload));

        invokeListenersOnProcessCalling(context.getRequest().getListener(), context.getRequest(),
                                        resultBuilder.build());
        CloseableHttpResponse response = HttpBasicConnectorDelegateUtils.execute(request, context.getHttpclient(),
                                                                                 resultBuilder.build());

        resultBuilder.addStatusCode(response.getStatusLine().getStatusCode());
        resultBuilder.addMessage(response.getStatusLine().getReasonPhrase());

        HttpBasicConnectorDelegateUtils.ReadResponseResultDTO responsePayload = null;
        try {
            responsePayload = HttpBasicConnectorDelegateUtils.readResponse(
                    response, context.getRequest().getUrl());
        }
        finally {
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
        }
        else if (request.getBody() != null) {
            String json = serializeBodyToJson(request.getBody(), request.getListener());
            return new StringEntity(json, StandardCharsets.UTF_8);
        }
        else {
            return null;
        }
    }

    private String serializeBodyToJson(final Object body,
                                       final ConnectorListener listener) throws ConnectorNonSerializableBodyException {
        String result = callListenerSerializeToJson(body, listener);


        if (result == null) {
            try {
                result = JsonMarshaller.getInstance().getDefaultObjectMapper().writeValueAsString(body);
            }
            catch (JsonProcessingException e) {
                throw new ConnectorNonSerializableBodyException(e.getMessage(), e);
            }
        }
        return result;
    }


    // =========================================================================
    // PUT
    // =========================================================================
    public HttpConnectorResult put(final HttpRequest request) throws ConnectorException {
        Asserts.notNull(REQUEST_REQUIRE, request);
        return processGenericRequest(request.toBuilder().verb(PUT).build(), this::processPut);
    }

    private HttpConnectorResult processPut(final GenericRequestContext context) throws ConnectorException {
        final HttpConnectorResultBuilder resultBuilder = new HttpConnectorResultBuilder();
        resultBuilder.addVerb(PUT).addUrl(context.getRequest().getUrl());

        final HttpPut request = new HttpPut(context.getRequest().getUrl());
        HttpBasicConnectorDelegateUtils.defineRequestConfig(request, context.getRequest().getRequestConfig(),
                                                            request::setConfig);
        HttpBasicConnectorDelegateUtils.defineHearders(context.getRequest(),
                                                       (key, value) -> {
                                                           request.setHeader(key, value);
                                                           resultBuilder.addRequestHeader(key, value);
                                                       });

        HttpEntity bodyPayload = buildBodyPayload(context.getRequest());
        request.setEntity(bodyPayload);
        resultBuilder.addBodyData(convertToJson(bodyPayload));

        invokeListenersOnProcessCalling(context.getRequest().getListener(), context.getRequest(),
                                        resultBuilder.build());
        CloseableHttpResponse response = HttpBasicConnectorDelegateUtils.execute(request, context.getHttpclient(),
                                                                                 resultBuilder.build());

        resultBuilder.addStatusCode(response.getStatusLine().getStatusCode());
        resultBuilder.addMessage(response.getStatusLine().getReasonPhrase());

        HttpBasicConnectorDelegateUtils.ReadResponseResultDTO responsePayload = null;
        try {
            responsePayload = HttpBasicConnectorDelegateUtils.readResponse(
                    response, context.getRequest().getUrl());
        }
        finally {
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
                StringWriter      writer  = new StringWriter();
                final InputStream content = ((StringEntity) bodyPayload).getContent();
                IOUtils.copy(content, writer, StandardCharsets.UTF_8);
                return writer.toString().getBytes(StandardCharsets.UTF_8);
            }
            catch (IOException e) {
            }
        }

        return null;
    }

    // =========================================================================
    // PATCH
    // =========================================================================
    public HttpConnectorResult patch(final HttpRequest request) throws ConnectorException {
        Asserts.notNull(REQUEST_REQUIRE, request);
        return processGenericRequest(request.toBuilder().verb(PATCH).build(), this::processPatch);
    }

    private HttpConnectorResult processPatch(final GenericRequestContext context) throws ConnectorException {
        final HttpConnectorResultBuilder resultBuilder = new HttpConnectorResultBuilder();
        resultBuilder.addVerb(PATCH).addUrl(context.getRequest().getUrl());

        final HttpPatch request = new HttpPatch(context.getRequest().getUrl());
        HttpBasicConnectorDelegateUtils.defineRequestConfig(request, context.getRequest().getRequestConfig(),
                                                            request::setConfig);
        HttpBasicConnectorDelegateUtils.defineHearders(context.getRequest(),
                                                       (key, value) -> {
                                                           request.setHeader(key, value);
                                                           resultBuilder.addRequestHeader(key, value);
                                                       });

        HttpEntity bodyPayload = buildBodyPayload(context.getRequest());
        request.setEntity(bodyPayload);
        resultBuilder.addBodyData(convertToJson(bodyPayload));

        invokeListenersOnProcessCalling(context.getRequest().getListener(), context.getRequest(),
                                        resultBuilder.build());
        CloseableHttpResponse response = HttpBasicConnectorDelegateUtils.execute(request, context.getHttpclient(),
                                                                                 resultBuilder.build());

        resultBuilder.addStatusCode(response.getStatusLine().getStatusCode());
        resultBuilder.addMessage(response.getStatusLine().getReasonPhrase());

        HttpBasicConnectorDelegateUtils.ReadResponseResultDTO responsePayload = null;
        try {
            responsePayload = HttpBasicConnectorDelegateUtils.readResponse(
                    response, context.getRequest().getUrl());
        }
        finally {
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
    public HttpConnectorResult delete(final HttpRequest request) throws ConnectorException {
        Asserts.notNull(REQUEST_REQUIRE, request);
        return processGenericRequest(request.toBuilder().verb(DELETE).build(), this::processDelete);
    }

    private HttpConnectorResult processDelete(final GenericRequestContext context) throws ConnectorException {
        final HttpConnectorResultBuilder resultBuilder = new HttpConnectorResultBuilder();
        resultBuilder.addVerb(DELETE).addUrl(context.getRequest().getUrl());

        final HttpDelete request = new HttpDelete(context.getRequest().getUrl());
        HttpBasicConnectorDelegateUtils.defineRequestConfig(request, context.getRequest().getRequestConfig(),
                                                            request::setConfig);
        HttpBasicConnectorDelegateUtils.defineHearders(context.getRequest(),
                                                       (key, value) -> request.setHeader(key, value));

        invokeListenersOnProcessCalling(context.getRequest().getListener(), context.getRequest(),
                                        resultBuilder.build());
        CloseableHttpResponse response = HttpBasicConnectorDelegateUtils.execute(request, context.getHttpclient(),
                                                                                 resultBuilder.build());

        resultBuilder.addStatusCode(response.getStatusLine().getStatusCode());
        resultBuilder.addMessage(response.getStatusLine().getReasonPhrase());

        HttpBasicConnectorDelegateUtils.ReadResponseResultDTO responsePayload = null;
        try {
            responsePayload = HttpBasicConnectorDelegateUtils.readResponse(
                    response, context.getRequest().getUrl());
        }
        finally {
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
    public HttpConnectorResult option(final HttpRequest request) throws ConnectorException {
        Asserts.notNull(REQUEST_REQUIRE, request);
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
        CloseableHttpResponse response = HttpBasicConnectorDelegateUtils.execute(request, context.getHttpclient(),
                                                                                 resultBuilder.build());

        resultBuilder.statusCode(response.getStatusLine().getStatusCode());
        resultBuilder.message(response.getStatusLine().getReasonPhrase());

        HttpBasicConnectorDelegateUtils.ReadResponseResultDTO responsePayload = null;
        try {
            responsePayload = HttpBasicConnectorDelegateUtils.readResponse(
                    response, context.getRequest().getUrl());
        }
        finally {
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

        Asserts.notNull(REQUEST_REQUIRE, request);

        final CloseableHttpClient httpclient = request.getHttpclient() == null ? HttpBasicConnectorDelegateUtils.buildClient(
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

        currentRequest = callListenersOnBeforeCalling(currentRequest, request.getListener());


        for (int i = retry; i >= 0; i--) {
            try {
                chrono     = Chrono.startChrono();
                stepResult = function.process(GenericRequestContext.builder()
                                                                   .request(currentRequest)
                                                                   .httpclient(httpclient)
                                                                   .build());
                exception  = null;
                if (stepResult.getStatusCode() < 400) {
                    callListenerOnDone(request.getListener(), stepResult);
                    break;
                }
                else {
                    callListenerOnError(request.getListener(), stepResult);
                }

            }
            catch (ConnectorException error) {
                callListenerOnError(request.getListener(), error);

                if (error instanceof ConnectorRetryableException) {
                    exception = error;
                }
                else {
                    exception = error;
                    break;
                }
            }
            finally {
                chrono.stop();
            }
        }

        HttpConnectorResult.HttpConnectorResultBuilder resultBuilder = HttpConnectorResult.builder();
        if (stepResult != null) {
            resultBuilder = stepResult.toBuilder();
            resultBuilder.delais(chrono.getDelaisInMillis());
        }


        if (exception != null || (exception == null && stepResult != null && stepResult.getStatusCode() >= 400)) {
            if (exception != null && stepResult != null && stepResult.getStatusCode() >= 400) {
                exception = new ConnectorException(stepResult.getStatusCode(), stepResult.getMessage(), exception);
            }

            if (request.isThrowable()) {
                throw exception;
            }
            else {
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
        }
        catch (final IOException e) {
            throw new ConnectorUndefinedCallException(e);
        }
    }


    // =========================================================================
    // BUILDERS
    // =========================================================================
    private StringEntity buildJsonEntry(final String data) throws UnsupportedEncodingException {
        return new StringEntity(data);
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
            }
            catch (Exception e) {
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
            }
            catch (Exception e) {
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


        for (ConnectorListener listenerSpi : CONNECTOR_LISTENERS) {
            try {
                newRequest = listenerSpi.beforeCalling(result);
                if (newRequest != null) {
                    result = newRequest;
                }
            }
            catch (Throwable e) {
                log.error(e.getMessage(), e);
            }

        }

        return result;
    }


    private String callListenerSerializeToJson(final Object body, final ConnectorListener listener) {
        String result = invokeListener(listener, l -> l.serializeToJson(body));

        if (result == null) {
            for (ConnectorListener listenerSpi : CONNECTOR_LISTENERS) {
                try {
                    result = listenerSpi.serializeToJson(body);
                    if (result != null) {
                        break;
                    }
                }
                catch (Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        return result;
    }

    private void invokeListenersOnProcessCalling(final ConnectorListener listener,
                                                 final HttpRequest request,
                                                 final HttpConnectorResult result) {
        invokeListener(listener, l -> l.processCalling(request, result));
        for (ConnectorListener listenerSpi : CONNECTOR_LISTENERS) {
            invokeListener(listenerSpi, l -> l.processCalling(request, result));
        }
    }

    private void callListenerOnDone(final ConnectorListener listener, final HttpConnectorResult stepResult) {
        if (listener != null) {
            invokeVoidListener(listener, l -> l.onDone(stepResult));
        }
        for (ConnectorListener listenerSpi : CONNECTOR_LISTENERS) {
            invokeVoidListener(listenerSpi, l -> l.onDone(stepResult));
        }
    }

    private void callListenerOnError(final ConnectorListener listener, final HttpConnectorResult stepResult) {
        invokeVoidListener(listener, l -> l.onError(stepResult));
        for (ConnectorListener listenerSpi : CONNECTOR_LISTENERS) {
            invokeVoidListener(listenerSpi, l -> l.onError(stepResult));
        }
    }

    private void callListenerOnError(final ConnectorListener listener, final ConnectorException error) {
        invokeVoidListener(listener, l -> l.onError(error));
        for (ConnectorListener listenerSpi : CONNECTOR_LISTENERS) {
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


    public void close() {
        try {
            httpClient.close();
        }
        catch (final IOException e) {
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
        }
        catch (ConnectorException e) {
            throw new ConnectorFatalException(new ConnectorBadUrException(e));
        }
    }

    public static String buildRequest(final String baseUrl, final List<Tuple<String, String>> parameters) {
        try {
            return HttpBasicConnectorDelegateUtils.buildRequest(baseUrl, parameters);
        }
        catch (ConnectorException e) {
            throw new ConnectorFatalException(new ConnectorBadUrException(e));
        }
    }


}
