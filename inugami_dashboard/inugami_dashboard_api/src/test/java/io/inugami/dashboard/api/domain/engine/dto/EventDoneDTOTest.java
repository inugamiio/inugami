package io.inugami.dashboard.api.domain.engine.dto;

import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.dto.AssertDtoContext;
import io.inugami.dashboard.api.domain.engine.exception.EngineErrors;
import io.inugami.framework.configuration.models.plugins.Plugin;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.models.engine.Status;
import io.inugami.framework.interfaces.models.event.GenericEvent;
import io.inugami.framework.interfaces.models.event.SimpleEvent;
import io.inugami.framework.interfaces.models.maven.Gav;
import io.inugami.framework.interfaces.task.ProviderFutureResult;
import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestData.OTHER;
import static io.inugami.commons.test.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class EventDoneDTOTest {
    @Test
    void eventDoneDTO() {
        assertDto(AssertDtoContext.<EventDoneDTO>builder()
                                  .objectClass(EventDoneDTO.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(EventDoneDTO::new)
                                  .fullArgConstructor(this::buildEventDoneDTO)
                                  .fullArgConstructorRefPath("io/inugami/dashboard/api/domain/engine/dto/eventDoneDTO/model.json")
                                  .getterRefPath("io/inugami/dashboard/api/domain/engine/dto/eventDoneDTO/getter.json")
                                  .toStringRefPath("io/inugami/dashboard/api/domain/engine/dto/eventDoneDTO/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .checkSetters(true)
                                  .build());
    }


    private void notEquals(final EventDoneDTO instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().date(null).build());
        assertThat(instance.toBuilder().date(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().date(UnitTestData.DATE_TIME.minusDays(5)).build());
        assertThat(instance.toBuilder().date(UnitTestData.DATE_TIME.minusDays(5)).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().date(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder()
                                                             .date(UnitTestData.DATE_TIME.minusDays(5))
                                                             .build()
                                                             .hashCode());
        //
        final var otherEvent = SimpleEvent.builder().name(OTHER).build();
        assertThat(instance).isNotEqualTo(instance.toBuilder().event(null).build());
        assertThat(instance.toBuilder().event(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().event(otherEvent).build());
        assertThat(instance.toBuilder().event(otherEvent).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().event(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder()
                                                             .event(otherEvent)
                                                             .build()
                                                             .hashCode());
        //
        final var otherPlugin = Plugin.builder().gav(Gav.builder().build()).build();
        assertThat(instance).isNotEqualTo(instance.toBuilder().plugin(null).build());
        assertThat(instance.toBuilder().plugin(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().plugin(otherPlugin).build());
        assertThat(instance.toBuilder().plugin(otherPlugin).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().plugin(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder()
                                                             .plugin(otherPlugin)
                                                             .build()
                                                             .hashCode());
    }

    private EventDoneDTO buildEventDoneDTO() {
        return EventDoneDTO.builder()
                           .plugin(buildPlugin())
                           .event(buildEvent())
                           .data(buildData())
                           .date(UnitTestData.DATE_TIME)
                           .build()
                           .toBuilder()
                           .build();
    }

    private EnginePluginEventResultDTO buildData() {
        return EnginePluginEventResultDTO.builder()
                                         .name("simple-event")
                                         .errorCode(EngineErrors.APPLICATION_CONFIG_ERROR)
                                         .status(Status.ERROR)
                                         .message("sorry")
                                         .error(new UncheckedException("my bad"))
                                         .data(ProviderFutureResult.builder().build())
                                         .build();
    }

    private GenericEvent<?> buildEvent() {
        return SimpleEvent.builder()
                          .name("simple-event")
                          .build();
    }

    private Plugin buildPlugin() {
        return Plugin.builder()
                     .gav(buildGav())
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