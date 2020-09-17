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

import io.inugami.configuration.services.functions.FunctionData;
import io.inugami.configuration.services.functions.ProviderAttributFunction;
import io.inugami.configuration.services.functions.ProviderAttributFunctionException;

/**
 * TimestampFunction
 * 
 * @author patrick_guillerm
 * @since 7 nov. 2016
 */
public class HostsFunction implements ProviderAttributFunction {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static String NAME = "hosts";
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public String getName() {
        return NAME;
    }
    
    @Override
    public String apply(final FunctionData data) {
        ProviderAttributFunctionException.assertParams(1, data);
        
        final StringBuilder result = new StringBuilder();
        for (final String name : data.getParameters()) {
            result.append('\\');
            result.append('\\');
            result.append('"');
            result.append(clean(name));
            result.append('\\');
            result.append('\\');
            result.append('"');
            result.append(' ');
        }
        return result.toString();
    }
    
    private Object clean(final String value) {
        return value.replaceAll("\"", "").trim();
    }
}
