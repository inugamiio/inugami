package io.inugami.commons.test.mock;

import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.commons.test.api.UuidLineMatcher;
import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestHelper.assertText;

class MockContextTest {
    @Test
    void basic_get() {
        assertText(MockContext.builder()
                              .get("/ws/user/{id}")
                              .addRequestOptions("id", UnitTestData.UID)
                              .addRequestHeaderTracking()
                              .addResponseHeaderTracking()
                              .statusSuccess()
                              .response(UnitTestData.USER_1)
                              .build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "requestHeaders" : {
                               "x-device-identifier" : "43212122-aa1e-4948-93fb-0077e62630bb",
                               "x-correlation-id" : "1da62170-3f2b-42bf-9f64-e626736f7d52",
                               "x-b3-traceid" : "7320360a-cc9c-4927-b390-b18417f02cf8"
                             },
                             "requestOptions" : {
                               "id" : [ "bb895294-efe7-484b-b670-14d004eaf461" ]
                             },
                             "response" : "{\\n  \\"birthday\\" : \\"1988-04-12\\",\\n  \\"canton\\" : \\"VD\\",\\n  \\"city\\" : \\"Cheseaux-sur-Lausanne\\",\\n  \\"deviceIdentifier\\" : \\"401f0498-c43f-43ad-a3f4-2888838332ad\\",\\n  \\"email\\" : \\"emilie.lalonde@mock.org\\",\\n  \\"firstName\\" : \\"Émilie\\",\\n  \\"id\\" : 1,\\n  \\"lastName\\" : \\"Lalonde\\",\\n  \\"nationality\\" : \\"CH\\",\\n  \\"old\\" : 35,\\n  \\"phoneNumber\\" : \\"0615031522\\",\\n  \\"sex\\" : \\"FEMALE\\",\\n  \\"socialId\\" : \\"7564971247732\\",\\n  \\"streetName\\" : \\"du Château\\",\\n  \\"streetNumber\\" : \\"10\\",\\n  \\"streetType\\" : \\"Chem.\\",\\n  \\"zipCode\\" : \\"1033\\"\\n}",
                             "responseHeaders" : {
                               "x-device-identifier" : "b8d8df51-dc08-433b-a78e-2261d9bceaae",
                               "x-correlation-id" : "a90af6d6-9981-4d5c-936c-74c99921ab7c",
                               "x-b3-traceid" : "8de33908-d33b-4260-a046-ab80927dc40f"
                             },
                             "status" : 200,
                             "url" : "/ws/user/{id}",
                             "verb" : "GET"
                           }
                           """,
                   UuidLineMatcher.of(4,5,6,13,14,15));
    }
}