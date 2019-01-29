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
package org.inugami.api.exceptions;

import java.text.MessageFormat;
import java.util.regex.Pattern;

/**
 * MessagesFormatter
 * 
 * @author patrick_guillerm
 * @since 22 juil. 2016
 */
public final class MessagesFormatter {
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private final static Pattern REGEX = Pattern.compile("[{][^}]+[}]");
    
    private MessagesFormatter() {
        
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static String format(final String format, final Object... values) {
        String result = null;
        if (format != null) {
            if ((values == null) || (values.length == 0)) {
                result = format;
            }
            else if (REGEX.matcher(format).find()) {
                final MessageFormat formater = new MessageFormat(format.replaceAll("'", "''"));
                result = formater.format(values);
            }
            else {
                result = format;
            }
        }
        return result;
    }
}
