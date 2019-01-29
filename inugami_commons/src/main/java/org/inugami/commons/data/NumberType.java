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
package org.inugami.commons.data;

import java.math.BigDecimal;

/**
 * NumberType
 * 
 * @author patrick_guillerm
 * @since 8 nov. 2017
 */
public enum NumberType {
    BYTE,
    
    SHORT,
    
    INTEGER,
    
    LONG,
    
    FLOAT,
    
    DOUBLE,
    
    BIG_DECIMAL;
    
    public synchronized static NumberType getType(final Number value) {
        NumberType result = null;
        if (value != null) {
            if (value instanceof Byte) {
                result = NumberType.BYTE;
            }
            else if (value instanceof Short) {
                result = NumberType.SHORT;
            }
            else if (value instanceof Integer) {
                result = NumberType.INTEGER;
            }
            else if (value instanceof Long) {
                result = NumberType.LONG;
            }
            else if (value instanceof Float) {
                result = NumberType.FLOAT;
            }
            else if (value instanceof Double) {
                result = NumberType.DOUBLE;
            }
            else if (value instanceof BigDecimal) {
                result = NumberType.BIG_DECIMAL;
            }
        }
        return result;
    }
    
}
