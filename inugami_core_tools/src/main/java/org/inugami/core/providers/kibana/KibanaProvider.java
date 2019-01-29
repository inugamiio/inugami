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

import java.util.ArrayList;
import java.util.List;

import org.inugami.api.exceptions.services.ProviderException;
import org.inugami.api.models.Gav;
import org.inugami.api.models.data.JsonObject;
import org.inugami.api.models.data.basic.JsonObjects;
import org.inugami.api.models.events.SimpleEvent;
import org.inugami.api.processors.ClassBehavior;
import org.inugami.api.processors.ConfigHandler;
import org.inugami.api.providers.AbstractProvider;
import org.inugami.api.providers.Provider;
import org.inugami.api.providers.ProviderRunner;
import org.inugami.api.providers.ProviderWithHttpConnector;
import org.inugami.api.providers.concurrent.FutureData;
import org.inugami.api.providers.concurrent.FutureDataBuilder;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.api.providers.task.ProviderFutureResultBuilder;
import org.inugami.commons.spi.SpiLoader;
import org.inugami.core.context.ContextSPI;
import org.inugami.core.services.connectors.HttpConnector;

/**
 * KibanaProvider
 * 
 * @author patrick_guillerm
 * @since 2 oct. 2017
 */
public class KibanaProvider extends AbstractProvider implements Provider, ProviderWithHttpConnector {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    
    private final FutureData<ProviderFutureResult> futureDataRef;
    
    private final long                             timeout;
    
    private final HttpConnector                    httpConnector;
    
    private final ConfigHandler<String, String>    config;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public KibanaProvider(final ClassBehavior classBehavior, final ConfigHandler<String, String> config,
                          final ProviderRunner providerRunner) {
        super(classBehavior, config, providerRunner);
        this.config = config;
        this.timeout = Long.parseLong(config.optionnal().grabOrDefault(CONFIG_TIMEOUT, "15000"));
        
        final ContextSPI ctx = new SpiLoader().loadSpiSingleService(ContextSPI.class);
        //@formatter:off
        this.httpConnector = ctx.getHttpConnector(classBehavior.getClassName(),
                                                  getMaxConnections(config,   10 ),
                                                  getTimeout(config,          14500),
                                                  getTTL(config,              500),
                                                  getMaxPerRoute(config,      50),
                                                  getSocketTimeout(config,    60000));
        
        futureDataRef = FutureDataBuilder.buildDefaultFuture(this.timeout);
        //@formatter:on
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav) {
        final KibanaProviderTask task = buildTask(event, pluginGav);
        return runTask(task, event, futureDataRef);
    }
    
    private synchronized <T extends SimpleEvent> KibanaProviderTask buildTask(final T event, final Gav pluginGav) {
        return new KibanaProviderTask(event, pluginGav, httpConnector, config);
    }
    
    @Override
    public ProviderFutureResult aggregate(final List<ProviderFutureResult> data) throws ProviderException {
        final List<JsonObject> result = new ArrayList<>();
        if (data != null) {
            for (final ProviderFutureResult providerResult : data) {
                if (providerResult.getData().isPresent()) {
                    result.add(providerResult.getData().get());
                }
            }
        }
        
        return new ProviderFutureResultBuilder().addData(new JsonObjects(result)).build();
    }
    
    @Override
    public String getType() {
        return KibanaProvider.class.getSimpleName();
    }
    
    @Override
    public ConfigHandler<String, String> getConfig() {
        return config;
    }
}
