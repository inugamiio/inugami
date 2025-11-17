package io.inugami.framework.interfaces.connectors.exceptions;

import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

class HttpErrorTest {
    @Test
    void assertHttpError() {
        UnitTestHelper.assertErrorCodeUnique(HttpError.values());
        UnitTestHelper.assertErrorCode("io/inugami/framework/interfaces/connectors/exceptions/assertHttpError.json",
                                       HttpError.values());
    }
}