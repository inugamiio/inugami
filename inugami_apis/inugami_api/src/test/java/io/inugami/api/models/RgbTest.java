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
package io.inugami.api.models;

import io.inugami.api.tools.unit.test.dto.AssertDtoContext;
import io.inugami.interfaces.tools.Rgb;
import org.junit.jupiter.api.Test;

import static io.inugami.api.tools.unit.test.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * RgbTest
 *
 * @author patrick_guillerm
 * @since 12 avr. 2018
 */
class RgbTest {

    public static final int VALUE = (int) 0x262626;

    @Test
    void rgb() {
        assertDto(new AssertDtoContext<Rgb>().toBuilder()
                                             .objectClass(Rgb.class)
                                             .fullArgConstructorRefPath("api/models/rgb/model.json")
                                             .getterRefPath("api/models/rgb/getter.json")
                                             .toStringRefPath("api/models/rgb/toString.txt")
                                             .cloneFunction(instance -> instance.toBuilder().build())
                                             .noArgConstructor(Rgb::new)
                                             .fullArgConstructor(this::buildDataSet)
                                             .noEqualsFunction(this::notEquals)
                                             .checkSetters(false)
                                             .checkSerialization(false)
                                             .build());
    }

    void notEquals(final Rgb instance) {
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().hashCode());

        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().color(VALUE).build());
        assertThat(instance.toBuilder().color(VALUE).build()).isNotEqualTo(instance);
        assertThat(instance.toBuilder().color(VALUE).build().hashCode()).isNotEqualTo(instance.hashCode());
    }

    private Rgb buildDataSet() {
        return Rgb.builder()
                  .addColor((int) 0xadf2be)
                  .build();
    }
}
