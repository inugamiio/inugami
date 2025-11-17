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
package io.inugami.framework.interfaces.models;

import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NumberTypeTest {
    @Test
    void assertEnum() {
        UnitTestHelper.assertEnum(NumberType.class,
                                  """
                                          {
                                            "BYTE" : { },
                                            "SHORT" : { },
                                            "INTEGER" : { },
                                            "LONG" : { },
                                            "FLOAT" : { },
                                            "DOUBLE" : { },
                                            "BIG_DECIMAL" : { }
                                          }
                                          """);
    }

    @Test
    void getType_nominal(){
        assertThat(NumberType.getType(10)).isEqualTo(NumberType.INTEGER);
        assertThat(NumberType.getType(10L)).isEqualTo(NumberType.LONG);
        assertThat(NumberType.getType(10.0)).isEqualTo(NumberType.DOUBLE);
        assertThat(NumberType.getType(10.0f)).isEqualTo(NumberType.FLOAT);
    }
}