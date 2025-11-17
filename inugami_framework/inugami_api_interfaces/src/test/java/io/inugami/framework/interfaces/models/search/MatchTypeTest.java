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
package io.inugami.framework.interfaces.models.search;

import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.testing.commons.UnitTestData.OTHER;
import static org.assertj.core.api.Assertions.assertThat;

class MatchTypeTest {
    @Test
    void assertEnum() {
        UnitTestHelper.assertEnum(MatchType.class,
                                  """
                                          {
                                            "EQUALS" : { },
                                            "CONTAINS" : { },
                                            "START_WITH" : { },
                                            "END_WITH" : { },
                                            "LESS" : { },
                                            "HIGHER" : { },
                                            "LESS_OR_EQUALS" : { },
                                            "HIGHER_OR_EQUALS" : { },
                                            "LOWER_OR_EQUALS" : { },
                                            "BETWEEN" : { },
                                            "IN" : { }
                                          }
                                          """);
    }

    @Test
    void getTypes_nominal() {
        assertThat(MatchType.getTypes()).isNotEmpty();
    }

    @Test
    void getEnum_nominal() {
        assertThat(MatchType.getEnum("EQUALS")).isEqualTo(MatchType.EQUALS);
        assertThat(MatchType.getEnum(OTHER)).isNull();
    }
}