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
package io.inugami.commons.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import io.inugami.api.exceptions.Asserts;
import io.inugami.commons.data.model.Record;

/**
 * Aggregator
 * 
 * @author patrick_guillerm
 * @since 8 nov. 2017
 */
public class AggregatorHelper {
    
    // =========================================================================
    // SUM OBJECT
    // =========================================================================
    public <R extends Number, T> R sumObject(final T ref, final T newValue, final Function<T, R> extractor) {
        Asserts.notNull("Extractor function is mandatory!", extractor);
        R result = ref == null ? null : extractor.apply(ref);
        
        if (newValue != null) {
            if (result == null) {
                result = extractor.apply(newValue);
            }
            else {
                final R newValueExtracted = extractor.apply(newValue);
                if (newValueExtracted != null) {
                    result = sumGenericType(result, newValueExtracted);
                }
            }
        }
        
        return result;
    }
    
    public <R extends Number> R sumGenericType(final R ref, final R newValue) {
        R result = null;
        final NumberType numberType = NumberType.getType(ref);
        Asserts.notNull("Can't manage number type " + ref.getClass().getName(), numberType);
        
        switch (numberType) {
            case BYTE:
                result = (R) Byte.valueOf((byte) ((Byte) ref + (Byte) newValue));
                break;
            case SHORT:
                result = (R) Short.valueOf((short) ((Short) ref + (Short) newValue));
                break;
            case INTEGER:
                result = (R) Integer.valueOf((Integer) ref + (Integer) newValue);
                break;
            case LONG:
                result = (R) Long.valueOf((Long) ref + (Long) newValue);
                break;
            case FLOAT:
                result = (R) Float.valueOf((Float) ref + (Float) newValue);
                break;
            case DOUBLE:
                result = (R) Double.valueOf((Double) ref + (Double) newValue);
                break;
            case BIG_DECIMAL:
                result = (R) ((BigDecimal) ref).add((BigDecimal) newValue);
                break;
            default:
                break;
        }
        
        return result;
    }
    
    // =========================================================================
    // SUM MAP
    // =========================================================================
    public <N extends Number, K> Map<K, N> sumMap(final Map<K, N> ref, final Map<K, N> newValue) {
        Asserts.notNull("Reference map is mandatory!", ref);
        Asserts.notNull("new map is mandatory!", newValue);
        
        final Map<K, N> result = new HashMap<K, N>();
        final List<K> foundKeys = new ArrayList<>();
        
        for (final Map.Entry<K, N> entry : ref.entrySet()) {
            final K key = entry.getKey();
            if (newValue.containsKey(key)) {
                result.put(key, sumGenericType(entry.getValue(), newValue.get(key)));
                foundKeys.add(key);
            }
            else {
                result.put(key, entry.getValue());
            }
        }
        
        for (final Map.Entry<K, N> entry : newValue.entrySet()) {
            final K key = entry.getKey();
            if (!foundKeys.contains(key)) {
                if (ref.containsKey(key)) {
                    result.put(key, sumGenericType(entry.getValue(), newValue.get(key)));
                }
                else {
                    result.put(key, entry.getValue());
                }
            }
        }
        
        return result;
    }
    
    // =========================================================================
    // KEEP RECORD
    // =========================================================================
    public Record keepRecord(final double value, final long timestamp, final Record previousRecord, final String unit) {
        Asserts.notNull("Value number is mandatory!", value);
        
        Record result = null;
        if (previousRecord == null) {
            result = new Record(timestamp, value, unit);
        }
        else {
            result = value > previousRecord.getValue() ? new Record(timestamp, value, unit) : previousRecord;
        }
        
        return result;
    }
}
