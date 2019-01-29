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
package org.inugami.commons.engine.js.objects;

import java.util.regex.Pattern;

/**
 * RegexFlags
 * 
 * @author patrick_guillerm
 * @since 29 janv. 2018
 */
public enum RegexFlags {
    
    // =========================================================================
    // ENUM
    // =========================================================================
    DEFAULT(0),
    
    DOTALL(Pattern.DOTALL, "g"),
    
    CASE_INSENSITIVE(Pattern.CASE_INSENSITIVE, "i"),
    
    MULTI(Pattern.MULTILINE, "m"),
    
    UNICODE(Pattern.UNICODE_CASE, "u");
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final int    flag;
    
    private final String param;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    RegexFlags(final int flag) {
        this.flag = flag;
        this.param = null;
    }
    
    RegexFlags(final int flag, final String param) {
        this.flag = flag;
        this.param = param;
    }
    
    public static RegexFlags getEnum(final String key) {
        RegexFlags result = DEFAULT;
        if (key != null) {
            final String localKey = key.toLowerCase();
            for (final RegexFlags item : RegexFlags.values()) {
                if ((item != RegexFlags.DEFAULT) && item.param.equals(localKey)) {
                    result = item;
                }
            }
            
        }
        
        return result;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public int getFlag() {
        return flag;
    }
    
    public String getParam() {
        return param;
    }
    
}
