package io.inugami.monitoring.springboot.exception;

import feign.FeignException;
import feign.Request;
import io.inugami.api.exceptions.DefaultErrorCode;
import io.inugami.api.exceptions.UncheckedException;
import io.inugami.api.monitoring.MdcService;
import io.inugami.api.monitoring.logs.BasicLogEvent;
import io.inugami.api.monitoring.logs.DefaultLogListener;
import io.inugami.api.monitoring.logs.LogListener;
import io.inugami.commons.test.UnitTestHelper;
import io.inugami.commons.test.logs.LogTestAppender;
import io.inugami.monitoring.springboot.partnerlog.feign.FeignRequestBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.zalando.problem.ThrowableProblem;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class DefaultDefaultExceptionHandlerServiceTest {

    // ========================================================================
    // INIT
    // ========================================================================

    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter         writer;

    @Captor
    private ArgumentCaptor<String> writerCaptor;

    @BeforeEach
    public void init() {
        MdcService.getInstance().clear();
    }

    @AfterEach
    public void shutdown() {
        MdcService.getInstance().clear();
    }

    // ========================================================================
    // TEST
    // ========================================================================
    @Test
    public void manageException_withNullValue_shouldProduceThrowableProblem() {
        processTest(
                buildService(),
                null,
                "exception/exceptionHandlerServiceTest/manageException_withNullValue_shouldProduceThrowableProblem.json",
                "exception/exceptionHandlerServiceTest/manageException_withNullValue_shouldProduceThrowableProblem.log.json");
    }

    @Test
    public void manageException_withStandardException_shouldProduceThrowableProblem() {
        processTest(
                buildService(),
                new IllegalArgumentException("some error"),
                "exception/exceptionHandlerServiceTest/manageException_withStandardException_shouldProduceThrowableProblem.json",
                "exception/exceptionHandlerServiceTest/manageException_withStandardException_shouldProduceThrowableProblem.log.json");
    }

    @Test
    public void manageException_withInugamiException_produceProblem() {
        processTest(
                buildService(),
                new UncheckedException(
                        DefaultErrorCode.buildUndefineError()
                ),
                "exception/exceptionHandlerServiceTest/manageException_withInugamiException_produceProblem.json",
                "exception/exceptionHandlerServiceTest/manageException_withInugamiException_produceProblem.log.json");
    }


    @Test
    public void manageException_withFieldException_produceProblem() {
        processTest(
                buildService(),
                new UncheckedException(
                        DefaultErrorCode.buildUndefineErrorCode()
                                        .errorCode("ERR-0._0")
                                        .field("user.email")
                                        .category("user")
                                        .messageDetail("invalid email address")
                                        .build()
                ),
                "exception/exceptionHandlerServiceTest/manageException_withFieldException_produceProblem.json",
                "exception/exceptionHandlerServiceTest/manageException_withFieldException_produceProblem.log.json");
    }


    @Test
    public void manageException_withExploitationException_produceProblem() {
        processTest(
                buildService(),
                new UncheckedException(
                        DefaultErrorCode.buildUndefineErrorCode()
                                        .errorCode("ERR-0._0")
                                        .field("user.email")
                                        .category("user")
                                        .messageDetail("invalid email address")
                                        .exploitationError()
                                        .build()
                ),
                "exception/exceptionHandlerServiceTest/manageException_withExploitationException_produceProblem.json",
                "exception/exceptionHandlerServiceTest/manageException_withExploitationException_produceProblem.json.log.json");
    }

    @Test
    public void manageException_withoutDetail_produceProblem() {
        processTest(
                buildService(false),
                new UncheckedException(
                        DefaultErrorCode.buildUndefineErrorCode()
                                        .errorCode("ERR-0._0")
                                        .field("user.email")
                                        .category("user")
                                        .messageDetail("invalid email address")
                                        .exploitationError()
                                        .build()
                ),
                "exception/exceptionHandlerServiceTest/manageException_withoutDetail_produceProblem.json",
                "exception/exceptionHandlerServiceTest/manageException_withoutDetail_produceProblem.json.log.json");
    }

    @Test
    public void manageException_withoutMessageDetail_produceProblem() {
        processTest(
                buildService(),
                new UncheckedException(
                        DefaultErrorCode.buildUndefineErrorCode()
                                        .errorCode("ERR-0._0")
                                        .field("user.email")
                                        .category("user")
                                        .exploitationError()
                                        .build()
                ),
                "exception/exceptionHandlerServiceTest/manageException_withoutMessageDetail_produceProblem.json",
                "exception/exceptionHandlerServiceTest/manageException_withoutMessageDetail_produceProblem.log.json");
    }

    @Test
    public void manageException_withStatusLessThan200_produceProblem() {
        processTest(
                buildService(),
                new UncheckedException(
                        DefaultErrorCode.buildUndefineErrorCode()
                                        .statusCode(150)
                                        .errorCode("ERR-0._0")
                                        .field("user.email")
                                        .category("user")
                                        .build()
                ),
                "exception/exceptionHandlerServiceTest/manageException_withStatusLessThan200_produceProblem.json",
                "exception/exceptionHandlerServiceTest/manageException_withStatusLessThan200_produceProblem.log.json");
    }

    @Test
    public void manageException_withStatusHigherThan511_produceProblem() {
        processTest(
                buildService(),
                new UncheckedException(
                        DefaultErrorCode.buildUndefineErrorCode()
                                        .statusCode(600)
                                        .errorCode("ERR-0._0")
                                        .field("user.email")
                                        .category("user")
                                        .build()
                ),
                "exception/exceptionHandlerServiceTest/manageException_withStatusHigherThan511_produceProblem.json",
                "exception/exceptionHandlerServiceTest/manageException_withStatusHigherThan511_produceProblem.log.json");
    }

    @Test
    public void manageException_withFeignException_produceProblem() {
        MdcService.getInstance().partner("feign-client");
        final Request request = FeignRequestBuilder.builder()
                                                   .buildFeignRequest();
        processTest(
                buildService(),
                new FeignException.Unauthorized("bad credentials", request, null, null),
                "exception/exceptionHandlerServiceTest/manageException_withFeignException_produceProblem.json",
                "exception/exceptionHandlerServiceTest/manageException_withFeignException_produceProblem.log.json");
    }
    //-------------------------------------------------------------------------
    // WITH RESPONSE
    //-------------------------------------------------------------------------

    @Test
    public void manageException_withResponse_shouldProduceError() throws IOException {
        final List<BasicLogEvent> logs     = new ArrayList<>();
        final LogListener         listener = new DefaultLogListener(".*", logs::add);
        LogTestAppender.register(listener);
        when(response.getWriter()).thenReturn(writer);

        final DefaultExceptionHandlerService service = buildService();

        service.manageException(new UncheckedException(
                DefaultErrorCode.buildUndefineErrorCode()
                                .statusCode(400)
                                .errorCode("ERR-0._0")
                                .field("user.email")
                                .category("user")
                                .messageDetail("invalid email address")
                                .build()
        ), response);

        LogTestAppender.removeListener(listener);

        verify(response).setStatus(eq(400));
        verify(response).setContentType(eq(MediaType.APPLICATION_JSON_VALUE));
        verify(writer).write(writerCaptor.capture());

        UnitTestHelper.assertTextRelative(writerCaptor.getValue(), "exception/exceptionHandlerServiceTest/manageException_withResponse_shouldProduceError.json");
        UnitTestHelper.assertTextRelative(logs, "exception/exceptionHandlerServiceTest/manageException_withResponse_shouldProduceError.log.json");
    }

    // ========================================================================
    // TOOLS
    // ========================================================================
    private void processTest(final DefaultExceptionHandlerService service, final Throwable exception, final String bodyPath, final String logPath) {
        final List<BasicLogEvent> logs     = new ArrayList<>();
        final LogListener         listener = new DefaultLogListener(".*", logs::add);
        LogTestAppender.register(listener);

        final ResponseEntity<ThrowableProblem> result = service.manageException(exception);

        LogTestAppender.removeListener(listener);
        UnitTestHelper.assertTextRelative(result, bodyPath);
        UnitTestHelper.assertTextRelative(logs, logPath);
    }

    private DefaultExceptionHandlerService buildService() {
        return buildService(true);
    }

    private DefaultExceptionHandlerService buildService(final boolean showDetail) {
        return DefaultExceptionHandlerService.builder()
                                             .applicationName("inugami")
                                             .applicationVersion("2.3.0")
                                             .wikiPage("http://inugami.io/wiki/{0}#{1}")
                                             .showAllDetail(showDetail)
                                             .build()
                                             .init();
    }
}