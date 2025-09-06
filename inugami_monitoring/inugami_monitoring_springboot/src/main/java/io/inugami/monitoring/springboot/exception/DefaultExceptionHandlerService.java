package io.inugami.monitoring.springboot.exception;

import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.api.monitoring.RequestContext;
import io.inugami.framework.commons.spring.exception.ExceptionHandlerService;
import io.inugami.framework.interfaces.exceptions.*;
import io.inugami.framework.interfaces.monitoring.data.RequestData;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.spi.SpiLoader;
import io.inugami.framework.interfaces.exceptions.ProblemAdditionalFieldBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static io.inugami.framework.interfaces.exceptions.ErrorCode.*;

@SuppressWarnings({"java:S1181", "java:S108"})
@Slf4j
@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ControllerAdvice
public class DefaultExceptionHandlerService implements ExceptionHandlerService {


    // ========================================================================
    // ATTRIBUTES
    // ========================================================================
    public static final  String APPLICATION    = "application";
    public static final  String EMPTY          = "";
    public static final  String ERROR_CATEGORY = "errorCategory";
    public static final  String ERROR_CODE     = "errorCode";
    public static final  String ERROR_TYPE     = "errorType";
    public static final  String FIELDS         = "fields";
    public static final  String VERSION        = "version";
    private static final String CONTENT_TYPE   = "Content-Type";
    public static final  String SERVICE        = "service";

    @Value("${application.name:#{null}}")
    private String applicationName;
    @Value("${application.version:#{null}}")
    private String applicationVersion;
    @Value("${application.wiki:#{null}}")
    private String wikiPage;

    @Value("${inugami.monitoring.exception.show.detail.enabled:#{true}}")
    private boolean showAllDetail;

    private List<ProblemAdditionalFieldBuilder> problemAdditionalFieldBuilders;
    private List<ErrorCodeResolver>             errorCodeResolvers;

    @PostConstruct
    public DefaultExceptionHandlerService init() {
        problemAdditionalFieldBuilders = SpiLoader.getInstance()
                                                  .loadSpiServicesByPriority(ProblemAdditionalFieldBuilder.class);
        errorCodeResolvers = SpiLoader.getInstance().loadSpiServicesByPriority(ErrorCodeResolver.class);
        return this;
    }

    // ========================================================================
    // API
    // ========================================================================

    @Override
    public void manageException(final Throwable throwable, final HttpServletResponse response) {
        final ResponseEntity<ThrowableProblem> result = manageException(throwable);

        response.setStatus(result.getStatusCodeValue());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            final PrintWriter writer = response.getWriter();
            final String json = JsonMarshaller.getInstance()
                                              .getDefaultObjectMapper()
                                              .writer()
                                              .writeValueAsString(result.getBody());
            writer.write(json);
        } catch (final Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ThrowableProblem> manageException(final Throwable throwable) {
        final Throwable exception = throwable == null ? new UncheckedException("undefined error") : throwable;
        final ErrorCode errorCode = resolveErrorCode(exception);

        MdcService.getInstance().errorCode(errorCode);

        log.error(exception.getMessage(), exception);
        final StatusType currentStatus = resolveStatus(errorCode);
        final ProblemBuilder problemBuilder = Problem.builder()
                                                     .with(ERROR_CODE, errorCode.getErrorCode())
                                                     .with(FIELDS, resolveField(errorCode, exception))
                                                     .withStatus(currentStatus);
        final RequestData requestContext = RequestContext.getInstance();

        if (showAllDetail) {
            problemBuilder.withTitle(errorCode.getMessage())
                          .withDetail(errorCode.getMessageDetail() == null ? EMPTY : errorCode.getMessageDetail())
                          .with(APPLICATION, applicationName)
                          .with(VERSION, applicationVersion)
                          .with(ERROR_TYPE, errorCode.getErrorType())
                          .with(ERROR_CATEGORY, errorCode.getCategory())
                          .with(ROLLBACK, errorCode.isRollbackRequire())
                          .with(RETRYABLE, errorCode.isRetryable())
                          .with(FIELD, errorCode.getField())
                          .with(CATEGORY, errorCode.getCategory())
                          .with(DOMAIN, errorCode.getDomain())
                          .with(SUB_DOMAIN, errorCode.getSubDomain())
                          .with(SERVICE, requestContext == null ? null : requestContext.getService())
                          .withCause(Problem.builder().withTitle(exception.getMessage()).build());
        }

        if (problemAdditionalFieldBuilders != null) {
            for (final ProblemAdditionalFieldBuilder additionalBuilder : problemAdditionalFieldBuilders) {
                additionalBuilder.addInformation(problemBuilder, exception, errorCode);
            }
        }

        final ThrowableProblem problem = problemBuilder.build();
        problem.setStackTrace(new StackTraceElement[]{});

        if (problem.getCause() != null) {
            problem.getCause().setStackTrace(new StackTraceElement[]{});
        }


        if (errorCode.isExploitationError()) {
            MdcService.getInstance().errorUrl(buildWikiPage(errorCode));
            if (errorCode.getMessageDetail() == null) {
                Loggers.XLLOG.error(errorCode.getMessage());
            } else {
                Loggers.XLLOG.error("{} : {}", errorCode.getMessage(), errorCode.getMessageDetail());
            }
        }
        final MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return new ResponseEntity<>(problem, headers, HttpStatus.valueOf(currentStatus.getStatusCode()));
    }


    private String buildWikiPage(final ErrorCode errorCode) {
        try {
            return MessagesFormatter.format(wikiPage, applicationName, errorCode.getErrorCode());
        } catch (final Throwable e) {
            return null;
        }
    }


    // ========================================================================
    // INTERNAL
    // ========================================================================
    ErrorCode resolveErrorCode(final Throwable exception) {
        ErrorCode result = null;
        for (final ErrorCodeResolver resolver : errorCodeResolvers) {
            result = resolver.resolve(exception);
            if (result != null) {
                break;
            }
        }
        if (result == null) {
            result = DefaultErrorCode.buildUndefineErrorCode()
                                     .errorCode("ERR-0.0")
                                     .message(exception.getMessage())
                                     .errorTypeTechnical()
                                     .statusCode(500)
                                     .build();
        }
        return result;
    }

    StatusType resolveStatus(final ErrorCode errorCode) {
        StatusType result = Status.INTERNAL_SERVER_ERROR;
        int        status = errorCode == null ? 500 : errorCode.getStatusCode();
        if (errorCode.getStatusCode() <= 200 || errorCode.getStatusCode() > 511) {
            status = 500;
        }

        try {
            result = Status.valueOf(status);
        } catch (final Throwable e) {
        }

        return result;
    }


    List<FieldError> resolveField(final ErrorCode errorCode, final Throwable exception) {
        final List<FieldError> result = new ArrayList<>();
        List<ErrorCode>        errors = null;

        if (exception instanceof MultiUncheckedException) {
            final MultiUncheckedException multiError = (MultiUncheckedException) exception;
            errors = multiError.getErrorCodes();
        } else {
            errors = List.of(errorCode);
        }

        if (errors != null) {
            for (final ErrorCode error : errors) {
                if (error.getField() != null) {
                    final FieldError.FieldErrorBuilder builder = FieldError.builder()
                                                                           .name(error.getField())
                                                                           .errorCode(error.getErrorCode());

                    if (showAllDetail) {
                        builder.message(error.getMessage())
                               .messageDetail(error.getMessageDetail())
                               .errorType(error.getErrorType())
                               .errorCategory(error.getCategory());
                    }

                    result.add(builder.build());
                }
            }
        }

        return result;
    }
}
