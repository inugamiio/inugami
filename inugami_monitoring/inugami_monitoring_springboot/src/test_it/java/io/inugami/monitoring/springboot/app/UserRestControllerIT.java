package io.inugami.monitoring.springboot.app;

import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.commons.test.logs.LogTestAppender;
import io.inugami.commons.test.obfuscator.DefaultITObfuscator;
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


    public static final  String       CONTENT_TYPE                   = "Content-Type";
    public static final  String       APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=UTF-8";
    private static final List<String> BAN_HEADERS                    = List.of(
            "deviceNetworkSpeedDown",
            "host",
            "connection",
            "remoteAddress",
            "userAgent",
            "sessionId",
            "deviceNetworkSpeedLatency",
            "accept",
            "deviceNetworkSpeedUp",
            "accept-encoding",
            "user-agent");

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
        assertTextIntegration(DefaultITObfuscator.renderLogs(logs.stream()
                                                                 .map(this::cleanLogs)
                                                                 .toList()),
                              "io/inugami/monitoring/springboot/app/userRestControllerIT/crud.logs.txt",
                              SkipLineMatcher.of(8,12,14,16,22,40,44,48,51,57,97,100,103,105,107,113,133,136,139,143,146,152));
    }


    private BasicLogEvent cleanLogs(BasicLogEvent basicLogEvent) {
        final var mdc = basicLogEvent.getMdc();
        for (String headerToRemove : BAN_HEADERS) {
            mdc.remove(headerToRemove);
        }

        mdc.put("correlation_id", "XXXX");
        mdc.put("duration", "0");
        return basicLogEvent;
    }
}