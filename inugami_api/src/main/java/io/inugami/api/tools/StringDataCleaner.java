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
package io.inugami.api.tools;

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
        return StringTools.replaceAll(regex, data, replacement);
    }
}
