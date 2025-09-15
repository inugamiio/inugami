package io.inugami.dashboard.api.domain.alerting.exception;

import io.inugami.commons.test.UnitTestHelper;
import org.junit.jupiter.api.Test;

class AlertingErrorsTest {

    @Test
    void assertAlertingErrors() {
        UnitTestHelper.assertErrorCodeUnique(AlertingErrors.values());
        UnitTestHelper.assertErrorCode("domain/alerting/exception/assertAlertingErrors.json",
                                       AlertingErrors.values());
    }
}