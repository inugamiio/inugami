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
package io.inugami.core.context.scripts;

import io.inugami.api.alertings.AlertingResult;

/**
 * JsBuildersHelper
 * 
 * @author patrick_guillerm
 * @since 27 déc. 2017
 */
public class JsBuildersHelper {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static AlertingResult buildAlert(final String level, final String message, final Object data) {
        final AlertingResult result = new AlertingResult();
        result.setMessage(message);
        result.setLevel(level);
        result.setData(data);
        return result;
    }
}
