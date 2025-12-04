package io.inugami.commons.test.mock;

import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.commons.test.api.UuidLineMatcher;
import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.commons.test.mock.MockGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;

class MockGeneratorTest {

    @Test
    void generate_nominal() {
        System.setProperty(MOCK_GENERATOR_PATH, "./target/mocks");
        final var mockFile = generate(MockContext.builder()
                                                 .folder("administration/user")
                                                 .get("/administration/user/{id}")
                                                 .addRequestParam("id", UnitTestData.UID)
                                                 .addRequestHeaderTracking()
                                                 .addResponseHeaderTracking()
                                                 .statusSuccess()
                                                 .response(UnitTestData.USER_1)
                                                 .build());

        assertThat(mockFile).isNotNull();

        assertText(readMock(mockFile),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "request" : "null",
                             "requestHeaders" : {
                               "x-device-identifier" : "a3443290-4d76-4ec4-8d5d-2efed6cfea36",
                               "x-correlation-id" : "d49d6dd1-99e8-4efe-b8d3-a5cc39d8f179"
                             },
                             "requestParams" : {
                               "id" : "bb895294-efe7-484b-b670-14d004eaf461"
                             },
                             "response" : "\\"{\\\\n  \\\\\\"birthday\\\\\\" : \\\\\\"1988-04-12\\\\\\",\\\\n  \\\\\\"canton\\\\\\" : \\\\\\"VD\\\\\\",\\\\n  \\\\\\"city\\\\\\" : \\\\\\"Cheseaux-sur-Lausanne\\\\\\",\\\\n  \\\\\\"deviceIdentifier\\\\\\" : \\\\\\"401f0498-c43f-43ad-a3f4-2888838332ad\\\\\\",\\\\n  \\\\\\"email\\\\\\" : \\\\\\"emilie.lalonde@mock.org\\\\\\",\\\\n  \\\\\\"firstName\\\\\\" : \\\\\\"Émilie\\\\\\",\\\\n  \\\\\\"id\\\\\\" : 1,\\\\n  \\\\\\"lastName\\\\\\" : \\\\\\"Lalonde\\\\\\",\\\\n  \\\\\\"nationality\\\\\\" : \\\\\\"CH\\\\\\",\\\\n  \\\\\\"old\\\\\\" : 35,\\\\n  \\\\\\"phoneNumber\\\\\\" : \\\\\\"0615031522\\\\\\",\\\\n  \\\\\\"sex\\\\\\" : \\\\\\"FEMALE\\\\\\",\\\\n  \\\\\\"socialId\\\\\\" : \\\\\\"7564971247732\\\\\\",\\\\n  \\\\\\"streetName\\\\\\" : \\\\\\"du Château\\\\\\",\\\\n  \\\\\\"streetNumber\\\\\\" : \\\\\\"10\\\\\\",\\\\n  \\\\\\"streetType\\\\\\" : \\\\\\"Chem.\\\\\\",\\\\n  \\\\\\"zipCode\\\\\\" : \\\\\\"1033\\\\\\"\\\\n}\\"",
                             "responseHeaders" : {
                               "x-device-identifier" : "a3443290-4d76-4ec4-8d5d-2efed6cfea36",
                               "x-correlation-id" : "d49d6dd1-99e8-4efe-b8d3-a5cc39d8f179",
                               "x-b3-traceid" : "e2151347-6242-466f-b044-2a1da5fb4fa2"
                             },
                             "status" : 200,
                             "url" : "/administration/user/{id}",
                             "verb" : "GET"
                           }
                           """,
                   UuidLineMatcher.of(5, 6, 13, 14, 15));
    }
}