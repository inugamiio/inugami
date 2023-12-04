package io.inugami.monitoring.springboot.exception;

import io.inugami.api.loggers.Loggers;
import io.inugami.commons.test.api.SkipLineMatcher;
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
                                   .addLineMatcher(SkipLineMatcher.of(9, 13, 17, 18, 26, 41, 45, 46, 58, 62, 70, 78, 79, 82, 83, 91))
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
                                   .addLineMatcher(SkipLineMatcher.of(9, 13, 17, 18, 26, 41, 45, 46, 56, 60, 68, 76, 77, 80, 81, 89))
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