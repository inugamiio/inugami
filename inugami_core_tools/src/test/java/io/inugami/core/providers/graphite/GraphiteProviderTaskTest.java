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
package io.inugami.core.providers.graphite;

import io.inugami.api.exceptions.FatalException;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.exceptions.services.ProviderException;
import io.inugami.api.models.Tuple;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.data.graphite.GraphiteTarget;
import io.inugami.api.models.data.graphite.GraphiteTargets;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.models.events.SimpleEventBuilder;
import io.inugami.api.providers.task.ProviderFutureResult;
import io.inugami.commons.files.FilesUtils;
import io.inugami.core.services.connectors.HttpConnector;
import io.inugami.core.services.connectors.mock.HttpConnectorMock;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GraphiteProviderTaskTest
 *
 * @author patrick_guillerm
 * @since 10 janv. 2017
 */
public class GraphiteProviderTaskTest {

    private static final String FOOBAR = "foobar";

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static String URL = "https://graphiteapp.org/render";

    private final Logger LOGGER = LoggerFactory.getLogger(GraphiteProvider.class);

    //@formatter:off
    private final HttpConnector httpConnector = new HttpConnectorMock()
                                                        .addGetResult((uri)->uri.contains("testC"),readBytes("GraphiteProviderTaskTest_testBuildResult.json"))
                                                        .build();                                   
    //@formatter:off
    
    private byte[] readBytes(final String classpathFile) {
        try {
            return new FilesUtils().readFromClassLoader(classpathFile);
        }
        catch (final TechnicalException e) {
          throw new FatalException(e.getMessage(),e);
        }
    }
    // =========================================================================
    // METHODS
    // =========================================================================
    /**
     * Test build request.
     *
     * @throws ProviderException the provider exception
     */
    @Test
    public void testBuildRequest() throws ProviderException {
        final String providerName = "";
        
        final GraphiteProviderTask task = new GraphiteProviderTask(null, URL, providerName, httpConnector, Collections.EMPTY_LIST,
                null,null);

        //@formatter:off
        final SimpleEvent eventA = new SimpleEvent("eventA",
                                             "-20min",
                                             "-2d",null, null,
                                             "org.foo.bar.testA.1min.count",
                                             null,
                                             null,
                                             null,
                                             null);
        final String firstQuery = "https://graphiteapp.org/render?format=json"
                + "&target=alias%28org.foo.bar.testA.1min.count%2C%22eventA%22%29"
                + "&from=-20min"
                + "&until=-2d";
        LOGGER.info("first query : {}",firstQuery);
        
        assertEquals(firstQuery,task.buildRequest(eventA));
        //@formatter:on

        //@formatter:off
        final SimpleEvent eventB = new SimpleEvent("eventB",
                                             null,
                                             "-20w",null, null,
                                             "sumSeries(transformNull(org.foo.bar.testB.*.A{1,2},0))",
                                             null,
                                             null,
                                             null,
                                             null);
        final String secondQuery = "https://graphiteapp.org/render?format=json&target=alias%28sumSeries%28transformNull%28org.foo.bar.testB.*.A%7B1%2C2%7D%2C0%29%29%2C%22eventB%22%29&from=-0min&until=-20w";
        LOGGER.info("second query : {}",secondQuery);
        assertEquals(secondQuery,task.buildRequest(eventB));
        //@formatter:on
        httpConnector.close();
    }

    /**
     * Test build result.
     *
     * @throws Exception the exception
     */
    @Test
    public void testBuildResult() throws Exception {
        final GraphiteProviderTask task = new GraphiteProviderTask(null, URL, FOOBAR, false, httpConnector,
                                                                   Collections.EMPTY_LIST, null, null);

        //@formatter:off
        final SimpleEvent event = new SimpleEventBuilder().addName("foobar")
                    .addFrom("-20w")
                    .addQuery("sumSeries(transformNull(org.foo.bar.testC.count,0))")
                    .build();
        //@formatter:on

        final ProviderFutureResult result = task.buildResult(event, httpConnector.get("http://testC"));
        assertNotNull(result);
        assertFalse(result.getException().isPresent());

        final JsonObject dataModel = result.getData().get();
        assertNotNull(dataModel);
        assertTrue(dataModel instanceof GraphiteTargets);

        final GraphiteTargets targets = (GraphiteTargets) dataModel;
        assertNotNull(targets.getTargets());
        assertEquals(1, targets.getTargets().size());

        final GraphiteTarget target = targets.getTargets().get(0);
        assertEquals("foobar", target.getTarget());
        assertNotNull(target.getDatapoints());

        final List<Tuple<Double, Long>> refs = new ArrayList<>();
        refs.add(new Tuple<>(0.0, 1472133600L));
        refs.add(new Tuple<>(0.0, 1472137200L));
        refs.add(new Tuple<>(32.0, 1474282800L));
        refs.add(new Tuple<>(41672.0, 1474286400L));
        refs.add(new Tuple<>(70830.0, 1474290000L));
        refs.add(new Tuple<>(346.0, 1474293600L));
        refs.add(new Tuple<>(44217.0, 1474297200L));
        refs.add(new Tuple<>(84831.0, 1474300800L));
        refs.add(new Tuple<>(84788.0, 1474304400L));
        refs.add(new Tuple<>(85792.0, 1474308000L));
        refs.add(new Tuple<>(85010.0, 1474311600L));
        refs.add(new Tuple<>(84977.0, 1474315200L));
        refs.add(new Tuple<>(84909.0, 1474318800L));
        refs.add(new Tuple<>(84113.0, 1474322400L));
        refs.add(new Tuple<>(83585.0, 1474326000L));
        refs.add(new Tuple<>(85659.0, 1474329600L));
        refs.add(new Tuple<>(78975.0, 1474333200L));
        refs.add(new Tuple<>(474.0, 1474336800L));
        refs.add(new Tuple<>(66418.0, 1474383600L));
        refs.add(new Tuple<>(85998.0, 1474387200L));
        assertEquals(refs.size(), target.getDatapoints().size());

        for (int i = 0; i < refs.size(); i++) {
            final Tuple<Double, Long> ref = refs.get(i);
        }
        httpConnector.close();
    }

}
