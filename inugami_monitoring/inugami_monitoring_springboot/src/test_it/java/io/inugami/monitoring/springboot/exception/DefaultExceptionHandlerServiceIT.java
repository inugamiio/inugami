package io.inugami.monitoring.springboot.exception;

import io.inugami.api.loggers.Loggers;
import io.inugami.commons.test.api.NumberLineMatcher;
import io.inugami.commons.test.api.RegexLineMatcher;
import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.commons.test.api.UuidLineMatcher;
import io.inugami.commons.test.dto.AssertLogContext;
import io.inugami.monitoring.springboot.SpringBootIntegrationTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.concurrent.atomic.AtomicReference;

import static io.inugami.commons.test.UnitTestHelper.assertLogs;
import static io.inugami.commons.test.UnitTestHelper.assertTextIntegration;

class DefaultExceptionHandlerServiceIT extends SpringBootIntegrationTest {

    @Test
    void exception_withErrorCode() {
        final AtomicReference<String> response = new AtomicReference<>();

        assertLogs(AssertLogContext.builder()
                                   .path("io/inugami/monitoring/springboot/exception/exception_withErrorCode.iolog.txt")
                                   .integrationTest(true)
                                   .addPattern(Loggers.IOLOG_NAME)
                                   .addLineMatcher(SkipLineMatcher.of(23, 71, 94))
                                   .addLineMatcher(UuidLineMatcher.of(4, 15, 16, 50, 61, 63, 83, 84, 85))
                                   .addLineMatcher(RegexLineMatcher.of(".*/user.*", 7, 54))
                                   .addLineMatcher(NumberLineMatcher.of(41, 79, 80))
                                   .process(() -> {
                                       final String result = RestAssured.given()
                                                                        .headers(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                                                        .get("/user/0")
                                                                        .asString();
                                       response.set(result);
                                   })
                                   .build());

        assertTextIntegration(response.get(), "io/inugami/monitoring/springboot/exception/exception_withErrorCode.json");
    }


    @Test
    void exception_withoutErrorCode() {
        final AtomicReference<String> response = new AtomicReference<>();

        assertLogs(AssertLogContext.builder()
                                   .path("io/inugami/monitoring/springboot/exception/exception_withoutErrorCode.iolog.txt")
                                   .integrationTest(true)
                                   .addPattern(Loggers.IOLOG_NAME)
                                   .addLineMatcher(SkipLineMatcher.of(23, 69, 92))
                                   .addLineMatcher(UuidLineMatcher.of(4, 15, 16, 48, 59, 61, 81, 82, 83))
                                   .addLineMatcher(RegexLineMatcher.of(".*/user.*", 7, 52))
                                   .addLineMatcher(NumberLineMatcher.of(40, 77, 78))
                                   .process(() -> {
                                       final String result = RestAssured.given()
                                                                        .headers(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                                                        .get("/user/42")
                                                                        .asString();
                                       response.set(result);
                                   })
                                   .build());

        assertTextIntegration(response.get(), "io/inugami/monitoring/springboot/exception/exception_withoutErrorCode.json");
    }


}