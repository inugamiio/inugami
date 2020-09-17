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
package io.inugami.commons.engine.js;

import java.util.Date;

import io.inugami.api.models.data.graphite.TimeValue;
import io.inugami.api.models.data.graphite.number.FloatNumber;
import io.inugami.api.models.data.graphite.number.GraphiteNumber;
import io.inugami.api.models.data.graphite.number.LongNumber;
import io.inugami.api.tools.NashornTools;

/**
 * JavaScriptEngineBuildersFunctions
 * 
 * @author patrick_guillerm
 * @since 28 mai 2018
 */
public final class JavaScriptEngineBuildersFunctions {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private JavaScriptEngineBuildersFunctions() {
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static TimeValue timeValue(final Object path, final Object value, final Object time) {
        TimeValue result = null;
        if (!NashornTools.isUndefine(path) && !NashornTools.isUndefine(value)) {
            final String localPath = String.valueOf(path);
            final GraphiteNumber localValue = converToGraphiteNumber(value);
            final Long timestamp = converToLong(time);
            
            result = new TimeValue(localPath, localValue, timestamp == null ? new Date().getTime() / 1000 : timestamp);
            
        }
        return result;
    }
    
    // =========================================================================
    // CONVERT
    // =========================================================================
    private static GraphiteNumber converToGraphiteNumber(final Object value) {
        GraphiteNumber result = null;
        if (!NashornTools.isUndefine(value)) {
            if (value instanceof Float) {
                result = new FloatNumber((Float) value);
            }
            else if (value instanceof Double) {
                result = new FloatNumber((Double) value);
            }
            
            else if (value instanceof Short) {
                result = new LongNumber((Short) value);
            }
            else if (value instanceof Integer) {
                result = new LongNumber((Integer) value);
            }
            else if (value instanceof Long) {
                result = new LongNumber((Long) value);
            }
            
        }
        return result;
    }
    
    private static Long converToLong(final Object value) {
        Long result = null;
        if (!NashornTools.isUndefine(value)) {
            result = Long.parseLong(String.valueOf(value));
        }
        return result;
    }
}
