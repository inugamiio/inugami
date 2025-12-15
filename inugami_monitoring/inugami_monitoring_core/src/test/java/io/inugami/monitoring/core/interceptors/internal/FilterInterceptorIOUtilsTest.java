package io.inugami.monitoring.core.interceptors.internal;

import io.inugami.commons.test.UnitTestHelper;
import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.monitoring.core.interceptors.ResponseWrapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.monitoring.core.interceptors.internal.FilterInterceptorIOUtils.convertToResponseData;
import static io.inugami.monitoring.core.interceptors.internal.FilterInterceptorIOUtils.readInput;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterInterceptorIOUtilsTest {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    @Mock
    private ServletInputStream inputStream;
    @Mock
    private HttpServletRequest httpRequest;
    @Mock
    private ResponseWrapper    httpResponse;

    // =================================================================================================================
    // UTILITY CLASS
    // =================================================================================================================
    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClass(FilterInterceptorIOUtils.class);
    }

    // =================================================================================================================
    // READ
    // =================================================================================================================
    @Test
    void readInput_withoutData() {
        final var result = new String(readInput(null), StandardCharsets.UTF_8);
        assertThat(result).isEmpty();
    }

    @Test
    void readInput_nominal() throws IOException {
        final var input = new ByteArrayInputStream("Hello the world".getBytes(StandardCharsets.UTF_8));
        when(inputStream.read(any())).thenAnswer(answer -> input.read(answer.getArgument(0)));

        final var result = new String(readInput(inputStream), StandardCharsets.UTF_8);
        assertText(result, "Hello the world");

        verify(inputStream).close();
    }

    @Test
    void readInput_withoutError() throws IOException {
        when(inputStream.read(any())).thenThrow(new IOException("sorry"));

        final var result = new String(readInput(inputStream), StandardCharsets.UTF_8);
        assertThat(result).isEmpty();
        verify(inputStream).close();
    }

    // =================================================================================================================
    // convertToResponseData
    // =================================================================================================================
    @Test
    void convertToResponseData_nominal() {

        final Map<String, String> headers = Map.ofEntries(
                Map.entry("source", "user"),
                Map.entry("compress", "true"),
                Map.entry("keepAlive", "true")
                                                         );
        when(httpResponse.getStatus()).thenReturn(200);
        when(httpResponse.getData()).thenReturn("Hello");
        when(httpResponse.getContentType()).thenReturn("plain/text");
        when(httpResponse.getHeaderNames()).thenReturn(headers.keySet());
        when(httpResponse.getHeader(any())).thenAnswer(answer -> headers.get(answer.getArgument(0)));

        final var result = convertToResponseData(httpRequest, httpResponse, 15L);

        assertThat(result.getHttpRequest()).isNotNull();
        assertThat(result.getHttpResponse()).isNotNull();
        assertText(result.toBuilder()
                         .httpRequest(null)
                         .httpResponse(null)
                         .build(),
                   """
                           {
                             "code" : 200,
                             "content" : "Hello",
                             "contentType" : "plain/text",
                             "datetime" : 1765810801955,
                             "duration" : 15,
                             "hearder" : {
                               "compress" : "true",
                               "keepAlive" : "true",
                               "source" : "user"
                             }
                           }
                           """,
                   SkipLineMatcher.of(4));
    }
}