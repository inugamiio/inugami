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
package io.inugami.monitoring.core.interceptors.exceptions;

import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.loggers.Loggers;
import io.inugami.monitoring.api.exceptions.ExceptionHandlerMapper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import static io.inugami.api.exceptions.DefaultErrorCode.newBuilder;

public class DefaultExceptionMapper implements ExceptionHandlerMapper {


    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String SECURITY_TYPE   = "security";
    private static final String TECHNICAL_TYPE  = "technical";
    private static final String FONCTIONAL_TYPE = "functional";
    private static final String TECHNICAL_ERROR = "Technical error";

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public Map<Pattern, ErrorCode> produceMapping() {
        final Map<Pattern, ErrorCode> errors = new LinkedHashMap<>();

        //@formatter:off
        final BiConsumer<String, Exception> technicalConsumer = Loggers.DEBUG::error;
        final BiConsumer<String, Exception> xllogConsumer     = (msg,error) -> Loggers.XLLOG.error("{} : {}",msg,error.getMessage());
        //@formatter:on

        //@formatter:off
        // security
        errors.put(buildPattern(".*AuthenticationException.*"), buildError(401, "ERR-1-0", SECURITY_TYPE,"Authentication failed", (msg,error)->Loggers.SECURITY.error(msg)));
        errors.put(buildPattern(".*AccessDenied.*"),            buildError(403, "ERR-1-1", SECURITY_TYPE,"access denied", (msg,error)->Loggers.SECURITY.warn(msg)));
        errors.put(buildPattern(".*UnauthorizedException.*"),   buildError(403, "ERR-1-2", SECURITY_TYPE,"Authentication failed", (msg,error)->Loggers.SECURITY.error(msg)));
        errors.put(buildPattern(".*MaxUserSocketException.*"),  buildError(403, "ERR-1-3", SECURITY_TYPE,"You have too many sockets opens", (msg,error)->Loggers.SECURITY.error(msg)));
        
        
        
        // ENV
        errors.put(buildPattern(".*SocketTimeoutException.*"),  buildError(504, "ERR-2-0", TECHNICAL_TYPE,"timeout", xllogConsumer));
        errors.put(buildPattern(".*TimeoutException.*"),        buildError(504, "ERR-2-1", TECHNICAL_TYPE,"timeout", xllogConsumer));
        
        
        // DEV
        errors.put(buildPattern(".*NullPointer.*"),             buildError(501, "ERR-3-0", TECHNICAL_TYPE,TECHNICAL_ERROR,technicalConsumer));
        errors.put(buildPattern(".*NumberFormatException.*"),   buildError(501, "ERR-3-1", TECHNICAL_TYPE,TECHNICAL_ERROR,technicalConsumer));
        errors.put(buildPattern(".*ClassCastException.*"),      buildError(501, "ERR-3-2", TECHNICAL_TYPE,TECHNICAL_ERROR,technicalConsumer));
        errors.put(buildPattern(".*NoClassDefFoundError.*"),    buildError(501, "ERR-3-3", TECHNICAL_TYPE,TECHNICAL_ERROR,technicalConsumer));
        errors.put(buildPattern(".*NoClassDefFoundError.*"),    buildError(501, "ERR-3-4", TECHNICAL_TYPE,TECHNICAL_ERROR,technicalConsumer));
        errors.put(buildPattern(".*MethodNotFoundException.*"), buildError(501, "ERR-3-5", TECHNICAL_TYPE,TECHNICAL_ERROR,technicalConsumer));
        
        errors.put(buildPattern(".*IllegalStateException.*"),   buildError(501, "ERR-3-6", TECHNICAL_TYPE,TECHNICAL_ERROR,technicalConsumer));
        
        // DATA
        errors.put(buildPattern(".*DaoValidatorException.*")        ,buildError(422, "ERR-4-3", TECHNICAL_TYPE,"Entity contraint exception",technicalConsumer));
        errors.put(buildPattern(".*DaoEntityNotFoundException.*")   ,buildError(404, "ERR-4-2", FONCTIONAL_TYPE,"Entity not found",technicalConsumer));
        errors.put(buildPattern(".*DaoEntityExistsException.*")     ,buildError(409, "ERR-4-1", TECHNICAL_TYPE,"Entity already exists",technicalConsumer));
        errors.put(buildPattern(".*Dao.*")                          ,buildError(501, "ERR-4-0", TECHNICAL_TYPE,"Storage data error",technicalConsumer));
        
        // PROCESSING
        errors.put(buildPattern(".*ProcessingRunningException.*")   ,buildError(403, "ERR-5-0", TECHNICAL_TYPE,"Event already processing",technicalConsumer));
        
        return errors;
    }


    private ErrorCode buildError(final int statusCode,
                                 final String errorCode,
                                 final String errorType,
                                 final String message,
                                 final BiConsumer<String, Exception> errorHandler) {
       
        return  newBuilder().statusCode(statusCode)
                .errorCode(errorCode)
                .errorType(errorType)
                .message(message)
                .errorHandler(errorHandler)
                .build();
    }
}
