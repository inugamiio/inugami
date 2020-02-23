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
package org.inugami.core.services.connectors;

import java.util.List;
import java.util.Map;

import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.inugami.api.exceptions.services.ConnectorException;
import org.inugami.api.models.Tuple;
import org.inugami.api.spi.SpiLoader;
import org.inugami.commons.connectors.HttpBasicConnector;
import org.inugami.commons.connectors.HttpConnectorResult;
import org.inugami.commons.connectors.HttpProxy;
import org.inugami.core.context.ContextSPI;
import org.inugami.core.services.cache.CacheTypes;

/**
 * HttpConnector.
 *
 * @author patrick_guillerm
 * @since 26 oct. 2016
 */
public class HttpConnector {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final HttpBasicConnector connector;
    
    private final ContextSPI         context;
    
    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public HttpConnector() {
        connector = new HttpBasicConnector();
        context = new SpiLoader().loadSpiSingleService(ContextSPI.class);
    }
    
    public HttpConnector(final int maxConnections) {
        connector = new HttpBasicConnector(maxConnections);
        context = new SpiLoader().loadSpiSingleService(ContextSPI.class);
    }
    
    public HttpConnector(final int timeToLive, final int maxConnections) {
        connector = new HttpBasicConnector(timeToLive, maxConnections);
        context = new SpiLoader().loadSpiSingleService(ContextSPI.class);
    }
    
    public HttpConnector(final int timeout, final int timeToLive, final int maxConnections, final int maxPerRoute,
                         final int socketTimeout) {
        connector = new HttpBasicConnector(timeout, timeToLive, maxConnections, maxPerRoute, socketTimeout);
        context = new SpiLoader().loadSpiSingleService(ContextSPI.class);
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    public void setProxy(final HttpProxy proxy) {
        connector.setProxy(proxy);
    }
    
    public void close() {
        connector.close();
    }
    
    @Override
    public String toString() {
        return connector.toString();
    }
    
    // =========================================================================
    // BUILDS
    // =========================================================================
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
        return HttpBasicConnector.buildRequest(baseUrl, parameters);
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
        return HttpBasicConnector.buildRequest(baseUrl, parameters);
    }
    
    // =========================================================================
    // GET
    // =========================================================================
    
    public PoolingHttpClientConnectionManager getConnectionManager() {
        return null;
    }
    
    public HttpConnectorResult getWithoutCache(final String url) throws ConnectorException {
        return getWithoutCache(url, null);
    }
    
    /**
     * Gets the without cache.
     *
     * @param url the url
     * @return the without cache
     * @throws ConnectorException the connector exception
     */
    public HttpConnectorResult getWithoutCache(final String url,
                                               final CredentialsProvider credentialsProvider) throws ConnectorException {
        return connector.get(url, credentialsProvider);
        
    }
    
    public HttpConnectorResult getWithoutCache(final String url, final int retry) throws ConnectorException {
        return connector.get(url, retry);
    }
    
    public HttpConnectorResult get(final String url) throws ConnectorException {
        return get(url, CacheTypes.IO_QUERIES, null);
    }
    
    public HttpConnectorResult get(final String url, final CacheTypes cache) throws ConnectorException {
        return get(url, cache == null ? CacheTypes.IO_QUERIES : cache, null);
    }
    
    public HttpConnectorResult get(final String url,
                                   final CredentialsProvider credentialsProvider) throws ConnectorException {
        return get(url, CacheTypes.IO_QUERIES, credentialsProvider);
    }
    
    /**
     * Gets the.
     *
     * @param url the url
     * @return the http connector result
     * @throws ConnectorException the connector exception
     */
    public synchronized HttpConnectorResult get(final String url, final CacheTypes cache,
                                                final CredentialsProvider credentialsProvider) throws ConnectorException {
        
        connector.buildUrl(url);
        final String key = String.join("_", HttpBasicConnector.HTTP_GET, url);
        //@formatter:off
        HttpConnectorResult result = grabFromCache(cache,key);
        //@formatter:on
        if (result == null) {
            result = connector.get(url, credentialsProvider);
            pushToCache(cache, result, key);
        }
        return result;
    }
    
    // =========================================================================
    // POST
    // =========================================================================
    public HttpConnectorResult post(final String url, final String jsonData) throws ConnectorException {
        return connector.post(url, jsonData);
    }
    
    public HttpConnectorResult post(final String url, final String jsonData,
                                    final CredentialsProvider credentialsProvider) throws ConnectorException {
        return connector.post(url, jsonData, credentialsProvider, null);
    }
    
    public HttpConnectorResult post(final String url,
                                    final Map<String, String> urlEncodedData) throws ConnectorException {
        return connector.post(url, urlEncodedData);
    }
    
    public HttpConnectorResult post(final String url, final Map<String, String> urlEncodedData,
                                    final CredentialsProvider credentialsProvider) throws ConnectorException {
        return connector.post(url, urlEncodedData, credentialsProvider, null);
    }
    
    /**
     * Post.
     *
     * @param url the url
     * @param jsonData the json data
     * @return the http connector result
     * @throws ConnectorException the connector exception
     */
    public HttpConnectorResult post(final String url, final String jsonData,
                                    final CredentialsProvider credentialsProvider,
                                    final Map<String, String> header) throws ConnectorException {
        return connector.post(url, jsonData, credentialsProvider, header);
    }
    
    // =========================================================================
    // CACHE
    // =========================================================================
    /**
     * Push to cache.
     * 
     * @param cache
     *
     * @param data the data
     */
    private void pushToCache(final CacheTypes cache, final HttpConnectorResult data, final String key) {
        context.getCache().put(cache, key, data);
    }
    
    /**
     * Grab from cache.
     * 
     * @param cache
     *
     * @param key the key
     * @return the http connector result
     */
    private HttpConnectorResult grabFromCache(final CacheTypes cache, final String key) {
        return context.getCache().get(cache, key);
    }
    
}
