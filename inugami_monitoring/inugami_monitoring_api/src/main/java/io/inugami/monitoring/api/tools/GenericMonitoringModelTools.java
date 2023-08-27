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

import io.inugami.api.monitoring.RequestContext;
import io.inugami.api.monitoring.RequestInformation;
import io.inugami.api.monitoring.models.GenericMonitoringModelDTO;
import io.inugami.api.tools.CalendarTools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * GenericMonitoringModelTools
 *
 * @author patrickguillerm
 * @since Jan 18, 2019
 */
public final class GenericMonitoringModelTools {

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private GenericMonitoringModelTools() {
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public static GenericMonitoringModelDTO.GenericMonitoringModelDTOBuilder initResultBuilder() {
        final RequestInformation infos = RequestContext.getInstance();

        final GenericMonitoringModelDTO.GenericMonitoringModelDTOBuilder data = GenericMonitoringModelDTO.builder();
        data.environment(infos.getEnv());
        data.asset(infos.getAsset());
        data.instanceName(infos.getInstanceName());
        data.instanceNumber(infos.getInstanceNumber());
        data.timestamp(CalendarTools.buildCalendarBySecond().getTimeInMillis());
        data.service(infos.getService());
        data.device(infos.getDeviceType());

        return data;
    }

    public static List<GenericMonitoringModelDTO> buildSingleResult(final GenericMonitoringModelDTO.GenericMonitoringModelDTOBuilder builder) {
        final List<GenericMonitoringModelDTO> result = new ArrayList<>();
        if (builder != null) {
            result.add(builder.build());
        }
        return result;
    }

    public static Long getPercentilValues(final List<Long> data, final double percentil) {
        return getPercentilValues(data, percentil, null);
    }

    public static <T> T getPercentilValues(final List<T> values, final double percentil,
                                           final Comparator<T> comparator) {
        T result = null;
        if ((values != null) && !values.isEmpty() && (percentil >= 0) && (percentil <= 1)) {
            final int size = values.size();
            if (comparator != null) {
                values.sort(comparator);
            }

            int index = (int) (values.size() * percentil);
            if (index < 0) {
                index = 0;
            }
            if (index >= size) {
                index = size - 1;
            }
            result = values.get(index);
        }
        return result;
    }

    public static String buildTimeUnit(final String timeUnit, final long interval) {
        String result = timeUnit;
        if (result == null) {
            result = String.format("%sms", interval);
        }
        return result;
    }

}
