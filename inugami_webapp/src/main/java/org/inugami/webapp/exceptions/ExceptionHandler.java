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
package org.inugami.webapp.exceptions;

import static org.inugami.api.exceptions.ErrorCodeBuilder.newBuilder;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.inugami.api.exceptions.ErrorCode;
import org.inugami.api.loggers.Loggers;

/**
 * ExceptionHandler
 * 
 * @author patrickguillerm
 * @since 12 déc. 2017
 */
@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String                  SECURITY_TYPE   = "security";
    
    private static final String                  TECHNICAL_TYPE  = "technical";
    
    private static final String                  FONCTIONAL_TYPE = "functional";
    
    private static final String                  TECHNICAL_ERROR = "Technical error";
    
    private static final Map<Matcher, ErrorCode> ERRORS_TYPES    = initErrorsType();
    
    private static final Set<Matcher>            KEYS_SET        = ERRORS_TYPES.keySet();
    
    //@formatter:off
    private static final ErrorCode               DEFAULT_ERROR   = newBuilder().setStatusCode(500)
                                                                                .setErrorCode("ERR-0-000")
                                                                                .setMessage("unknow error")
                                                                                .setErrorHandler((msg, error) ->Loggers.DEBUG.error(error.getMessage(),error))
                                                                                .build();
            
            
            
    //@formatter:on
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private static Map<Matcher, ErrorCode> initErrorsType() {
        final Map<Matcher, ErrorCode> errors = new LinkedHashMap<>();
        
        //@formatter:off
        final BiConsumer<String, Exception> technicalConsumer = (msg,error) -> Loggers.DEBUG.error(msg,error);
        final BiConsumer<String, Exception> xllogConsumer     = (msg,error) -> Loggers.XLLOG.error("{} : {}",msg,error.getMessage());
        //@formatter:on
        
        //@formatter:off
        // security
        
        errors.put(buildMatcher(".*AuthenticationException.*"), buildError(401, "ERR-1-0", SECURITY_TYPE,"Authentication failed", (msg,error)->Loggers.SECURITY.error(msg)));
        errors.put(buildMatcher(".*AccessDenied.*"),            buildError(403, "ERR-1-1", SECURITY_TYPE,"access denied", (msg,error)->Loggers.SECURITY.warn(msg)));
        errors.put(buildMatcher(".*UnauthorizedException.*"),   buildError(403, "ERR-1-2", SECURITY_TYPE,"Authentication failed", (msg,error)->Loggers.SECURITY.error(msg)));
        errors.put(buildMatcher(".*MaxUserSocketException.*"),  buildError(403, "ERR-1-3", SECURITY_TYPE,"You have too many sockets opens", (msg,error)->Loggers.SECURITY.error(msg)));
        
        
        // ENV
        errors.put(buildMatcher(".*SocketTimeoutException.*"),  buildError(504, "ERR-2-0", TECHNICAL_TYPE,"timeout", xllogConsumer));
        errors.put(buildMatcher(".*TimeoutException.*"),        buildError(504, "ERR-2-1", TECHNICAL_TYPE,"timeout", xllogConsumer));
        
        // DEV
        errors.put(buildMatcher(".*NullPointer.*"),             buildError(501, "ERR-3-0", TECHNICAL_TYPE,TECHNICAL_ERROR,technicalConsumer));
        errors.put(buildMatcher(".*NumberFormatException.*"),   buildError(501, "ERR-3-1", TECHNICAL_TYPE,TECHNICAL_ERROR,technicalConsumer));
        errors.put(buildMatcher(".*ClassCastException.*"),      buildError(501, "ERR-3-2", TECHNICAL_TYPE,TECHNICAL_ERROR,technicalConsumer));
        errors.put(buildMatcher(".*NoClassDefFoundError.*"),    buildError(501, "ERR-3-3", TECHNICAL_TYPE,TECHNICAL_ERROR,technicalConsumer));
        errors.put(buildMatcher(".*NoClassDefFoundError.*"),    buildError(501, "ERR-3-4", TECHNICAL_TYPE,TECHNICAL_ERROR,technicalConsumer));
        errors.put(buildMatcher(".*MethodNotFoundException.*"), buildError(501, "ERR-3-5", TECHNICAL_TYPE,TECHNICAL_ERROR,technicalConsumer));
        
        errors.put(buildMatcher(".*IllegalStateException.*"),   buildError(501, "ERR-3-6", TECHNICAL_TYPE,TECHNICAL_ERROR,technicalConsumer));
        
        // DATA
        errors.put(buildMatcher(".*DaoValidatorException.*")        ,buildError(422, "ERR-4-3", TECHNICAL_TYPE,"Entity contraint exception",technicalConsumer));
        errors.put(buildMatcher(".*DaoEntityNotFoundException.*")   ,buildError(404, "ERR-4-2", FONCTIONAL_TYPE,"Entity not found",technicalConsumer));
        errors.put(buildMatcher(".*DaoEntityExistsException.*")     ,buildError(409, "ERR-4-1", FONCTIONAL_TYPE,"Entity already exists",technicalConsumer));
        errors.put(buildMatcher(".*Dao.*")                          ,buildError(501, "ERR-4-0", TECHNICAL_TYPE,"Storage data error",technicalConsumer));
        
        // PROCESSING
        errors.put(buildMatcher(".*ProcessingRunningException.*")   ,buildError(403, "ERR-5-0", TECHNICAL_TYPE,"Event already processing",technicalConsumer));
        
        //@formatter:on        
        return errors;
    }
    
    private static Matcher buildMatcher(final String regex) {
        return Pattern.compile(regex).matcher("");
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    
    @Override
    public Response toResponse(final Exception error) {
        ErrorCode errorType = DEFAULT_ERROR;
        for (final Matcher matcher : KEYS_SET) {
            if (matcher.reset(error.getClass().getName()).matches()) {
                errorType = ERRORS_TYPES.get(matcher);
                break;
            }
        }
        
        if (errorType.getErrorHandler() != null) {
            errorType.getErrorHandler().accept(errorType.getMessage(), error);
        }
        Loggers.DEBUG.error(error.getMessage(), error);
        //@formatter:off
        final Response response = Response.status(errorType.getStatusCode())
                                    .entity(errorType)
                                    .build();
        //@formatter:on
        
        response.getHeaders().put("Content-Type", Arrays.asList(MediaType.APPLICATION_JSON));
        
        return response;
    }
    
    private static ErrorCode buildError(final int statusCode, final String errorCode, final String errorType,
                                 final String message, final BiConsumer<String, Exception> errorHandler) {
        
        return newBuilder().setStatusCode(statusCode).setErrorCode(errorCode).setErrorType(errorType).setMessage(message).setErrorHandler(errorHandler).build();
    }
    
}
