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
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import io.inugami.framework.interfaces.tools.ListUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertDto;
import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

class EnginePluginResultDTOTest {
    @Test
    void enginePluginResultDTO() {
        assertDto(AssertDtoContext.<EnginePluginResultDTO>builder()
                                  .objectClass(EnginePluginResultDTO.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(EnginePluginResultDTO::new)
                                  .fullArgConstructor(this::buildEnginePluginResultDTO)
                                  .fullArgConstructorRefPath("io/inugami/dashboard/api/domain/engine/dto/enginePluginResultDTO/model.json")
                                  .getterRefPath("io/inugami/dashboard/api/domain/engine/dto/enginePluginResultDTO/getter.json")
                                  .toStringRefPath("io/inugami/dashboard/api/domain/engine/dto/enginePluginResultDTO/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .checkSetters(true)
                                  .build());
    }

    @Test
    void compareTo_nominal() {
        final var data = ListUtils.toList(EnginePluginResultDTO.builder()
                                                               .gav(buildGav())
                                                               .build(),
                                          EnginePluginResultDTO.builder()
                                                               .gav(Gav.builder()
                                                                       .groupId("io.inugami")
                                                                       .artifactId("inugami_api_test")
                                                                       .version("3.3.0")
                                                                       .qualifier("jar")
                                                                       .build())
                                                               .build(),
                                          EnginePluginResultDTO.builder()
                                                               .build()
        );
        Collections.sort(data);
        assertText(data,
                   """
                           [ {
                                     "events" : [ ],
                                     "gav" : {
                                       "artifactId" : "inugami_api",
                                       "groupId" : "io.inugami",
                                       "hash" : "io.inugami:inugami_api:3.3.0:jar",
                                       "qualifier" : "jar",
                                       "version" : "3.3.0"
                                     }
                                   }, {
                                     "events" : [ ],
                                     "gav" : {
                                       "artifactId" : "inugami_api_test",
                                       "groupId" : "io.inugami",
                                       "hash" : "io.inugami:inugami_api_test:3.3.0:jar",
                                       "qualifier" : "jar",
                                       "version" : "3.3.0"
                                     }
                                   }, {
                                     "events" : [ ]
                                   } ]
                           """);
    }

    private void notEquals(final EnginePluginResultDTO instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().status(null).build());
        assertThat(instance.toBuilder().status(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().status(Status.SUCCESS).build());
        assertThat(instance.toBuilder().status(Status.SUCCESS).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().status(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder()
                                                             .status(Status.SUCCESS)
                                                             .build()
                                                             .hashCode());
        //
        final var otherGav = Gav.builder().build();
        assertThat(instance).isNotEqualTo(instance.toBuilder().gav(null).build());
        assertThat(instance.toBuilder().gav(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().gav(otherGav).build());
        assertThat(instance.toBuilder().gav(otherGav).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().gav(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder()
                                                             .gav(otherGav)
                                                             .build()
                                                             .hashCode());
    }

    private EnginePluginResultDTO buildEnginePluginResultDTO() {
        return EnginePluginResultDTO.builder()
                                    .events(buildEnginePluginEventResultDTO())
                                    .clearEvents()
                                    .events(List.of(buildEnginePluginEventResultDTO()))
                                    .gav(buildGav())
                                    .status(Status.ERROR)
                                    .message("sorry")
                                    .build()
                                    .toBuilder()
                                    .build();
    }

    private EnginePluginEventResultDTO buildEnginePluginEventResultDTO() {
        return EnginePluginEventResultDTO.builder()
                                         .name("simple-event")
                                         .errorCode(EngineErrors.APPLICATION_CONFIG_ERROR)
                                         .status(Status.ERROR)
                                         .message("sorry")
                                         .error(new UncheckedException("my bad"))
                                         .data(ProviderFutureResult.builder().build())
                                         .build();
    }

    private Gav buildGav() {
        return Gav.builder()
                  .groupId("io.inugami")
                  .artifactId("inugami_api")
                  .version("3.3.0")
                  .qualifier("jar")
                  .build();
    }
}