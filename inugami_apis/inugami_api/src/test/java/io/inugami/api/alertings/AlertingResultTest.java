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
package io.inugami.api.alertings;

import io.inugami.api.tools.unit.test.UnitTestHelper;
import io.inugami.api.tools.unit.test.dto.AssertDtoContext;
import org.junit.jupiter.api.Test;

import static io.inugami.api.tools.unit.test.UnitTestData.OTHER;
import static io.inugami.api.tools.unit.test.UnitTestHelper.assertDto;
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
        assertDto(new AssertDtoContext<AlertingResult>().toBuilder()
                                                        .objectClass(AlertingResult.class)
                                                        .fullArgConstructorRefPath("api/alertings/alertingResult/model.json")
                                                        .getterRefPath("api/alertings/alertingResult/getter.json")
                                                        .toStringRefPath("api/alertings/alertingResult/toString.txt")
                                                        .cloneFunction(instance -> instance.toBuilder().build())
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
        assertThat(instance).isNotEqualTo(instance.toBuilder().alerteName(OTHER).build());
        assertThat(instance.toBuilder().alerteName(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().alerteName(null).build().hashCode());
        assertThat(instance.toBuilder().alerteName(OTHER).build().hashCode()).isNotEqualTo(instance.hashCode());

    }

    @Test
    void convertToJson_nominal() {
        UnitTestHelper.assertTextRelative(buildDataSet().convertToJson(), "api/alertings/alertingResult/convertToJson_nominal.json");
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
