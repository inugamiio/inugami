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
package io.inugami.core.providers.els;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.inugami.api.exceptions.services.ProviderException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.Gav;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.AbstractProvider;
import io.inugami.api.providers.Provider;
import io.inugami.api.providers.ProviderRunner;
import io.inugami.api.providers.ProviderWithHttpConnector;
import io.inugami.api.providers.ProviderWriter;
import io.inugami.api.providers.concurrent.FutureData;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.spi.SpiLoader;
import io.inugami.commons.threads.RunAndCloseService;
import io.inugami.core.context.ContextSPI;
import io.inugami.core.providers.kibana.KibanaProvider;
import io.inugami.core.services.connectors.HttpConnector;

/**
 * ElasticSearchWriter
 * 
 * @author patrick_guillerm
 * @since 12 sept. 2018
 */
public class ElasticSearchWriter extends AbstractProvider
        implements Provider, ProviderWithHttpConnector, ProviderWriter {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final KibanaProvider kibanaProvider;
    
    private final HttpConnector  httpConnector;
    
    private final String         url;
    
    private final long           timeout;
    
    private final int            maxThreads;
    
    private final String         defaultIndex;
    
    private final String         name;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ElasticSearchWriter(final ClassBehavior classBehavior, final ConfigHandler<String, String> config,
                               final ProviderRunner providerRunner) {
        super(classBehavior, config, providerRunner);
        this.name = classBehavior.getName();
        this.url = config.grab("url_bulk") + "/_bulk";
        kibanaProvider = new KibanaProvider(classBehavior, config, providerRunner);
        final ContextSPI ctx = new SpiLoader().loadSpiSingleService(ContextSPI.class);
        
        this.timeout = Long.parseLong(config.grabOrDefault("timeout", "60000"));
        this.maxThreads = config.grabInt("maxThreads", 20);
        this.defaultIndex = config.grabOrDefault("index", "dashboard_tv");
        
       //@formatter:off
        this.httpConnector = ctx.getHttpConnector(classBehavior.getClassName(),
                                                  getMaxConnections(config,   10 ),
                                                  getTimeout(config,          14500),
                                                  getTTL(config,              500),
                                                  getMaxPerRoute(config,      50),
                                                  getSocketTimeout(config,    60000));
        //@formatter:on
        
    }
    
    // =========================================================================
    // METHODS WRITE
    // =========================================================================
    @Override
    public void write(final JsonObject data) {
        if (data instanceof ElsData) {
            processWrite((ElsData) data);
        }
    }
    
    private void processWrite(final ElsData data) {
        final int nbItems = 500;
        final List<Callable<Void>> tasks = new ArrayList<>();
        final List<JsonObject> values = data.getValues();
        final int size = values.size();
        final int step = size / nbItems;
        for (int i = 0; i < (step + 1); i++) {
            final int begin = i * nbItems;
            int end = begin + nbItems;
            if (end > size) {
                end = size;
            }
            tasks.add(new ElasticSearchWriterTask(httpConnector, url, data, values.subList(begin, end)));
        }
        final int nbThreads = tasks.size() < maxThreads ? tasks.size() : maxThreads;
        final String threadName = this.getClass().getSimpleName();
        //@formatter:off
        final RunAndCloseService<Void> treadsPool = new RunAndCloseService<>(this.getClass().getSimpleName(),
                                                                             timeout,
                                                                             nbThreads,
                                                                             tasks,
                                                                             null);
        //@formatter:on
        treadsPool.run();
        treadsPool.forceShutdown();
        Loggers.PROVIDER.info("done sending data to ELS : {} documents, {} : {}", values.size(), data.getIndex(),
                              data.getType());
    }
    
    // =========================================================================
    // METHODS OTHERS
    // =========================================================================
    @Override
    public <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav) {
        return kibanaProvider.callEvent(event, pluginGav);
    }
    
    @Override
    public ProviderFutureResult aggregate(final List<ProviderFutureResult> data) throws ProviderException {
        return kibanaProvider.aggregate(data);
    }
    
    @Override
    public String getType() {
        return kibanaProvider.getType();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public String getName() {
        return name;
    }
}
