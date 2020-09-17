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
package io.inugami.core.services.processors;

import java.util.List;

import io.inugami.api.models.data.graphite.GraphiteTarget;

/**
 * ProcessorHelper
 * 
 * @author patrick_guillerm
 * @since 13 oct. 2016
 */
@Deprecated
public class ProcessorHelper {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public List<GraphiteTarget> deserializeGraphite(final String data) {
        final List<GraphiteTarget> result = null;
        return result;
    }
    
    public String serializeGraphite(final List<GraphiteTarget> data) {
        final String result = "null";
        return result;
    }
}
