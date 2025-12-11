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
package io.inugami.framework.interfaces.monitoring.logger;

import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

class ConsoleColorsTest {
    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClassLombok(ConsoleColors.class);
    }

    @Test
    void assertEnum() {
        UnitTestHelper.assertEnum(ConsoleColors.State.class,
                                  """
                                          {
                                            "ERROR" : { },
                                            "WARN" : { },
                                            "SUCCESS" : { },
                                            "UNDEFINE" : { }
                                          }
                                          """);
    }

    @Test
    void renderState_nominal() {
        assertThat(ConsoleColors.renderState(ConsoleColors.State.ERROR)).isEqualTo("\u001B[1;31mX\t\u001B[0m");
        assertThat(ConsoleColors.renderState(ConsoleColors.State.ERROR,"sorry")).isEqualTo("\u001B[1;31mX\tsorry\u001B[0m");
        assertThat(ConsoleColors.renderState(ConsoleColors.State.WARN,"sorry")).isEqualTo("\u001B[1;33m~\tsorry\u001B[0m");
        assertThat(ConsoleColors.renderState(ConsoleColors.State.SUCCESS,"sorry")).isEqualTo("\u001B[0;32m✔\tsorry\u001B[0m");
        assertThat(ConsoleColors.renderState(null,"sorry")).isEqualTo("");
    }
}
