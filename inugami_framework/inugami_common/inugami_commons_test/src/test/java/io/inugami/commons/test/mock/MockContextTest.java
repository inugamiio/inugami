package io.inugami.commons.test.mock;

import io.inugami.commons.test.UnitTestData;
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
                                "x-device-identifier" : "1046f92d-734b-4a32-8368-2539e3f2c6dd",
                                "x-correlation-id" : "c7f8945a-dfa5-4a4c-89a0-13144834f588"
                              },
                              "requestOptions" : {
                                "id" : [ "bb895294-efe7-484b-b670-14d004eaf461" ]
                              },
                              "response" : "{\\n  \\"birthday\\" : \\"1988-04-12\\",\\n  \\"canton\\" : \\"VD\\",\\n  \\"city\\" : \\"Cheseaux-sur-Lausanne\\",\\n  \\"deviceIdentifier\\" : \\"401f0498-c43f-43ad-a3f4-2888838332ad\\",\\n  \\"email\\" : \\"emilie.lalonde@mock.org\\",\\n  \\"firstName\\" : \\"Émilie\\",\\n  \\"id\\" : 1,\\n  \\"lastName\\" : \\"Lalonde\\",\\n  \\"nationality\\" : \\"CH\\",\\n  \\"old\\" : 35,\\n  \\"phoneNumber\\" : \\"0615031522\\",\\n  \\"sex\\" : \\"FEMALE\\",\\n  \\"socialId\\" : \\"7564971247732\\",\\n  \\"streetName\\" : \\"du Château\\",\\n  \\"streetNumber\\" : \\"10\\",\\n  \\"streetType\\" : \\"Chem.\\",\\n  \\"zipCode\\" : \\"1033\\"\\n}",
                              "responseHeaders" : {
                                "x-device-identifier" : "15414bf7-897b-44af-8d44-f4c8801ddc62",
                                "x-correlation-id" : "bcf79a25-32e1-41fa-8d7e-de0f33858c61",
                                "x-b3-traceid" : "98a21f38-5f39-4cef-9d36-24cb8b019356"
                              },
                              "status" : 200,
                              "url" : "/ws/user/{id}",
                              "verb" : "GET"
                            }
                           """,
                   UuidLineMatcher.of(4,5,12,13,14));
    }
}