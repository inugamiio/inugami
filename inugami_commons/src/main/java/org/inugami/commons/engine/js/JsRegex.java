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
package org.inugami.commons.engine.js;

import java.util.regex.Pattern;

import javax.script.Bindings;

import org.inugami.commons.engine.js.objects.RegexFlags;

/**
 * JsRegex
 * 
 * @author patrick_guillerm
 * @since 29 janv. 2018
 */
public final class JsRegex {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static Pattern newRegEx(final String regex, final String options) {
        final RegexFlags flag = RegexFlags.getEnum(options);
        return flag == RegexFlags.DEFAULT ? Pattern.compile(regex) : Pattern.compile(regex, flag.getFlag());
    }
    
    public static boolean match(final String value, final Object regex) {
        boolean result = false;
        Pattern pattern = null;
        if (regex instanceof String) {
            pattern = Pattern.compile((String) regex);
        }
        else if (regex instanceof Bindings) {
            final Object patternRaw = ((Bindings) regex).getOrDefault("_innnerRegEx", null);
            if (patternRaw instanceof Pattern) {
                pattern = (Pattern) patternRaw;
            }
            
        }
        
        if (pattern != null) {
            result = pattern.matcher(value).matches();
        }
        return result;
    }
    
    public static String trim(final String value) {
        return value == null ? null : value.trim();
    }
}
