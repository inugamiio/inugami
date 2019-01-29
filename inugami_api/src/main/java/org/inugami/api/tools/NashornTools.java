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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.script.Bindings;

/**
 * NashornTools
 * 
 * @author patrick_guillerm
 * @since 7 févr. 2018
 */
public final class NashornTools {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String OBJECT_ARRAY      = "[object Array]";
    
    private static final String NASHORN_UNDEFINED = "jdk.nashorn.internal.runtime.Undefined";
    
    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    private NashornTools() {
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static boolean isUndefine(final Object object) {
        return (object == null) || NASHORN_UNDEFINED.equals(object.getClass().getName());
    }
    
    public static boolean isArray(final Object object) {
        boolean result = false;
        if ((object != null) && (object instanceof Bindings)) {
            result = OBJECT_ARRAY.equals(String.valueOf(object));
        }
        return result;
    }
    
    public static List<Object> convertToList(final Object object) {
        List<Object> result = null;
        if (object instanceof Bindings) {
            final Collection<Object> raw = ((Bindings) object).values();
            if (raw != null) {
                result = new ArrayList<>();
                result.addAll(raw);
            }
        }
        return result;
    }
    
}
