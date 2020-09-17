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
package io.inugami.core.providers.cache;

import java.io.Serializable;
import java.util.List;

import org.ehcache.Cache;
import io.inugami.api.exceptions.services.ProviderException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.Gav;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.processors.ClassBehavior;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.AbstractProvider;
import io.inugami.api.providers.NoForcingEventProvider;
import io.inugami.api.providers.Provider;
import io.inugami.api.providers.ProviderRunner;
import io.inugami.api.providers.concurrent.FutureData;
import io.inugami.api.providers.concurrent.FutureDataBuilder;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.api.providers.task.ProviderFutureResultBuilder;
import io.inugami.api.spi.SpiLoader;
import io.inugami.core.context.ContextSPI;
import io.inugami.core.services.cache.CacheTypes;

/**
 * PurgeCacheProvider
 * 
 * @author patrick_guillerm
 * @since 4 oct. 2017
 */
public class PurgeCacheProvider extends AbstractProvider implements Provider, NoForcingEventProvider {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final ProviderFutureResult        emptyResult;
    
    private final CacheTypes                  region;
    
    private final Cache<String, Serializable> cache;
    
    private final ContextSPI                  context;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public PurgeCacheProvider(final ClassBehavior classBehavior, final ConfigHandler<String, String> config,
                              final ProviderRunner providerRunner) {
        super(classBehavior, config, providerRunner);
        
        context = new SpiLoader().loadSpiSingleService(ContextSPI.class);
        this.region = CacheTypes.getEnum(config.optionnal().grab("region"));
        if (this.region != null) {
            cache = context.getCache().getCache(region);
        }
        else {
            cache = null;
        }
        emptyResult = new ProviderFutureResultBuilder().build();
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public <T extends SimpleEvent> FutureData<ProviderFutureResult> callEvent(final T event, final Gav pluginGav) {
        Loggers.CACHE.info("clean cache region : {}", event.getQuery());
        if ("ALL".equals(event.getQuery())) {
            cleanAll();
        }
        else if (cache != null) {
            cache.remove(event.getQuery());
        }
        return new FutureDataBuilder().addImmediateFuture(emptyResult).addEvent(event).build();
    }
    
    private void cleanAll() {
        if (cache == null) {
            context.getCache().clear();
        }
        else {
            context.getCache().clear(region);
        }
    }
    
    @Override
    public ProviderFutureResult aggregate(final List<ProviderFutureResult> data) throws ProviderException {
        return emptyResult;
    }
    
    @Override
    public String getType() {
        return PurgeCacheProvider.class.getSimpleName();
    }
}
