package io.inugami.dashboard.api.domain.administration.exception;

import io.inugami.commons.test.UnitTestHelper;
import org.junit.jupiter.api.Test;

class AdministrationErrorsTest {
    @Test
    void administrationErrors() {
        UnitTestHelper.assertErrorCodeUnique(AdministrationErrors.values());
        UnitTestHelper.assertErrorCode("domain/administration/exception/administrationErrors.json",
                                       AdministrationErrors.values());
    }
}