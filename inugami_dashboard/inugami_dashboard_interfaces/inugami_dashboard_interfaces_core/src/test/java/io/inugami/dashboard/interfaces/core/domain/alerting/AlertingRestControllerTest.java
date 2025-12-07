package io.inugami.dashboard.interfaces.core.domain.alerting;

import io.inugami.commons.test.UnitTestData;
import io.inugami.commons.test.mock.MockContext;
import io.inugami.commons.test.mock.MockGenerator;
import io.inugami.commons.test.mock.MockOpenApiContext;
import io.inugami.dashboard.api.domain.alerting.IAlertingService;
import io.inugami.dashboard.api.domain.alerting.exception.AlertingErrors;
import io.inugami.dashboard.interfaces.core.domain.alerting.mapper.InugamiInterfaceAlertingConfiguration;
import io.inugami.dashboard.interfaces.domain.alerting.AlertingRestClient;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.models.event.AlertingModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertingRestControllerTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private static final String           BASE_FOLDER = "io/inugami/dashboard/interfaces/core/domain/alerting/alertingRestClient/";
    private static final String           CREATE      = BASE_FOLDER + "create";
    private static final String           GET_BY_ID   = BASE_FOLDER + "getById";
    @Mock
    private              IAlertingService alertingService;

    @AfterAll
    public static void generateOpenApi() {
        MockGenerator.generateOpenApiDocumentation(MockOpenApiContext.builder()
                                                                     .restClientClass(AlertingRestClient.class)
                                                                     .folders(List.of(CREATE,
                                                                                      GET_BY_ID))
                                                                     .build());
    }

    //==================================================================================================================
    // CREATE
    //==================================================================================================================
    @Test
    void create_nominal() {
        when(alertingService.create(any())).thenReturn(List.of(buildAlertingModel()));
        final var request = List.of(buildAlertingModel().toBuilder()
                                                        .uid(null)
                                                        .build());
        final var response = controller().create(request);
        assertText(response,
                   """
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
        assertText(response,
                   """
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
    //==================================================================================================================
    // UPDATE
    //==================================================================================================================

    //==================================================================================================================
    // DELETE
    //==================================================================================================================

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