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
package io.inugami.core.providers.graphite;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.inugami.api.exceptions.services.ProviderException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.Gav;
import io.inugami.api.models.data.graphite.GraphiteTarget;
import io.inugami.api.models.data.graphite.GraphiteTargets;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.AbstractProvider;
import io.inugami.api.providers.Provider;
import io.inugami.api.providers.ProviderForce;
import io.inugami.api.providers.ProviderRunner;
import io.inugami.api.providers.ProviderWithHttpConnector;
import io.inugami.api.providers.concurrent.FutureData;
import io.inugami.api.providers.concurrent.FutureDataBuilder;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.providers.task.ProviderFutureResultBuilder;
import io.inugami.api.providers.task.ProviderTask;
import io.inugami.api.spi.SpiLoader;
import io.inugami.configuration.services.functions.ProviderAttributFunction;
import io.inugami.core.context.ContextSPI;
import io.inugami.core.services.connectors.HttpConnector;

/**
 * The Class GraphiteCaller.
 */
public class GraphiteProvider extends AbstractProvider implements Provider, ProviderForce, ProviderWithHttpConnector {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final String                     TYPE       = "GRAPHITE";
    
    public static final String                     CONFIG_URL = "url";
    
    private final FutureData<ProviderFutureResult> futureDataRef;
    
    private final List<ProviderAttributFunction>   attributsFunctions;
    
    private final String                           url;
    
    private final int                              timeout;
    
    private ConfigHandler<String, String>          config;
    
    private final HttpConnector                    httpConnector;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public GraphiteProvider(final ClassBehavior classBehavior, final ConfigHandler<String, String> config,
                            final ProviderRunner providerRunner) {
        super(classBehavior, config, providerRunner);
        this.config = config;
        url = config.grab(CONFIG_URL);
        timeout = Integer.parseInt(config.optionnal().grabOrDefault(CONFIG_TIMEOUT, "15000"));
        final SpiLoader spiLoader = SpiLoader.getInstance();
        
        attributsFunctions = spiLoader.loadSpiService(ProviderAttributFunction.class);
        
        this.config = config;
        
        final ContextSPI ctx = spiLoader.loadSpiSingleService(ContextSPI.class);
        //@formatter:off
        httpConnector = ctx.getHttpConnector(classBehavior.getName(),
                                                  getMaxConnections(config,   10 ),
                                                  getTimeout(config,          14500),
                                                  getTTL(config,              500),
                                                  getMaxPerRoute(config,      50),
                                                  getSocketTimeout(config,    60000));
        
        futureDataRef = FutureDataBuilder.buildDefaultFuture(timeout);
        //@formatter:on
        
    }
    
    @Override
    public String getType() {
        return TYPE;
    }
    
    // =========================================================================
    // CALL EVENT
    // =========================================================================
    @Override
    public <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav) {
        final ProviderTask task = new GraphiteProviderTask(event, url, getName(), httpConnector, attributsFunctions,
                                                           config, pluginGav);
        return runTask(task, event, futureDataRef);
    }
    
    @Override
    public <T extends SimpleEvent> ProviderFutureResult callEvent(final T event) {
        ProviderFutureResult result = null;
        try {
            result = new GraphiteProviderTask(event, url, getName(), httpConnector, attributsFunctions, config,
                                              null).call();
        }
        catch (final Exception e) {
            Loggers.DEBUG.error(e.getMessage(), e);
            Loggers.PROVIDER.error(e.getMessage());
        }
        return result;
    }
    
    // =========================================================================
    // AGGREGATE
    // =========================================================================
    @Override
    public ProviderFutureResult aggregate(final List<ProviderFutureResult> data) throws ProviderException {
        assertDataType(data);
        final List<GraphiteTarget> resultData = new ArrayList<>();
        
        data.stream().filter(this::validResultWithData).map(this::toGraphiteTargets).map(GraphiteTargets::getTargets).forEach(resultData::addAll);
        
        final GraphiteTargets targets = new GraphiteTargets(resultData);
        return new ProviderFutureResultBuilder().addData(targets).build();
    }
    
    private boolean validResultWithData(final ProviderFutureResult item) {
        return !item.getException().isPresent() && item.getData().isPresent();
    }
    
    private GraphiteTargets toGraphiteTargets(final ProviderFutureResult item) {
        return (GraphiteTargets) item.getData().orElse(null);
    }
    
    private void assertDataType(final List<ProviderFutureResult> datas) throws ProviderException {
        final Optional<ProviderFutureResult> anInvalidResult = datas.stream().filter(this::validResultWithData).filter(this::notAGraphiteResult).findAny();
        if (anInvalidResult.isPresent()) {
            throw new ProviderException("can't aggregate unknow data type : {0}",
                                        anInvalidResult.get().getData().getClass().getName());
        }
    }
    
    private boolean notAGraphiteResult(final ProviderFutureResult item) {
        return Optional.ofNullable(item).filter(this::validResultWithData).map(ProviderFutureResult::getData).filter(data -> !(data.get() instanceof GraphiteTargets)).isPresent();
    }
    
    // =========================================================================
    // GETTERS
    // =========================================================================
    @Override
    public long getTimeout() {
        return timeout;
    }
}
