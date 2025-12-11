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
package io.inugami.framework.interfaces.models.graphite;

import io.inugami.framework.interfaces.models.number.DataPoint;
import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GraphiteTypeTest {
    @Test
    void assertEnum() {
        UnitTestHelper.assertEnum(GraphiteType.class,
                                  """
                                          {
                                            "GRAPHITE_TARGET" : {
                                              "checkType" : { }
                                            },
                                            "GRAPHITE_TARGETS" : {
                                              "checkType" : { }
                                            },
                                            "DATA_POINT" : {
                                              "checkType" : { }
                                            },
                                            "TIME_VALUE" : {
                                              "checkType" : { }
                                            },
                                            "LIST_GRAPHITE_TARGET" : { },
                                            "LIST_TIME_VALUE" : { }
                                          }
                                          """);
    }

    @Test
    void getType_nominal(){
        assertThat(GraphiteType.getType(new GraphiteTarget())).isEqualTo(GraphiteType.GRAPHITE_TARGET);
        assertThat(GraphiteType.getType(new GraphiteTargets())).isEqualTo(GraphiteType.GRAPHITE_TARGETS);
        assertThat(GraphiteType.getType(new DataPoint())).isEqualTo(GraphiteType.DATA_POINT);
        assertThat(GraphiteType.getType(new TimeValue())).isEqualTo(GraphiteType.TIME_VALUE);
    }
}