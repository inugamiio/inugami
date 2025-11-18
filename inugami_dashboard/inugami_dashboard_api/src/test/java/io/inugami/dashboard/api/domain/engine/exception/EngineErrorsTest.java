package io.inugami.dashboard.api.domain.engine.exception;

import io.inugami.commons.test.UnitTestHelper;
import org.junit.jupiter.api.Test;

class EngineErrorsTest {
    @Test
    void assertEngineErrors() {
        UnitTestHelper.assertErrorCodeUnique(EngineErrors.values());
        UnitTestHelper.assertErrorCode("domain/engine/exception/assertEngineErrors.json",
                                       EngineErrors.values());
    }
}