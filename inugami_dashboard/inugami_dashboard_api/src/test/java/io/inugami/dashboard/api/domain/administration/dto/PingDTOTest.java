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
package io.inugami.dashboard.api.domain.administration.dto;

import io.inugami.commons.test.dto.AssertDtoContext;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static io.inugami.commons.test.UnitTestData.DATE_TIME;
import static io.inugami.commons.test.UnitTestData.OTHER;
import static io.inugami.commons.test.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class PingDTOTest {
    public static Clock CLOCK = Clock.fixed(Instant.parse("2025-12-02T20:53:42.00Z"), ZoneOffset.UTC);


    @Test
    void pingDTO() {
        assertDto(AssertDtoContext.<PingDTO>builder()
                                  .objectClass(PingDTO.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(PingDTO::new)
                                  .fullArgConstructor(this::buildPingDTO)
                                  .fullArgConstructorRefPath("io/inugami/dashboard/api/domain/administration/dto/pingDTO/model.json")
                                  .getterRefPath("io/inugami/dashboard/api/domain/administration/dto/pingDTO/getter.json")
                                  .toStringRefPath("io/inugami/dashboard/api/domain/administration/dto/pingDTO/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .checkSetters(true)
                                  .build());
    }

    private void notEquals(final PingDTO instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().now(null).build());
        assertThat(instance.toBuilder().now(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().now(DATE_TIME).build());
        assertThat(instance.toBuilder().now(DATE_TIME).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().now(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder()
                                                             .now(DATE_TIME)
                                                             .build()
                                                             .hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().applicationName(null).build());
        assertThat(instance.toBuilder().applicationName(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().applicationName(OTHER).build());
        assertThat(instance.toBuilder().applicationName(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().applicationName(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder()
                                                             .applicationName(OTHER)
                                                             .build()
                                                             .hashCode());
    }

    private PingDTO buildPingDTO() {
        return PingDTO.builder()
                      .applicationName("inugami")
                      .now(LocalDateTime.now(CLOCK))
                      .build()
                      .toBuilder()
                      .build();
    }
}