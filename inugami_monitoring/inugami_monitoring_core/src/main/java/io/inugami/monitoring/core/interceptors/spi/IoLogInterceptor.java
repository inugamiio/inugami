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
package io.inugami.monitoring.core.interceptors.spi;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import io.inugami.api.models.JsonBuilder;
import io.inugami.api.monitoring.data.ResponseData;
import io.inugami.api.monitoring.data.ResquestData;
import io.inugami.api.monitoring.exceptions.ErrorResult;
import io.inugami.api.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.api.monitoring.models.GenericMonitoringModel;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.monitoring.api.obfuscators.ObfuscatorTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LogInterceptor
 * 
 * @author patrickguillerm
 * @since Jan 7, 2019
 */
public class IoLogInterceptor implements MonitoringFilterInterceptor {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Logger LOGGER        = LoggerFactory.getLogger(IoLogInterceptor.class.getSimpleName());
    
    private static final String EMPTY_CONTENT = "empty";
    
    private static final String EMPTY         = "";
    
    private final String        inputDecorator;
    
    private final String        outputDecorator;
    
    private final boolean       enableDecorator;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public IoLogInterceptor() {
        enableDecorator = true;
        inputDecorator = "[IN] ";
        outputDecorator = "[OUT] ";
    }
    
    public IoLogInterceptor(final ConfigHandler<String, String> configuration) {
        enableDecorator = configuration.grabBoolean("enableDecorator", true);
        inputDecorator = configuration.grabOrDefault("inputDecorator", "[IN] ");
        outputDecorator = configuration.grabOrDefault("outputDecorator", "[OUT] ");
    }
    
    @Override
    public MonitoringFilterInterceptor buildInstance(final ConfigHandler<String, String> configuration) {
        return new IoLogInterceptor(configuration);
    }
    
    @Override
    public String getName() {
        return "iolog";
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public List<GenericMonitoringModel> onBegin(final ResquestData request) {
        final String playload = buildPlayload(request, null);
        //@formatter:off
        final String msg = String.join("|", 
                                       renderMethod(request.getMethod()),
                                       renderContentType(request.getContentType()),
                                       request.getUri(),
                                       playload
                                       );
        //@formatter:on
        LOGGER.info((enableDecorator ? inputDecorator : EMPTY) + msg);
        
        return null;
    }
    
    @Override
    public List<GenericMonitoringModel> onDone(final ResquestData request, final ResponseData httpResponse,
                                               final ErrorResult error) {
        final String playload = buildPlayload(request, httpResponse);
        //@formatter:off
        final String msg = String.join("|", 
                                       renderMethod(request.getMethod()),
                                       renderContentType(request.getContentType()),
                                       request.getUri(),
                                       String.valueOf(httpResponse.getDuration()),
                                       playload
                                       );
        //@formatter:on
        if (error == null) {
            LOGGER.info((enableDecorator ? outputDecorator : EMPTY) + msg);
        }
        else {
            LOGGER.error((enableDecorator ? outputDecorator : EMPTY) + msg);
        }
        
        return null;
    }
    
    // =========================================================================
    // RENDERING
    // =========================================================================
    
    private String buildPlayload(final ResquestData request, final ResponseData httpResponse) {
        final StringBuilder result = new StringBuilder();
        result.append(buildHeader(request));
        result.append('|');
        result.append(renderContent(request.getContentType(), request.getContent()));
        
        if (httpResponse != null) {
            result.append('|');
            result.append(renderContent(request.getContentType(), httpResponse.getContent()));
        }
        
        return result.toString();
    }
    
    private String buildHeader(final ResquestData request) {
        final JsonBuilder json = new JsonBuilder();
        json.openObject();
        if (request.getHearder() != null) {
            final Iterator<Entry<String, String>> entrySet = request.getHearder().entrySet().iterator();
            
            while (entrySet.hasNext()) {
                final Entry<String, String> entry = entrySet.next();
                json.addField(entry.getKey().toLowerCase()).valueQuot(entry.getValue());
                if (entrySet.hasNext()) {
                    json.addSeparator();
                }
                
            }
        }
        
        json.closeObject();
        return ObfuscatorTools.applyObfuscators(json.toString());
    }
    
    private String renderContent(final String contentType, final String content) {
        String result = null;
        if (content == null) {
            result = JsonBuilder.VALUE_NULL;
        }
        else if (content.isEmpty()) {
            result = EMPTY_CONTENT;
        }
        else {
            result = content;
        }
        return result;
    }
    
    private String renderMethod(final String method) {
        return "[" + method + "]";
    }
    
    private String renderContentType(final String contentType) {
        return contentType == null ? EMPTY : "|" + contentType;
    }
    
}
