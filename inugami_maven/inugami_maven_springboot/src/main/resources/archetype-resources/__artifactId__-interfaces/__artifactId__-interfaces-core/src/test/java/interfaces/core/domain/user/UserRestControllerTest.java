package ${package}.interfaces.core.domain.user;

import ${package}.api.domain.user.IUserService;
import ${package}.api.domain.user.dto.UserDTO;
import ${package}.interfaces.api.domain.user.UserRestClient;
import ${package}.interfaces.api.domain.user.dto.UserAPI;
import ${package}.interfaces.api.domain.user.dto.UserDTOSearchRequestAPI;
import ${package}.interfaces.core.domain.user.mapper.UserRestMapperConfiguration;
import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.mock.MockContext;
import io.inugami.commons.test.mock.MockGenerator;
import io.inugami.commons.test.mock.MockOpenApiContext;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.models.basic.AuditDTO;
import io.inugami.framework.interfaces.models.search.SearchResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static ${package}.api.domain.user.exception.UserErrors.*;
import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.commons.test.mock.MockContext.PATCH;
import static io.inugami.commons.test.mock.MockContext.PUT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private static final String BASE_FOLDER  = "interfaces/api/domain/user/userRestClient/";
    private static final String CREATE       = BASE_FOLDER + "create";
    private static final String GET_BY_ID    = BASE_FOLDER + "getById";
    private static final String SEARCH       = BASE_FOLDER + "search";
    private static final String UPDATE_FORCE = BASE_FOLDER + "updateForce";
    private static final String UPDATE       = BASE_FOLDER + "update";
    private static final String DELETE       = BASE_FOLDER + "delete";
    public static final  String BASE_PATH    = "/user";

    @Mock
    private IUserService userService;

    @AfterAll
    public static void generateOpenApi() {
        MockGenerator.generateOpenApiDocumentation(MockOpenApiContext.builder()
                                                                     .restClientClass(UserRestClient.class)
                                                                     .folders(List.of(CREATE, GET_BY_ID, SEARCH, UPDATE_FORCE, UPDATE, DELETE))
                                                                     .build());
    }

    //==================================================================================================================
    // CREATE
    //==================================================================================================================
    @Test
    void create_nominal() {
        when(userService.create(any())).thenReturn(List.of(buildUserDTO()));
        final var request  = List.of(buildUserAPI());
        final var response = controller().create(request);
        assertText(response, """
                [ {
                  "audit" : {
                    "createdBy" : "system",
                    "createdDate" : "2023-05-27T12:00:00",
                    "lastModifiedBy" : "user-1",
                    "lastModifiedDate" : "2023-06-01T12:00:00",
                    "version" : 1
                  },
                  "email" : "john.smith@mock.org",
                  "firstName" : "John",
                  "lastName" : "Smith",
                  "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                } ]
                """);

        MockGenerator.generate(MockContext.builder()
                                          .folder(CREATE)
                                          .post(BASE_PATH)
                                          .addRequestHeaderTracking()
                                          .addResponseHeaderTracking()
                                          .statusSuccess()
                                          .requestPayload(request)
                                          .responsePayload(response)
                                          .build());

        final List<ErrorCode> errors = List.of(CREATE_INVALID_DATA,
                                               CREATE_UID_FORBIDDEN,
                                               CREATE_FIRSTNAME_INVALID,
                                               CREATE_LASTNAME_INVALID,
                                               CREATE_EMAIL_INVALID);
        for (ErrorCode error : errors) {
            MockGenerator.generate(MockContext.builder()
                                              .folder(CREATE)
                                              .post(BASE_PATH)
                                              .addRequestHeaderTracking()
                                              .addResponseHeaderTracking()
                                              .statusSuccess()
                                              .requestPayload(request)
                                              .errorCode(error)
                                              .build());
        }

    }

    //==================================================================================================================
    // READ
    //==================================================================================================================
    @Test
    void getById_nominal() {
        when(userService.getById(any(), anyBoolean())).thenReturn(buildUserDTO());
        final var response = controller().getById(UnitTestData.UID, false);
        assertText(response, """
                {
                  "audit" : {
                    "createdBy" : "system",
                    "createdDate" : "2023-05-27T12:00:00",
                    "lastModifiedBy" : "user-1",
                    "lastModifiedDate" : "2023-06-01T12:00:00",
                    "version" : 1
                  },
                  "email" : "john.smith@mock.org",
                  "firstName" : "John",
                  "lastName" : "Smith",
                  "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                }
                """);


        MockGenerator.generate(MockContext.builder()
                                          .folder(GET_BY_ID)
                                          .get(BASE_PATH + "/{id}")
                                          .addRequestParam("id", UnitTestData.UID)
                                          .addRequestHeaderTracking()
                                          .addResponseHeaderTracking()
                                          .statusSuccess()
                                          .responsePayload(response)
                                          .build());

        final List<ErrorCode> errors = List.of(READ_USER_UID_REQUIRED,
                                               READ_USER_NOT_FOUND);

        for (ErrorCode error : errors) {
            MockGenerator.generate(MockContext.builder()
                                              .folder(GET_BY_ID)
                                              .get(BASE_PATH + "/{id}")
                                              .addRequestParam("id", UnitTestData.UID)
                                              .addRequestHeaderTracking()
                                              .addResponseHeaderTracking()
                                              .statusSuccess()
                                              .errorCode(error)
                                              .build());
        }
    }

    @Test
    void search_nominal() {
        when(userService.search(any())).thenReturn(SearchResponse.<UserDTO>builder()
                                                                 .data(List.of(buildUserDTO()))
                                                                 .page(0)
                                                                 .pageSize(10)
                                                                 .totalPages(1)
                                                                 .nbFoundItems(1)
                                                                 .previous(false)
                                                                 .next(false)
                                                                 .build());
        final var response = controller().search(UserDTOSearchRequestAPI.builder().build());
        assertText(response, """
                {
                  "data" : [ {
                    "audit" : {
                      "createdBy" : "system",
                      "createdDate" : "2023-05-27T12:00:00",
                      "lastModifiedBy" : "user-1",
                      "lastModifiedDate" : "2023-06-01T12:00:00",
                      "version" : 1
                    },
                    "email" : "john.smith@mock.org",
                    "firstName" : "John",
                    "lastName" : "Smith",
                    "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                  } ],
                  "nbFoundItems" : 1,
                  "next" : false,
                  "page" : 0,
                  "pageSize" : 10,
                  "previous" : false,
                  "totalPages" : 1
                }
                """);

        MockGenerator.generate(MockContext.builder()
                                          .folder(SEARCH)
                                          .get(BASE_PATH)
                                          .addRequestHeaderTracking()
                                          .addResponseHeaderTracking()
                                          .statusSuccess()
                                          .responsePayload(response)
                                          .build());
    }

    //==================================================================================================================
    // UPDATE
    //==================================================================================================================
    @Test
    void update_nominal() {
        final var request = List.of(buildUserAPI().toBuilder()
                                                  .uid(UnitTestData.UID)
                                                  .build());
        when(userService.update(any(), anyBoolean())).thenReturn(List.of(buildUserDTO()));

        final List<ErrorCode> errors = List.of(UPDATE_INVALID_DATA,
                                               UPDATE_UID_REQUIRED,
                                               UPDATE_USERS_NOT_FOUND,
                                               UPDATE_FIRSTNAME_INVALID,
                                               UPDATE_LASTNAME_INVALID,
                                               UPDATE_EMAIL_INVALID);

        for (String folder : List.of(UPDATE_FORCE, UPDATE)) {
            final String verb = UPDATE_FORCE.equals(folder) ? PUT : PATCH;
            final var response = PUT.equals(verb)
                                 ? controller().updateForce(request)
                                 : controller().update(request);

            assertText(response, """
                    [ {
                      "audit" : {
                        "createdBy" : "system",
                        "createdDate" : "2023-05-27T12:00:00",
                        "lastModifiedBy" : "user-1",
                        "lastModifiedDate" : "2023-06-01T12:00:00",
                        "version" : 1
                      },
                      "email" : "john.smith@mock.org",
                      "firstName" : "John",
                      "lastName" : "Smith",
                      "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                    } ]
                    """);

            MockGenerator.generate(MockContext.builder()
                                              .folder(folder)
                                              .verb(verb)
                                              .url(BASE_PATH)
                                              .addRequestHeaderTracking()
                                              .addResponseHeaderTracking()
                                              .statusSuccess()
                                              .responsePayload(response)
                                              .build());


            for (ErrorCode error : errors) {
                MockGenerator.generate(MockContext.builder()
                                                  .folder(folder)
                                                  .verb(verb)
                                                  .url(BASE_PATH)
                                                  .addRequestHeaderTracking()
                                                  .addResponseHeaderTracking()
                                                  .statusSuccess()
                                                  .requestPayload(request)
                                                  .errorCode(error)
                                                  .build());
            }
        }
    }

    //==================================================================================================================
    // DELETE
    //==================================================================================================================
    @Test
    void delete_nominal() {
        controller().delete(List.of(UnitTestData.UID));
        verify(userService).delete(List.of(UnitTestData.UID));

        MockGenerator.generate(MockContext.builder()
                                          .folder(DELETE)
                                          .delete(BASE_PATH)
                                          .addRequestOptions("uid", UnitTestData.UID)
                                          .addRequestHeaderTracking()
                                          .addResponseHeaderTracking()
                                          .statusSuccess()
                                          .build());

        final List<ErrorCode> errors = List.of(DELETE_USER_UID_REQUIRED,
                                               DELETE_USERS_NOT_FOUND);
        for (ErrorCode error : errors) {
            MockGenerator.generate(MockContext.builder()
                                              .folder(DELETE)
                                              .delete(BASE_PATH)
                                              .addRequestHeaderTracking()
                                              .addResponseHeaderTracking()
                                              .statusSuccess()
                                              .errorCode(error)
                                              .build());
        }
    }

    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    private UserRestController controller() {
        return UserRestController.builder()
                                 .userService(userService)
                                 .userAPIMapper(new UserRestMapperConfiguration().userAPIMapper())
                                 .build();
    }

    private UserDTO buildUserDTO() {
        return UserDTO.builder()
                      .uid(UnitTestData.UID)
                      .firstName(UnitTestData.FIRSTNAME)
                      .lastName(UnitTestData.LASTNAME)
                      .email(UnitTestData.EMAIL)
                      .audit(AuditDTO.builder()
                                     .createdBy("system")
                                     .createdDate(UnitTestData.DATE_TIME.minusDays(5))
                                     .lastModifiedBy("user-1")
                                     .lastModifiedDate(UnitTestData.DATE_TIME)
                                     .version(1L)
                                     .build())
                      .build();
    }

    private UserAPI buildUserAPI() {
        return UserAPI.builder()
                      .firstName(UnitTestData.FIRSTNAME)
                      .lastName(UnitTestData.LASTNAME)
                      .email(UnitTestData.EMAIL)
                      .build();
    }
}