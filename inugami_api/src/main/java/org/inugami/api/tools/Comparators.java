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
package org.inugami.api.tools;

import java.util.Comparator;

/**
 * Comparators
 * 
 * @author patrickguillerm
 * @since Jan 17, 2019
 */
public final class Comparators {
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private Comparators() {
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    //@formatter:off
    public static Comparator<Long> longComparator = (ref, value) -> {
        int result = 0;
        if ((ref == null) && (value == null)) {
            result = 0;
        }
        else if ((ref == null) && (value != null)) {
            result = 1;
        }
        else {
            result = ref.compareTo(value);
        }
        return result;
    };
    
    public static Comparator<Double> doubleComparator = (ref, value) -> {
        int result = 0;
        if ((ref == null) && (value == null)) {
            result = 0;
        }
        else if ((ref == null) && (value != null)) {
            result = 1;
        }
        else {
            result = ref.compareTo(value);
        }
        return result;
    };
    //@formatter:on
}
