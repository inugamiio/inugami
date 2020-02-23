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
package org.inugami.core.providers.rest;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.services.ConnectorException;
import org.inugami.api.models.Gav;
import org.inugami.api.models.data.basic.Json;
import org.inugami.api.models.data.basic.JsonObject;
import org.inugami.api.models.events.GenericEvent;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.providers.task.ProviderFutureResultBuilder;
import org.inugami.api.providers.task.ProviderTask;
import org.inugami.commons.connectors.HttpBasicConnector;
import org.inugami.commons.connectors.HttpConnectorResult;
import org.inugami.core.services.connectors.HttpConnector;

/**
 * RestTask
 * 
 * @author patrick_guillerm
 * @since 9 mai 2017
 */
public class RestProviderTask implements ProviderTask {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final SimpleEvent                   event;
    
    private final String                        url;
    
    private final String                        verbe;
    
    private final HttpConnector                 httpConnector;
    
    private final Gav                           pluginGav;
    
    private final ConfigHandler<String, String> config;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public <T extends SimpleEvent> RestProviderTask(final T event, final String url, final String verbe,
                                                    final HttpConnector httpConnector, final Gav pluginGav,
                                                    final ConfigHandler<String, String> config) {
        super();
        Asserts.notNull("URL mustn't be null!", url);
        Asserts.notNull("HTTP verbe mustn't be null!", verbe);
        this.event = event;
        this.url = url;
        this.verbe = verbe;
        this.httpConnector = httpConnector;
        this.pluginGav = pluginGav;
        this.config = config;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public ProviderFutureResult callProvider() {
        final ProviderFutureResultBuilder result = new ProviderFutureResultBuilder();
        result.addEvent(event);
        
        HttpConnectorResult data = null;
        try {
            data = callRestService();
        }
        catch (final ConnectorException e) {
            result.addException(e);
        }
        
        if (data != null) {
            if ((data.getStatusCode() != 200) && (data.getStatusCode() != 0)) {
                result.addException(new ConnectorException(data.getMessage()));
            }
            else {
                result.addData(buildData(data));
            }
        }
        
        return result.build();
    }
    
    private JsonObject buildData(final HttpConnectorResult data) {
        return new Json(new String(data.getData(), data.getCharset()));
    }
    
    // =========================================================================
    // callRestService
    // =========================================================================
    private HttpConnectorResult callRestService() throws ConnectorException {
        final String realVerbe = grabRealVerbe();
        HttpConnectorResult result = null;
        
        switch (realVerbe) {
            case HttpBasicConnector.HTTP_POST:
                result = httpConnector.post(url, applyProperties(event.getQuery()));
                break;
            case HttpBasicConnector.HTTP_GET:
                result = httpConnector.get(url);
                break;
            
            default:
                throw new IllegalArgumentException("Illegal verbe :" + realVerbe);
        }
        
        return result;
    }
    
    private String applyProperties(final String query) {
        
        if (event.getFrom().isPresent()) {
            config.put("from", config.applyProperties(event.getFrom().get()) + "000");
        }
        
        if (event.getUntil().isPresent()) {
            config.put("to", config.applyProperties(event.getUntil().get()) + "000");
        }
        
        return config.applyProperties(query);
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private String grabRealVerbe() {
        String result = verbe;
        if (!event.getQuery().isEmpty()) {
            result = HttpBasicConnector.HTTP_POST;
        }
        
        return result;
    }
    
    @Override
    public GenericEvent getEvent() {
        return event;
    }
    
    @Override
    public Gav getPluginGav() {
        return pluginGav;
    }
}
