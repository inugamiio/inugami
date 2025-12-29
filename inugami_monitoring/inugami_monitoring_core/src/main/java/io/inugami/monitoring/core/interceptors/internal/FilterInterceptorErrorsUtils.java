/* --------------------------------------------------------------------
 *  Inugami
 * --------------------------------------------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.monitoring.core.interceptors.internal;

import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.api.tools.RunSafeUtils;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.ExceptionResolver;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.monitoring.ErrorResult;
import io.inugami.monitoring.core.interceptors.ResponseWrapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static io.inugami.framework.interfaces.exceptions.DefaultErrorCode.*;

@Slf4j
@UtilityClass
public class FilterInterceptorErrorsUtils {
    public static final String DEFAULT_ERROR_CODE = "ERR-0000";
    public static final String EMPTY              = "";

    public static @Nullable ErrorResult resolveError(final Exception currentError,
                                                     final ResponseWrapper responseWrapper,
                                                     final List<ExceptionResolver> resolvers) {
        final List<ExceptionResolver> errorResolvers   = Optional.ofNullable(resolvers).orElse(List.of());
        final ErrorCode               currentErrorCode = MdcService.getInstance().errorCode();
        if (currentError == null && currentErrorCode == null) {
            return null;
        }

        final ErrorCode errorCode = Optional.ofNullable(currentErrorCode)
                                            .orElse(buildDefaultErrorCode(responseWrapper,
                                                                          currentErrorCode));

        final Exception error = Optional.ofNullable(currentError).orElse(new UncheckedException(errorCode));


        ErrorResult.ErrorResultBuilder result = null;

        ErrorResult resolvedError = null;
        for (final ExceptionResolver resolver : errorResolvers) {
            resolvedError = RunSafeUtils.runSafe(() -> resolver.resolve(error), log);
            if (resolvedError != null) {
                break;
            }
        }

        result = Optional.ofNullable(resolvedError)
                         .orElse(convertToErrorResult(errorCode, currentError))
                         .toBuilder()
                         .httpCode(errorCode.getStatusCode())
                         .errorCode(errorCode.getErrorCode())
                         .errorType(errorCode.getErrorType())
                         .message(errorCode.getMessage())
                         .exploitationError(errorCode.isExploitationError())
                         .currentErrorCode(errorCode);
        return result.exception(error).build();
    }

    private static ErrorResult convertToErrorResult(final ErrorCode errorCode, final Exception exception) {
        return ErrorResult.builder()
                          .httpCode(errorCode.getStatusCode())
                          .errorCode(errorCode.getErrorCode())
                          .errorType(errorCode.getErrorType())
                          .message(errorCode.getMessage())
                          .cause(Optional.ofNullable(Optional.ofNullable(exception)
                                                             .map(Exception::getCause)
                                                             .orElse(exception))
                                         .map(Throwable::getMessage)
                                         .orElse(EMPTY))
                          .exploitationError(errorCode.isExploitationError())
                          .exception(exception)
                          .build();
    }

    protected static DefaultErrorCode buildDefaultErrorCode(final ResponseWrapper responseWrapper,
                                                            final ErrorCode errorCode) {
        final ErrorCode currentErrorCode = Optional.ofNullable(errorCode)
                                                   .orElse(DefaultErrorCode.buildUndefineErrorCode()
                                                                           .errorCode(DEFAULT_ERROR_CODE)
                                                                           .statusCode(500)
                                                                           .build());

        final Integer status = Optional.ofNullable(responseWrapper).map(ResponseWrapper::getStatus).orElse(500);

        String type = TECHNICAL;
        if (status == 401 || status == 403) {
            type = SECURITY;
        } else if (status < 500) {
            type = FUNCTIONAL;
        }

        return DefaultErrorCode.fromErrorCode(currentErrorCode)
                               .errorCode(Optional.ofNullable(currentErrorCode.getErrorCode())
                                                  .orElse(DEFAULT_ERROR_CODE))
                               .statusCode(status)
                               .errorType(type)
                               .build();
    }


    public static void defineStatusAndDuration(final ResponseWrapper httpResponse,
                                               final long duration) {
        MdcService.getInstance().duration(duration).status(httpResponse.getStatus());

        if (httpResponse.getStatus() >= 400) {
            MdcService.getInstance().globalStatusError();
        } else {
            MdcService.getInstance().globalStatusSuccess();
        }
    }
}
