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
import io.inugami.framework.interfaces.models.number.FloatNumber;
import io.inugami.framework.interfaces.models.number.LongNumber;
import io.inugami.framework.interfaces.monitoring.ServicesSensorAggregator;
import io.inugami.framework.interfaces.monitoring.models.CurrentApplicationDTO;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModelDTO;
import io.inugami.framework.interfaces.monitoring.models.MonitoringContextDTO;
import io.inugami.framework.interfaces.monitoring.sensors.MonitoringSensor;
import io.inugami.framework.interfaces.spi.SpiLoader;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;
import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNull;

/**
 * ServicesSensor
 *
 * @author patrickguillerm
 * @since Jan 18, 2019
 */
public class ServicesSensor implements MonitoringSensor {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    protected static final LinkedBlockingQueue<GenericMonitoringModel> BUFFER = new LinkedBlockingQueue<>();

    protected static final List<ServicesSensorAggregator> AGGREGATORS     = SpiLoader.getInstance()
                                                                                     .loadSpiServicesByPriority(ServicesSensorAggregator.class);
    public static final    String                         SERVICES_SENSOR = "servicesSensor";
    private                CurrentApplicationDTO          currentApplication;

    protected static long                          interval;
    protected        MonitoringContextDTO          context;
    protected        ConfigHandler<String, String> configuration;

    // =================================================================================================================
    // CONSTRUCTORS
    // =================================================================================================================
    @Override
    public MonitoringSensor buildInstance(final long interval,
                                          final String query,
                                          final ConfigHandler<String, String> configuration,
                                          final MonitoringContextDTO context) {
        defineInterval(interval);
        this.configuration = configuration;
        this.context       = context;
        return this;
    }

    protected static void clean() {
        BUFFER.clear();
    }

    protected synchronized void defineInterval(final long value) {
        interval = value;
    }

    @Override
    public long getInterval() {
        return interval;
    }

    @Override
    public String getName() {
        return SERVICES_SENSOR;
    }

    // =================================================================================================================
    // OVERRIDES
    // =================================================================================================================
    @Override
    public List<GenericMonitoringModel> process() {
        return processExtracting();
    }

    protected synchronized List<GenericMonitoringModel> processExtracting() {
        final List<GenericMonitoringModel> result = new ArrayList<>();
        final List<GenericMonitoringModel> data   = new ArrayList<>();
        BUFFER.drainTo(data);

        if (data.isEmpty()) {
            return List.of();
        }
        final Map<GenericMonitoringModelDTO, List<Object>> reducedValues = reduceData(data);

        for (final Map.Entry<GenericMonitoringModelDTO, List<Object>> entry : reducedValues.entrySet()) {
            result.addAll(computeValue(entry.getKey(), entry.getValue()));
        }

        return result;
    }

    protected Map<GenericMonitoringModelDTO, List<Object>> reduceData(final List<GenericMonitoringModel> data) {
        final Map<String, Tuple<GenericMonitoringModelDTO, List<Object>>> localBuffer = new HashMap<>();

        for (final GenericMonitoringModel item : data) {
            final var                                      id     = item.getNonTemporalHash();
            Tuple<GenericMonitoringModelDTO, List<Object>> bucket = localBuffer.get(id);
            if (bucket == null) {
                bucket = Tuple.<GenericMonitoringModelDTO, List<Object>>builder()
                              .key(GenericMonitoringModelDTO.builder().from(item).build())
                              .value(new ArrayList<>())
                              .build();
                localBuffer.put(id, bucket);
            }
            bucket.getValue().add(item.getValue());
        }

        final Map<GenericMonitoringModelDTO, List<Object>> result = new HashMap<>();
        for (final Map.Entry<String, Tuple<GenericMonitoringModelDTO, List<Object>>> entry : localBuffer.entrySet()) {
            result.put(entry.getValue().getKey(), entry.getValue().getValue());
        }
        return result;
    }

    protected List<GenericMonitoringModel> computeValue(final GenericMonitoringModelDTO data,
                                                        final List<Object> values) {

        if (values == null || values.isEmpty()) {
            return List.of();
        }
        if (values.size() == 1) {
            return List.of(processCleanValue(data.toBuilder()
                                                 .value(values.getFirst())
                                                 .build()));
        }


        final ServicesSensorAggregator currentAggregator = chooseAggregator(data);
        return Optional.ofNullable(currentAggregator)
                       .map(aggregator -> processAggregation(aggregator, data, values))
                       .orElse(List.of());
    }

    protected List<GenericMonitoringModel> processAggregation(final ServicesSensorAggregator currentAggregator,
                                                              final GenericMonitoringModel data,
                                                              final List<Object> values) {
        List<GenericMonitoringModel>       result     = new ArrayList<>();
        final List<GenericMonitoringModel> aggregated = currentAggregator.compute(data, values, configuration);
        for (GenericMonitoringModel value : Optional.ofNullable(aggregated).orElse(List.of())) {
            GenericMonitoringModelDTO cleanValue = processCleanValue(value);
            applyIfNotNull(cleanValue, result::add);
        }
        return result;
    }

    private GenericMonitoringModelDTO processCleanValue(final GenericMonitoringModel kpi) {
        if(kpi.getValue()==null){
            return null;
        }
        final var builder = GenericMonitoringModelDTO.builder().from(kpi);

        applyIfNull(kpi.getEnvironment(), () -> builder.environment(context.getCurrentApplication().getEnv()));
        applyIfNull(kpi.getAsset(), () -> builder.asset(context.getCurrentApplication().getAsset()));
        applyIfNull(kpi.getInstanceName(), () -> builder.instanceName(context.getCurrentApplication()
                                                                               .getInstanceName()));
        applyIfNull(kpi.getInstanceNumber(), () -> builder.instanceNumber(context.getCurrentApplication()
                                                                                   .getInstanceNumber()));
        if(kpi.getDate() == null){
            final LocalDateTime now = LocalDateTime.now(context.getClock());
            builder.date(now);
            builder.timestamp(now.toEpochSecond(ZoneOffset.UTC));
        }

        if(kpi.getValue() instanceof Integer v){
            builder.value(LongNumber.of(v));
        }
        if(kpi.getValue() instanceof Long v){
            builder.value(LongNumber.of(v));
        }
        if(kpi.getValue() instanceof Float v){
            builder.value(FloatNumber.of(v));
        }
        if(kpi.getValue() instanceof Double v){
            builder.value(FloatNumber.of(v));
        }

        return builder.build();
    }

    protected ServicesSensorAggregator chooseAggregator(final GenericMonitoringModel data) {
        for (final ServicesSensorAggregator aggregator : AGGREGATORS) {
            if (aggregator.accept(data, configuration)) {
                return aggregator;
            }
        }
        return null;
    }

    // =================================================================================================================
    // ADD DATA
    // =================================================================================================================
    public static synchronized void addData(final List<GenericMonitoringModelDTO> data) {
        applyIfNotNull(data, BUFFER::addAll);
    }

}
