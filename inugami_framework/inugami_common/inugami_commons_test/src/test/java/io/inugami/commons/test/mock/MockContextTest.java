package io.inugami.commons.test.mock;

import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.api.UuidLineMatcher;
import org.junit.jupiter.api.Test;

import static io.inugami.commons.test.UnitTestHelper.assertText;

class MockContextTest {

    public static final String URI = "/some/uri";

    @Test
    void basic_get() {
        assertText(MockContext.builder()
                              .get("/ws/user/{id}")
                              .addRequestOptions("id", UnitTestData.UID)
                              .addRequestHeaderTracking()
                              .addResponseHeaderTracking()
                              .statusSuccess()
                              .responsePayload(UnitTestData.USER_1)
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
                   UuidLineMatcher.of(4, 5, 12, 13, 14));
    }

    @Test
    void addRequestOption_nominal() {
        final var builder = MockContext.builder();
        assertText(builder.addRequestOptions("id", UnitTestData.UID).build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "requestOptions" : {
                               "id" : [ "bb895294-efe7-484b-b670-14d004eaf461" ]
                             },
                             "status" : 0
                           }
                           """);

        assertText(builder.addRequestOptions("page", 1).build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "requestOptions" : {
                               "id" : [ "bb895294-efe7-484b-b670-14d004eaf461" ],
                               "page" : [ 1 ]
                             },
                             "status" : 0
                           }
                           """);
    }

    @Test
    void addRequestParam_nominal() {
        final var builder = MockContext.builder();
        assertText(builder.addRequestParam("id", UnitTestData.UID).build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "requestParams" : {
                               "id" : "bb895294-efe7-484b-b670-14d004eaf461"
                             },
                             "status" : 0
                           }
                           """);

        assertText(builder.addRequestOptions("page", 1).build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "requestOptions" : {
                               "page" : [ 1 ]
                             },
                             "requestParams" : {
                               "id" : "bb895294-efe7-484b-b670-14d004eaf461"
                             },
                             "status" : 0
                           }
                           """);
    }

    @Test
    void verb_nominal() {
        assertText(MockContext.builder().post(URI).build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "status" : 0,
                             "url" : "/some/uri",
                             "verb" : "POST"
                           }
                           """);
        assertText(MockContext.builder().put(URI).build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "status" : 0,
                             "url" : "/some/uri",
                             "verb" : "PUT"
                           }
                           """);
        assertText(MockContext.builder().patch(URI).build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "status" : 0,
                             "url" : "/some/uri",
                             "verb" : "PATCH"
                           }
                           """);
        assertText(MockContext.builder().delete(URI).build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "status" : 0,
                             "url" : "/some/uri",
                             "verb" : "DELETE"
                           }
                           """);
        assertText(MockContext.builder().options(URI).build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "status" : 0,
                             "url" : "/some/uri",
                             "verb" : "OPTIONS"
                           }
                           """);
        assertText(MockContext.builder().head(URI).build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "status" : 0,
                             "url" : "/some/uri",
                             "verb" : "HEAD"
                           }
                           """);
        assertText(MockContext.builder().connect(URI).build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "status" : 0,
                             "url" : "/some/uri",
                             "verb" : "CONNECT"
                           }
                           """);
        assertText(MockContext.builder().trace(URI).build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "status" : 0,
                             "url" : "/some/uri",
                             "verb" : "TRACE"
                           }
                           """);
    }

    @Test
    void status_nominal() {
        assertText(MockContext.builder().statusSuccess().build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "status" : 200
                           }
                           """);
        assertText(MockContext.builder().statusFunctionalError().build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "status" : 400
                           }
                           """);
        assertText(MockContext.builder().statusTechnicalError().build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "status" : 500
                           }
                           """);
    }


    @Test
    void request_nominal() {
        assertText(MockContext.builder().request(null).build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "status" : 0
                           }
                           """);
        assertText(MockContext.builder().requestPayload(UnitTestData.USER_1).build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "request" : "{\\n  \\"birthday\\" : \\"1988-04-12\\",\\n  \\"canton\\" : \\"VD\\",\\n  \\"city\\" : \\"Cheseaux-sur-Lausanne\\",\\n  \\"deviceIdentifier\\" : \\"401f0498-c43f-43ad-a3f4-2888838332ad\\",\\n  \\"email\\" : \\"emilie.lalonde@mock.org\\",\\n  \\"firstName\\" : \\"Émilie\\",\\n  \\"id\\" : 1,\\n  \\"lastName\\" : \\"Lalonde\\",\\n  \\"nationality\\" : \\"CH\\",\\n  \\"old\\" : 35,\\n  \\"phoneNumber\\" : \\"0615031522\\",\\n  \\"sex\\" : \\"FEMALE\\",\\n  \\"socialId\\" : \\"7564971247732\\",\\n  \\"streetName\\" : \\"du Château\\",\\n  \\"streetNumber\\" : \\"10\\",\\n  \\"streetType\\" : \\"Chem.\\",\\n  \\"zipCode\\" : \\"1033\\"\\n}",
                             "status" : 0
                           }
                           """);
    }

    @Test
    void response_nominal() {
        assertText(MockContext.builder().response(null).build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "status" : 0
                           }
                           """);
        assertText(MockContext.builder().responsePayload(UnitTestData.USER_1).build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "response" : "{\\n  \\"birthday\\" : \\"1988-04-12\\",\\n  \\"canton\\" : \\"VD\\",\\n  \\"city\\" : \\"Cheseaux-sur-Lausanne\\",\\n  \\"deviceIdentifier\\" : \\"401f0498-c43f-43ad-a3f4-2888838332ad\\",\\n  \\"email\\" : \\"emilie.lalonde@mock.org\\",\\n  \\"firstName\\" : \\"Émilie\\",\\n  \\"id\\" : 1,\\n  \\"lastName\\" : \\"Lalonde\\",\\n  \\"nationality\\" : \\"CH\\",\\n  \\"old\\" : 35,\\n  \\"phoneNumber\\" : \\"0615031522\\",\\n  \\"sex\\" : \\"FEMALE\\",\\n  \\"socialId\\" : \\"7564971247732\\",\\n  \\"streetName\\" : \\"du Château\\",\\n  \\"streetNumber\\" : \\"10\\",\\n  \\"streetType\\" : \\"Chem.\\",\\n  \\"zipCode\\" : \\"1033\\"\\n}",
                             "status" : 0
                           }
                           """);
    }

    @Test
    void addRequestHeader_nominal() {
        assertText(MockContext.builder().addRequestHeader("source","default").build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "requestHeaders" : {
                               "source" : "default"
                             },
                             "status" : 0
                           }
                           """);
    }

    @Test
    void addResponseHeader_nominal() {
        assertText(MockContext.builder().addResponseHeader("source","default").build(),
                   """
                           {
                             "contentType" : "application/json",
                             "encoding" : "UTF-8",
                             "responseHeaders" : {
                               "source" : "default"
                             },
                             "status" : 0
                           }
                           """);
    }
}