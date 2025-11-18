package io.inugami.dashboard.api.domain.event;

import io.inugami.commons.test.UnitTestHelper;
import org.junit.jupiter.api.Test;

class EventErrorsTest {
    @Test
    void assertEventErrors() {
        UnitTestHelper.assertErrorCodeUnique(EventErrors.values());
        UnitTestHelper.assertErrorCode("domain/event/exception/assertEngineErrors.json",
                                       EventErrors.values());
    }
}