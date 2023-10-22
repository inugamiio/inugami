package io.inugami.commons.connectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import io.inugami.api.exceptions.services.ConnectorException;
import io.inugami.api.exceptions.services.exceptions.ConnectorUndefinedCallException;
import io.inugami.api.marshalling.JsonMarshaller;
import io.inugami.api.monitoring.MdcService;
import lombok.Getter;
import lombok.ToString;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.inugami.commons.UnitTestHelper.assertTextRelatif;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SuppressWarnings({"java:S1607", "java:S5838"})
@WireMockTest(httpPort = 1234, httpsEnabled = false)
class HttpBasicConnectorTest {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static WireMockServer wireMockServer;

    public static final String URL  = "http://localhost:";
    private static      int    PORT = 1234;

    public static final String LASTNAME2      = "Lalonde";
    public static final String PHONE_NUMBER_1 = "0615031522";
    public static final String NAVS13_1       = "7564971247732";
    public static final String NATIONALITY    = "CH";
    public static final String CANTON         = "VD";
    public static final String STREET_NAME    = "du Château";
    public static final String STREET_TYPE    = "Chem.";
    public static final String CITY           = "Cheseaux-sur-Lausanne";
    public static final String ZIP_CODE       = "1033";
    public static final String STREET_NUMBER  = "10";

    // =========================================================================
    // INIT
    // =========================================================================
    @BeforeAll
    public static void init() {

    }

    @AfterAll
    public static void shutdown() {

    }

    // =========================================================================
    // GET
    // =========================================================================
    @Disabled
    @Test
    void get_withHttpRequestAndBadUrl_shouldInvokeListenerOnErrorWithException() throws ConnectorException {
        final HttpBasicConnector connector = buildConnector();

        stubFor(get(urlPathMatching("/.*"))
                        .willReturn(aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody("\"testing-library\": \"WireMock\"")));

        final BasicConnectorListener listener = buildListener();
        final HttpConnectorResult result = connector.get(HttpRequest.builder()
                                                                    .url(buildUrl("?page=1", 0))
                                                                    .listener(listener)
                                                                    .build());

        assertListernerException(listener, ConnectorUndefinedCallException.class);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(500);
    }


    @Test
    void get_withHttpRequest_nominal() throws ConnectorException {
        final HttpBasicConnector connector = buildConnector();

        stubFor(get("/?page=1")
                        .willReturn(aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody("\"testing-library\": \"WireMock\"")));

        final BasicConnectorListener listener = buildListener();
        MdcService.getInstance()
                  .deviceIdentifier("912c8721-f7d5-4bea-84ba-f13381a4fcdb")
                  .correlationId("0d9e9d18-f5cf-44ff-8537-276506d884b0")
                  .traceId("bff3187a6760")
                  .requestId("59cf058fc08b");

        final HttpConnectorResult result = connector.get(HttpRequest.builder()
                                                                    .url(buildUrl(""))
                                                                    .options(Map.of("page", "1"))
                                                                    .listener(listener)
                                                                    .build());

        assertListenerSuccess(listener);
        assertTextRelatif(result, "commons/connectors/get_withHttpRequest_nominal.json", 4);
        assertThat(new String(result.getData())).isEqualTo("\"testing-library\": \"WireMock\"");
    }

    @Test
    void get_withHttpRequest_withJson() throws ConnectorException, JsonProcessingException {
        final HttpBasicConnector connector = buildConnector();

        stubFor(get("/?page=1")
                        .willReturn(aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody(JsonMarshaller.getInstance()
                                                                    .getIndentedObjectMapper()
                                                                    .writeValueAsString(buildUser()))));

        final BasicConnectorListener listener = buildListener();
        MdcService.getInstance()
                  .deviceIdentifier("912c8721-f7d5-4bea-84ba-f13381a4fcdb")
                  .correlationId("0d9e9d18-f5cf-44ff-8537-276506d884b0")
                  .traceId("bff3187a6760")
                  .requestId("59cf058fc08b");

        final HttpConnectorResult result = connector.get(HttpRequest.builder()
                                                                    .url(buildUrl(""))
                                                                    .options(Map.of("page", "1"))
                                                                    .listener(listener)
                                                                    .build());

        assertListenerSuccess(listener);
        assertTextRelatif(result.getResponseBody(UserDataDTO.class),
                          "commons/connectors/get_withHttpRequest_withJson.json");

    }


    @Test
    void get_withOnlyUrl_nominal() throws ConnectorException {
        final HttpBasicConnector connector = buildConnector();

        stubFor(get("/?page=1")
                        .willReturn(aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody("\"testing-library\": \"WireMock\"")));

        final HttpConnectorResult result = connector.get(buildUrl("?page=1"));

        assertTextRelatif(result, "commons/connectors/get_withOnlyUrl_nominal.json", 4, 13, 16);
        assertThat(new String(result.getData())).isEqualTo("\"testing-library\": \"WireMock\"");
    }

    @Test
    void get_fullParams_nominal() throws ConnectorException {
        final HttpBasicConnector connector = buildConnector();

        stubFor(get("/?page=1")
                        .willReturn(aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody("\"testing-library\": \"WireMock\"")));

        CredentialsProvider credentials = new BasicCredentialsProvider();
        credentials.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("user", "password"));
        Map<String, String> headers = Map.of("my-app", "unit-test");

        final HttpConnectorResult result = connector.get(buildUrl("?page=1"), credentials, headers);

        assertTextRelatif(result, "commons/connectors/get_fullParams_nominal.json", 4, 15, 18);
        assertThat(new String(result.getData())).isEqualTo("\"testing-library\": \"WireMock\"");
    }

    // =========================================================================
    // POST
    // =========================================================================
    @Test
    void post_withHttpRequest_nominal() throws ConnectorException {
        final HttpBasicConnector connector = buildConnector();

        stubFor(post("/?page=1")
                        .willReturn(aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody("\"testing-library\": \"WireMock\"")));

        final BasicConnectorListener listener = buildListener();
        MdcService.getInstance()
                  .deviceIdentifier("912c8721-f7d5-4bea-84ba-f13381a4fcdb")
                  .correlationId("0d9e9d18-f5cf-44ff-8537-276506d884b0")
                  .traceId("bff3187a6760")
                  .requestId("59cf058fc08b");

        List<String> payload = List.of("joe", "foobar");
        final HttpConnectorResult result = connector.post(HttpRequest.builder()
                                                                     .url(buildUrl(""))
                                                                     .body(payload)
                                                                     .options(Map.of("page", "1"))
                                                                     .listener(listener)
                                                                     .build());

        assertListenerSuccess(listener);
        assertTextRelatif(result, "commons/connectors/post_withHttpRequest_nominal.json", 1, 5);
        assertThat(new String(result.getData())).isEqualTo("\"testing-library\": \"WireMock\"");
    }

    @Test
    void post_withError_shouldTraceError() throws ConnectorException {
        final HttpBasicConnector connector = buildConnector();

        stubFor(post("/?page=1")
                        .willReturn(aResponse()
                                            .withStatus(500)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody("{\"someError\": \"ERR-001\"}")));

        final BasicConnectorListener listener = buildListener();
        MdcService.getInstance()
                  .deviceIdentifier("912c8721-f7d5-4bea-84ba-f13381a4fcdb")
                  .correlationId("0d9e9d18-f5cf-44ff-8537-276506d884b0")
                  .traceId("bff3187a6760")
                  .requestId("59cf058fc08b");

        List<String> payload = List.of("joe", "foobar");
        final HttpConnectorResult result = connector.post(HttpRequest.builder()
                                                                     .nbRetry(0)
                                                                     .url(buildUrl(""))
                                                                     .body(payload)
                                                                     .options(Map.of("page", "1"))
                                                                     .listener(listener)
                                                                     .build());

        assertThat(listener.getRequest().size()).isEqualTo(1);
        assertThat(listener.getException().size()).isEqualTo(0);
        assertThat(listener.getDone().size()).isEqualTo(0);
        assertThat(listener.getError().size()).isEqualTo(1);

        assertTextRelatif(result, "commons/connectors/post_withError_shouldTraceError.json", 1, 5);
        assertThat(new String(result.getData())).isEqualTo("{\"someError\": \"ERR-001\"}");
    }


    // =========================================================================
    // PUT
    // =========================================================================
    @Test
    void put_withHttpRequest_nominal() throws ConnectorException {
        final HttpBasicConnector connector = buildConnector();

        stubFor(put("/?page=1")
                        .willReturn(aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody("\"testing-library\": \"WireMock\"")));

        final BasicConnectorListener listener = buildListener();
        MdcService.getInstance()
                  .deviceIdentifier("912c8721-f7d5-4bea-84ba-f13381a4fcdb")
                  .correlationId("0d9e9d18-f5cf-44ff-8537-276506d884b0")
                  .traceId("bff3187a6760")
                  .requestId("59cf058fc08b");

        List<String> payload = List.of("joe", "foobar");
        final HttpConnectorResult result = connector.put(HttpRequest.builder()
                                                                    .url(buildUrl(""))
                                                                    .body(payload)
                                                                    .options(Map.of("page", "1"))
                                                                    .listener(listener)
                                                                    .build());

        assertListenerSuccess(listener);
        assertTextRelatif(result, "commons/connectors/put_withHttpRequest_nominal.json", 1, 5);
        assertThat(new String(result.getData())).isEqualTo("\"testing-library\": \"WireMock\"");
    }

    // =========================================================================
    // DELETE
    // =========================================================================
    @Test
    void delete_withHttpRequest_nominal() throws ConnectorException {
        final HttpBasicConnector connector = buildConnector();

        stubFor(delete("/?page=1")
                        .willReturn(aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody("\"testing-library\": \"WireMock\"")));

        final BasicConnectorListener listener = buildListener();
        MdcService.getInstance()
                  .deviceIdentifier("912c8721-f7d5-4bea-84ba-f13381a4fcdb")
                  .correlationId("0d9e9d18-f5cf-44ff-8537-276506d884b0")
                  .traceId("bff3187a6760")
                  .requestId("59cf058fc08b");

        List<String> payload = List.of("joe", "foobar");
        final HttpConnectorResult result = connector.delete(HttpRequest.builder()
                                                                       .url(buildUrl(""))
                                                                       .options(Map.of("page", "1"))
                                                                       .listener(listener)
                                                                       .build());

        assertListenerSuccess(listener);
        assertTextRelatif(result, "commons/connectors/delete_withHttpRequest_nominal.json", 4);
        assertThat(new String(result.getData())).isEqualTo("\"testing-library\": \"WireMock\"");
    }

    // =========================================================================
    // OPTION
    // =========================================================================
    @Test
    void option_withHttpRequest_nominal() throws ConnectorException {
        final HttpBasicConnector connector = buildConnector();

        stubFor(options(UrlPattern.ANY)
                        .willReturn(aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody("\"testing-library\": \"WireMock\"")));

        final BasicConnectorListener listener = buildListener();
        MdcService.getInstance()
                  .deviceIdentifier("912c8721-f7d5-4bea-84ba-f13381a4fcdb")
                  .correlationId("0d9e9d18-f5cf-44ff-8537-276506d884b0")
                  .traceId("bff3187a6760")
                  .requestId("59cf058fc08b");

        List<String> payload = List.of("joe", "foobar");
        final HttpConnectorResult result = connector.option(HttpRequest.builder()
                                                                       .url(buildUrl(""))
                                                                       .options(Map.of("page", "1"))
                                                                       .listener(listener)
                                                                       .build());

        assertListenerSuccess(listener);
        assertTextRelatif(result, "commons/connectors/option_withHttpRequest_nominal.json", 4);
        assertThat(new String(result.getData())).isEqualTo("\"testing-library\": \"WireMock\"");
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private HttpBasicConnector buildConnector() {
        return new HttpBasicConnector();
    }

    private String buildUrl(final String path) {
        return URL + PORT + path;
    }

    private String buildUrl(final String path, final int port) {
        return URL + port + path;
    }

    private void assertListernerException(final BasicConnectorListener listener,
                                          final Class<? extends Exception> exception) {
        assertThat(listener.getRequest().size()).isEqualTo(1);
        assertThat(listener.getException().size()).isEqualTo(1);
        assertThat(listener.getException().get(0)).isInstanceOf(exception);
        assertThat(listener.getDone().size()).isEqualTo(0);
        assertThat(listener.getError().size()).isEqualTo(0);
    }

    private void assertListenerSuccess(final BasicConnectorListener listener) {
        assertThat(listener.getRequest().size()).isEqualTo(1);
        assertThat(listener.getException().size()).isEqualTo(0);
        assertThat(listener.getDone().size()).isEqualTo(1);
        assertThat(listener.getError().size()).isEqualTo(0);
    }


    private BasicConnectorListener buildListener() {
        return new BasicConnectorListener();
    }

    @ToString
    @Getter
    private static class BasicConnectorListener implements ConnectorListener {
        private List<HttpRequest>         request   = new ArrayList<>();
        private List<ConnectorException>  exception = new ArrayList<>();
        private List<HttpConnectorResult> done      = new ArrayList<>();
        private List<HttpConnectorResult> error     = new ArrayList<>();

        @Override
        public HttpRequest beforeCalling(final HttpRequest request) {
            this.request.add(request);
            return request;
        }

        @Override
        public void onError(final ConnectorException exception) {
            this.exception.add(exception);
        }

        @Override
        public void onDone(final HttpConnectorResult stepResult) {
            this.done.add(stepResult);
        }

        @Override
        public void onError(final HttpConnectorResult stepResult) {
            this.error.add(stepResult);
        }
    }

    UserDataDTO buildUser() {
        return UserDataDTO.builder()
                          .id(1L)
                          .sex(UserDataDTO.Sex.FEMALE)
                          .firstName("Émilie")
                          .lastName(LASTNAME2)
                          .email("emilie.lalonde@mock.org")
                          .phoneNumber(PHONE_NUMBER_1)
                          .old(35)
                          .birthday(LocalDate.of(1988, 4, 12))
                          .socialId(NAVS13_1)
                          .nationality(NATIONALITY)
                          .canton(CANTON)
                          .streetName(STREET_NAME)
                          .streetNumber(STREET_NUMBER)
                          .streetType(STREET_TYPE)
                          .zipCode(ZIP_CODE)
                          .city(CITY)
                          .deviceIdentifier("401f0498-c43f-43ad-a3f4-2888838332ad")
                          .build();
    }
}