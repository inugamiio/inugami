package io.inugami.dashboard.api.domain.engine.dto;

import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.UnitTestHelper;
import io.inugami.commons.test.dto.AssertDtoContext;
import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.models.maven.Gav;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class EngineResultDTOTest {
    @Test
    void engineResultDTO() {
        assertDto(AssertDtoContext.<EngineResultDTO>builder()
                                  .objectClass(EngineResultDTO.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(EngineResultDTO::new)
                                  .fullArgConstructor(this::buildEngineResultDTO)
                                  .fullArgConstructorRefPath("domain/engine/dto/engineResultDTO/model.json")
                                  .getterRefPath("domain/engine/dto/engineResultDTO/getter.json")
                                  .toStringRefPath("domain/engine/dto/engineResultDTO/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .checkSetters(true)
                                  .build());
    }

    private void notEquals(final EngineResultDTO instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().start(null).build());
        assertThat(instance.toBuilder().start(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().start(UnitTestData.DATE_TIME).build());
        assertThat(instance.toBuilder().start(UnitTestData.DATE_TIME).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().start(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().start(UnitTestData.DATE_TIME).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().end(null).build());
        assertThat(instance.toBuilder().end(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().end(UnitTestData.DATE_TIME).build());
        assertThat(instance.toBuilder().end(UnitTestData.DATE_TIME).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().end(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().end(UnitTestData.DATE_TIME).build().hashCode());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().status(null).build());
        assertThat(instance.toBuilder().status(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().status(Status.ERROR).build());
        assertThat(instance.toBuilder().status(Status.ERROR).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().status(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().status(Status.ERROR).build().hashCode());
    }

    private EngineResultDTO buildEngineResultDTO() {
        return EngineResultDTO.builder()
                              .traceId("ceb417ed-4330-4df2-83c8-438274edee7c")
                              .processId("27c01f44-fe12-4c38-9bf1-25f50ea1c1a9")
                              .start(LocalDateTime.of(2020, 11, 22, 13, 50, 12))
                              .end(LocalDateTime.of(2020, 11, 22, 13, 52, 12))
                              .status(Status.SUCCESS)
                              .plugins(List.of(EnginePluginResultDTO.builder()
                                                                    .events(List.of(EnginePluginEventResultDTO.builder()
                                                                                                              .build()))
                                                                    .gav(Gav.builder()
                                                                            .groupId("io.inugami")
                                                                            .artifactId("plugin")
                                                                            .version("1.0.0")
                                                                            .build())
                                                                    .status(Status.SUCCESS)
                                                                    .message("done")
                                                                    .build()))
                              .build()
                              .toBuilder()
                              .build();
    }

}