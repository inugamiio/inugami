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
package io.inugami.monitoring.api.exceptions;

import java.util.Map;
import java.util.regex.Pattern;

import io.inugami.api.exceptions.ErrorCode;

public interface ExceptionHandlerMapper {
    Map<Pattern, ErrorCode> produceMapping();
    
    String ERROR_TECHNICAL  = "technical";
    
    String ERROR_FUNCTIONAL = "functional";
    
    default Pattern buildPattern(final String regex) {
        return Pattern.compile(regex);
    }
    
}
