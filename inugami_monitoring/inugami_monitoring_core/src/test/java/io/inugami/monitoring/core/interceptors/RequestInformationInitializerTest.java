package io.inugami.monitoring.core.interceptors;

import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.monitoring.models.Headers;
import io.inugami.monitoring.core.tools.dto.EnumerationMap;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.monitoring.core.interceptors.RequestInformationInitializer.buildRequestInformation;
import static io.inugami.monitoring.core.interceptors.RequestInformationInitializer.extractOtherHeaders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestInformationInitializerTest {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    @Mock
    private HttpServletRequest httpRequest;

    //==================================================================================================================
    // TEST
    //==================================================================================================================
    @Test
    void buildRequestInformation_withoutRequest() {
        MdcService.getInstance()
                  .clear()
                  .env("test")
                  .asset("inugami")
                  .hostname("localhost")
                  .instanceName("inu")
                  .instanceNumber("001")
                  .correlationId("142d5517-a5ee-409b-871b-f840cbbfb73b")
                  .traceId("bf28d38c-c73e-4067-a8e0-64f5082c67b3");

        assertText(buildRequestInformation(null),
                   """
                           {
                             "asset" : "inugami",
                             "correlationId" : "142d5517-a5ee-409b-871b-f840cbbfb73b",
                             "env" : "test",
                             "hostname" : "localhost",
                             "instanceName" : "inu",
                             "instanceNumber" : "001",
                             "traceId" : "bf28d38c-c73e-4067-a8e0-64f5082c67b3"
                           }
                           """);

    }

    @Test
    void extractOtherHeaders_nominal() {
        final Map<String, String> headers = new LinkedHashMap<>();
        headers.put(Headers.X_DEVICE_IDENTIFIER, "e5e2577c-cd40-48ab-b10f-18bed1b506fc");
        headers.put(Headers.X_CORRELATION_ID, "902d3fce-f8fe-4a94-971f-37bd878833c8");
        headers.put(Headers.X_B_3_TRACEID, "7368934d-41b8-4aad-937a-b702497894e1");
        headers.put("source", "user");
        headers.put("keepAlive", "true");

        when(httpRequest.getHeaderNames()).thenReturn(new EnumerationMap(headers));
        when(httpRequest.getHeader(any())).thenAnswer(answer -> headers.get(answer.getArgument(0)));

        assertText(extractOtherHeaders(httpRequest),
                   """
                           {
                             "keepAlive" : "true",
                             "source" : "user"
                           }
                           """);
    }
}