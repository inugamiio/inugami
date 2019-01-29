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
package org.inugami.api.providers.task;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.inugami.api.models.data.graphite.DataPoint;
import org.inugami.api.models.data.graphite.GraphiteTarget;
import org.inugami.api.models.data.graphite.GraphiteTargets;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SseProviderResultTest
 * 
 * @author patrick_guillerm
 * @since 24 janv. 2017
 */
public class SseProviderResultTest {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Logger LOGGER = LoggerFactory.getLogger(SseProviderResultTest.class);
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testConvertToJson() {
        
        final SseProviderResult data = new SseProviderResult("chanel", "foo-event", "message", "a error message", true,
                                                             new GraphiteTargets(buildTargets()), "{{EVERY_MIN}}",
                                                             null);
        
        final String json = data.convertToJson();
        LOGGER.info("json : {}", json);
        assertNotNull(json);
    }
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    
    private List<GraphiteTarget> buildTargets() {
        final List<GraphiteTarget> targets = new ArrayList<>();
        targets.add(new GraphiteTarget("targetA", buildDataPoint(0.1, 0.15, 0.159)));
        targets.add(new GraphiteTarget("targetB", buildDataPoint(1, 2, 3)));
        targets.add(new GraphiteTarget("targetC", buildDataPoint(-1, 2.25, -3.15)));
        return targets;
    }
    
    private List<DataPoint> buildDataPoint(final double... values) {
        final List<DataPoint> result = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            result.add(new DataPoint(values[i], i));
        }
        return result;
    }
}
