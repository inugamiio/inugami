package io.inugami.commons.test.mock;

import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.api.UuidLineMatcher;
import io.inugami.commons.test.dto.UserDataDTO;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.commons.test.UnitTestHelper.assertTextRelative;
import static io.inugami.commons.test.mock.MockGenerator.*;
import static io.inugami.framework.interfaces.exceptions.DefaultErrorCode.newBuilder;
import static org.assertj.core.api.Assertions.assertThat;

class MockGeneratorTest {
    //==================================================================================================================
    // GENERATE MOCK
    //==================================================================================================================
    @Test
    void generate_nominal() {
        System.setProperty(MOCK_GENERATOR_PATH, "./target/mocks");
        final var mockFile = generate(buildSuccessGet());

        assertThat(mockFile).isNotNull();

        assertText(readMock(mockFile),
                   """
                           {
                               "contentType" : "application/json",
                               "encoding" : "UTF-8",
                               "requestHeaders" : {
                                 "x-device-identifier" : "eec09ca3-e569-4f08-a21b-ac0fa6e47b2c",
                                 "x-correlation-id" : "37672013-6be3-4867-9021-3efb2bd9440f"
                               },
                               "requestParams" : {
                                 "id" : "bb895294-efe7-484b-b670-14d004eaf461"
                               },
                               "response" : "{\\n  \\"birthday\\" : \\"1988-04-12\\",\\n  \\"canton\\" : \\"VD\\",\\n  \\"city\\" : \\"Cheseaux-sur-Lausanne\\",\\n  \\"deviceIdentifier\\" : \\"401f0498-c43f-43ad-a3f4-2888838332ad\\",\\n  \\"email\\" : \\"emilie.lalonde@mock.org\\",\\n  \\"firstName\\" : \\"Émilie\\",\\n  \\"id\\" : 1,\\n  \\"lastName\\" : \\"Lalonde\\",\\n  \\"nationality\\" : \\"CH\\",\\n  \\"old\\" : 35,\\n  \\"phoneNumber\\" : \\"0615031522\\",\\n  \\"sex\\" : \\"FEMALE\\",\\n  \\"socialId\\" : \\"7564971247732\\",\\n  \\"streetName\\" : \\"du Château\\",\\n  \\"streetNumber\\" : \\"10\\",\\n  \\"streetType\\" : \\"Chem.\\",\\n  \\"zipCode\\" : \\"1033\\"\\n}",
                               "responseHeaders" : {
                                 "x-device-identifier" : "eec09ca3-e569-4f08-a21b-ac0fa6e47b2c",
                                 "x-correlation-id" : "37672013-6be3-4867-9021-3efb2bd9440f",
                                 "x-b3-traceid" : "3a0f576c-81c7-4497-bf84-23799da5287b"
                               },
                               "status" : 200,
                               "url" : "/administration/user/{id}",
                               "verb" : "GET"
                             }
                           """,
                   UuidLineMatcher.of(4, 5, 12, 13, 14));
    }

    //==================================================================================================================
    // GENERATE MOCK
    //==================================================================================================================
    @Test
    void generateOpenApiDocumentation_nominal() throws IOException {
        System.setProperty(MOCK_GENERATOR_PATH, "./target/generateOpenApiDocumentation");

        final String createFolder  = "io/inugami/commons/test/mock/administration/userRestClient/create";
        final String getByIdFolder = "io/inugami/commons/test/mock/administration/userRestClient/getById";
        // POST
        generate(MockContext.builder()
                            .folder(createFolder)
                            .post("/administration/user")
                            .requestPayload(List.of(UnitTestData.USER_1.toBuilder().id(null).build()))
                            .responsePayload(List.of(UnitTestData.USER_1))
                            .addRequestHeaderTracking()
                            .addResponseHeaderTracking()
                            .build());

        generate(MockContext.builder()
                            .folder(createFolder)
                            .post("/administration/user")
                            .errorCode(UserRestClientError.DATA_REQUIRED)
                            .addRequestHeaderTracking()
                            .addResponseHeaderTracking()
                            .build());

        generate(MockContext.builder()
                            .folder(createFolder)
                            .post("/administration/user")
                            .errorCode(UserRestClientError.DATA_INVALID)
                            .addRequestHeaderTracking()
                            .addResponseHeaderTracking()
                            .build());

        // GET
        generate(buildSuccessGet().toBuilder()
                                  .folder(getByIdFolder)
                                  .build());
        generate(MockContext.builder()
                            .folder(getByIdFolder)
                            .get("/administration/user/{id}")
                            .addRequestParam("id", UnitTestData.UID)
                            .addRequestHeaderTracking()
                            .addResponseHeaderTracking()
                            .errorCode(UserRestClientError.USER_NOT_FOUND)
                            .build());

        // GENERATE OPEN API
        final var docFile = generateOpenApiDocumentation(MockOpenApiContext.builder()
                                                                           .restClientClass(UserRestClient.class)
                                                                           .folders(List.of(createFolder, getByIdFolder))
                                                                           .build());

        final String restClientDoc = FileUtils.readFileToString(docFile, StandardCharsets.UTF_8);
        assertTextRelative(restClientDoc,
                           "test/mockGeneratorTest/generateOpenApiDocumentation_nominal.txt");
    }

    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    private static MockContext buildSuccessGet() {
        return MockContext.builder()
                          .folder("administration/user")
                          .get("/administration/user/{id}")
                          .addRequestParam("id", UnitTestData.UID)
                          .addRequestHeaderTracking()
                          .addResponseHeaderTracking()
                          .statusSuccess()
                          .responsePayload(UnitTestData.USER_1)
                          .build();
    }

    @RequestMapping(path = "administration/ping")
    private interface UserRestClient {

        @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        Collection<UserDataDTO> create(@RequestBody Collection<UserDataDTO> users);

        @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        UserDataDTO getUserById(@PathVariable(required = true) final String id);
    }

    private enum UserRestClientError implements ErrorCode {
        DATA_REQUIRED(newBuilder().errorCode("TEST-1_0")
                                  .message("required data")
                                  .messageDetail("please check your input data")
                                  .url("http://inugami.io/doc/mock")
                                  .domain("administration")
                                  .subDomain("user")
                                  .statusCode(400)
                                  .errorTypeFunctional()),
        DATA_INVALID(newBuilder().errorCode("TEST-1_1")
                                 .message("data invalid")
                                 .messageDetail("please check your input data")
                                 .url("http://inugami.io/doc/mock")
                                 .statusCode(400)
                                 .domain("administration")
                                 .subDomain("user")
                                 .errorTypeFunctional()),
        USER_NOT_FOUND(newBuilder().errorCode("TEST-2_0")
                                   .message("user not found")
                                   .messageDetail("no user found")
                                   .url("http://inugami.io/doc/mock")
                                   .statusCode(404)
                                   .domain("administration")
                                   .subDomain("user")
                                   .errorTypeFunctional());
        private final ErrorCode errorCode;

        UserRestClientError(final DefaultErrorCode.DefaultErrorCodeBuilder errorBuilder) {
            errorCode = errorBuilder.build();
        }

        @Override
        public ErrorCode getCurrentErrorCode() {
            return errorCode;
        }
    }
}