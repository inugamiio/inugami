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
import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.commons.test.dto.AssertLogContext;
import io.inugami.commons.test.dto.UserDataDTO;
import io.inugami.monitoring.springboot.SpringBootIntegrationTest;
import io.inugami.monitoring.springboot.app.monitoring.FunctionalInterceptor;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.concurrent.atomic.AtomicReference;

import static io.inugami.commons.test.UnitTestHelper.assertLogs;
import static org.assertj.core.api.Assertions.assertThat;

class UserRestControllerIT extends SpringBootIntegrationTest {


    @Test
    void createUser_nominal() {
        FunctionalInterceptor.clean();
        AtomicReference<UserDataDTO> result = new AtomicReference<>();


        assertLogs(AssertLogContext.builder()
                                   .path("io/inugami/monitoring/springboot/app/createUser_nominal.iolog.1.txt")
                                   .integrationTest(true)
                                   .addPattern(Loggers.IOLOG_NAME)
                                   .addPattern(Loggers.IOLOG_NAME)
                                   .addLineMatcher(SkipLineMatcher.of(9, 13, 17, 18, 26, 59, 63, 67, 68, 76, 109, 113, 114,
                                                                      126, 130, 138, 163, 164, 167, 168, 169, 177, 221, 225, 226,
                                                                      238, 242, 250, 275, 276, 279, 280, 281, 289))
                                   .process(() -> {
                                       RestAssured.given()
                                                  .body(asJson(UnitTestData.USER_1.toBuilder()
                                                                                  .email(null)
                                                                                  .build()))
                                                  .headers(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                                  .post("/user");
                                   })
                                   .build());


        assertLogs(AssertLogContext.builder()
                                   .path("io/inugami/monitoring/springboot/app/createUser_nominal.iolog.2.txt")
                                   .integrationTest(true)
                                   .addPattern(Loggers.IOLOG_NAME)
                                   .addPattern(Loggers.IOLOG_NAME)
                                   .addLineMatcher(SkipLineMatcher.of(9, 13, 17, 18, 26, 60, 64, 68, 69, 77, 111, 115, 116,
                                                                      122, 124, 132, 158, 159, 162, 163, 164, 200, 204, 205, 211,
                                                                      213, 221, 247, 248, 251, 252, 253))
                                   .process(() -> {
                                       UserDataDTO user = RestAssured.given()
                                                                     .body(asJson(UnitTestData.USER_1))
                                                                     .headers(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                                                     .post("/user")
                                                                     .as(UserDataDTO.class);
                                       result.set(user);
                                   })
                                   .build());
        assertThat(FunctionalInterceptor.VALUE.get()).isEqualTo(FunctionalInterceptor.INTERCEPTED);
    }
}
