package io.inugami.monitoring.core.interceptors;

import io.inugami.commons.test.api.SkipLineMatcher;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.api.monitoring.RequestContext;
import io.inugami.framework.interfaces.exceptions.ExceptionResolver;
import io.inugami.framework.interfaces.monitoring.*;
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import io.inugami.framework.interfaces.monitoring.data.ResponseData;
import io.inugami.framework.interfaces.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.framework.interfaces.monitoring.models.CurrentApplicationDTO;
import io.inugami.framework.interfaces.monitoring.models.Headers;
import io.inugami.framework.interfaces.spi.SpiLoaderServiceSPI;
import io.inugami.monitoring.core.interceptors.internal.FilterInterceptorContext;
import io.inugami.monitoring.core.tools.dto.EnumerationMap;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilterInterceptorTest {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String                              URL   =
            "http://localhost/mock/user/56053af4-af5d-42f8-9539-3626f395cbe9";
    public static final String                              EMPTY = "";
    @Mock
    private             SpiLoaderServiceSPI                 spiLoaderServiceSPI;
    @Mock
    private             JavaRestMethodResolver              javaRestMethodResolver;
    @Mock
    private             JavaRestMethodTracker               javaRestMethodTracker;
    @Mock
    private             Interceptable                       interceptableResolver;
    @Mock
    private             ExceptionResolver                   exceptionResolver;
    @Mock
    private             ResponseListener                    responseListener;
    @Mock
    private             MonitoringFilterInterceptor         monitoringFilterInterceptor;
    @Mock
    private             FilterInterceptorCachePurgeStrategy filterInterceptorCachePurgeStrategy;
    @Mock
    private             HttpServletRequest                  request;
    @Mock
    private             HttpServletResponse                 response;
    @Mock
    private             FilterChain                         chain;
    @Captor
    private             ArgumentCaptor<RequestData>         requestDataCaptor;
    @Captor
    private             ArgumentCaptor<ResponseData>        responseDataCaptor;
    @Captor
    private             ArgumentCaptor<ErrorResult>         errorCaptor;

    @BeforeEach
    public void init() {
        MdcService.getInstance().clear();
        RequestContext.setInstance(null);
    }

    // =================================================================================================================
    // doFilter
    // =================================================================================================================
    @Test
    void doFilter_nominal() throws ServletException, IOException {
        final Map<String, String> headers = new LinkedHashMap<>();
        headers.put(Headers.X_DEVICE_IDENTIFIER, "e5e2577c-cd40-48ab-b10f-18bed1b506fc");
        headers.put(Headers.X_CORRELATION_ID, "902d3fce-f8fe-4a94-971f-37bd878833c8");
        headers.put(Headers.X_B_3_TRACEID, "7368934d-41b8-4aad-937a-b702497894e1");
        final var mdc = MdcService.getInstance();
        mdc.env("test")
           .asset("inugami")
           .hostname("localhost")
           .instanceName("inu")
           .instanceNumber("001");
        when(request.getHeader(any())).thenAnswer(answer -> headers.get(answer.getArgument(0)));
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn(URL);
        when(request.getContextPath()).thenReturn("/mock");
        when(request.getCharacterEncoding()).thenReturn("UTF-8");
        when(request.getHeaderNames()).thenReturn(new EnumerationMap(headers));
        when(interceptableResolver.isInterceptable(any())).thenReturn(true);
        when(filterInterceptorCachePurgeStrategy.shouldPurge(any())).thenReturn(true);

        final var interceptor = interceptor();
        interceptor.doFilter(request, response, chain);
        interceptor.purgeCacheIfRequired();

        assertText(RequestContext.getInstance().toBuilder()
                                 .request(null)
                                 .response(null)
                                 .build(),
                   """
                           {
                              "applicationVersion" : "e5e2577c-cd40-48ab-b10f-18bed1b506fc",
                              "characterEncoding" : "UTF-8",
                              "content" : "",
                              "contentType" : "application/json",
                              "contextPath" : "/mock",
                              "correlationId" : "902d3fce-f8fe-4a94-971f-37bd878833c8",
                              "deviceNetworkSpeedDown" : 0.0,
                              "deviceNetworkSpeedLatency" : 0.0,
                              "deviceNetworkSpeedUp" : 0.0,
                              "headers" : { },
                              "method" : "GET",
                              "options" : { },
                              "traceId" : "8ea68cd6-41c5-4cc5-b437-789c5d2e8d8d",
                              "uri" : "http://localhost/mock/user/56053af4-af5d-42f8-9539-3626f395cbe9"
                            }
                           """,
                   SkipLineMatcher.of(13));

        verify(chain).doFilter(any(), any());
        verify(monitoringFilterInterceptor).onDone(requestDataCaptor.capture(),
                                                   responseDataCaptor.capture(),
                                                   errorCaptor.capture());
        assertText(requestDataCaptor.getValue().toBuilder()
                                    .request(null)
                                    .response(null)
                                    .build(),
                   """
                           {
                             "applicationVersion" : "e5e2577c-cd40-48ab-b10f-18bed1b506fc",
                             "characterEncoding" : "UTF-8",
                             "content" : "",
                             "contentType" : "application/json",
                             "contextPath" : "/mock",
                             "correlationId" : "902d3fce-f8fe-4a94-971f-37bd878833c8",
                             "deviceNetworkSpeedDown" : 0.0,
                             "deviceNetworkSpeedLatency" : 0.0,
                             "deviceNetworkSpeedUp" : 0.0,
                             "headers" : { },
                             "method" : "GET",
                             "options" : { },
                             "traceId" : "841fbece-0405-4fea-baff-42fcfed54155",
                             "uri" : "http://localhost/mock/user/56053af4-af5d-42f8-9539-3626f395cbe9"
                           }
                           """,
                   SkipLineMatcher.of(13));
        assertText(responseDataCaptor.getValue().toBuilder()
                                     .httpRequest(null)
                                     .httpResponse(null)
                                     .build(),
                   """
                           {
                             "code" : 0,
                             "content" : "",
                             "datetime" : 1765832220228,
                             "duration" : 0,
                             "hearder" : {
                               "x-b3-traceid" : "7368934d-41b8-4aad-937a-b702497894e1",
                               "x-correlation-id" : "902d3fce-f8fe-4a94-971f-37bd878833c8",
                               "x-device-identifier" : "e5e2577c-cd40-48ab-b10f-18bed1b506fc"
                             }
                           }
                           """,
                   SkipLineMatcher.of(3));

        assertThat(errorCaptor.getValue()).isNull();
    }

    @Test
    void doFilter_notIntercepted() throws ServletException, IOException {
        final Map<String, String> headers = new LinkedHashMap<>();
        headers.put(Headers.X_DEVICE_IDENTIFIER, "e5e2577c-cd40-48ab-b10f-18bed1b506fc");
        headers.put(Headers.X_CORRELATION_ID, "902d3fce-f8fe-4a94-971f-37bd878833c8");
        headers.put(Headers.X_B_3_TRACEID, "7368934d-41b8-4aad-937a-b702497894e1");
        final var mdc = MdcService.getInstance();
        mdc.env("test")
           .asset("inugami")
           .hostname("localhost")
           .instanceName("inu")
           .instanceNumber("001");
        when(request.getHeader(any())).thenAnswer(answer -> headers.get(answer.getArgument(0)));
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn(URL);
        when(request.getContextPath()).thenReturn("/mock");
        when(request.getCharacterEncoding()).thenReturn("UTF-8");
        when(request.getHeaderNames()).thenReturn(new EnumerationMap(headers));
        when(interceptableResolver.isInterceptable(any())).thenReturn(false);
        when(filterInterceptorCachePurgeStrategy.shouldPurge(any())).thenReturn(true);

        final var interceptor = interceptor();
        interceptor.doFilter(request, response, chain);
        interceptor.purgeCacheIfRequired();
        interceptor.destroy();

        assertText(RequestContext.getInstance().toBuilder()
                                 .request(null)
                                 .response(null)
                                 .build(),
                   """
                           {
                             "applicationVersion" : "e5e2577c-cd40-48ab-b10f-18bed1b506fc",
                             "characterEncoding" : "UTF-8",
                             "contentType" : "application/json",
                             "contextPath" : "/mock",
                             "correlationId" : "902d3fce-f8fe-4a94-971f-37bd878833c8",
                             "deviceNetworkSpeedDown" : 0.0,
                             "deviceNetworkSpeedLatency" : 0.0,
                             "deviceNetworkSpeedUp" : 0.0,
                             "headers" : { },
                             "method" : "GET",
                             "options" : { },
                             "uri" : "http://localhost/mock/user/56053af4-af5d-42f8-9539-3626f395cbe9"
                           }
                           """);

        verify(chain).doFilter(any(), any());
        verify(monitoringFilterInterceptor, never()).onDone(any(),
                                                            any(),
                                                            any());

    }


    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    FilterInterceptor interceptor() {
        FilterInterceptor result = new FilterInterceptor(spiLoaderServiceSPI, CurrentApplicationDTO.builder()
                                                                                                   .groupId("io.inugami")
                                                                                                   .artifactId("test-app")
                                                                                                   .version("1.0.0")
                                                                                                   .commitId("6d0bd04a-ca20-4fec-b8fd-97b2991588b8")
                                                                                                   .commitDate("2026-01-07T20:46:15")
                                                                                                   .build());
        FilterInterceptor.FILTER_INTERCEPTOR_CONTEXT.set(FilterInterceptorContext.builder()
                                                                                 .initialized(true)
                                                                                 .spiLoaderServiceSPI(spiLoaderServiceSPI)
                                                                                 .javaRestMethodResolvers(List.of(javaRestMethodResolver))
                                                                                 .javaRestMethodTrackers(List.of(javaRestMethodTracker))
                                                                                 .interceptableResolvers(List.of(interceptableResolver))
                                                                                 .exceptionResolvers(List.of(exceptionResolver))
                                                                                 .responseListeners(List.of(responseListener))
                                                                                 .monitoringFilterInterceptors(List.of(monitoringFilterInterceptor))
                                                                                 .cachePurgeStrategies(List.of(filterInterceptorCachePurgeStrategy))
                                                                                 .build());
        result.onApplicationStarted(null);
        return result;
    }


}