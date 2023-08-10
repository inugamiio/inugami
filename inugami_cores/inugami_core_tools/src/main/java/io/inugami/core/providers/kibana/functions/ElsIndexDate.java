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
package io.inugami.core.providers.kibana.functions;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.inugami.configuration.services.functions.ProviderAttributFunction;
import io.inugami.core.providers.functions.StartHourAtFunction;

/**
 * ElsIndexDate
 * 
 * @author patrick_guillerm
 * @since 3 oct. 2017
 */
public class ElsIndexDate extends StartHourAtFunction implements ProviderAttributFunction {
    
    private static final String ELS_INDEX_DATE = "elsIndexDate";
    
    @Override
    protected String formatTime(final long timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(timestamp));
    }
    
    @Override
    public String getName() {
        return ELS_INDEX_DATE;
    }
}
