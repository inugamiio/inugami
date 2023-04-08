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
import io.inugami.api.exceptions.FatalException;
import io.inugami.api.exceptions.services.ConnectorException;
import io.inugami.api.exceptions.services.exceptions.*;
import io.inugami.api.exceptions.tools.StrategyException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.Tuple;
import io.inugami.api.models.tools.DefaultStrategy;
import io.inugami.api.models.tools.Strategy;
import io.inugami.api.monitoring.MdcService;
import io.inugami.commons.marshaling.JsonMarshaller;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProxySelector;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class HttpBasicConnectorDelegateUtils {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final  String  EQUALS               = "=";
    public static final  String  URL_OPITON_SEPARATOR = "?";
    public static final  String  AND                  = "&";
    public static final  String  AUTHORIZATION        = "Authorization";
    public static final  String  BASIC_AUTH_SEPARATOR = ":";
    public static final  String  BASIC                = "Basic ";
    private static final Charset CHARSET_US_ASCII     = Charset.forName("US-ASCII");
    private static final int     BUFFER_SIZE          = 1000 * 1024;
    private static final int     LOG_STEP             = 10 * 1024;

    private static final List<Strategy<StatusLine, ConnectorException>> EXCEPTIONS_STRATEGIES =
            List.of(
                    new DefaultStrategy<>(
                            statusLine -> statusLine.getStatusCode() == 401,
                            statusLine -> new ConnectorUnauthorizedException(statusLine.getStatusCode(),
                                                                             statusLine.getReasonPhrase())
                    ),
                    new DefaultStrategy<>(
                            statusLine -> statusLine.getStatusCode() == 403,
                            statusLine -> new ConnectorForbiddenException(statusLine.getStatusCode(),
                                                                          statusLine.getReasonPhrase())
                    ),
                    new DefaultStrategy<>(
                            statusLine -> statusLine.getStatusCode() > 404 && statusLine.getStatusCode() < 500,
                            statusLine -> new ConnectorBadRequestException(statusLine.getStatusCode(),
                                                                           statusLine.getReasonPhrase())
                    ),
                    new DefaultStrategy<>(
                            statusLine -> statusLine.getStatusCode() == 502,
                            statusLine -> new ConnectorBadGatewayException(statusLine.getStatusCode(),
                                                                           statusLine.getReasonPhrase())
                    ),
                    new DefaultStrategy<>(
                            statusLine -> statusLine.getStatusCode() == 503,
                            statusLine -> new ConnectorServiceUnavailablException(statusLine.getStatusCode(),
                                                                                  statusLine.getReasonPhrase())
                    )
            );

    // =========================================================================
    // BUILD CLIENT
    // =========================================================================
    static CloseableHttpClient buildClient(final int timeout,
                                           final int socketTimeout) {
        final CloseableHttpClient result;
        try {
            result = buildHttpClient(timeout, socketTimeout);
        } catch (final KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new FatalException(e.getMessage(), e);
        }
        return result;
    }

    static CloseableHttpClient buildHttpClient(final int timeout,
                                               final int socketTimeout) throws KeyManagementException, NoSuchAlgorithmException,
                                                                               KeyStoreException {
        final CloseableHttpClient result;
        //@formatter:off
        final RequestConfig.Builder requestBuilder = RequestConfig.custom()
                                                                  .setConnectTimeout(timeout)
                                                                  .setConnectionRequestTimeout(timeout)
                                                                  .setSocketTimeout(socketTimeout);

        final SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true).setTcpNoDelay(true).build();
        //@formatter:on

        final SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true);

        final HttpClientBuilder httpClientsCustom = HttpClients.custom();
        httpClientsCustom.setDefaultRequestConfig(requestBuilder.build());
        httpClientsCustom.setRoutePlanner(new SystemDefaultRoutePlanner(ProxySelector.getDefault()));

        httpClientsCustom.setSSLSocketFactory(new SSLConnectionSocketFactory(builder.build(),
                                                                             SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));

        builder.loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(final X509Certificate[] chain, final String authType) {
                return true;
            }
        });
        final SSLContext sslContext = builder.build();
        final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {
            @Override
            public void verify(final String host, final SSLSocket ssl) throws IOException {
            }

            @Override
            public void verify(final String host, final X509Certificate cert) throws SSLException {
            }

            @Override
            public void verify(final String host, final String[] cns, final String[] subjectAlts) throws SSLException {
            }

            @Override
            public boolean verify(final String s, final SSLSession sslSession) {
                return true;
            }
        });

        final ConnectionSocketFactory defaultConnection = new PlainConnectionSocketFactory();
        final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                                                                                       .register("http",
                                                                                                 defaultConnection)
                                                                                       .register("https",
                                                                                                 sslsf).build();

        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);

        httpClientsCustom.setConnectionManager(connectionManager);
        httpClientsCustom.setDefaultSocketConfig(socketConfig);

        result = httpClientsCustom.build();
        return result;
    }


    // =========================================================================
    // BUILDER
    // =========================================================================
    static String buildRequest(final String baseUrl,
                               final Tuple<String, String>... parameters) throws ConnectorException {
        return buildRequest(baseUrl, Arrays.asList(parameters));
    }

    static URL buildUrl(final String url) throws ConnectorException {
        URL result = null;
        try {
            result = new URL(url);
        } catch (final MalformedURLException e) {
            throw new ConnectorBadUrException(e);
        }
        return result;
    }


    static String buildRequest(final String baseUrl,
                               final List<Tuple<String, String>> parameters) throws ConnectorException {
        Asserts.assertNotNull("Request base url mustn't be null!", baseUrl);
        final StringBuilder result = new StringBuilder();

        result.append(baseUrl);

        final int size = parameters.size();
        if (size > 0) {
            result.append('?');
        }

        for (int i = 0; i < size; i++) {
            final Tuple<String, String> param = parameters.get(i);
            if (i != 0) {
                result.append('&');
            }
            result.append(param.getKey());
            result.append('=');
            result.append(URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8));
        }

        return result.toString();
    }


    static String buildFullUrl(final HttpRequest request) {
        if (request == null || request.getUrl() == null) {
            return null;
        }
        final StringBuilder url = new StringBuilder(request.getUrl());

        if (request.getOptions() != null && !request.getOptions().isEmpty()) {
            if (!request.getUrl().contains(URL_OPITON_SEPARATOR)) {
                url.append(URL_OPITON_SEPARATOR);
            }
            final Iterator<String> keys = request.getOptions().keySet().iterator();
            while (keys.hasNext()) {
                final String key   = keys.next();
                final String entry = request.getOptions().get(key);
                url.append(key).append(EQUALS);
                url.append(encodeUrlValue(entry));
                if (keys.hasNext()) {
                    url.append(AND);
                }
            }
        }

        return url.toString();
    }

    static String encodeUrlValue(final String entry) {
        return URLEncoder.encode(entry, StandardCharsets.UTF_8);
    }


    static void defineHearders(final HttpRequest request, final BiConsumer<String, String> consumer) {
        if (request == null || consumer == null) {
            return;
        }


        final Map<String, String> tracking = MdcService.getInstance().getTrackingInformation();
        for (final Map.Entry<String, String> trackingEntry : tracking.entrySet()) {
            consumer.accept(trackingEntry.getKey(), trackingEntry.getValue());
        }

        if (request.getHeaders() != null) {
            for (final Map.Entry<String, String> headerEntry : request.getHeaders().entrySet()) {
                consumer.accept(headerEntry.getKey(), headerEntry.getValue());
            }
        }


        if (request.getCredentialsProvider() != null || request.getToken() != null) {
            if (request.getToken() == null) {
                consumer.accept(AUTHORIZATION, buildAuthentification(request.getCredentialsProvider()));
            } else {
                consumer.accept(AUTHORIZATION, request.getToken());
            }
        }

    }

    static String buildAuthentification(final CredentialsProvider credentialsProvider) {
        final Credentials credentials = credentialsProvider.getCredentials(AuthScope.ANY);
        //@formatter:off
        final String auth = new StringBuilder(credentials.getUserPrincipal().getName())
                .append(BASIC_AUTH_SEPARATOR)
                .append(credentials.getPassword())
                .toString();
        //@formatter:on
        return BASIC + new String(Base64.encodeBase64(auth.getBytes(CHARSET_US_ASCII)));
    }

    static HttpEntity buildPayload(final Object body) throws ConnectorMarshallingException {
        String json = null;

        if (body != null) {
            if (body instanceof String) {
                json = (String) body;
            } else {
                try {
                    json = JsonMarshaller.getInstance().getDefaultObjectMapper().writeValueAsString(body);
                } catch (final JsonProcessingException e) {
                    throw new ConnectorMarshallingException(e.getMessage(), e);
                }
            }
        }
        return null;
    }


    // =========================================================================
    // EXECUTE REQUEST
    // =========================================================================
    static CloseableHttpResponse execute(final HttpRequestBase request,
                                         final CloseableHttpClient httpclient,
                                         final HttpConnectorResult result) throws ConnectorException {
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(request);
        } catch (final IOException e) {
            final ConnectorUndefinedCallException exception = new ConnectorUndefinedCallException(e.getMessage(), e);
            exception.setResult(result);
            throw exception;
        }


        final ConnectorException exception = resolveException(response.getStatusLine(), result);
        if (exception != null) {
            throw exception;
        }
        return response;
    }

    public static ConnectorException resolveException(final StatusLine httpCode, final HttpConnectorResult httpResult) {
        final ConnectorException result = resolveException(StatusLineDTO.builder()
                                                                        .statusCode(httpCode.getStatusCode())
                                                                        .reasonPhrase(httpCode.getReasonPhrase())
                                                                        .build());

        if (result != null) {
            result.setResult(httpResult);
        }

        return result;
    }

    public static ConnectorException resolveException(final StatusLine statusLine) {
        if (statusLine == null) {
            return null;
        }

        for (final Strategy<StatusLine, ConnectorException> strategy : EXCEPTIONS_STRATEGIES) {
            if (strategy.accept(statusLine)) {
                try {
                    return strategy.process(statusLine);
                } catch (final StrategyException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }

    public static void defineRequestConfig(final HttpRequestBase request,
                                           final RequestConfig requestConfig,
                                           final Consumer<RequestConfig> setter) {
        if (request != null && requestConfig != null && setter != null) {
            setter.accept(requestConfig);
        }
    }


    @Getter
    @Builder(toBuilder = true)
    @AllArgsConstructor
    public static class StatusLineDTO implements StatusLine {
        private final ProtocolVersion protocolVersion;
        private final int             statusCode;
        private final String          reasonPhrase;
    }

    // =========================================================================
    // READ RESPONSE
    // =========================================================================
    static ReadResponseResultDTO readResponse(final CloseableHttpResponse response,
                                              final String url) throws ConnectorNoReadableException {
        final ReadResponseResultDTO.ReadResponseResultDTOBuilder result     = ReadResponseResultDTO.builder();
        final HttpEntity                                         httpEntity = response == null ? null : response.getEntity();

        if (httpEntity == null) {
            return result.build();
        }

        result.contentType(httpEntity.getContentType().getValue());


        final int contentLength = Long.valueOf(response.getEntity().getContentLength()).intValue();
        byte[]    rawData       = null;
        try {
            rawData = readData(response.getEntity().getContent(), contentLength, url);
        } catch (final IOException e) {
            throw new ConnectorNoReadableException(e.getMessage(), e);
        }
        result.data(rawData);

        return result.build();
    }

    @Builder(toBuilder = true)
    @Getter
    @AllArgsConstructor
    public static class ReadResponseResultDTO {
        private final byte[] data;
        private final String contentType;
    }


    static byte[] readData(final InputStream inputStream,
                           final long dataLength,
                           final String url) throws ConnectorNoReadableException {
        byte[] result = null;

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final byte[]                buffer = new byte[BUFFER_SIZE];
        int                         length = 0;
        int                         size   = 0;
        try {
            while ((length = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, length);
                size = size + length;

                if (log.isDebugEnabled() && (size % LOG_STEP) == 0) {
                    log.debug("reading result ... {} - {}", size, url);
                }
            }
        } catch (final IOException e) {
            throw new ConnectorNoReadableException(e.getMessage(), e);
        }
        result = output.toByteArray();

        return result;
    }

    // =========================================================================
    // CLOSE
    // =========================================================================
    static void close(final CloseableHttpResponse response) {
        if (response != null) {
            try {
                response.close();
            } catch (final IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    static void close(final Closeable client) {
        if (client != null) {
            try {
                client.close();
            } catch (final IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    // =========================================================================
    // TOOLS
    // =========================================================================
    static void assertDataLength(final String url,
                                 final long contentLength) throws ConnectorException {
        if (contentLength > Integer.MAX_VALUE) {
            final String message = String.format("response grab to many bytes! (%s)", url);
            Loggers.PARTNERLOG.error(message);
            throw new ConnectorBadDataException(message);
        }
    }

    static void assertResponseOk(final HttpResponse response) throws ConnectorException {
        Asserts.assertNotNull(response);
        final int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode >= 400) {
            final ConnectorException exception = resolveException(response.getStatusLine());
            if (exception != null) {
                throw exception;
            }
        }
    }

}
