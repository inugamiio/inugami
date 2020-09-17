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
package io.inugami.monitoring.providers.els;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;

import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.monitoring.models.GenericMonitoringModel;
import io.inugami.api.monitoring.senders.MonitoringSender;
import io.inugami.api.monitoring.senders.MonitoringSenderException;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.api.providers.ProviderWithHttpConnector;
import io.inugami.api.tools.CalendarTools;
import io.inugami.commons.connectors.HttpBasicConnector;
import io.inugami.commons.threads.RunAndCloseService;
import io.inugami.monitoring.api.tools.IntervalValues;

/**
 * ElsSender
 * 
 * @author patrick_guillerm
 * @since 18 janv. 2019
 */
public class ElsSender implements MonitoringSender, ProviderWithHttpConnector {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final HttpBasicConnector                     httpConnector;
    
    private final int                                    timeout;
    
    private final String                                 url;
    
    private final String                                 elsType;
    
    private final String                                 elsIndex;
    
    private final String                                 indexTimestampFormat;
    
    private final boolean                                enableIndexTimestamped;
    
    private final IntervalValues<GenericMonitoringModel> intervalRunner;
    
    private final int                                    maxThreads;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ElsSender() {
        timeout = 10000;
        httpConnector = null;
        url = null;
        intervalRunner = null;
        elsType = "GenericServiceHitModel";
        elsIndex = "system";
        enableIndexTimestamped = true;
        indexTimestampFormat = "yyyy-MM";
        maxThreads = 10;
    }
    
    public ElsSender(final ConfigHandler<String, String> config) {
        //@formatter:off
        timeout = getTimeout(config,       10000);
        httpConnector =  new  HttpBasicConnector(timeout,
                                                 getTTL(config,             500),
                                                 getMaxPerRoute(config,      50),
                                                 getMaxPerRoute(config,      50),
                                                 getSocketTimeout(config,  9500));
        //@formatter:on
        url = config.grab("url");
        elsType = config.grabOrDefault("elsType", "GenericServiceHitModel");
        elsIndex = config.grabOrDefault("elsIndex", "system");
        enableIndexTimestamped = config.grabBoolean("enableIndexTimestamped", true);
        indexTimestampFormat = config.grabOrDefault("indexTimestampFormat", "yyyy-MM");
        this.intervalRunner = new IntervalValues<GenericMonitoringModel>(this::sendToEls, 500L);
        this.maxThreads = config.grabInt("maxThreads", 10);
    }
    
    @Override
    public MonitoringSender buildInstance(final ConfigHandler<String, String> configuration) {
        return new ElsSender(configuration);
    }
    
    @Override
    public String getName() {
        return "els";
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void process(final List<GenericMonitoringModel> data) throws MonitoringSenderException {
        intervalRunner.addValues(data);
    }
    
    public void sendToEls(final Queue<GenericMonitoringModel> data) {
        final List<JsonObject> values = new ArrayList<>();
        
        while (!data.isEmpty()) {
            values.add(data.poll());
        }
        
        if (!values.isEmpty()) {
            processSending(values);
        }
        
    }
    
    private void processSending(final List<JsonObject> values) {
        final ElsData elsData = new ElsData(buildIndex(), elsType);
        final int nbItems = 500;
        
        final List<Callable<Void>> tasks = new ArrayList<>();
        final int size = values.size();
        final int step = size / nbItems;
        for (int i = 0; i < (step + 1); i++) {
            final int begin = i * nbItems;
            int end = begin + nbItems;
            if (end > size) {
                end = size;
            }
            tasks.add(new ElasticSearchWriterTask(httpConnector, url, elsData, values.subList(begin, end)));
        }
        final int nbThreads = tasks.size() < maxThreads ? tasks.size() : maxThreads;
        //@formatter:off
        final RunAndCloseService<Void> treadsPool = new RunAndCloseService<>(this.getClass().getSimpleName(),
                                                                             timeout,
                                                                             nbThreads,
                                                                             tasks,
                                                                             null);
        //@formatter:on
        treadsPool.run();
        Loggers.PROVIDER.info("done sending data to ELS : {} documents, {} : {}", values.size(), elsData.getIndex(),
                              elsData.getType());
    }
    
    private String buildIndex() {
        final StringBuilder result = new StringBuilder();
        result.append(elsIndex);
        if (enableIndexTimestamped) {
            result.append("_");
            result.append(new SimpleDateFormat(indexTimestampFormat).format(CalendarTools.buildCalendarByMin().getTime()));
        }
        return result.toString();
    }
    
}
