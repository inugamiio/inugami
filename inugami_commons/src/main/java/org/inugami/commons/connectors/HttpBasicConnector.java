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
package org.inugami.commons.connectors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProxySelector;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.inugami.api.constants.JvmKeyValues;
import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.FatalException;
import org.inugami.api.exceptions.services.ConnectorException;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.models.Tuple;
import org.inugami.api.models.tools.Chrono;
import org.inugami.api.providers.concurrent.ThreadSleep;

/**
 * HttpConnector.
 *
 * @author patrick_guillerm
 * @since 26 oct. 2016
 */
public class HttpBasicConnector {
    
    // =========================================================================
    // ATTRIBUTES
    /** The Constant LOGGER. */
    // =========================================================================
    public static final String                       HTTP_GET                    = "GET";
    
    public static final String                       HTTP_POST                   = "POST";
    
    /** <strong>-Dhttp.connector.timeout=20000</strong> */
    public static final String                       TIME_OUT_CONFIG             = "http.connector.timeout";
    
    /** <strong>-Dhttp.connector.timeToLive=30000</strong> */
    public static final String                       TIME_TO_LIVE_CONFIG         = "http.connector.timeToLive";
    
    /** <strong>-Dhttp.connector.maxConnections=300</strong> */
    public static final String                       MAX_CONNEXIONS              = "http.connector.maxConnections";
    
    /** <strong>-Dhttp.connector.maxPerRoute=50</strong> */
    public static final String                       MAX_PER_ROUTE               = "http.connector.maxPerRoute";
    
    /** <strong>-Dapplication.name=inugami</strong> */
    public static final String                       APPLICATION_NAME            = "application.name";
    
    /** <strong>-Dapplication.name.header=APPLICATION-NAME</strong> */
    public static final String                       APPLICATION_NAME_HEADER     = "application.name.header";
    
    /** <strong>-Dapplication.hostname=</strong> */
    public static final String                       APPLICATION_HOSTNAME        = "application.hostname";
    
    public static final String                       APPLICATION_HOSTNAME_HEADER = "application.hostname.header";
    
    /** <strong>-Dhttp.connector.socket.timeout=60000</strong> */
    public static final String                       SOCKET_TIMEOUT              = "http.connector.socket.timeout";
    
    private static final String                      APPLICATION_JSON            = "application/json";
    
    private static final String                      APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
    
    private static final String                      CONTENT_TYPE                = "content-type";
    
    private static final Charset                     CHARSET_US_ASCII            = Charset.forName("US-ASCII");
    
    private static final int                         BUFFER_SIZE                 = 1000 * 1024;
    
    private static final int                         LOG_STEP                    = 10 * 1024;
    
    private final int                                timeout;
    
    private final String                             toStr;
    
    private final int                                timeToLive;
    
    private final Map<String, HttpRoute>             routesUrl                   = new ConcurrentHashMap<>();
    
    private final int                                maxPerRoute;
    
    private final int                                socketTimeout;
    
    private final PoolingHttpClientConnectionManager connectionManager           = new PoolingHttpClientConnectionManager();
    
    private final CloseableHttpClient                httpClient;
    
    private final String                             currentApplicationName;
    
    private final String                             headerApplicationName;
    
    private final String                             currentApplicationHostname;
    
    private final String                             applicationHostnameHeader;
    
    private final ThreadSleep                        threadSleep                 = new ThreadSleep(250);
    
    private RequestConfig                            requestConfig;
    
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
        
        currentApplicationName = initValue(APPLICATION_NAME,
                                           JvmKeyValues.APPLICATION_NAME.or(JvmKeyValues.DEFAUKLT_APPLICATION_NAME));
        headerApplicationName = initValue(APPLICATION_NAME_HEADER, "application-name");
        currentApplicationHostname = initValue(APPLICATION_HOSTNAME, "");
        applicationHostnameHeader = initValue(APPLICATION_HOSTNAME, "application-host");
        
        final StringBuilder str = new StringBuilder();
        str.append("timeout=").append(timeout);
        str.append(", timeToLive=").append(this.timeToLive);
        str.append(", maxConnections=").append(localmaxConnections);
        str.append(", maxPerRoute=").append(this.maxPerRoute);
        toStr = str.toString();
        
        connectionManager.setMaxTotal(localmaxConnections);
        connectionManager.setDefaultMaxPerRoute(maxPerRoute);
        httpClient = buildClient();
    }
    
    private String initValue(final String key, final String defaultValue) {
        String result = defaultValue;
        final String value = System.getProperty(key);
        if (value != null) {
            result = value;
        }
        return result;
    }
    
    private int initIntValue(final String timeOutConfig, final int defaultValue) {
        final int result;
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
    // CLOSE
    // =========================================================================
    /**
     * Builds the http client.
     *
     * @return the closeable http client
     * @throws KeyManagementException the key management exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws KeyStoreException the key store exception
     */
    private CloseableHttpClient buildHttpClient() throws KeyManagementException, NoSuchAlgorithmException,
                                                  KeyStoreException {
        CloseableHttpClient result;
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
        final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create().register("http",
                                                                                                                                    defaultConnection).register("https",
                                                                                                                                                                sslsf).build();
        
        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        
        httpClientsCustom.setConnectionManager(connectionManager);
        httpClientsCustom.setDefaultSocketConfig(socketConfig);
        
        result = httpClientsCustom.build();
        return result;
    }
    
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
    private void appendUrlToPool(final String url) {
        HttpRoute route = routesUrl.get(url);
        if (route == null) {
            route = new HttpRoute(new HttpHost(url));
            connectionManager.setMaxPerRoute(route, maxPerRoute);
            routesUrl.put(url, route);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("HttpBasicConnector [");
        builder.append(toStr);
        builder.append("]");
        return builder.toString();
    }
    
    /**
     * Builds the request.
     *
     * @param baseUrl the base url
     * @param parameters the parameters
     * @return the string
     * @throws ConnectorException the connector exception
     */
    public static String buildRequest(final String baseUrl,
                                      final Tuple<String, String>... parameters) throws ConnectorException {
        return buildRequest(baseUrl, Arrays.asList(parameters));
    }
    
    /**
     * Builds the request.
     *
     * @param baseUrl the base url
     * @param parameters the parameters
     * @return the string
     * @throws ConnectorException the connector exception
     */
    public static String buildRequest(final String baseUrl,
                                      final List<Tuple<String, String>> parameters) throws ConnectorException {
        Asserts.notNull("Request base url mustn't be null!", baseUrl);
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
            try {
                result.append(URLEncoder.encode(param.getValue(), "UTF-8"));
            }
            catch (final UnsupportedEncodingException e) {
                throw new ConnectorException(e.getMessage(), e);
            }
        }
        
        return result.toString();
    }
    
    // =========================================================================
    // GET
    // =========================================================================
    public HttpConnectorResult get(final String url, final int retry) throws ConnectorException {
        try {
            return get(url, retry, null);
        }
        catch (final Exception e) {
            throw new ConnectorException(e.getMessage(), e);
        }
    }
    
    public HttpConnectorResult get(final String url, final int retry,
                                   final CredentialsProvider credentialsProvider) throws ConnectorException {
        return get(url, retry, credentialsProvider, null);
    }
    
    /**
     * Gets the.
     *
     * @param url the url
     * @param retry the retry
     * @param credentialsProvider the credentials provider
     * @return the http connector result
     * @throws ConnectorException the connector exception
     */
    public HttpConnectorResult get(final String url, final int retry, final CredentialsProvider credentialsProvider,
                                   final Map<String, String> header) throws ConnectorException {
        appendUrlToPool(url);
        
        final URL realUrl = buildUrl(url);
        
        int nbTry = retry <= 0 ? 1 : retry;
        Exception error = null;
        HttpConnectorResult result = null;
        
        do {
            try {
                result = processGet(realUrl, credentialsProvider, header);
                error = null;
                break;
            }
            catch (final Exception e) {
                error = e;
                threadSleep.sleep();
            }
            finally {
                nbTry--;
            }
        }
        while (nbTry >= 0);
        
        if (error != null) {
            throw new ConnectorException(error.getMessage(), error);
        }
        return result;
    }
    
    public HttpConnectorResult get(final String url,
                                   final CredentialsProvider credentialsProvider) throws ConnectorException {
        return get(url, credentialsProvider, null);
    }
    
    public HttpConnectorResult get(final String url, final CredentialsProvider credentialsProvider,
                                   final Map<String, String> header) throws ConnectorException {
        final URL realUrl = buildUrl(url);
        try {
            return processGet(realUrl, credentialsProvider, header);
        }
        catch (URISyntaxException | IOException e) {
            throw new ConnectorException(e.getMessage(), e);
        }
    }
    
    /**
     * Gets the.
     *
     * @param url the url
     * @return the http connector result
     * @throws ConnectorException the connector exception
     */
    public HttpConnectorResult get(final String url) throws ConnectorException {
        final URL realUrl = buildUrl(url);
        try {
            return processGet(realUrl, null, null);
        }
        catch (URISyntaxException | IOException e) {
            throw new ConnectorException(e.getMessage(), e);
        }
    }
    
    public HttpConnectorResult get(final String url, final Map<String, String> header) throws HttpConnectorException {
        try {
            return processGet(new URL(url), null, header);
        }
        catch (URISyntaxException | IOException e) {
            throw new HttpConnectorException(500, e.getMessage());
        }
    }
    
    /**
     * Allow to execute GET request to webservice.
     *
     * @param url the webservice url
     * @param credentialsProvider http client api
     * @return webservice result
     * @throws URISyntaxException
     * @throws IOException
     * @throws HttpConnectorException
     * @throws ConnectorException if exception is occurs.
     */
    protected HttpConnectorResult processGet(final URL url, final CredentialsProvider credentialsProvider,
                                             final Map<String, String> header) throws URISyntaxException, IOException,
                                                                               HttpConnectorException {
        final HttpConnectorResultBuilder result = new HttpConnectorResultBuilder();
        Asserts.notNull("url is required!", url);
        // verify url
        url.toURI();
        
        result.addUrl(url);
        result.addVerbe(HTTP_GET);
        final HttpGet request = buildHttpGet(url);
        
        if (requestConfig != null) {
            request.setConfig(requestConfig);
        }
        
        if (credentialsProvider != null) {
            final String authentication = buildAuthentification(credentialsProvider);
            request.setHeader(HttpHeaders.AUTHORIZATION, authentication);
        }
        request.setHeader(headerApplicationName, currentApplicationName);
        request.setHeader(applicationHostnameHeader, currentApplicationHostname);
        
        if (header != null) {
            for (final Map.Entry<String, String> entry : header.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
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
            
            final HttpEntity httpEntity = httpResult.getEntity();
            result.addContenType(httpEntity.getContentType().getValue());
            if (httpEntity.getContentEncoding() != null) {
                result.addEncoding(httpEntity.getContentEncoding().getValue());
            }
            assertDataLength(url.toString(), httpEntity.getContentLength());
            
            final InputStream content = httpEntity.getContent();
            result.addData(readData(content, httpEntity.getContentLength(), url));
            
        }
        catch (final Exception e) {
            handlingError("GET", String.valueOf(url), e);
            throw new HttpConnectorException(109, e);
        }
        finally {
            request.releaseConnection();
            chrono.stop();
            Loggers.IO.info("[{}ms][GET] call request:{}", chrono.getDelaisInMillis(), url);
        }
        
        return result.build();
    }
    
    private HttpGet buildHttpGet(final URL url) throws URISyntaxException {
        return new HttpGet(url.toURI());
    }
    
    // =========================================================================
    // POST
    // =========================================================================
    public HttpConnectorResult post(final String url, final String jsonData) throws ConnectorException {
        return post(url, jsonData, null);
    }
    
    public HttpConnectorResult post(final String url, final String jsonData,
                                    final Map<String, String> header) throws ConnectorException {
        return post(url, jsonData, null, header);
    }
    
    public HttpConnectorResult post(final String url,
                                    final Map<String, String> urlEncodedData) throws ConnectorException {
        return post(url, urlEncodedData, null);
    }
    
    public HttpConnectorResult post(final String url, final Map<String, String> urlEncodedData,
                                    final Map<String, String> header) throws ConnectorException {
        return post(url, urlEncodedData, null, header);
    }
    
    /**
     * Post with Json Body.
     *
     * @param url the url
     * @param jsonData the json data
     * @param credentialsProvider
     * @return the http connector result
     * @throws ConnectorException the connector exception
     */
    public HttpConnectorResult post(final String url, final String jsonData,
                                    final CredentialsProvider credentialsProvider,
                                    final Map<String, String> header) throws ConnectorException {
        final CloseableHttpClient httpclient = buildClient();
        
        HttpConnectorResult result;
        final Chrono chrono = Chrono.startChrono();
        
        try {
            result = processPost(url, jsonData, httpclient, header, credentialsProvider);
        }
        catch (final IOException e) {
            handlingError("POST", url, e);
            throw new HttpConnectorException(102, e);
        }
        finally {
            chrono.snapshot();
            Loggers.IO.info("[{}ms][POST] call request:{}", chrono.getDelaisInMillis(), url);
        }
        
        return result;
    }
    
    /**
     * Post with Form-Data Body.
     *
     * @param url the url
     * @param urlEncodedData urlEncodedData
     * @param credentialsProvider
     * @return the http connector result
     * @throws ConnectorException the connector exception
     */
    public HttpConnectorResult post(final String url, final Map<String, String> urlEncodedData,
                                    final CredentialsProvider credentialsProvider,
                                    final Map<String, String> header) throws ConnectorException {
        final CloseableHttpClient httpclient = buildClient();
        HttpConnectorResult result;
        final Chrono chrono = Chrono.startChrono();
        
        try {
            result = processPost(url, urlEncodedData, httpclient, header, credentialsProvider);
        }
        finally {
            chrono.snapshot();
            Loggers.IO.info("[{}ms][POST] call request:{}", chrono.getDelaisInMillis(), url);
        }
        
        return result;
    }
    
    /**
     * Close.
     *
     * @param httpclient the httpclient
     * @throws HttpConnectorException the http connector exception
     */
    public void close(final CloseableHttpClient httpclient) throws HttpConnectorException {
        try {
            httpclient.close();
        }
        catch (final IOException e) {
            throw new HttpConnectorException(103, e);
        }
    }
    
    /**
     * Process post.
     *
     * @param url the url
     * @param jsonData the json data
     * @param httpclient the httpclient
     * @param header
     * @return the http connector result
     * @throws ConnectorException the connector exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private HttpConnectorResult processPost(final String url, final String jsonData,
                                            final CloseableHttpClient httpclient, final Map<String, String> header,
                                            final CredentialsProvider credentialsProvider) throws ConnectorException,
                                                                                           IOException {
        final HttpConnectorResultBuilder resultBuilder = new HttpConnectorResultBuilder();
        
        final StringEntity json = buildJsonEntry(jsonData);
        final URL realUrl = buildUrl(url);
        resultBuilder.addUrl(realUrl);
        resultBuilder.addVerbe(HTTP_POST);
        resultBuilder.addJsonInputData(jsonData);
        
        final HttpPost request = new HttpPost(realUrl.toString());
        if (requestConfig != null) {
            request.setConfig(requestConfig);
        }
        
        if (credentialsProvider != null) {
            final String authentication = buildAuthentification(credentialsProvider);
            request.setHeader(HttpHeaders.AUTHORIZATION, authentication);
        }
        
        request.addHeader(CONTENT_TYPE, APPLICATION_JSON);
        request.setHeader(headerApplicationName, currentApplicationName);
        request.setHeader(applicationHostnameHeader, currentApplicationHostname);
        
        if (header != null) {
            for (final Map.Entry<String, String> entry : header.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
        
        request.setEntity(json);
        if (Loggers.IO.isDebugEnabled()) {
            Loggers.IO.info("[POST] call request:{} - {}", url, jsonData);
        }
        else {
            Loggers.IO.info("POST to url : {}", url);
        }
        
        return executePost(request, httpclient, realUrl, url, resultBuilder);
    }
    
    /**
     * Process post.
     *
     * @param url the url
     * @param urlEncodedData the x-www-form-urlencoded
     * @param httpclient the httpclient
     * @param header
     * @return the http connector result
     * @throws ConnectorException the connector exception
     */
    private HttpConnectorResult processPost(final String url, final Map<String, String> urlEncodedData,
                                            final CloseableHttpClient httpclient, final Map<String, String> header,
                                            final CredentialsProvider credentialsProvider) throws ConnectorException {
        final HttpConnectorResultBuilder resultBuilder = new HttpConnectorResultBuilder();
        
        final List<NameValuePair> bodyParams = new ArrayList<>();
        urlEncodedData.forEach((key, value) -> bodyParams.add(new BasicNameValuePair(key, value)));
        
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(bodyParams);
        }
        catch (final UnsupportedEncodingException e) {
            Loggers.XLLOG.error("[POST] bodyParams:{}", bodyParams.toString());
        }
        
        final URL realUrl = buildUrl(url);
        resultBuilder.addUrl(realUrl);
        resultBuilder.addVerbe(HTTP_POST);
        
        final HttpPost request = new HttpPost(realUrl.toString());
        if (requestConfig != null) {
            request.setConfig(requestConfig);
        }
        
        if (credentialsProvider != null) {
            final String authentication = buildAuthentification(credentialsProvider);
            request.setHeader(HttpHeaders.AUTHORIZATION, authentication);
        }
        
        request.addHeader(CONTENT_TYPE, APPLICATION_FORM_URLENCODED);
        request.setHeader(headerApplicationName, currentApplicationName);
        request.setHeader(applicationHostnameHeader, currentApplicationHostname);
        
        if (header != null) {
            for (final Map.Entry<String, String> entry : header.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
        
        request.setEntity(urlEncodedFormEntity);
        
        Loggers.IO.info("[POST] call request:{} - {}", url, urlEncodedData.toString());
        
        return executePost(request, httpclient, realUrl, url, resultBuilder);
    }
    
    private HttpConnectorResult executePost(final HttpPost request, final CloseableHttpClient httpClient,
                                            final URL realUrl, final String url,
                                            final HttpConnectorResultBuilder resultBuilder) {
        
        HttpResponse response;
        try {
            response = httpClient.execute(request);
            assertResponseOk(response);
            resultBuilder.addStatusCode(response.getStatusLine().getStatusCode());
            resultBuilder.addMessage(response.getStatusLine().getReasonPhrase());
            
            assertDataLength(url, response.getEntity().getContentLength());
            
            if (response.getStatusLine().getStatusCode() == 200) {
                final HttpEntity httpEntity = response.getEntity();
                resultBuilder.addContenType(httpEntity.getContentType().getValue());
                
                if (httpEntity.getContentEncoding() != null) {
                    resultBuilder.addEncoding(httpEntity.getContentEncoding().getValue());
                }
                
                final int contentLength = Long.valueOf(response.getEntity().getContentLength()).intValue();
                final byte[] rawData = readData(response.getEntity().getContent(), contentLength, realUrl);
                resultBuilder.addData(rawData);
            }
        }
        catch (IOException | ConnectorException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Loggers.IO.error(e.getMessage() + " : " + url);
        }
        
        return resultBuilder.build();
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    /**
     * Read data.
     *
     * @param inputStream the input stream
     * @param dataLength the data length
     * @param url the url
     * @return the byte[]
     * @throws ConnectorException the connector exception
     */
    private byte[] readData(final InputStream inputStream, final long dataLength,
                            final URL url) throws ConnectorException {
        byte[] result = null;
        
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final byte[] buffer = new byte[BUFFER_SIZE];
        int length = 0;
        int size = 0;
        try {
            while ((length = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, length);
                size = size + length;
                
                if (Loggers.DEBUG.isDebugEnabled() && (size % LOG_STEP) == 0) {
                    Loggers.DEBUG.debug("reading result ... {} - {}", size, url);
                }
            }
        }
        catch (final IOException e) {
            throw new HttpConnectorException(107, e);
        }
        result = output.toByteArray();
        
        return result;
    }
    
    // =========================================================================
    // BUILDERS
    // =========================================================================
    /**
     * Builds the url.
     *
     * @param url the url
     * @return the url
     * @throws ConnectorException the connector exception
     */
    public URL buildUrl(final String url) throws ConnectorException {
        URL result = null;
        try {
            result = new URL(url);
        }
        catch (final MalformedURLException e) {
            throw new HttpConnectorException(105, e);
        }
        return result;
    }
    
    /**
     * Builds the client.
     *
     * @return the closeable http client
     * @throws HttpConnectorException the http connector exception
     */
    private CloseableHttpClient buildClient() {
        CloseableHttpClient result;
        try {
            result = buildHttpClient();
        }
        catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new FatalException(e.getMessage(), e);
        }
        return result;
    }
    
    /**
     * Builds the json entry.
     *
     * @param data the data
     * @return the string entity
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    private StringEntity buildJsonEntry(final String data) throws UnsupportedEncodingException {
        return new StringEntity(data);
    }
    
    // =========================================================================
    // AUTHENTIFICATION
    // =========================================================================
    private String buildAuthentification(final CredentialsProvider credentialsProvider) {
        final Credentials credentials = credentialsProvider.getCredentials(AuthScope.ANY);
        //@formatter:off
        final String auth = new StringBuilder(credentials.getUserPrincipal().getName())
                                .append(':')
                                .append(credentials.getPassword())
                                .toString();
        //@formatter:on
        return "Basic " + new String(Base64.encodeBase64(auth.getBytes(CHARSET_US_ASCII)));
    }
    
    // =========================================================================
    // EXCEPTION
    // =========================================================================
    /**
     * Assert response ok.
     *
     * @param response the response
     * @throws ConnectorException the connector exception
     */
    private void assertResponseOk(final HttpResponse response) throws ConnectorException {
        Asserts.notNull(response);
        if (200 != response.getStatusLine().getStatusCode()) {
            throw new HttpConnectorException(response.getStatusLine().getStatusCode(),
                                             response.getStatusLine().getReasonPhrase());
        }
    }
    
    /**
     * Assert data length.
     *
     * @param url the url
     * @param contentLength the content length
     * @throws HttpConnectorException the http connector exception
     */
    private void assertDataLength(final String url, final long contentLength) throws HttpConnectorException {
        if (contentLength > Integer.MAX_VALUE) {
            final String message = String.format("response grab to many bytes! (%s)", url);
            Loggers.IO.error(message);
            throw new HttpConnectorException(106, message);
        }
    }
    
    /**
     * The Class HttpConnectorException.
     */
    private class HttpConnectorException extends ConnectorException {
        HttpConnectorException(final int code, final Exception e) {
            super(new DecimalFormat("####").format(code) + " " + e.getMessage(), e);
        }
        
        HttpConnectorException(final int code, final String message) {
            super("{0}-{1}", code, message);
        }
        
        private static final long serialVersionUID = 4260783626701907721L;
        
    }
    
    private void handlingError(final String verbe, final String url, final Exception e) {
        final String message = String.format("[%s] %s  (%s)", verbe, e.getMessage(), url);
        Loggers.DEBUG.error(message, e);
        Loggers.IO.error(message);
        Loggers.XLLOG.error(message);
    }
    
    public void setProxy(final HttpProxy proxyConfig) {
        if (proxyConfig != null) {
            final HttpHost proxy = new HttpHost(proxyConfig.getHost(), proxyConfig.getPort(),
                                                proxyConfig.getProtocol());
            // org.apache.http.client.config.RequestConfig
            requestConfig = RequestConfig.custom().setProxy(proxy).build();
        }
    }
    
}
