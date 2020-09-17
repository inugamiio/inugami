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
package io.inugami.commons.engine.js;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.inugami.api.models.data.graphite.DataPoint;
import io.inugami.api.models.data.graphite.GraphiteTarget;
import io.inugami.api.models.data.graphite.GraphiteTargets;

/**
 * JavaScriptEngineFunctions
 * 
 * @author patrickguillerm
 * @since 20 déc. 2017
 */
public class JavaScriptExtractDataFunctions {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    
    // =========================================================================
    // Graphite
    // =========================================================================
    public static Object lastGraphiteValue(final Object data) {
        Object result = null;
        if ((data != null) && (data instanceof GraphiteTargets)) {
            final GraphiteTargets targets = (GraphiteTargets) data;
            if (targets.getTargets() != null) {
                if (targets.getTargets().size() == 1) {
                    result = extractLastGraphiteValueSimpleTarget(targets.getTargets().get(0));
                }
                else {
                    result = extractLastGraphiteValueMultiTarget(targets.getTargets());
                }
            }
        }
        return result;
        
    }
    
    private static Map<String, Double> extractLastGraphiteValueMultiTarget(final List<GraphiteTarget> targets) {
        final Map<String, Double> result = new HashMap<>();
        for (final GraphiteTarget target : targets) {
            final Double targetData = extractLastGraphiteValueSimpleTarget(target);
            if (targetData != null) {
                result.put(cleanTargetName(target.getTarget()), targetData);
            }
            
        }
        return result;
    }
    
    private static Double extractLastGraphiteValueSimpleTarget(final GraphiteTarget graphiteTarget) {
        Double value = null;
        if ((graphiteTarget != null) && (graphiteTarget.getDatapoints() != null)) {
            for (int i = graphiteTarget.getDatapoints().size() - 1; i >= 0; i--) {
                final DataPoint point = graphiteTarget.getDatapoints().get(i);
                final Double data = point == null ? null : point.getValue();
                if (data != null) {
                    value = data;
                    break;
                }
            }
        }
        return value;
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private static String cleanTargetName(final String target) {
        return target.replaceAll(" ", "_").replaceAll("[.]", "_").replaceAll(":", "_");
    }
    
}
