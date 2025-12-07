package io.inugami.dashboard.interfaces.core.domain.alerting;

import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.mock.MockContext;
import io.inugami.commons.test.mock.MockGenerator;
import io.inugami.commons.test.mock.MockOpenApiContext;
import io.inugami.dashboard.api.domain.alerting.IAlertingService;
import io.inugami.dashboard.api.domain.alerting.exception.AlertingErrors;
import io.inugami.dashboard.interfaces.core.domain.alerting.mapper.InugamiInterfaceAlertingConfiguration;
import io.inugami.dashboard.interfaces.domain.alerting.AlertingRestClient;
import io.inugami.dashboard.interfaces.domain.alerting.dto.AlertingSearchRequestAPI;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.models.event.AlertingModel;
import io.inugami.framework.interfaces.models.search.SearchResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.commons.test.mock.MockContext.PATCH;
import static io.inugami.commons.test.mock.MockContext.PUT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertingRestControllerTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private static final String BASE_FOLDER  = "io/inugami/dashboard/interfaces/domain/alerting/alertingRestClient/";
    private static final String CREATE       = BASE_FOLDER + "create";
    private static final String GET_BY_ID    = BASE_FOLDER + "getById";
    private static final String SEARCH       = BASE_FOLDER + "search";
    private static final String UPDATE_FORCE = BASE_FOLDER + "updateForce";
    private static final String UPDATE       = BASE_FOLDER + "update";
    private static final String DELETE       = BASE_FOLDER + "delete";


    @Mock
    private IAlertingService alertingService;

    @AfterAll
    public static void generateOpenApi() {
        MockGenerator.generateOpenApiDocumentation(MockOpenApiContext.builder()
                                                                     .restClientClass(AlertingRestClient.class)
                                                                     .folders(List.of(CREATE, GET_BY_ID, SEARCH, UPDATE_FORCE, UPDATE, DELETE))
                                                                     .build());
    }

    //==================================================================================================================
    // CREATE
    //==================================================================================================================
    @Test
    void create_nominal() {
        when(alertingService.create(any())).thenReturn(List.of(buildAlertingModel()));
        final var request  = List.of(buildAlertingModel().toBuilder().uid(null).build());
        final var response = controller().create(request);
        assertText(response, """
                [ {
                  "condition" : "value > 5",
                  "description" : "lorem ipsum",
                  "function" : "handlerFunction",
                  "message" : "sorry",
                  "name" : "simple-alert",
                  "provider" : "graphite",
                  "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                } ]
                """);

        MockGenerator.generate(MockContext.builder()
                                          .folder(CREATE)
                                          .post("/alerting")
                                          .addRequestHeaderTracking()
                                          .addResponseHeaderTracking()
                                          .statusSuccess()
                                          .requestPayload(request)
                                          .responsePayload(response)
                                          .build());

        MockGenerator.generate(MockContext.builder()
                                          .folder(CREATE)
                                          .post("/alerting")
                                          .addRequestHeaderTracking()
                                          .addResponseHeaderTracking()
                                          .statusSuccess()
                                          .requestPayload(request)
                                          .errorCode(AlertingErrors.CREATE_INVALID_DATA)
                                          .build());
    }

    //==================================================================================================================
    // READ
    //==================================================================================================================
    @Test
    void getById_nominal() {
        when(alertingService.getById(any(), anyBoolean())).thenReturn(buildAlertingModel());
        final var response = controller().getById(UnitTestData.UID, false);
        assertText(response, """
                {
                  "condition" : "value > 5",
                  "description" : "lorem ipsum",
                  "function" : "handlerFunction",
                  "message" : "sorry",
                  "name" : "simple-alert",
                  "provider" : "graphite",
                  "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                }
                """);


        MockGenerator.generate(MockContext.builder()
                                          .folder(GET_BY_ID)
                                          .get("/alerting/{id}")
                                          .addRequestParam("id", UnitTestData.UID)
                                          .addRequestHeaderTracking()
                                          .addResponseHeaderTracking()
                                          .statusSuccess()
                                          .responsePayload(response)
                                          .build());

        List<ErrorCode> errorCodes = List.of(AlertingErrors.READ_INVALID_DATA, AlertingErrors.READ_NOT_FOUND);
        for (ErrorCode error : errorCodes) {
            MockGenerator.generate(MockContext.builder()
                                              .folder(GET_BY_ID)
                                              .get("/alerting/{id}")
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
        when(alertingService.search(any())).thenReturn(SearchResponse.<AlertingModel>builder()
                                                                     .data(List.of(buildAlertingModel()))
                                                                     .page(0)
                                                                     .pageSize(10)
                                                                     .totalPages(1)
                                                                     .nbFoundItems(1)
                                                                     .previous(false)
                                                                     .next(false)
                                                                     .build());
        final var response = controller().search(AlertingSearchRequestAPI.builder().build());
        assertText(response, """
                {
                  "data" : [ {
                    "condition" : "value > 5",
                    "description" : "lorem ipsum",
                    "function" : "handlerFunction",
                    "message" : "sorry",
                    "name" : "simple-alert",
                    "provider" : "graphite",
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
                                          .get("/alerting")
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
        final var request = List.of(buildAlertingModel());
        when(alertingService.update(any(), anyBoolean())).thenReturn(request);

        for (String folder : List.of(UPDATE_FORCE, UPDATE)) {
            final String verb = UPDATE_FORCE.equals(folder) ? PUT : PATCH;
            final var response = PUT.equals(verb)
                    ? controller().updateForce(request)
                    : controller().update(request);

            assertText(response, """
                    [ {
                      "condition" : "value > 5",
                      "description" : "lorem ipsum",
                      "function" : "handlerFunction",
                      "message" : "sorry",
                      "name" : "simple-alert",
                      "provider" : "graphite",
                      "uid" : "bb895294-efe7-484b-b670-14d004eaf461"
                    } ]
                    """);

            MockGenerator.generate(MockContext.builder()
                                              .folder(folder)
                                              .verb(verb)
                                              .url("/alerting")
                                              .addRequestHeaderTracking()
                                              .addResponseHeaderTracking()
                                              .statusSuccess()
                                              .responsePayload(response)
                                              .build());
        }
    }

    //==================================================================================================================
    // DELETE
    //==================================================================================================================
    @Test
    void delete_nominal() {
        controller().delete(List.of(UnitTestData.UID));
        verify(alertingService).delete(List.of(UnitTestData.UID));

        MockGenerator.generate(MockContext.builder()
                                          .folder(DELETE)
                                          .delete("/alerting")
                                          .addRequestOptions("id", UnitTestData.UID)
                                          .addRequestHeaderTracking()
                                          .addResponseHeaderTracking()
                                          .statusSuccess()
                                          .build());
    }

    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    private AlertingRestController controller() {
        return AlertingRestController.builder()
                                     .alertingService(alertingService)
                                     .alertingSearchRequestAPIMapper(new InugamiInterfaceAlertingConfiguration().alertingSearchRequestAPIMapper())
                                     .build();
    }

    private AlertingModel buildAlertingModel() {
        return AlertingModel.builder()
                            .uid(UnitTestData.UID)
                            .name("simple-alert")
                            .description("lorem ipsum")
                            .provider("graphite")
                            .message("sorry")
                            .condition("value > 5")
                            .function("handlerFunction")
                            .build();
    }
}