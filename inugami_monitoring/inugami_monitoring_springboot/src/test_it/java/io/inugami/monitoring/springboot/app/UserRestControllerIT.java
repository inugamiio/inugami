package io.inugami.monitoring.springboot.app;

import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.commons.test.logs.LogTestAppender;
import io.inugami.framework.interfaces.monitoring.logger.BasicLogEvent;
import io.inugami.framework.interfaces.monitoring.logger.LogListener;
import io.inugami.monitoring.springboot.spring.SpringBootIntegrationTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.commons.test.UnitTestHelper.assertTextIntegration;
import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.with;

class UserRestControllerIT extends SpringBootIntegrationTest {


    public static final String CONTENT_TYPE                   = "Content-Type";
    public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=UTF-8";

    @Test
    void crud() {
        List<BasicLogEvent> logs = new ArrayList<>();
        final LogListener listener = new LogListener() {
            @Override
            public boolean accept(final String loggerName) {
                return "IOLOG".equalsIgnoreCase(loggerName);
            }

            @Override
            public void append(final BasicLogEvent event) {
                logs.add(event);
            }
        };
        LogTestAppender.register(listener);
        assertText(when().get("/user").body().prettyPrint(),
                   """
                           {
                               "data": [
                                  \s
                               ],
                               "nbFoundItems": 0,
                               "next": false,
                               "page": 0,
                               "pageSize": 0,
                               "previous": false,
                               "totalPages": 0
                           }
                           """);

        final var createResponse = with().body(List.of(UnitTestData.USER_1))
                                         .when()
                                         .header(CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8)
                                         .post("/user")
                                         .body().prettyPrint();


        assertText(createResponse,
                   """
                           [
                               {
                                   "birthday": "1988-04-12",
                                   "canton": "VD",
                                   "city": "Cheseaux-sur-Lausanne",
                                   "deviceIdentifier": "401f0498-c43f-43ad-a3f4-2888838332ad",
                                   "email": "emilie.lalonde@mock.org",
                                   "firstName": "Émilie",
                                   "id": 1,
                                   "lastName": "Lalonde",
                                   "nationality": "CH",
                                   "old": 35,
                                   "phoneNumber": "0615031522",
                                   "sex": "FEMALE",
                                   "socialId": "7564971247732",
                                   "streetName": "du Château",
                                   "streetNumber": "10",
                                   "streetType": "Chem.",
                                   "zipCode": "1033"
                               }
                           ]
                           """);
        LogTestAppender.removeListener(listener);
        assertTextIntegration(logs,
                              "io/inugami/monitoring/springboot/app/userRestControllerIT/crud.logs.json",
                              SkipLineMatcher.of(6, 7, 18, 19, 27, 34, 35, 46, 47, 55, 64, 68, 73, 74, 81, 87,
                                                 92, 98, 106, 107, 118, 119, 128, 136, 137, 148, 149, 158, 168, 172,
                                                 178, 179, 186, 192, 197, 203));
    }
}