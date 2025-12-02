package io.inugami.dashboard.interfaces.domain.plugin.dto;

import io.inugami.commons.test.dto.AssertDtoContext;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestData.OTHER;
import static io.inugami.commons.test.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class EnginePluginEventResultAPITest {
    @Test
    void enginePluginEventResultAPI() {
        assertDto(AssertDtoContext.<EnginePluginEventResultAPI>builder()
                                  .objectClass(EnginePluginEventResultAPI.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(EnginePluginEventResultAPI::new)
                                  .fullArgConstructor(this::buildEnginePluginEventResultAPI)
                                  .fullArgConstructorRefPath("io/inugami/dashboard/interfaces/domain/engine/dto/enginePluginEventResultDTO/model.json")
                                  .getterRefPath("io/inugami/dashboard/interfaces/domain/engine/dto/enginePluginEventResultDTO/getter.json")
                                  .toStringRefPath("io/inugami/dashboard/interfaces/domain/engine/dto/enginePluginEventResultDTO/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .checkSetters(true)
                                  .build());
    }

    private void notEquals(final EnginePluginEventResultAPI instance) {
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
        final var otherErrorCode = DefaultErrorCode.fromErrorCode(DefaultErrorCode.buildUndefineError()).errorCode("ERR-0001").build();
        assertThat(instance).isNotEqualTo(instance.toBuilder().errorCode(null).build());
        assertThat(instance.toBuilder().errorCode(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().errorCode(otherErrorCode).build());
        assertThat(instance.toBuilder().errorCode(otherErrorCode).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().errorCode(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder()
                                                             .errorCode(otherErrorCode)
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

    private EnginePluginEventResultAPI buildEnginePluginEventResultAPI() {
        return EnginePluginEventResultAPI.builder()
                                         .name("simple-event")
                                         .errorCode(DefaultErrorCode.buildUndefineError())
                                         .status(Status.ERROR)
                                         .message("sorry")
                                         .error(new UncheckedException("my bad"))
                                         .data(ProviderFutureResult.builder().build())
                                         .build()
                                         .toBuilder()
                                         .build();
    }
}