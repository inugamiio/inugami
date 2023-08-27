package io.inugami.monitoring.core.interceptors.spi;

import io.inugami.api.exceptions.DefaultWarning;
import io.inugami.api.exceptions.WarningContext;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.monitoring.MdcService;
import io.inugami.api.monitoring.data.ResponseData;
import io.inugami.api.monitoring.data.ResquestData;
import io.inugami.api.monitoring.exceptions.ErrorResult;
import io.inugami.api.monitoring.logs.BasicLogEvent;
import io.inugami.api.monitoring.logs.DefaultLogListener;
import io.inugami.api.monitoring.logs.LogListener;
import io.inugami.commons.test.logs.LogTestAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static io.inugami.commons.test.UnitTestHelper.assertTextRelative;

class IoLogInterceptorTest {

    private static final Long   DATE_TIME = LocalDateTime.of(2023, 7, 1, 14, 54, 0).toEpochSecond(ZoneOffset.UTC);
    private static final String UID       = "fd508efe-9b7e-403f-b0bc-44e379d0171d";

    @AfterEach
    @BeforeEach
    public void init() {
        MdcService.getInstance().clear();
        WarningContext.clean();
    }

    // =================================================================================================================
    // onBegin
    // =================================================================================================================
    @Test
    void onBegin_nominal() {
        final ResquestData request = ResquestData.builder()
                                                 .method("POST")
                                                 .uri("/my/service")
                                                 .contextPath("/application")
                                                 .addHeader("app", "myApplication")
                                                 .content("{\"name\":\"John Smith\"}")
                                                 .build();
        assertTextRelative(processOnBegin(request),
                           "monitoring/core/interceptors/spi/ioLogInterceptor/onBegin_nominal.json");
    }


    @Test
    void onBegin_withoutContextPath() {
        final ResquestData request = ResquestData.builder()
                                                 .method("POST")
                                                 .uri("/my/service")
                                                 .addHeader("app", "myApplication")
                                                 .content("{\"name\":\"John Smith\"}")
                                                 .build();

        assertTextRelative(processOnBegin(request),
                           "monitoring/core/interceptors/spi/ioLogInterceptor/onBegin_withoutContextPath.json");
    }

    @Test
    void onBegin_withContextPathInUri() {
        final ResquestData request = ResquestData.builder()
                                                 .method("POST")
                                                 .uri("/application/my/service")
                                                 .contextPath("/application")
                                                 .addHeader("app", "myApplication")
                                                 .content("{\"name\":\"John Smith\"}")
                                                 .build();

        assertTextRelative(processOnBegin(request),
                           "monitoring/core/interceptors/spi/ioLogInterceptor/onBegin_withContextPathInUri.json");
    }

    // =================================================================================================================
    // onDone
    // =================================================================================================================
    @Test
    void onDone_nominal() {
        final ResquestData request = ResquestData.builder()
                                                 .method("POST")
                                                 .uri("/my/service")
                                                 .contextPath("/application")
                                                 .addHeader("app", "myApplication")
                                                 .content("{\"name\":\"John Smith\"}")
                                                 .build();
        final ResponseData responseData = ResponseData.builder()
                                                      .code(200)
                                                      .datetime(DATE_TIME)
                                                      .duration(42L)
                                                      .contentType("UTF-8")
                                                      .content("{\"id\":1,\"name\":\"John Smith\"}")
                                                      .addHeader("x-correlation-id", UID)
                                                      .build();
        WarningContext.getInstance().addWarnings(DefaultWarning.builder()
                                                               .warningType("functional")
                                                               .warningCode("WARN-1")
                                                               .message("account need to be confirmed ")
                                                               .messageDetail("some detail")
                                                               .build());
        assertTextRelative(processOnDone(request, responseData, null),
                           "monitoring/core/interceptors/spi/ioLogInterceptor/onDone_nominal.json");
    }

    @Test
    void onDone_withError() {
        final ResquestData request = ResquestData.builder()
                                                 .method("POST")
                                                 .uri("/my/service")
                                                 .addHeader("app", "myApplication")
                                                 .content("{\"name\":\"John Smith\"}")
                                                 .build();
        final ResponseData responseData = ResponseData.builder()
                                                      .code(500)
                                                      .datetime(DATE_TIME)
                                                      .duration(42L)
                                                      .contentType("UTF-8")
                                                      .addHeader("x-correlation-id", UID)
                                                      .build();
        assertTextRelative(processOnDone(request, responseData, ErrorResult.builder()
                                                                           .exception(new NullPointerException("oups"))
                                                                           .message("Null pointer occurs")
                                                                           .errorType("technic")
                                                                           .errorCode("ERR-0")
                                                                           .cause("NPE")
                                                                           .fallBack("{}")
                                                                           .stack("io.inugami....")

                                                                           .build()),
                           "monitoring/core/interceptors/spi/ioLogInterceptor/onDone_withError.json");
    }


    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    IoLogInterceptor buildInterceptor() {
        return new IoLogInterceptor();
    }

    private List<BasicLogEvent> processOnBegin(final ResquestData request) {
        final List<BasicLogEvent> logs     = new ArrayList<>();
        final LogListener         listener = new DefaultLogListener(Loggers.IOLOG_NAME, logs::add);
        LogTestAppender.register(listener);
        buildInterceptor().onBegin(request);
        LogTestAppender.removeListener(listener);
        return logs;
    }

    private List<BasicLogEvent> processOnDone(final ResquestData request,
                                              final ResponseData responseData,
                                              final ErrorResult error) {
        final List<BasicLogEvent> logs     = new ArrayList<>();
        final LogListener         listener = new DefaultLogListener(Loggers.IOLOG_NAME, logs::add);
        LogTestAppender.register(listener);
        buildInterceptor().onDone(request, responseData, error);
        LogTestAppender.removeListener(listener);
        return logs;
    }

}