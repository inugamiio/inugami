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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringDataCleaner
 * 
 * @author patrick_guillerm
 * @since 3 mai 2018
 */
@FunctionalInterface
public interface StringDataCleaner {
    
    default boolean accept(final String data) {
        return true;
    }
    
    String clean(String data);
    
    default String replaceAll(final Pattern regex, final String data, final String replacement) {
        String result = data;
        final Matcher matcher = regex.matcher(data);
        
        if (matcher.find()) {
            result = matcher.replaceAll(replacement);
        }
        
        return result;
    }
}
