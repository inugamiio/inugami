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
package io.inugami.api.models.data.graphite;

import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * GraphiteTargetTest
 *
 * @author patrick_guillerm
 * @since 12 janv. 2017
 */
class GraphiteTargetTest {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testConvertToObject() throws Exception {
        final byte[] data = new TestFileUtils().readFromClassLoader("GraphiteTargetTest.json");

        final GraphiteTarget graphiteTarget = new GraphiteTarget().convertToObject(data, Charset.forName("UTF-8"));
        assertNotNull(graphiteTarget);
        assertNotNull(graphiteTarget.getTarget());
        assertEquals("foobar", graphiteTarget.getTarget());
        assertNotNull(graphiteTarget.getDatapoints());
        assertEquals(7, graphiteTarget.getDatapoints().size());

    }
}
