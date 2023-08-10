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
package io.inugami.core.processors;

import io.inugami.api.models.data.graphite.DataPoint;
import io.inugami.api.models.data.graphite.GraphiteTarget;
import io.inugami.api.models.data.graphite.GraphiteTargets;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.configuration.services.ConfigHandlerHashMap;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * GraphiteBucketProcessorTest
 *
 * @author patrick_guillerm
 * @since 1 déc. 2017
 */
class GraphiteBucketProcessorTest {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testBuildTargetsBuckets() throws Exception {

        final ConfigHandler<String, String> config        = buildConfig();
        final GraphiteBucketProcessor       processor     = new GraphiteBucketProcessor("foobar", config);
        final GraphiteTargets               targetsSimple = buildSimpleTargets();

        final GraphiteTargets resultSimple = processor.buildTargetsBuckets(targetsSimple);
        assertNotNull(resultSimple);
        assertNotNull(resultSimple.getTargets());
        assertEquals(1, resultSimple.getTargets().size());

        final GraphiteTarget data = resultSimple.getTargets().get(0);
        assertEquals("foobar", data.getTarget());
        final List<DataPoint> points = data.getDatapoints();
        assertEquals(5, points.size());

        assertEquals(1512118800L, points.get(4).getTimestamp());
        assertEquals(1.0703545427, points.get(4).getValue(), 0.0001);

        assertEquals(1512118500L, points.get(3).getTimestamp());
        assertEquals(1.2243623347, points.get(3).getValue(), 0.0001);

        assertEquals(1512118200L, points.get(2).getTimestamp());
        assertEquals(1.063843965, points.get(2).getValue(), 0.0001);

        assertEquals(1512117900L, points.get(1).getTimestamp());
        assertEquals(1.2359633001, points.get(1).getValue(), 0.0001);

        assertEquals(1512117600L, points.get(0).getTimestamp());
        assertEquals(1.2810571135, points.get(0).getValue(), 0.0001);
    }

    // =========================================================================
    // BUILDER
    // =========================================================================
    private ConfigHandler<String, String> buildConfig() {
        final Map<String, String> data = new HashMap<>();
        data.put("size", "300");
        return new ConfigHandlerHashMap(data);
    }

    private GraphiteTargets buildSimpleTargets() {
        final List<GraphiteTarget> targets = new ArrayList<>();
        targets.add(buildTarget());
        return new GraphiteTargets(targets);
    }

    private GraphiteTarget buildTarget() {
        final List<DataPoint> data = new ArrayList<>();
        data.add(new DataPoint(null, 1512117300));
        // 9:36:00
        data.add(new DataPoint(0.92436974789915971, 1512117360L));
        // 9:37:00
        data.add(new DataPoint(0.94664371772805511, 1512117420L));
        // 9:38:00
        data.add(new DataPoint(1.9195612431444242, 1512117480L));
        // 9:39:00
        data.add(new DataPoint(1.1764705882352942, 1512117540L));
        // 9:40:00
        data.add(new DataPoint(1.4382402707275803, 1512117600L));
        // 9:41:00
        data.add(new DataPoint(0.5988023952095809, 1512117660L));
        // 9:42:00
        data.add(new DataPoint(1.3570822731128074, 1512117720L));
        // 9:43:00
        data.add(new DataPoint(1.5219337511190689, 1512117780L));
        // 9:44:00
        data.add(new DataPoint(1.3661202185792349, 1512117840L));
        // 9:45:00
        data.add(new DataPoint(1.3358778625954197, 1512117900L));
        // 9:46:00
        data.add(new DataPoint(1.0820559062218216, 1512117960L));
        // 9:47:00
        data.add(new DataPoint(1.1576135351736421, 1512118020L));
        // 9:48:00
        data.add(new DataPoint(1.0082493125572869, 1512118080L));
        // 9:49:00
        data.add(new DataPoint(1.068566340160285, 1512118140L));
        // 9:50:00
        data.add(new DataPoint(1.0027347310847767, 1512118200L));
        // 9:51:00
        data.add(new DataPoint(1.0587102983638113, 1512118260L));
        // 9:52:00
        data.add(new DataPoint(1.1461318051575931, 1512118320L));
        // 9:53:00
        data.add(new DataPoint(1.2455516014234875, 1512118380L));
        // 9:54:00
        data.add(new DataPoint(1.3345195729537367, 1512118440L));
        // 9:55:00
        data.add(new DataPoint(1.3368983957219251, 1512118500L));
        // 9:56:00
        data.add(new DataPoint(1.3020833333333335, 1512118560L));
        // 9:57:00
        data.add(new DataPoint(1.3537906137184115, 1512118620L));
        // 9:58:00
        data.add(new DataPoint(0.8828250401284109, 1512118680L));
        // 9:59:00
        data.add(new DataPoint(0.8166969147005444, 1512118740L));
        // 10:00:00
        data.add(new DataPoint(0.99637681159420277, 1512118800L));
        // 10:01:00
        data.add(new DataPoint(0.74142724745134381, 1512118860L));
        // 10:02:00
        data.add(new DataPoint(1.4297729184188395, 1512118920L));
        // 10:03:00
        data.add(new DataPoint(2.066772655007949, 1512118980L));
        // 10:04:00
        data.add(new DataPoint(0.24937655860349126, 1512119040L));
        return new GraphiteTarget("foobar", data);
    }

}
