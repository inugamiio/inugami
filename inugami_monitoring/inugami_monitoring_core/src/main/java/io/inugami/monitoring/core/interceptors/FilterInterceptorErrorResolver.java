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
package io.inugami.monitoring.core.interceptors;

import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.monitoring.exceptions.ErrorResult;
import io.inugami.api.spi.SpiLoader;
import io.inugami.monitoring.api.exceptions.ExceptionHandlerMapper;
import io.inugami.monitoring.api.exceptions.ExceptionResolver;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.inugami.api.exceptions.DefaultErrorCode.newBuilder;

/**
 * FilterInterceptorErrorResolver
 *
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
public class FilterInterceptorErrorResolver implements ExceptionResolver {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================

    private static final Map<Pattern, ErrorCode> ERRORS_TYPES = initErrorsType();

    private static final Set<Pattern> KEYS_SET = ERRORS_TYPES.keySet();

    //@formatter:off
    private static final ErrorCode               DEFAULT_ERROR   = newBuilder().statusCode(500)
                                                                            .errorCode("ERR-0-000")
                                                                            .message("unknow error")
                                                                            .errorHandler((msg, error)-> Loggers.DEBUG.error(error.getMessage(),error))
                                                                            .build();
            

    //@formatter:on
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private static Map<Pattern, ErrorCode> initErrorsType() {
        final Map<Pattern, ErrorCode>      errors  = new LinkedHashMap<>();
        final List<ExceptionHandlerMapper> mappers = SpiLoader.getInstance().loadSpiServicesByPriority(ExceptionHandlerMapper.class);
        mappers.stream().map(ExceptionHandlerMapper::produceMapping).forEach(errors::putAll);
        return errors;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public ErrorResult resolve(final Exception error) {
        ErrorCode errorType = DEFAULT_ERROR;
        for (final Pattern pattern : KEYS_SET) {
            final Matcher matcher = pattern.matcher(error.getClass().getName());
            if (matcher.matches()) {
                errorType = ERRORS_TYPES.get(matcher);
                break;
            }
        }

        final ErrorResult.ErrorResultBuilder errorBuilder = ErrorResult.builder();
        errorBuilder.errorType(errorType.getErrorType());
        errorBuilder.errorCode(errorType.getErrorCode());
        errorBuilder.cause(buildCause(error));
        errorBuilder.httpCode(errorType.getStatusCode());
        errorBuilder.currentErrorCode(errorType);
        errorBuilder.exploitationError(errorType.isExploitationError());
        return errorBuilder.build();
    }

    private String buildCause(final Exception error) {
        return error.getCause() == null ? null : error.getCause().getMessage();
    }

}
