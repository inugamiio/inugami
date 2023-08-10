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

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.services.ConnectorException;
import io.inugami.api.exceptions.services.exceptions.ConnectorBadUrException;
import io.inugami.api.exceptions.services.exceptions.ConnectorUndefinedCallException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.tools.Chrono;
import io.inugami.api.providers.concurrent.ThreadSleep;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.*;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.inugami.commons.connectors.ConnectorConstants.*;

@SuppressWarnings({"java:S6355", "java:S1123", "java:S107", "java:S107", "java:S2629", "java:S3824", "java:S1133"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Deprecated
public final class HttpBasicConnectorLegacy {
    // =========================================================================
    // GLOBAL VAUES
    // =========================================================================
    private static final Map<String, HttpRoute> ROUTES_URL = new ConcurrentHashMap<>();

    private static final ThreadSleep THREAD_SLEEP = new ThreadSleep(250);

    private static final PoolingHttpClientConnectionManager CONNECTION_MANAGER = new PoolingHttpClientConnectionManager();
    public static final  int                                MAX_PER_ROUTE      = 200;

    // =========================================================================
    // GET
    // =========================================================================
    @Deprecated
    static HttpConnectorResult get(final CloseableHttpClient httpClient,
                                   final String url,
                                   final int retry,
                                   final CredentialsProvider credentialsProvider,
                                   final Map<String, String> header) throws ConnectorException {
        appendUrlToPool(url);

        final URL realUrl = HttpBasicConnectorDelegateUtils.buildUrl(url);

        int                 nbTry  = retry <= 0 ? 1 : retry;
        Exception           error  = null;
        HttpConnectorResult result = null;

        do {
            try {
                result = processGet(httpClient, realUrl, credentialsProvider, header);
                break;
            } catch (final Exception e) {
                error = e;
                THREAD_SLEEP.sleep();
            } finally {
                nbTry--;
            }
        }
        while (nbTry >= 0);

        if (error != null) {
            throw new ConnectorException(error.getMessage(), error);
        }
        return result;
    }


    @Deprecated
    static HttpConnectorResult processGet(final CloseableHttpClient httpClient,
                                          final URL url,
                                          final CredentialsProvider credentialsProvider,
                                          final Map<String, String> header) throws ConnectorException {
        return processGet(httpClient, url, credentialsProvider, header, null);
    }

    @Deprecated
    static HttpConnectorResult processGet(final CloseableHttpClient httpClient,
                                          final URL url,
                                          final CredentialsProvider credentialsProvider,
                                          final Map<String, String> header,
                                          final RequestConfig requestConfig) throws ConnectorException {
        final HttpConnectorResultBuilder result = new HttpConnectorResultBuilder();
        Asserts.assertNotNull("url is required!", url);
        // verify url
        try {
            url.toURI();
        } catch (final URISyntaxException e) {
            throw new ConnectorBadUrException(e.getMessage(), e);
        }

        result.addUrl(url);
        result.addVerb(HTTP_GET);
        final HttpGet request = buildHttpGet(url);

        if (requestConfig != null) {
            request.setConfig(requestConfig);
        }

        if (credentialsProvider != null) {
            final String authentication = HttpBasicConnectorDelegateUtils.buildAuthentification(credentialsProvider);
            request.setHeader(HttpHeaders.AUTHORIZATION, authentication);
            result.addRequestHeader(HttpHeaders.AUTHORIZATION, authentication);
        }
        request.setHeader(HEADER_APPLICATION_NAME, CURRENT_APPLICATION_NAME);
        result.addRequestHeader(HEADER_APPLICATION_NAME, CURRENT_APPLICATION_NAME);
        request.setHeader(APPLICATION_HOSTNAME_HEADER, CURRENT_APPLICATION_HOSTNAME);
        result.addRequestHeader(APPLICATION_HOSTNAME_HEADER, CURRENT_APPLICATION_HOSTNAME);

        if (header != null) {
            for (final Map.Entry<String, String> entry : header.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
                result.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }

        final Chrono chrono = Chrono.startChrono();
        try {
            CloseableHttpResponse httpResult = null;
            result.addBegin();
            httpResult = httpClient.execute(request);
            result.addResponseAt();
            result.addStatusCode(httpResult.getStatusLine().getStatusCode());
            result.addMessage(httpResult.getStatusLine().getReasonPhrase());

            final HeaderIterator headers = httpResult.headerIterator();
            while (headers.hasNext()) {
                final Header currentHeader = headers.nextHeader();
                result.addResponseHeader(currentHeader.getName(), currentHeader.getValue());
            }

            final HttpEntity httpEntity = httpResult.getEntity();
            result.addContentType(httpEntity.getContentType().getValue());
            if (httpEntity.getContentEncoding() != null) {
                result.addEncoding(httpEntity.getContentEncoding().getValue());
            }

            HttpBasicConnectorDelegateUtils.assertDataLength(url.toString(), httpEntity.getContentLength());

            final InputStream content = httpEntity.getContent();
            result.addData(
                    HttpBasicConnectorDelegateUtils.readData(content, url.toString()));

        } catch (final Exception e) {
            handlingError("GET", String.valueOf(url), e);
            throw new ConnectorUndefinedCallException(e.getMessage(), e);
        } finally {
            request.releaseConnection();
            chrono.stop();
            Loggers.PARTNERLOG.info("[{}ms][GET] call request:{}", chrono.getDelaisInMillis(), url);
        }

        return result.build();
    }


    static HttpGet buildHttpGet(final URL url) throws ConnectorException {
        try {
            return new HttpGet(url.toURI());
        } catch (final URISyntaxException e) {
            throw new ConnectorBadUrException(e);
        }
    }


    // =========================================================================
    // POST
    // =========================================================================

    @Deprecated
    static HttpConnectorResult post(final CloseableHttpClient httpclient,
                                    final String url,
                                    final String jsonData,
                                    final CredentialsProvider credentialsProvider,
                                    final Map<String, String> header,
                                    final int timeout,
                                    final int socketTimeout,
                                    final RequestConfig requestConfig) throws ConnectorException {
        final CloseableHttpClient currentHttpclient = httpclient == null ? HttpBasicConnectorDelegateUtils.buildClient(
                timeout, socketTimeout) : httpclient;

        HttpConnectorResult result;
        final Chrono        chrono = Chrono.startChrono();

        try {
            result = processPost(url, jsonData, currentHttpclient, header, credentialsProvider, requestConfig);
        } catch (final IOException e) {
            handlingError("POST", url, e);
            throw new ConnectorUndefinedCallException(e);
        } finally {
            chrono.snapshot();
            Loggers.PARTNERLOG.info("[{}ms][POST] call request:{}", chrono.getDelaisInMillis(), url);
        }

        return result;
    }

    @Deprecated
    static HttpConnectorResult post(final CloseableHttpClient httpclient,
                                    final String url,
                                    final Map<String, String> urlEncodedData,
                                    final CredentialsProvider credentialsProvider,
                                    final Map<String, String> header,
                                    final int timeout,
                                    final int socketTimeout,
                                    final RequestConfig requestConfig) throws ConnectorException {
        final CloseableHttpClient currentHttpclient = httpclient == null ? HttpBasicConnectorDelegateUtils.buildClient(
                timeout, socketTimeout) : httpclient;
        HttpConnectorResult result;
        final Chrono        chrono = Chrono.startChrono();

        try {
            result = processPost(url, urlEncodedData, currentHttpclient, header, credentialsProvider, requestConfig);
        } finally {
            chrono.snapshot();
            Loggers.PARTNERLOG.info("[{}ms][POST] call request:{}", chrono.getDelaisInMillis(), url);
            HttpBasicConnectorDelegateUtils.close(httpclient);
        }

        return result;
    }


    @Deprecated
    static HttpConnectorResult processPost(final String url,
                                           final String jsonData,
                                           final CloseableHttpClient httpclient,
                                           final Map<String, String> header,
                                           final CredentialsProvider credentialsProvider,
                                           final RequestConfig requestConfig) throws ConnectorException,
                                                                                     IOException {
        final HttpConnectorResultBuilder resultBuilder = new HttpConnectorResultBuilder();

        final StringEntity json    = buildJsonEntry(jsonData);
        final URL          realUrl = HttpBasicConnectorDelegateUtils.buildUrl(url);
        resultBuilder.addUrl(realUrl);
        resultBuilder.addVerb(HTTP_POST);
        resultBuilder.addJsonInputData(jsonData);

        final HttpPost request = new HttpPost(realUrl.toString());
        if (requestConfig != null) {
            request.setConfig(requestConfig);
        }

        if (credentialsProvider != null) {
            final String authentication = HttpBasicConnectorDelegateUtils.buildAuthentification(credentialsProvider);
            request.setHeader(HttpHeaders.AUTHORIZATION, authentication);
        }

        request.addHeader(CONTENT_TYPE, APPLICATION_JSON);
        request.setHeader(HEADER_APPLICATION_NAME, CURRENT_APPLICATION_NAME);
        request.setHeader(APPLICATION_HOSTNAME_HEADER, CURRENT_APPLICATION_HOSTNAME);

        if (header != null) {
            for (final Map.Entry<String, String> entry : header.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }

        request.setEntity(json);
        if (Loggers.PARTNERLOG.isDebugEnabled()) {
            Loggers.PARTNERLOG.info("[POST] call request:{} - {}", url, jsonData);
        } else {
            Loggers.PARTNERLOG.info("POST to url : {}", url);
        }

        return executePost(request, httpclient, realUrl, url, resultBuilder);
    }


    @Deprecated
    static HttpConnectorResult processPost(final String url,
                                           final Map<String, String> urlEncodedData,
                                           final CloseableHttpClient httpclient,
                                           final Map<String, String> header,
                                           final CredentialsProvider credentialsProvider,
                                           final RequestConfig requestConfig) throws ConnectorException {
        final HttpConnectorResultBuilder resultBuilder = new HttpConnectorResultBuilder();

        final List<NameValuePair> bodyParams = new ArrayList<>();
        urlEncodedData.forEach((key, value) -> bodyParams.add(new BasicNameValuePair(key, value)));

        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(bodyParams);
        } catch (final UnsupportedEncodingException e) {
            Loggers.XLLOG.error("[POST] bodyParams:{}", bodyParams);
        }

        final URL realUrl = HttpBasicConnectorDelegateUtils.buildUrl(url);
        resultBuilder.addUrl(realUrl);
        resultBuilder.addVerb(HTTP_POST);

        final HttpPost request = new HttpPost(realUrl.toString());
        if (requestConfig != null) {
            request.setConfig(requestConfig);
        }

        if (credentialsProvider != null) {
            final String authentication = HttpBasicConnectorDelegateUtils.buildAuthentification(credentialsProvider);
            request.setHeader(HttpHeaders.AUTHORIZATION, authentication);
        }

        request.addHeader(CONTENT_TYPE, APPLICATION_FORM_URLENCODED);
        request.setHeader(HEADER_APPLICATION_NAME, CURRENT_APPLICATION_NAME);
        request.setHeader(APPLICATION_HOSTNAME_HEADER, CURRENT_APPLICATION_HOSTNAME);

        if (header != null) {
            for (final Map.Entry<String, String> entry : header.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }

        request.setEntity(urlEncodedFormEntity);

        Loggers.PARTNERLOG.info("[POST] call request:{} - {}", url, urlEncodedData);

        return executePost(request, httpclient, realUrl, url, resultBuilder);
    }

    @Deprecated
    static HttpConnectorResult executePost(final HttpPost request, final CloseableHttpClient httpClient,
                                           final URL realUrl, final String url,
                                           final HttpConnectorResultBuilder resultBuilder) {

        final HttpResponse response;
        try {
            response = httpClient.execute(request);
            HttpBasicConnectorDelegateUtils.assertResponseOk(response);
            resultBuilder.addStatusCode(response.getStatusLine().getStatusCode());
            resultBuilder.addMessage(response.getStatusLine().getReasonPhrase());

            HttpBasicConnectorDelegateUtils.assertDataLength(url, response.getEntity().getContentLength());

            if (response.getStatusLine().getStatusCode() >= MAX_PER_ROUTE && response.getStatusLine()
                                                                                     .getStatusCode() < 400) {
                final HttpEntity httpEntity = response.getEntity();
                resultBuilder.addContentType(httpEntity.getContentType().getValue());

                if (httpEntity.getContentEncoding() != null) {
                    resultBuilder.addEncoding(httpEntity.getContentEncoding().getValue());
                }

                final byte[] rawData = HttpBasicConnectorDelegateUtils.readData(response.getEntity().getContent(),
                                                                                realUrl.toString());
                resultBuilder.addData(rawData);
            }
        } catch (final IOException | ConnectorException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Loggers.PARTNERLOG.error("{} : {}", e.getMessage(), url);
        }

        return resultBuilder.build();
    }


    // =========================================================================
    // TOOLS
    // =========================================================================

    static synchronized void appendUrlToPool(final String url) {
        HttpRoute route = ROUTES_URL.get(url);
        if (route == null) {
            route = new HttpRoute(new HttpHost(url));
            CONNECTION_MANAGER.setMaxPerRoute(route, MAX_PER_ROUTE);
            ROUTES_URL.put(url, route);
        }
    }


    static StringEntity buildJsonEntry(final String data) throws UnsupportedEncodingException {
        return new StringEntity(data);
    }

    static void handlingError(final String verbe, final String url, final Exception e) {
        final String message = String.format("[%s] %s  (%s)", verbe, e.getMessage(), url);
        Loggers.DEBUG.error(message, e);
        Loggers.PARTNERLOG.error(message);
        Loggers.XLLOG.error(message);
    }
}
