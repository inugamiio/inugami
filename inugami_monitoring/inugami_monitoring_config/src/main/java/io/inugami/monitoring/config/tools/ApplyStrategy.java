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
package io.inugami.monitoring.config.tools;

import io.inugami.api.constants.JvmKeyValues;

public class ApplyStrategy {
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private ApplyStrategy() {
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static String applyStrategy(final JvmKeyValues jvmKey, final String value) {
        return applyStrategy(jvmKey, value, null);
    }
    
    public static String applyStrategy(final JvmKeyValues jvmKey, final String value, final String defaultValue) {
        String result = jvmKey.get();
        if (result == null) {
            result = value == null ? defaultValue : value;
        }
        return result;
    }
    
    public static Boolean applyStrategyBoolean(final JvmKeyValues jvmKey, final Boolean enable,
                                               final boolean defaultValue) {
        Boolean result = null;
        
        if (jvmKey.get() != null) {
            result = Boolean.parseBoolean(jvmKey.get());
        }
        else {
            result = enable == null ? defaultValue : enable;
        }
        return result;
    }
}
