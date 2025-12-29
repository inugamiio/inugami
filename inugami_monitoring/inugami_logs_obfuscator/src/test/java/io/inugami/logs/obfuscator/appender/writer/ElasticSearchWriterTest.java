package io.inugami.logs.obfuscator.appender.writer;

import ch.qos.logback.classic.encoder.JsonEncoder;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.inugami.framework.api.connectors.HttpBasicConnector;
import io.inugami.framework.interfaces.connectors.HttpRequest;
import io.inugami.framework.interfaces.exceptions.services.ConnectorException;
import io.inugami.logs.obfuscator.appender.AppenderConfiguration;
import io.inugami.logs.obfuscator.dto.LoggingEventDTO;
import io.inugami.logs.obfuscator.utils.UnitTestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.internal.verification.AtMost;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static io.inugami.logs.obfuscator.appender.writer.ElasticSearchWriter.ELASTIC_SEARCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SuppressWarnings({"java:S2925"})
@ExtendWith(MockitoExtension.class)
class ElasticSearchWriterTest {


    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    public static final String                      OTHER = "XXX";
    @Mock
    private             HttpBasicConnector          connector;
    @Captor
    private             ArgumentCaptor<HttpRequest> captor;

    //==================================================================================================================
    // accept
    //==================================================================================================================
    @Test
    void accept_nominal() {
        final var writer = buildWriter();
        assertThat(writer.accept(AppenderConfiguration.builder()
                                                      .mode(ELASTIC_SEARCH)
                                                      .build())).isTrue();

        assertThat(writer.accept(AppenderConfiguration.builder()
                                                      .mode(OTHER)
                                                      .build())).isFalse();
    }


    //==================================================================================================================
    // start
    //==================================================================================================================
    @Test
    void start_nominal() throws Exception {
        final var writer = buildWriter();
        writer.setConnector(connector);
        final Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2025-12-14 15:09");
        writer.setDateGenerator(() -> date);
        try {
            processStartNominal(writer);
        } finally {
            writer.stop();
        }
    }

    private void processStartNominal(final ElasticSearchWriter writer) throws InterruptedException, ConnectorException, JsonProcessingException {
        writer.accept(AppenderConfiguration.builder()
                                           .mode(ELASTIC_SEARCH)
                                           .timeout(15000)
                                           .host("http://localhost/elasticsearch")
                                           .headersMap(Map.of("token", "123456a23132a"))
                                           .index("app-index")
                                           .indexDatePattern("yyyy-MM-dd")
                                           .build());

        writer.start(new JsonEncoder());
        Thread.sleep(150);
        writer.write(LoggingEventDTO.builder()
                                    .loggerName("MyLogger")
                                    .message("Hello the world")
                                    .build());


        Thread.sleep(500);
        verify(connector, new AtMost(5)).post(captor.capture());


        final HttpRequest data = captor.getAllValues().get(0);
        final Object      body = data.getBody();
        data.setBody(null);

        UnitTestHelper.assertText(data,
                                  """
                                          {
                                            "disableListener" : true,
                                            "headers" : {
                                              "token" : "123456a23132a",
                                              "ContentType" : "application/json"
                                            },
                                            "throwable" : true,
                                            "url" : "http://localhost/elasticsearch",
                                            "verb" : "POST"
                                          }
                                          """);
        UnitTestHelper.assertText(clean(body),
                                  """
                                          { "index" : { "_index" : "app-index-2025-12-14"} }
                                          {"sequenceNumber":0,"timestamp":0,"nanoseconds":0,"level":"null","threadName":"null","loggerName":"MyLogger","context":null,"markers": [],"mdc": {},"message":"Hello the world","throwable":null}
                                          """);

    }

    private String clean(final Object body) {
        final var value  = String.valueOf(body);
        String    result = String.join("\n", value.split("\\n"));
        return result;
    }


    //==================================================================================================================
    // TOOLS
    //==================================================================================================================
    ElasticSearchWriter buildWriter() {
        return new ElasticSearchWriter();
    }
}