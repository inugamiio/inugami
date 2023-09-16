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
import io.inugami.api.monitoring.models.Headers;
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
    private static final String CORRELATION_ID = "b0116bae-1da0-465d-845b-79bd7a82ec8d";
    private static final String TRACE_ID       = "79bd7a82ec8d";

    @Test
    void createUser_nominal() {
        FunctionalInterceptor.clean();
        AtomicReference<UserDataDTO> result = new AtomicReference<>();


        assertLogs(AssertLogContext.builder()
                                   .path("io/inugami/monitoring/springboot/app/createUser_nominal.iolog.1.txt")
                                   .integrationTest(true)
                                   .addPattern(Loggers.IOLOG_NAME)
                                   .addPattern(Loggers.IOLOG_NAME)
                                   .addLineMatcher(SkipLineMatcher.of(9, 28, 61, 80, 113, 118, 144, 169, 170, 182, 226, 231, 257, 282, 283, 295))
                                   .process(() -> {
                                       RestAssured.given()
                                                  .body(asJson(UnitTestData.USER_1.toBuilder()
                                                                                  .email(null)
                                                                                  .build()))
                                                  .headers(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                                  .headers(Headers.X_CORRELATION_ID, CORRELATION_ID)
                                                  .headers(Headers.X_B_3_TRACEID, TRACE_ID)
                                                  .post("/user");
                                   })
                                   .build());


        assertLogs(AssertLogContext.builder()
                                   .path("io/inugami/monitoring/springboot/app/createUser_nominal.iolog.2.txt")
                                   .integrationTest(true)
                                   .addPattern(Loggers.IOLOG_NAME)
                                   .addPattern(Loggers.IOLOG_NAME)
                                   .addLineMatcher(SkipLineMatcher.of(9, 28, 62, 81, 115, 120, 138, 164, 165, 205, 210, 228, 254, 255))
                                   .process(() -> {
                                       UserDataDTO user = RestAssured.given()
                                                                     .body(asJson(UnitTestData.USER_1))
                                                                     .headers(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                                                     .headers(Headers.X_CORRELATION_ID, CORRELATION_ID)
                                                                     .headers(Headers.X_B_3_TRACEID, TRACE_ID)
                                                                     .post("/user")
                                                                     .as(UserDataDTO.class);
                                       result.set(user);
                                   })
                                   .build());
        assertThat(FunctionalInterceptor.VALUE.get()).isEqualTo(FunctionalInterceptor.INTERCEPTED);
    }
}
