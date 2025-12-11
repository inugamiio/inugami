package io.inugami.framework.interfaces.models.event;

import io.inugami.framework.interfaces.testing.commons.AssertDtoContext;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.interfaces.testing.commons.UnitTestData.OTHER;
import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertDto;
import static org.assertj.core.api.Assertions.assertThat;

class EventStateTest {
    @Test
    void eventState() {
        assertDto(AssertDtoContext.<EventState>builder()
                                  .objectClass(EventState.class)
                                  .cloneFunction(i -> i.toBuilder().build())
                                  .noArgConstructor(EventState::new)
                                  .fullArgConstructor(this::buildEventState)
                                  .fullArgConstructorRefPath("io/inugami/framework/interfaces/models/event/eventState/model.json")
                                  .getterRefPath("io/inugami/framework/interfaces/models/event/eventState/getter.json")
                                  .toStringRefPath("io/inugami/framework/interfaces/models/event/eventState/toString.txt")
                                  .noEqualsFunction(this::notEquals)
                                  .build());
    }

    private void notEquals(EventState instance) {
        assertThat(instance).isNotEqualTo(null);
        assertThat(instance).isNotEqualTo(instance.toBuilder());
        //
        assertThat(instance).isNotEqualTo(instance.toBuilder().eventName(null).build());
        assertThat(instance.toBuilder().eventName(null).build()).isNotEqualTo(instance);
        assertThat(instance).isNotEqualTo(instance.toBuilder().eventName(OTHER).build());
        assertThat(instance.toBuilder().eventName(OTHER).build()).isNotEqualTo(instance);
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().eventName(null).build().hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(instance.toBuilder().eventName(OTHER).build().hashCode());

    }

    private EventState buildEventState() {
        return EventState.builder()
                         .eventName("value")
                         .start(1764643637482L)
                         .end(1764643637632L)
                         .running(true)
                         .delais(150L)
                         .build()
                         .toBuilder()
                         .build();
    }
}