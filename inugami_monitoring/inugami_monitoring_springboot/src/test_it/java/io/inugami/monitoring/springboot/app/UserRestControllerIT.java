/* --------------------------------------------------------------------
 *  Inugami
 * --------------------------------------------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.monitoring.springboot.app;

import io.inugami.api.loggers.Loggers;
import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.api.NumberLineMatcher;
import io.inugami.commons.test.api.RegexLineMatcher;
import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.commons.test.api.UuidLineMatcher;
import io.inugami.commons.test.dto.AssertLogContext;
import io.inugami.commons.test.dto.UserDataDTO;
import io.inugami.monitoring.springboot.SpringBootIntegrationTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.concurrent.atomic.AtomicReference;

import static io.inugami.commons.test.UnitTestHelper.assertLogs;

class UserRestControllerIT extends SpringBootIntegrationTest {


    @Test
    void createUser_nominal() {
        AtomicReference<UserDataDTO> result = new AtomicReference<>();

        assertLogs(AssertLogContext.builder()
                                   .path("io/inugami/monitoring/springboot/app/createUser_nominal.iolog.txt")
                                   .integrationTest(true)
                                   .addPattern(Loggers.IOLOG_NAME)
                                   .addLineMatcher(SkipLineMatcher.of(24, 78))
                                   .addLineMatcher(UuidLineMatcher.of(4, 15, 16, 53, 67, 69, 108, 109, 110))
                                   .addLineMatcher(RegexLineMatcher.of(".*/user.*", 7, 56))
                                   .addLineMatcher(NumberLineMatcher.of(62, 104, 105))
                                   .process(() -> {
                                       UserDataDTO user = RestAssured.given()
                                                                     .body(asJson(UnitTestData.USER_1))
                                                                     .headers(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                                                     .post("/user")
                                                                     .as(UserDataDTO.class);
                                       result.set(user);
                                   })
                                   .build());
    }
}
