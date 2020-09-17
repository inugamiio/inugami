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
package io.inugami.core.alertings.messages;

import java.util.List;

import io.inugami.api.alertings.AlertingResult;

/**
 * DefaultAlertMessageDataExtractor
 * 
 * @author patrick_guillerm
 * @since 22 mars 2018
 */
public class DefaultAlertMessageDataExtractor implements AlertMessageDataExtractorSPI {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public boolean accept(final String value, final AlertingResult alert) {
        return true;
    }
    
    @Override
    public List<String> extract(final String value, final AlertingResult alert) {
        return null;
    }
}
