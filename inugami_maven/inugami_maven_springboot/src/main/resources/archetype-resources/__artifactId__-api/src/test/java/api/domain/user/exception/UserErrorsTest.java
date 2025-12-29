package ${package}.api.domain.user.exception;

import io.inugami.commons.test.UnitTestHelper;
import org.junit.jupiter.api.Test;

class UserErrorsTest {
    @Test
    void assertUserErrors() {
        UnitTestHelper.assertErrorCodeUnique(UserErrors.values());
        UnitTestHelper.assertErrorCode("api/domain/user/exception/assertUserErrors.json",
                                       UserErrors.values());
    }
}