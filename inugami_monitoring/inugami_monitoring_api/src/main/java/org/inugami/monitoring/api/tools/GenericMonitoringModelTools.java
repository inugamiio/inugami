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
package org.inugami.monitoring.api.tools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.inugami.api.monitoring.RequestContext;
import org.inugami.api.monitoring.RequestInformation;
import org.inugami.api.monitoring.models.GenericMonitoringModel;
import org.inugami.api.monitoring.models.GenericMonitoringModelBuilder;
import org.inugami.api.tools.CalendarTools;

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
    public static GenericMonitoringModelBuilder initResultBuilder() {
        final RequestInformation infos = RequestContext.getInstance();
        
        final GenericMonitoringModelBuilder data = new GenericMonitoringModelBuilder();
        data.setEnvironment(infos.getEnv());
        data.setAsset(infos.getAsset());
        data.setInstanceName(infos.getInstanceName());
        data.setInstanceNumber(infos.getInstanceNumber());
        data.setTimestamp(CalendarTools.buildCalendarBySecond().getTimeInMillis());
        data.setService(infos.getService());
        data.setDevice(infos.getDeviceType());
        
        return data;
    }
    
    public static List<GenericMonitoringModel> buildSingleResult(final GenericMonitoringModelBuilder builder) {
        final List<GenericMonitoringModel> result = new ArrayList<>();
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
