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
package io.inugami.monitoring.core.sensors.aggregators;

import io.inugami.framework.api.metrics.MetricsUtils;
import io.inugami.framework.interfaces.configurtation.ConfigHandler;
import io.inugami.framework.interfaces.monitoring.ServicesSensorAggregator;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModel;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModelDTO;
import io.inugami.framework.interfaces.spi.SpiPriority;
import io.inugami.monitoring.core.sensors.ServiceValueTypes;

import java.util.List;

/**
 * ServicesSensorAggregatorHits
 *
 * @author patrickguillerm
 * @since Jan 18, 2019
 */
@SpiPriority(0)
public class ServicesSensorAggregatorHits implements ServicesSensorAggregator {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public boolean accept(final GenericMonitoringModel data, final ConfigHandler<String, String> configuration) {
        return ServiceValueTypes.HITS.getKeywork().equals(data.getCounterType());
    }

    @Override
    public List<GenericMonitoringModel> compute(final GenericMonitoringModel data,
                                                final List<Object> values,
                                                final ConfigHandler<String, String> configuration) {


        final String timeUnit = configuration.grabOrDefault("timeUnit", "min");
        final var builder = GenericMonitoringModelDTO.builder()
                                                     .from(data);

        builder.timeUnit(timeUnit);
        if(data.getValueType()==null){
            builder.valueType("count");
        }
        builder.value(sum(values));

        return List.of(builder.build());
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    private Long sum(final List<Object> values) {
        long result = 0;

        for (final Long value : MetricsUtils.convertToLong(values)) {
            result += value.longValue();
        }
        return result;
    }
}
