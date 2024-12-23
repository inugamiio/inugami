package io.inugami.framework.api.connectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.interfaces.connectors.ConnectorListener;
import io.inugami.framework.interfaces.connectors.HttpRequest;
import io.inugami.framework.interfaces.connectors.IHttpConnectorResult;
import io.inugami.framework.interfaces.connectors.config.HttpBasicConnectorConfiguration;
import io.inugami.framework.interfaces.exceptions.services.ConnectorException;
import io.inugami.framework.interfaces.monitoring.models.Headers;
import lombok.*;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static io.inugami.framework.api.tools.unit.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HttpBasicConnectorTest {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private final static Clock                            CLOCK         = Clock.fixed(Instant.parse("2018-08-19T16:02:42.00Z"), ZoneId.of("UTC"));
    private final static TypeReference<List<EndpointDTO>> TYPE          = new TypeReference<List<EndpointDTO>>() {
    };
    public static final  String                           FULL_URL      = "http://localhost:8080/mock/my/endpoint?full=true";
    public static final  String                           HEADERS       = """
            [ {
              "first" : "trace",
              "second" : "91e0e458-93df-4d23-8653-3c6e5cd8794e"
            }, {
              "first" : "action",
              "second" : "test"
            } ]
            """;
    public static final  String                           ENDPOINT      = "my/endpoint";
    public static final  String                           RESPONSE_LIST = """
            [ {
              "status" : "success"
            } ]
            """;

    @Mock
    private OkHttpClient            client;
    @Mock
    private Call                    call;
    @Captor
    private ArgumentCaptor<Request> requestCaptor;

    @BeforeEach
    public void init() {

    }

    // =================================================================================================================
    // GET
    // =================================================================================================================
    @Test
    void get_nominal() throws Exception {
        final TestConnectorListener    listener = new TestConnectorListener();
        final AtomicReference<Request> request  = new AtomicReference<>();
        when(client.newCall(any())).thenAnswer(answer -> {
            request.set(answer.getArgument(0));
            return call;
        });
        when(call.execute()).thenAnswer(a -> buildResponse(request.get()));


        final HttpConnectorResult result = connector().get(HttpRequest.builder()
                                                                      .addHeader("trace", "91e0e458-93df-4d23-8653-3c6e5cd8794e")
                                                                      .addOption("full", "true")
                                                                      .listener(listener)
                                                                      .url(ENDPOINT)
                                                                      .build());


        assertThat(result).isNotNull();
        verify(client).newCall(requestCaptor.capture());
        assertThat(requestCaptor.getValue().url()).hasToString(FULL_URL);
        assertText(requestCaptor.getValue().headers(), HEADERS);
        assertText(result, """
                 {
                   "bodyData" : "eyJzdGF0dXMiOiJzdWNjZXNzIn0K",
                   "charset" : "UTF-8",
                   "delay" : 0,
                   "encoding" : "UTF-8",
                   "hashHumanReadable" : "[GET]http://localhost:8080/mock/my/endpoint?full=true",
                   "length" : 0,
                   "message" : "success",
                   "responseAt" : 1534694562,
                   "responseHeaders" : {
                     "x-correlation-id" : "06adc5f2-22b7-4e68-b436-05304f484ca4"
                   },
                   "statusCode" : 200,
                   "url" : "http://localhost:8080/mock/my/endpoint?full=true",
                   "verb" : "GET",
                   "errorCode" : null
                 }                 
                """);
        assertText(result.getBodyFromJson(EndpointDTO.class), """
                {
                  "status" : "success"
                }
                """);
    }


    // =================================================================================================================
    // POST
    // =================================================================================================================
    @Test
    void post_nominal() throws Exception {
        final var requestPayload = List.of(EndpointDTO.builder().status("success").build());

        final TestConnectorListener    listener = new TestConnectorListener();
        final AtomicReference<Request> request  = new AtomicReference<>();
        when(client.newCall(any())).thenAnswer(answer -> {
            request.set(answer.getArgument(0));
            return call;
        });
        when(call.execute()).thenAnswer(a -> buildListResponse(request.get(), requestPayload));


        final HttpConnectorResult result = connector().post(HttpRequest.builder()
                                                                       .addHeader("trace", "91e0e458-93df-4d23-8653-3c6e5cd8794e")
                                                                       .addOption("full", "true")
                                                                       .listener(listener)
                                                                       .url(ENDPOINT)
                                                                       .body(requestPayload)
                                                                       .build());


        assertThat(result).isNotNull();
        verify(client).newCall(requestCaptor.capture());
        assertThat(requestCaptor.getValue().url()).hasToString(FULL_URL);
        assertText(requestCaptor.getValue().headers(), HEADERS);
        assertText(result, """
                {
                    "bodyData" : "WyB7CiAgInN0YXR1cyIgOiAic3VjY2VzcyIKfSBd",
                    "charset" : "UTF-8",
                    "delay" : 0,
                    "encoding" : "UTF-8",
                    "hashHumanReadable" : "[POST]http://localhost:8080/mock/my/endpoint?full=true?data=[{\\"status\\":\\"success\\"}]",
                    "length" : 0,
                    "message" : "success",
                    "requestData" : "[{\\"status\\":\\"success\\"}]",
                    "responseAt" : 1534694562,
                    "responseHeaders" : {
                      "x-correlation-id" : "06adc5f2-22b7-4e68-b436-05304f484ca4"
                    },
                    "statusCode" : 200,
                    "url" : "http://localhost:8080/mock/my/endpoint?full=true",
                    "verb" : "POST",
                    "errorCode" : null
                  }                 
                """);
        assertText(result.getBodyFromJson(TYPE), RESPONSE_LIST);

    }


    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    private HttpBasicConnector connector() {
        return new HttpBasicConnector(HttpBasicConnectorConfiguration.builder()
                                                                     .baseUrl("http://localhost:8080/mock")
                                                                     .clientBuilder(() -> client)
                                                                     .clock(CLOCK)
                                                                     .build());
    }

    @Getter
    private static class TestConnectorListener implements ConnectorListener {
        private List<String> steps = new ArrayList<>();

        @Override
        public HttpRequest beforeCalling(final HttpRequest request) {
            steps.add("beforeCalling");
            return request.toBuilder().addHeader("action", "test").build();
        }

        @Override
        public HttpRequest processCalling(final HttpRequest request) {
            steps.add("processCalling");
            return request;
        }

        @Override
        public void onError(final ConnectorException exception) {
            steps.add("onError");
        }

        @Override
        public void onDone(final IHttpConnectorResult stepResult) {
            steps.add("onDone");
        }

        @Override
        public void onError(final IHttpConnectorResult stepResult) {
            steps.add("onError with step result");
        }

    }

    @ToString
    @Builder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @EqualsAndHashCode
    public static class EndpointDTO {
        private String status;
    }

    private Response buildResponse(final Request request) {
        return new Response.Builder().code(200)
                                     .request(request)
                                     .addHeader(Headers.X_CORRELATION_ID, "06adc5f2-22b7-4e68-b436-05304f484ca4")
                                     .body(ResponseBody.create("""
                                                                       {"status":"success"}
                                                                       """.getBytes(StandardCharsets.UTF_8), MediaType.get("application/json")))
                                     .protocol(Protocol.HTTP_1_1)
                                     .message("success")
                                     .build();
    }

    private Response buildListResponse(final Request request, final Object value) throws JsonProcessingException {
        return new Response.Builder().code(200)
                                     .request(request)
                                     .addHeader(Headers.X_CORRELATION_ID, "06adc5f2-22b7-4e68-b436-05304f484ca4")
                                     .body(ResponseBody.create(JsonMarshaller.getInstance()
                                                                             .getIndentedObjectMapper()
                                                                             .writeValueAsBytes(value), MediaType.get("application/json")))
                                     .protocol(Protocol.HTTP_1_1)
                                     .message("success")
                                     .build();
    }
}