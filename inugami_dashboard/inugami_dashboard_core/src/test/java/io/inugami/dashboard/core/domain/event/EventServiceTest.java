package io.inugami.dashboard.core.domain.event;

import io.inugami.framework.interfaces.exceptions.NotYetImplementedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.inugami.commons.test.UnitTestHelper.assertThrows;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    @InjectMocks
    private EventService eventService;

    //==================================================================================================================
    // READ
    //==================================================================================================================
    @Test
    void getEvent_nominal() {
        assertThrows(NotYetImplementedException.class, () -> eventService.getEvent(null));
    }
}