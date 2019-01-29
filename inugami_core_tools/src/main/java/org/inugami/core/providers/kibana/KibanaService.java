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
package org.inugami.core.providers.kibana;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.services.ConnectorException;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.commons.connectors.HttpConnectorResult;
import org.inugami.configuration.services.ConfigHandlerHashMap;
import org.inugami.core.providers.kibana.models.Hit;
import org.inugami.core.providers.kibana.models.Hits;
import org.inugami.core.providers.kibana.models.ResultData;
import org.inugami.core.services.connectors.HttpConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSONDeserializer;

/**
 * KibanaService
 * 
 * @author patrick_guillerm
 * @since 20 sept. 2017
 */
public class KibanaService {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Logger                LOGGER           = LoggerFactory.getLogger(KibanaService.class.getSimpleName());
    
    private static final String                URL_PARAM_SIZE   = "&size=";
    
    private static final String                URL_PARAM_FROM   = "from=";
    
    private static final int                   UNDEFINE_INT     = -1;
    
    private final int                          maxCall;
    
    private final int                          maxHitsInCall;
    
    private final HttpConnector                connector;
    
    private final JSONDeserializer<ResultData> jsonDeserializer = new JSONDeserializer<ResultData>().use(null,
                                                                                                         ResultData.class);
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public KibanaService(final HttpConnector connector) {
        this(UNDEFINE_INT, UNDEFINE_INT, connector);
    }
    
    public KibanaService(final int maxCall, final int maxHitsInCall, final HttpConnector connector) {
        super();
        this.maxCall = maxCall == UNDEFINE_INT ? 400 : maxCall;
        this.maxHitsInCall = maxHitsInCall == UNDEFINE_INT ? 5000 : maxHitsInCall;
        this.connector = connector;
    }
    
    // =========================================================================
    // METHODS Call From Url
    // =========================================================================
    public List<Hit> searchFromUrlWithoutAggreagate(final String url, final String json,
                                                    final Map<String, String> placeholder,
                                                    final KibanaHandler handler) throws KibanaExcpetion {
        return searchFromUrlWithoutAggreagate(url, json, placeholder, handler, null);
    }
    
    public List<Hit> searchFromUrlWithoutAggreagate(final String url, final String json,
                                                    final Map<String, String> placeholder, final KibanaHandler handler,
                                                    final Writer writer) throws KibanaExcpetion {
        return searchFromUrl(url, json, placeholder, false, handler, writer);
    }
    
    public List<Hit> searchFromUrl(final String url, final String json, final Map<String, String> placeholder,
                                   final KibanaHandler handler) throws KibanaExcpetion {
        return searchFromUrl(url, json, placeholder, true, handler, null);
    }
    
    public List<Hit> searchFromUrl(final String url, final String json, final Map<String, String> placeholder,
                                   final KibanaHandler handler, final Writer writer) throws KibanaExcpetion {
        return searchFromUrl(url, json, placeholder, true, handler, writer);
    }
    
    //@formatter:off
    protected List<Hit> searchFromUrl(final String url, final String json, final Map<String, String> placeholder, final boolean aggregate, final KibanaHandler handler, final Writer writer) throws KibanaExcpetion{
        //@formatter:on
        final List<Hit> result = new ArrayList<>();
        final ConfigHandler<String, String> config = new ConfigHandlerHashMap(placeholder);
        
        final String urlToUse = config.applyProperties(url);
        final String data = config.applyProperties(json);
        final boolean hasHandler = handler != null;
        
        final int size = maxHitsInCall;
        boolean hasResult = false;
        long total = 0L;
        long totalCursor = 0L;
        int step = 0;
        
        do {
            LOGGER.info("call kibana ... {} -> {} / {}", (step * size), (step + 1) * size, total);
            final String urlWithParam = addUrlParam(urlToUse, step, size);
            final String resultData = connectorCall(urlWithParam, data);
            
            final ResultData jsonData = deserialize(resultData);
            
            if ((jsonData == null) || (jsonData.getHits() == null)) {
                hasResult = false;
                LOGGER.info("no result found!");
            }
            else {
                writeAllHits(jsonData.getHits(), writer);
                flushWriter(writer);
                
                total = jsonData.getHits().getTotal();
                totalCursor += jsonData.getHits().getHits().size();
                
                hasResult = totalCursor < total;
                LOGGER.info("grab data : {}/{}", totalCursor, total);
                
                if (hasHandler) {
                    handler.onData(jsonData, config);
                }
                
                if (aggregate && (jsonData.getHits() != null)) {
                    result.addAll(jsonData.getHits().getHits());
                }
                step++;
            }
        }
        while (hasResult || (step > maxCall));
        
        LOGGER.info("done");
        return result;
    }
    
    private synchronized void flushWriter(final Writer writer) {
        if (writer != null) {
            try {
                writer.flush();
            }
            catch (final IOException e) {
                Loggers.DEBUG.error(e.getMessage(), e);
                Loggers.IO.error(e.getMessage());
            }
        }
    }
    
    private void writeAllHits(final Hits hits, final Writer writer) {
        if (writer != null) {
            synchronized (KibanaService.class) {
                for (final Hit hit : hits.getHits()) {
                    try {
                        
                        final String jsonData = hit.convertToJson();
                        writer.write(jsonData.toCharArray());
                        writer.write("\n");
                    }
                    catch (final IOException e) {
                        Loggers.DEBUG.error(e.getMessage(), e);
                        Loggers.IO.error(e.getMessage());
                    }
                }
            }
        }
    }
    
    private String connectorCall(final String urlWithParam, final String data) {
        String result = null;
        HttpConnectorResult httpResult = null;
        
        try {
            httpResult = connector.post(urlWithParam, data);
        }
        catch (final ConnectorException e) {
            Loggers.IO.error(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        
        if ((httpResult != null) && (httpResult.getStatusCode() == 200)) {
            result = new String(httpResult.getData());
        }
        else {
            LOGGER.error("Error on calling kibana : response code : {}",
                         httpResult == null ? "null" : String.valueOf(httpResult.getStatusCode()));
        }
        
        return result;
    }
    
    // =========================================================================
    // APPLY PROPERTIES
    // =========================================================================
    private String addUrlParam(final String url, final int step, final int size) {
        Asserts.notNull(url);
        final StringBuilder result = new StringBuilder(url);
        if (!url.contains("?")) {
            result.append('?');
        }
        result.append(URL_PARAM_FROM).append(size * step);
        result.append(URL_PARAM_SIZE).append(size);
        return result.toString();
    }
    
    // =========================================================================
    // deserialize
    // =========================================================================
    private synchronized ResultData deserialize(final String value) {
        return value == null ? null : jsonDeserializer.deserialize(value);
    }
}
