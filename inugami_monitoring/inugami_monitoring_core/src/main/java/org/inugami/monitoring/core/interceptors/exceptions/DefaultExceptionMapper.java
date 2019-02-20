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
package org.inugami.monitoring.core.interceptors.exceptions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import org.inugami.api.exceptions.ErrorType;
import org.inugami.api.loggers.Loggers;
import org.inugami.monitoring.api.exceptions.ExceptionHandlerMapper;

public class DefaultExceptionMapper implements ExceptionHandlerMapper {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String TECHNICAL_ERROR = "Technical error";
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public Map<Pattern, ErrorType> produceMapping() {
        final Map<Pattern, ErrorType> errors = new LinkedHashMap<>();
        
      //@formatter:off
        final BiConsumer<String, Exception> technicalConsumer = (msg,error) -> Loggers.DEBUG.error(msg,error);
        final BiConsumer<String, Exception> xllogConsumer     = (msg,error) -> Loggers.XLLOG.error("{} : {}",msg,error.getMessage());
        //@formatter:on
        
        //@formatter:off
        // security
        
        errors.put(buildPattern(".*AuthenticationException.*"), new ErrorType(401, "ERR-1-0", "Authentication failed", (msg,error)->Loggers.SECURITY.error(msg)));
        errors.put(buildPattern(".*AccessDenied.*"),            new ErrorType(403, "ERR-1-1", "access denied", (msg,error)->Loggers.SECURITY.warn(msg)));
        errors.put(buildPattern(".*UnauthorizedException.*"),   new ErrorType(403, "ERR-1-2", "Authentication failed", (msg,error)->Loggers.SECURITY.error(msg)));
        errors.put(buildPattern(".*MaxUserSocketException.*"),  new ErrorType(403, "ERR-1-3", "You have too many sockets opens", (msg,error)->Loggers.SECURITY.error(msg)));
        
        
        // ENV
        errors.put(buildPattern(".*SocketTimeoutException.*"),  new ErrorType(504, "ERR-2-0", "timeout", xllogConsumer));
        errors.put(buildPattern(".*TimeoutException.*"),        new ErrorType(504, "ERR-2-1", "timeout", xllogConsumer));
        
        // DEV
        errors.put(buildPattern(".*NullPointer.*"),             new ErrorType(501, "ERR-3-0", TECHNICAL_ERROR,technicalConsumer));
        errors.put(buildPattern(".*NumberFormatException.*"),   new ErrorType(501, "ERR-3-1", TECHNICAL_ERROR,technicalConsumer));
        errors.put(buildPattern(".*ClassCastException.*"),      new ErrorType(501, "ERR-3-2", TECHNICAL_ERROR,technicalConsumer));
        errors.put(buildPattern(".*NoClassDefFoundError.*"),    new ErrorType(501, "ERR-3-3", TECHNICAL_ERROR,technicalConsumer));
        errors.put(buildPattern(".*NoClassDefFoundError.*"),    new ErrorType(501, "ERR-3-4", TECHNICAL_ERROR,technicalConsumer));
        errors.put(buildPattern(".*MethodNotFoundException.*"), new ErrorType(501, "ERR-3-5", TECHNICAL_ERROR,technicalConsumer));
        
        errors.put(buildPattern(".*IllegalStateException.*"),   new ErrorType(501, "ERR-3-6", TECHNICAL_ERROR,technicalConsumer));
        
        // DATA
        errors.put(buildPattern(".*DaoValidatorException.*")        ,new ErrorType(422, "ERR-4-3", "Entity contraint exception",technicalConsumer));
        errors.put(buildPattern(".*DaoEntityNotFoundException.*")   ,new ErrorType(404, "ERR-4-2", "Entity not found",technicalConsumer, "functional"));
        errors.put(buildPattern(".*DaoEntityExistsException.*")     ,new ErrorType(409, "ERR-4-1", "Entity already exists",technicalConsumer));
        errors.put(buildPattern(".*Dao.*")                          ,new ErrorType(501, "ERR-4-0", "Storage data error",technicalConsumer));
        
        // PROCESSING
        errors.put(buildPattern(".*ProcessingRunningException.*")   ,new ErrorType(403, "ERR-5-0", "Event already processing",technicalConsumer));
        
        return errors;
    }
}
