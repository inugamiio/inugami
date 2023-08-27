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
package io.inugami.commons.data;

import io.inugami.commons.data.model.Record;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * AggregatorHelperTest
 *
 * @author patrick_guillerm
 * @since 8 nov. 2017
 */
@SuppressWarnings({"java:S3415"})
class AggregatorHelperTest {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testSumGenericType() {
        final AggregatorHelper aggregator = new AggregatorHelper();

        final Byte byteR = aggregator.sumGenericType(new Byte((byte) 1), new Byte((byte) 1));
        assertEquals(new Byte((byte) 2), byteR);

        final Short shortR = aggregator.sumGenericType(new Short((short) 1), new Short((short) 1));
        assertEquals((short) 2, (short) shortR);

        final Integer intR = aggregator.sumGenericType(new Integer(1), new Integer(1));
        assertEquals(2, (int) intR);

        final Long longR = aggregator.sumGenericType(new Long(1L), new Long(1L));
        assertEquals(2L, (long) longR);

        final Float floatR = aggregator.sumGenericType(new Float(1.0f), new Float(1.0f));
        assertEquals(2.0f, floatR, 0.0001f);

        final Double doubleR = aggregator.sumGenericType(new Double(1.0d), new Double(1.0d));
        assertEquals(2.0d, doubleR, 0.0001d);

        final BigDecimal bigR = aggregator.sumGenericType(BigDecimal.ONE, BigDecimal.ONE);
        assertEquals(new BigDecimal(2), bigR);

    }

    @Test
    void testSumMap() {
        final Map<String, Integer> ref      = new HashMap<>();
        final Map<String, Integer> newValue = new HashMap<>();

        ref.put("foo", 1);
        ref.put("bar", 1);
        ref.put("acme", 1);

        newValue.put("foo", 2);
        newValue.put("bar", 3);
        newValue.put("joe", 5);

        final AggregatorHelper     aggregator = new AggregatorHelper();
        final Map<String, Integer> newMap     = aggregator.sumMap(ref, newValue);
        assertEquals(4, newMap.size());
        assertEquals(3, (int) newMap.get("foo"));
        assertEquals(4, (int) newMap.get("bar"));
        assertEquals(1, (int) newMap.get("acme"));
        assertEquals(5, (int) newMap.get("joe"));
    }

    @Test
    void testKeepRecord() {
        final AggregatorHelper aggregator = new AggregatorHelper();
        final String           unit       = "foo";

        final Record recordA = aggregator.keepRecord(1, 10L, null, unit);
        assertEquals(recordA.getTime(), 10L);
        assertEquals(recordA.getValue(), 1, 0.01);

    }
}
