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
package org.inugami.core.services.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.inugami.api.loggers.Loggers;
import org.inugami.api.metrics.MetricsData;
import org.inugami.api.metrics.MetricsMapper;
import org.inugami.api.metrics.MetricsProvider;
import org.inugami.api.metrics.MetricsProviderException;
import org.inugami.api.models.data.basic.JsonObject;
import org.inugami.api.providers.task.ProviderFutureResult;
import org.inugami.core.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MetricManager
 * 
 * @author patrick_guillerm
 * @since 6 juin 2017
 */
@Deprecated
public class MetricsManager {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Logger         LOGGER = LoggerFactory.getLogger(MetricsManager.class.getSimpleName());
    
    private final ApplicationContext    context;
    
    private final List<MetricsMapper>   mappers;
    
    private final List<MetricsProvider> metricsProviders;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public MetricsManager(final ApplicationContext context) {
        super();
        this.context = context;
        mappers = context.loadSpiService(MetricsMapper.class);
        metricsProviders = context.loadSpiService(MetricsProvider.class);
        metricsProviders.forEach(provider -> provider.initialize(context.getGlobalConfiguration()));
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public void send(final List<ProviderFutureResult> resultData) {
        if (resultData != null) {
            processSend(resultData);
        }
    }
    
    // =========================================================================
    // PRIVATE
    // =========================================================================
    private void processSend(final List<ProviderFutureResult> resultData) {
        //@formatter:off
        final List<MetricsData> data = resultData.stream()
                                           .filter(item->item.getData().isPresent())
                                           .map(item->item.getData().get())
                                           .flatMap(itemData->mapMetrics(itemData).stream())
                                           .collect(Collectors.toList()) ;
        //@formatter:on
        if (data != null) {
            for (final MetricsProvider provider : metricsProviders) {
                try {
                    provider.sendMetrics(data);
                }
                catch (final MetricsProviderException e) {
                    Loggers.METRICS.error(e.getMessage());
                }
            }
        }
    }
    
    private List<MetricsData> mapMetrics(final JsonObject item) {
        final Class<? extends JsonObject> clazz = item.getClass();
        final List<MetricsData> result = new ArrayList<>();
        
        final Optional<MetricsMapper> currentMapper = mappers.stream().filter(mapper -> mapper.accept(clazz)).findFirst();
        
        if (currentMapper.isPresent()) {
            
            final List<MetricsData> flatData = currentMapper.get().process(item, context.getGlobalConfiguration());
            if (flatData != null) {
                result.addAll(flatData);
            }
        }
        return result;
    }
    
    public void shutdown() {
        if (metricsProviders != null) {
            metricsProviders.forEach(item -> item.shutdown());
        }
        
    }
    
}
