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
package org.inugami.commons.spi;

import java.util.Comparator;

import javax.annotation.Priority;

/**
 * PriorityComparator
 * 
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
public class PriorityComparator<T> implements Comparator<T> {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public int compare(final T ref, final T value) {
        final int refPriority = loadPriority(ref);
        final int valuePriority = loadPriority(ref);
        return new Integer(refPriority).compareTo(valuePriority);
    }
    
    private int loadPriority(final T ref) {
        int result = 0;
        
        Priority priority = null;
        if (ref != null) {
            priority = ref.getClass().getAnnotation(Priority.class);
        }
        if (priority != null) {
            result = priority.value();
        }
        return result;
    }
}
