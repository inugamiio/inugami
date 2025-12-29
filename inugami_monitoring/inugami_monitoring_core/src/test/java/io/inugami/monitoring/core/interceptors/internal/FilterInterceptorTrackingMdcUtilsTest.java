package io.inugami.monitoring.core.interceptors.internal;

import io.inugami.commons.test.UnitTestHelper;
import io.inugami.commons.test.dto.UserDataDTO;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.api.tools.ReflectionUtils;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.monitoring.ErrorResult;
import io.inugami.framework.interfaces.monitoring.JavaRestMethodDTO;
import io.inugami.framework.interfaces.monitoring.JavaRestMethodResolver;
import io.inugami.framework.interfaces.monitoring.JavaRestMethodTracker;
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import io.inugami.framework.interfaces.monitoring.models.Headers;
import io.inugami.framework.interfaces.rest.RestService;
import io.inugami.monitoring.core.interceptors.ResponseWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.*;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static io.inugami.monitoring.core.interceptors.internal.FilterInterceptorTrackingMdcUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilterInterceptorTrackingMdcUtilsTest {

    public static final String                 CORRELATION_ID = "230bc5e7-4dcd-4ca2-855a-619c001d378b";
    public static final String                 TRACE_ID       = "764597e2-8191-4ebd-b638-96531277a4d3";
    public static final String                 USER_GET_BY_ID = "user_get_by_id";
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    @Mock
    private             HttpServletRequest     httpRequest;
    @Mock
    private             ResponseWrapper        httpResponse;
    @Mock
    private             JavaRestMethodTracker  javaRestMethodTracker;
    @Mock
    private             JavaRestMethodTracker  javaRestMethodTrackerSecond;
    @Mock
    private             JavaRestMethodResolver javaRestMethodResolver;
    @Mock
    private             JavaRestMethodResolver javaRestMethodResolverSecond;
    @Mock
    private             Principal              principal;

    @BeforeEach
    public void init() {
        MdcService.getInstance().clear();
    }

    // =================================================================================================================
    // UTILITY CLASS
    // =================================================================================================================
    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClass(FilterInterceptorTrackingMdcUtils.class);
    }

    // =================================================================================================================
    // EXTRACT HEADERS
    // =================================================================================================================
    @Test
    void extractHeaders_nominal() {
        assertThat(extractHeaders(null)).isEmpty();
        final Map<String, String> values = new HashMap<>();
        values.put("source", "user");
        values.put("keepAlive", "true");
        when(httpRequest.getHeaderNames()).thenReturn(new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return false;
            }

            @Override
            public String nextElement() {
                return "";
            }

            @Override
            public Iterator<String> asIterator() {
                return values.keySet().iterator();
            }
        });
        when(httpRequest.getHeader(any())).thenAnswer(answer -> values.get(answer.getArgument(0)));

        assertText(extractHeaders(httpRequest),
                   """
                           {
                             "keepAlive" : [ "true" ],
                             "source" : [ "user" ]
                           }
                           """);
    }

    // =================================================================================================================
    // CORRELATION ID AND TRACE ID
    // =================================================================================================================
    @Test
    void initCorrelationIdAndTraceId_nominal() {
        when(httpRequest.getMethod()).thenReturn("GET");
        when(httpRequest.getRequestURI()).thenReturn("http:://localhost/mock");
        initCorrelationIdAndTraceId(RequestData.builder()
                                               .correlationId(CORRELATION_ID)
                                               .traceId(TRACE_ID)
                                               .build(), httpRequest);
        assertText(MdcService.getInstance().getAllMdc(),
                   """
                           {
                             "appUrl" : "http:://localhost/mock",
                             "appVerb" : "GET",
                             "correlation_id" : "230bc5e7-4dcd-4ca2-855a-619c001d378b",
                             "traceId" : "764597e2-8191-4ebd-b638-96531277a4d3"
                           }
                           """);
    }

    @Test
    void initCorrelationIdAndTraceId_withoutRequest() {
        initCorrelationIdAndTraceId(RequestData.builder()
                                               .correlationId(CORRELATION_ID)
                                               .traceId(TRACE_ID)
                                               .build(), null);
        assertText(MdcService.getInstance().getAllMdc(),
                   """
                           {
                             "correlation_id" : "230bc5e7-4dcd-4ca2-855a-619c001d378b",
                             "traceId" : "764597e2-8191-4ebd-b638-96531277a4d3"
                           }
                           """);
    }

    @Test
    void addTrackingInformation_nominal() {
        MdcService.getInstance().correlationId(CORRELATION_ID).traceId(TRACE_ID);
        when(javaRestMethodTrackerSecond.accept(any())).thenReturn(false);
        when(javaRestMethodTracker.accept(any())).thenReturn(true);
        addTrackingInformation(httpResponse, JavaRestMethodDTO.builder().build(), List.of(javaRestMethodTrackerSecond,
                                                                                          javaRestMethodTracker));


        verify(javaRestMethodTracker).track(any());
        verify(javaRestMethodTrackerSecond, never()).track(any());
        verify(httpResponse).setHeader(Headers.X_CORRELATION_ID, CORRELATION_ID);
        verify(httpResponse).setHeader(Headers.X_B_3_TRACEID, TRACE_ID);
    }

    @Test
    void addTrackingInformation_withTrackerError() {
        MdcService.getInstance().correlationId(CORRELATION_ID).traceId(TRACE_ID);
        when(javaRestMethodTracker.accept(any())).thenReturn(true);
        doThrow(new UncheckedException("sorry")).when(javaRestMethodTracker).track(any());

        addTrackingInformation(httpResponse, JavaRestMethodDTO.builder().build(), List.of(javaRestMethodTracker));

        verify(httpResponse).setHeader(Headers.X_CORRELATION_ID, CORRELATION_ID);
        verify(httpResponse).setHeader(Headers.X_B_3_TRACEID, TRACE_ID);
    }

    @Test
    void addTrackingInformation_withoutJAvaRestMethod() {
        MdcService.getInstance().correlationId(CORRELATION_ID).traceId(TRACE_ID);

        addTrackingInformation(httpResponse, null, List.of(javaRestMethodTracker));

        verify(javaRestMethodTracker, never()).track(any());
        verify(httpResponse).setHeader(Headers.X_CORRELATION_ID, CORRELATION_ID);
        verify(httpResponse).setHeader(Headers.X_B_3_TRACEID, TRACE_ID);
    }


    // =================================================================================================================
    //  SERVICE NAME
    // =================================================================================================================
    @Test
    void resolveServiceName_nominal() {
        assertThat(resolveServiceName(null)).isNull();
        assertThat(resolveServiceName(JavaRestMethodDTO.builder().build())).isEqualTo("");
        assertThat(resolveServiceName(JavaRestMethodDTO.builder()
                                                       .restClass(MyUserRestService.class)
                                                       .restMethod(ReflectionUtils.searchMethodByName(MyUserRestService.class, "getById"))
                                                       .build())).isEqualTo(USER_GET_BY_ID);

        assertThat(resolveServiceName(JavaRestMethodDTO.builder()
                                                       .restClass(OtherUserRestService.class)
                                                       .restMethod(ReflectionUtils.searchMethodByName(OtherUserRestService.class, "getById"))
                                                       .build())).isEqualTo("OtherUserRestService_getById");
    }

    @Test
    void resolveJavaRestMethod_nominal() {
        assertThat(resolveJavaRestMethod(httpRequest, null)).isNull();
        assertThat(resolveJavaRestMethod(httpRequest, List.of())).isNull();

        when(javaRestMethodResolver.resolve(any())).thenReturn(JavaRestMethodDTO.builder()
                                                                                .restClass(MyUserRestService.class)
                                                                                .restMethod(ReflectionUtils.searchMethodByName(MyUserRestService.class, "getById"))
                                                                                .build());
        assertThat(resolveServiceName(resolveJavaRestMethod(httpRequest, List.of(javaRestMethodResolver)))).isEqualTo(USER_GET_BY_ID);

        when(javaRestMethodResolverSecond.resolve(any())).thenThrow(new UncheckedException("sorry"));
        assertThat(resolveServiceName(resolveJavaRestMethod(httpRequest, List.of(javaRestMethodResolverSecond, javaRestMethodResolver)))).isEqualTo(USER_GET_BY_ID);

    }

    // =================================================================================================================
    //  LIFECYCLE
    // =================================================================================================================
    @Test
    void onBeginInitMdcFields_nominal() {
        onBeginInitMdcFields(RequestData.builder().build(), null);
        assertThat(MdcService.getInstance().getAllMdc()).isEmpty();

        when(httpRequest.getMethod()).thenReturn("GET");
        when(httpRequest.getAuthType()).thenReturn("basic");
        when(httpRequest.getRequestURL()).thenReturn(new StringBuffer("http://localhost/mock/user/f75ee436-dd9f-4f98-b5af-21562cf7e460"));
        when(httpRequest.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("user_login");
        onBeginInitMdcFields(RequestData.builder()
                                        .uri("/user/{id}")
                                        .build(), httpRequest);
        assertText(MdcService.getInstance().getAllMdc(),
                   """
                           {
                             "appUri" : "/user/{id}",
                             "appUrl" : "http://localhost/mock/user/f75ee436-dd9f-4f98-b5af-21562cf7e460",
                             "appVerb" : "GET",
                             "authProtocol" : "basic",
                             "callType" : "REST",
                             "principal" : "user_login"
                           }
                           """);
    }

    @Test
    void onEndInitMdcFields_nominal() {
        when(httpResponse.getStatus()).thenReturn(500);
        onEndInitMdcFields(ErrorResult.builder()
                                      .currentErrorCode(DefaultErrorCode.buildUndefineError())
                                      .build(),
                           150L,
                           httpResponse);

        assertText(MdcService.getInstance().getAllMdc(),
                   """
                           {
                             "duration" : "150",
                             "errorCode" : "err-undefine",
                             "errorStatus" : "500",
                             "errorType" : "technical",
                             "exploitationError" : "false",
                             "httpStatus" : "500",
                             "retryable" : "false",
                             "rollback" : "false"
                           }
                           """);
    }

    // =================================================================================================================
    //  DATA
    // =================================================================================================================
    @RestService("user")
    private static class MyUserRestService {
        @RestService("get_by_id")
        UserDataDTO getById(final String id) {
            return null;
        }
    }


    private static class OtherUserRestService {
        UserDataDTO getById(final String id) {
            return null;
        }
    }
}
