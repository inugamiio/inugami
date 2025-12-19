package io.inugami.dashboard.interfaces.core.domain.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EventRestControllerTest {


    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================

    //==================================================================================================================
    // READ
    //==================================================================================================================
    @Test
    void getEvent_nominal() {
        assertThat(controller().getEvent(null)).isNull();
    }

    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    EventRestController controller() {
        return EventRestController.builder()
                                  .build();
    }
}