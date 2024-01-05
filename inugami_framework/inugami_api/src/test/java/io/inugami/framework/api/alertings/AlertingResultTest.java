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
package io.inugami.framework.api.alertings;

import io.inugami.framework.api.tools.unit.test.UnitTestData;
import io.inugami.framework.api.tools.unit.test.UnitTestHelper;
import io.inugami.framework.api.tools.unit.test.dto.AssertDtoContext;
import io.inugami.framework.interfaces.alertings.AlerteLevels;
import io.inugami.framework.interfaces.alertings.AlertingResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AlertingResultTest
 *
 * @author patrick_guillerm
 * @since 9 mars 2018
 */
class AlertingResultTest {


    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void alertingResult() {
        UnitTestHelper.assertDto(new AssertDtoContext<AlertingResult>().toBuilder()
                                                                       .objectClass(AlertingResult.class)
                                                                       .fullArgConstructorRefPath("io/inugami/framework/api/alertings/alertingResult/model.json")
                                                                       .getterRefPath("io/inugami/framework/api/alertings/alertingResult/getter.json")
                                                                       .toStringRefPath("io/inugami/framework/api/alertings/alertingResult/toString.txt")
                                                                       .cloneFunction(instance -> instance.toBuilder()
                                                                                                          .build())
                                                                       .noArgConstructor(() -> new AlertingResult())
                                                                       .fullArgConstructor(this::buildDataSet)
                                                                       .noEqualsFunction(this::notEquals)
                                                                       .checkSetters(false)
                                                                       .checkSerialization(false)
                                                                       .build());
    }

    void notEquals(final AlertingResult instance) {
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().hashCode());

        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().alerteName(null).build());
        assertThat(instance.toBuilder().alerteName(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().alerteName(UnitTestData.OTHER).build());
        assertThat(instance.toBuilder().alerteName(UnitTestData.OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().alerteName(null).build().hashCode());
        assertThat(instance.toBuilder()
                           .alerteName(UnitTestData.OTHER)
                           .build()
                           .hashCode()).isNotEqualTo(instance.hashCode());

    }


    // =========================================================================
    // TOOLS
    // =========================================================================
    private AlertingResult buildDataSet() {
        return AlertingResult.builder()
                             .alerteName("alertName")
                             .level("error ios")
                             .levelType(AlerteLevels.ERROR)
                             .message("foobar message")
                             .subLabel("sub message")
                             .duration(60L)
                             .created(1520605752814L)
                             .channel("plugin")
                             .addProviders("elkProvider", "kafkaProvider")
                             .build()
                             .toBuilder()
                             .build();
    }

    private String buildRefData() {
        //@formatter:off
        return "{\"alertName\":\"alertName\","
                + "\"channel\":\"plugin\","
                + "\"level\":\"error ios\","
                + "\"levelType\":\"ERROR\","
                + "\"levelNumber\":100000,"
                + "\"message\":\"foobar message\","
                + "\"subLabel\":\"sub message\","
                + "\"url\":null,"
                + "\"created\":1520605752814,"
                + "\"duration\":60,"
                + "\"data\":null,"
                + "\"multiAlerts\":false}";
        //@formatter:on
    }
}
