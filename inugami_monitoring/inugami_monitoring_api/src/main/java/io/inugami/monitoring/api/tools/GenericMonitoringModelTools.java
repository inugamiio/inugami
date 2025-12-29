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
package io.inugami.monitoring.api.tools;


import io.inugami.framework.api.monitoring.RequestContext;
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import io.inugami.framework.interfaces.monitoring.models.GenericMonitoringModelDTO;
import io.inugami.framework.interfaces.tools.CalendarTools;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.Nullable;

import java.util.*;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

/**
 * GenericMonitoringModelTools
 *
 * @author patrickguillerm
 * @since Jan 18, 2019
 */
@UtilityClass
public final class GenericMonitoringModelTools {

    // =================================================================================================================
    // METHODS
    // =================================================================================================================
    public static GenericMonitoringModelDTO initResultBuilder() {
        final RequestData infos = RequestContext.getInstance();
        final var         data  = GenericMonitoringModelDTO.builder();

        data.environment(infos.getEnv());
        data.asset(infos.getAsset());
        data.instanceName(infos.getInstanceName());
        data.instanceNumber(infos.getInstanceNumber());
        data.timestamp(CalendarTools.buildCalendarBySecond().getTimeInMillis());
        data.service(infos.getService());
        data.device(infos.getDeviceType());

        return data.build();
    }

    public static List<GenericMonitoringModelDTO> buildSingleResult(@Nullable final GenericMonitoringModelDTO value) {
        final List<GenericMonitoringModelDTO> result = new ArrayList<>();
        applyIfNotNull(value, result::add);
        return result;
    }

    public static @Nullable Long getPercentilValues(final List<Long> data, final double percentil) {
        final List<Long> values = new ArrayList<>(Optional.ofNullable(data).orElse(List.of()));
        Collections.sort(values);
        return getPercentilValues(values, percentil, null);
    }

    @SuppressWarnings({"java:S1612"})
    public static <T> @Nullable T getPercentilValues(final List<T> values, final double percentil,
                                                     final Comparator<T> comparator) {

        if (values == null || values.isEmpty() || percentil < 0 || percentil > 1) {
            return null;
        }

        final int size = values.size();
        applyIfNotNull(comparator, c -> values.sort(c));

        int index = (int) (values.size() * percentil);
        if (index < 0) {
            index = 0;
        }
        if (index >= size) {
            index = size - 1;
        }
        return values.get(index);
    }

    public static String buildTimeUnit(final String timeUnit, final long interval) {
        String result = timeUnit;
        if (result == null) {
            result = String.format("%sms", interval);
        }
        return result;
    }

}
