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
package io.inugami.core.providers.kibana;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.mapping.Mapper;
import io.inugami.api.models.Gav;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.data.basic.JsonObjects;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.providers.task.ProviderFutureResultBuilder;
import io.inugami.api.providers.task.ProviderTask;
import io.inugami.commons.spi.ClassBuilder;
import io.inugami.core.providers.kibana.models.Hit;
import io.inugami.core.services.connectors.HttpConnector;

/**
 * KibanaProviderTask
 * 
 * @author patrick_guillerm
 * @since 2 oct. 2017
 */
public class KibanaProviderTask implements ProviderTask {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final SimpleEvent                   event;
    
    private final Gav                           gav;
    
    private final KibanaService                 KibanaService;
    
    private final String                        url;
    
    private final Map<String, String>           placeholder;
    
    private final ConfigHandler<String, String> config;
    
    private final Mapper<JsonObject, Hit>       mapper;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public KibanaProviderTask(final SimpleEvent event, final Gav gav, final HttpConnector httpConnector,
                              final ConfigHandler<String, String> config) {
        super();
        url = config.applyProperties(config.grab("url"));
        placeholder = resolveConfigValues(config);
        this.event = event;
        this.gav = gav;
        KibanaService = new KibanaService(httpConnector);
        this.config = config;
        
        mapper = event.getMapper().isPresent() ? initMapper(event.getMapper().get()) : (hit) -> hit;
        
    }
    
    private Mapper<JsonObject, Hit> initMapper(final String className) {
        final ClassBuilder classBuilder = new ClassBuilder();
        final Class<?> clazz = classBuilder.load(config.applyProperties(className));
        return classBuilder.buildInstance(clazz);
    }
    
    private Map<String, String> resolveConfigValues(final ConfigHandler<String, String> conf) {
        final Map<String, String> result = new HashMap<>();
        for (final Map.Entry<String, String> entry : conf.entrySet()) {
            result.put(entry.getKey(), conf.applyProperties(entry.getValue()));
        }
        return result;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    
    @Override
    public ProviderFutureResult callProvider() {
        Exception error = null;
        final List<JsonObject> data = new ArrayList<>();
        Asserts.isTrue("from is mandatory", event.getFrom().isPresent());
        Asserts.isTrue("until is mandatory", event.getUntil().isPresent());
        try {
            placeholder.put("from", config.applyProperties(event.getFrom().get()) + "000");
            placeholder.put("to", config.applyProperties(event.getUntil().get()) + "000");
            
            KibanaService.searchFromUrlWithoutAggreagate(url, event.getQuery(), placeholder, (currentData, config) -> {
                if ((currentData.getHits() != null) && (currentData.getHits().getHits() != null)) {
                    for (final Hit hit : currentData.getHits().getHits()) {
                        data.add(mapper.mapping(hit));
                    }
                }
            });
        }
        catch (final KibanaExcpetion e) {
            error = e;
        }
        
        final ProviderFutureResultBuilder result = new ProviderFutureResultBuilder();
        if (error != null) {
            result.addException(error);
        }
        result.addEvent(event);
        
        if (data != null) {
            result.addData(new JsonObjects<>(data));
        }
        
        return result.build();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public GenericEvent getEvent() {
        return event;
    }
    
    @Override
    public Gav getPluginGav() {
        return gav;
    }
    
}
