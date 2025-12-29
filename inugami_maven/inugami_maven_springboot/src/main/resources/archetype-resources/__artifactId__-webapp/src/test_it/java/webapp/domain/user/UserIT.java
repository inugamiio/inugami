package ${package}.webapp.domain.user;

import ${package}.interfaces.api.domain.user.dto.UserAPI;
import ${package}.webapp.spring.SpringBootIntegrationRunner;
import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.framework.interfaces.models.search.SearchResponse;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.restassured.RestAssured.given;

/**
 * @since 2025-12-29
 */
public class UserIT extends SpringBootIntegrationRunner {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private static final TypeRef<SearchResponse<UserAPI>> TYPE_SEARCH_USERS = new TypeRef<>() {
    };
    private static final TypeRef<Collection<UserAPI>>     TYPE_USERS        = new TypeRef<>() {
    };
    public static final  String                           ENDPOINT_USER     = "/ws/user";
    public static final  String                           NOMINAL_USER      = """
            {
              "audit" : {
                "createdBy" : "system",
                "createdDate" : "2025-12-29T14:00:36.575616616",
                "version" : 1
              },
              "email" : "john.smith@mock.org",
              "firstName" : "John",
              "lastName" : "Smith",
              "uid" : "fa5efc85-1e7d-49f9-b17e-1feb3575e6b8"
            }
            """;
    public static final String EMPTY_USERS = """
            {
              "data" : [ ],
              "nbFoundItems" : 0,
              "next" : false,
              "page" : 0,
              "pageSize" : 20,
              "previous" : false,
              "totalPages" : 0
            }
            """;

    //==================================================================================================================
    // TEST
    //==================================================================================================================
    @Test
    void crud_nominal() {
        final SearchResponse<UserAPI> users = searchUsers();
        assertText(users, EMPTY_USERS);

        final UserAPI user = createUser(UserAPI.builder()
                                               .firstName(UnitTestData.FIRSTNAME)
                                               .lastName(UnitTestData.LASTNAME)
                                               .email(UnitTestData.EMAIL)
                                               .build());

        assertText(user, NOMINAL_USER, SkipLineMatcher.of(3, 9));

        assertText(searchUsers(), """
                {
                  "data" : [ {
                    "audit" : {
                      "createdBy" : "system",
                      "createdDate" : "2025-12-29T14:09:18.801092",
                      "version" : 1
                    },
                    "email" : "john.smith@mock.org",
                    "firstName" : "John",
                    "lastName" : "Smith",
                    "uid" : "6ba43a5c-471d-425f-8bec-a70d0e8ddd7f"
                  } ],
                  "nbFoundItems" : 1,
                  "next" : false,
                  "page" : 0,
                  "pageSize" : 20,
                  "previous" : false,
                  "totalPages" : 1
                }
                """, SkipLineMatcher.of(4, 10));

        assertText(getUser(user.getUid()), NOMINAL_USER, SkipLineMatcher.of(3, 9));

        updateUser(user.toBuilder()
                       .firstName(UnitTestData.USER_1.getFirstName())
                       .lastName(UnitTestData.USER_1.getLastName())
                       .email(UnitTestData.USER_1.getEmail())
                       .build());

        assertText(getUser(user.getUid()), """
                {
                  "audit" : {
                    "createdBy" : "system",
                    "createdDate" : "2025-12-29T14:12:19.161857",
                    "lastModifiedBy" : "system",
                    "lastModifiedDate" : "2025-12-29T14:12:19.535271",
                    "version" : 2
                  },
                  "email" : "emilie.lalonde@mock.org",
                  "firstName" : "Émilie",
                  "lastName" : "Lalonde",
                  "uid" : "62b0f83d-bb1a-4f44-a3fa-e6d6fe2867ae"
                }
                """, SkipLineMatcher.of(3, 5,11));

        deleteUser(user.getUid());
        assertText(searchUsers(), EMPTY_USERS);
    }


    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    private SearchResponse<UserAPI> searchUsers() {
        return given().contentType(APPLICATION_JSON).when().get(ENDPOINT_USER).body().as(TYPE_SEARCH_USERS);
    }

    private UserAPI createUser(final UserAPI user) {
        return given().contentType(APPLICATION_JSON)
                      .body(List.of(user))
                      .when()
                      .post(ENDPOINT_USER)
                      .body()
                      .as(TYPE_USERS)
                      .stream()
                      .findFirst()
                      .orElse(null);
    }

    private UserAPI getUser(final String uid) {
        return given().contentType(APPLICATION_JSON).when().get(ENDPOINT_USER + "/{uid}", uid).body().as(UserAPI.class);
    }

    private UserAPI updateUser(final UserAPI user) {
        return given().contentType(APPLICATION_JSON)
                      .body(List.of(user))
                      .when()
                      .put(ENDPOINT_USER)
                      .body()
                      .as(TYPE_USERS)
                      .stream()
                      .findFirst()
                      .orElse(null);
    }

    private void deleteUser(final String uid) {
        given()
                .queryParam("uid", uid)
                .when()
                .delete(ENDPOINT_USER)
                .then().statusCode(200);
    }
}
