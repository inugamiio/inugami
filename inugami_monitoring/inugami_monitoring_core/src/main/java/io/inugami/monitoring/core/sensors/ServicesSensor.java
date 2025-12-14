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
package io.inugami.monitoring.core.sensors;

import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.models.Tuple;
import io.inugami.framework.interfaces.monitoring.ServicesSensorAggregator;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModelDTO;
import io.inugami.framework.interfaces.monitoring.sensors.MonitoringSensor;
import io.inugami.framework.interfaces.spi.SpiLoader;
import io.inugami.framework.interfaces.tools.BlockingQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

/**
 * ServicesSensor
 *
 * @author patrickguillerm
 * @since Jan 18, 2019
 */
public class ServicesSensor implements MonitoringSensor {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    protected static final BlockingQueue<GenericMonitoringModel> BUFFER = new BlockingQueue<>();

    protected static final List<ServicesSensorAggregator> AGGREGATORS = SpiLoader.getInstance()
                                                                                 .loadSpiServicesByPriority(ServicesSensorAggregator.class);

    protected static long interval;

    protected ConfigHandler<String, String> configuration;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @Override
    public MonitoringSensor buildInstance(final long interval, final String query,
                                          final ConfigHandler<String, String> configuration) {
        defineInterval(interval);
        this.configuration = configuration;
        return this;
    }

    protected synchronized void defineInterval(final long value) {
        interval = value;
    }

    //
    @Override
    public long getInterval() {
        return interval;
    }

    @Override
    public String getName() {
        return "servicesSensor";
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public List<GenericMonitoringModel> process() {
        final List<GenericMonitoringModel>              result        = new ArrayList<>();
        final List<GenericMonitoringModel>              data          = BUFFER.pollAll();
        final Map<GenericMonitoringModel, List<Object>> reducedValues = reduceData(data);

        for (final Map.Entry<GenericMonitoringModel, List<Object>> entry : reducedValues.entrySet()) {
            result.addAll(computeValue(entry.getKey(), entry.getValue()));
        }

        return result;
    }

    protected Map<GenericMonitoringModel, List<Object>> reduceData(final List<GenericMonitoringModel> data) {
        final Map<String, Tuple<GenericMonitoringModel, List<Object>>> localBuffer = new HashMap<>();

        for (final GenericMonitoringModel item : data) {
            Tuple<GenericMonitoringModel, List<Object>> saved = localBuffer.get(item.getNonTemporalHash());
            if (saved == null) {
                final List<Object> values = new ArrayList<>();
                values.add(item.getValue());
                saved = new Tuple<>(item, values);
                localBuffer.put(item.getNonTemporalHash(), saved);
            } else if (item.getValue() != null) {
                saved.getValue().add(item.getValue());
            }
        }

        final Map<GenericMonitoringModel, List<Object>> result = new HashMap<>();
        for (final Map.Entry<String, Tuple<GenericMonitoringModel, List<Object>>> entry : localBuffer.entrySet()) {
            result.put(entry.getValue().getKey(), entry.getValue().getValue());
        }
        return result;
    }

    protected List<GenericMonitoringModel> computeValue(final GenericMonitoringModel data,
                                                        final List<Object> values) {
        final ServicesSensorAggregator currentAggregator = chooseAggregator(data);
        return processAggregation(currentAggregator, data, values);
    }

    protected List<GenericMonitoringModel> processAggregation(final ServicesSensorAggregator currentAggregator,
                                                              final GenericMonitoringModel data,
                                                              final List<Object> values) {
        List<GenericMonitoringModel> result = new ArrayList<>();
        if (currentAggregator != null) {
            final List<GenericMonitoringModel> aggregated = currentAggregator.compute(data, values, configuration);
            applyIfNotNull(aggregated, result::addAll);
        }
        return result;
    }

    protected ServicesSensorAggregator chooseAggregator(final GenericMonitoringModel data) {

        for (final ServicesSensorAggregator aggregator : AGGREGATORS) {
            if (aggregator.accept(data, configuration)) {
                return aggregator;
            }
        }
        return null;
    }

    // =========================================================================
    // ADD DATA
    // =========================================================================
    public static synchronized void addData(final List<GenericMonitoringModelDTO> data) {
        if (data != null) {
            BUFFER.addAll(data);
        }
    }

}
