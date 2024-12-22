package io.inugami.framework.api.connectors;

import io.inugami.framework.interfaces.connectors.ConnectorListener;
import io.inugami.framework.interfaces.connectors.HttpRequest;
import io.inugami.framework.interfaces.connectors.IHttpConnectorResult;
import io.inugami.framework.interfaces.connectors.config.HttpBasicConnectorConfiguration;
import io.inugami.framework.interfaces.exceptions.services.ConnectorException;
import io.inugami.framework.interfaces.monitoring.models.Headers;
import lombok.Getter;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
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
                                                                      .url("my/endpoint")
                                                                      .build());


        assertThat(result).isNotNull();
        verify(client).newCall(requestCaptor.capture());
        assertThat(requestCaptor.getValue().url()).hasToString("http://localhost:8080/mock/my/endpoint?full=true");
        assertText(requestCaptor.getValue().headers(),
                   """
                           [ {
                             "first" : "trace",
                             "second" : "91e0e458-93df-4d23-8653-3c6e5cd8794e"
                           }, {
                             "first" : "action",
                             "second" : "test"
                           } ]
                           """);
        assertText(result,
                   """
                            {
                              "charset" : "UTF-8",
                              "data" : "eyJzdGF0dXMiOiJzdWNjZXNzIn0K",
                              "delay" : 0,
                              "encoding" : "UTF-8",
                              "hashHumanReadable" : "[GET]http://localhost:8080/mock/my/endpoint?full=true",
                              "length" : 21,
                              "message" : "success",
                              "responseAt" : 0,
                              "responseHeaders" : {
                                "x-correlation-id" : "06adc5f2-22b7-4e68-b436-05304f484ca4"
                              },
                              "statusCode" : 200,
                              "url" : "http://localhost:8080/mock/my/endpoint?full=true",
                              "verb" : "GET",
                              "errorCode" : null,
                              "requestData" : ""
                            }                      
                           """);
    }


    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    private HttpBasicConnector connector() {
        return new HttpBasicConnector(HttpBasicConnectorConfiguration.builder()
                                                                     .baseUrl("http://localhost:8080/mock")
                                                                     .clientBuilder(() -> client)
                                                                     .build());
    }

    @Getter
    private static class TestConnectorListener implements ConnectorListener {
        private List<String> steps = new ArrayList<>();

        @Override
        public HttpRequest beforeCalling(final HttpRequest request) {
            steps.add("beforeCalling");
            return request.toBuilder()
                          .addHeader("action", "test")
                          .build();
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


}