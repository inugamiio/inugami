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


import io.inugami.framework.api.connectors.HttpBasicConnector;
import io.inugami.framework.commons.threads.RunAndCloseService;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.connectors.config.HttpBasicConnectorConfiguration;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;
import io.inugami.framework.interfaces.monitoring.senders.MonitoringSender;
import io.inugami.framework.interfaces.monitoring.senders.MonitoringSenderException;
import io.inugami.framework.interfaces.providers.ProviderWithHttpConnector;
import io.inugami.framework.interfaces.tools.CalendarTools;
import io.inugami.monitoring.api.tools.IntervalValues;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Callable;

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
    private final HttpBasicConnector httpConnector;

    private final int timeout;

    private final String url;

    private final String elsType;

    private final String elsIndex;

    private final String indexTimestampFormat;

    private final boolean enableIndexTimestamped;

    private final IntervalValues<GenericMonitoringModel> intervalRunner;

    private final int maxThreads;

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
        timeout = getTimeout(config,       30000);
        httpConnector =  new  HttpBasicConnector(HttpBasicConnectorConfiguration.builder()
                                                         .timeoutConnecting(timeout)
                                                                                .timeoutWriting(timeout)
                                                                                .timeoutReading(timeout)
                                                                                .build());

        //@formatter:on
        url = config.grab("url");
        elsType = config.grabOrDefault("elsType", "GenericServiceHitModel");
        elsIndex = config.grabOrDefault("elsIndex", "system");
        enableIndexTimestamped = config.grabBoolean("enableIndexTimestamped", true);
        indexTimestampFormat = config.grabOrDefault("indexTimestampFormat", "yyyy-MM");
        this.intervalRunner = IntervalValues.<GenericMonitoringModel>builder()
                                            .consumer(this::sendToEls)
                                            .interval(500L)
                                            .build();
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
        final List<Object> values = new ArrayList<>();

        while (!data.isEmpty()) {
            values.add(data.poll());
        }

        if (!values.isEmpty()) {
            processSending(values);
        }

    }

    private void processSending(final List<Object> values) {
        final ElsData elsData = ElsData.builder()
                                       .index(buildIndex())
                                       .type(elsType)
                                       .build();
        final int nbItems = 500;

        final List<Callable<Void>> tasks = new ArrayList<>();
        final int                  size  = values.size();
        final int                  step  = size / nbItems;
        for (int i = 0; i < (step + 1); i++) {
            final int begin = i * nbItems;
            int       end   = begin + nbItems;
            if (end > size) {
                end = size;
            }
            tasks.add(ElasticSearchWriterTask.builder()
                                             .httpConnector(httpConnector)
                                             .url(url)
                                             .data(elsData)
                                             .values(values.subList(begin, end))
                                             .build());
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
        Loggers.PROVIDER.info("done sending data to ELS : {} documents, {} : {}", values.size(), elsData.getIndex(), elsData.getType());
    }

    private String buildIndex() {
        final StringBuilder result = new StringBuilder();
        result.append(elsIndex);
        if (enableIndexTimestamped) {
            result.append("_");
            result.append(new SimpleDateFormat(indexTimestampFormat).format(CalendarTools.buildCalendarByMin()
                                                                                         .getTime()));
        }
        return result.toString();
    }

}
