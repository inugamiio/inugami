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
package io.inugami.dashboard.api.domain.engine.dto;

import io.inugami.commons.test.dto.AssertDtoContext;
import io.inugami.dashboard.api.domain.engine.exception.EngineErrors;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import io.inugami.framework.interfaces.tools.ListUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static io.inugami.commons.test.UnitTestData.OTHER;
import static io.inugami.commons.test.UnitTestHelper.assertDto;
import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

class EnginePluginEventResultDTOTest {

    @Test
    void enginePluginEventResultDTO() {
        assertDto(AssertDtoContext.<EnginePluginEventResultDTO>builder()
                                  .objectClass(EnginePluginEventResultDTO.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(EnginePluginEventResultDTO::new)
                                  .fullArgConstructor(this::buildEnginePluginEventResultDTO)
                                  .fullArgConstructorRefPath("io/inugami/dashboard/api/domain/engine/dto/enginePluginEventResultDTO/model.json")
                                  .getterRefPath("io/inugami/dashboard/api/domain/engine/dto/enginePluginEventResultDTO/getter.json")
                                  .toStringRefPath("io/inugami/dashboard/api/domain/engine/dto/enginePluginEventResultDTO/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .checkSetters(true)
                                  .build());
    }

    @Test
    void compareTo_nominal() {
        final var data = ListUtils.toList(EnginePluginEventResultDTO.builder()
                                                                    .name("simple-event")
                                                                    .build(),
                                          EnginePluginEventResultDTO.builder()
                                                                    .name("zzz")
                                                                    .build(),
                                          EnginePluginEventResultDTO.builder()
                                                                    .build(),
                                          EnginePluginEventResultDTO.builder()
                                                                    .name("aa")
                                                                    .build()
        );
        Collections.sort(data);
        assertText(data,
                   """
                           [ {
                             "name" : "aa"
                           }, {
                             "name" : "simple-event"
                           }, {
                             "name" : "zzz"
                           }, { } ]
                           """);
    }

    private void notEquals(final EnginePluginEventResultDTO instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().name(null).build());
        assertThat(instance.toBuilder().name(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().name(OTHER).build());
        assertThat(instance.toBuilder().name(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().name(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder()
                                                             .name(OTHER)
                                                             .build()
                                                             .hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().errorCode(null).build());
        assertThat(instance.toBuilder().errorCode(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().errorCode(EngineErrors.WORKSPACE_NOT_EXISTS).build());
        assertThat(instance.toBuilder().errorCode(EngineErrors.WORKSPACE_NOT_EXISTS).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().errorCode(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder()
                                                             .errorCode(EngineErrors.WORKSPACE_NOT_EXISTS)
                                                             .build()
                                                             .hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().status(null).build());
        assertThat(instance.toBuilder().status(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().status(Status.FATAL).build());
        assertThat(instance.toBuilder().status(Status.FATAL).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().status(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder()
                                                             .status(Status.FATAL)
                                                             .build()
                                                             .hashCode());
    }

    private EnginePluginEventResultDTO buildEnginePluginEventResultDTO() {
        return EnginePluginEventResultDTO.builder()
                                         .name("simple-event")
                                         .errorCode(EngineErrors.APPLICATION_CONFIG_ERROR)
                                         .status(Status.ERROR)
                                         .message("sorry")
                                         .error(new UncheckedException("my bad"))
                                         .data(ProviderFutureResult.builder().build())
                                         .build()
                                         .toBuilder()
                                         .build();
    }
}