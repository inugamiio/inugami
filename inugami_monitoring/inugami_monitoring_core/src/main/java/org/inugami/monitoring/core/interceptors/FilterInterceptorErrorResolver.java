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
package org.inugami.monitoring.core.interceptors;
import static org.inugami.api.exceptions.ErrorCodeBuilder.newBuilder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.type.ErrorType;

import org.inugami.api.exceptions.ErrorCode;
import org.inugami.api.loggers.Loggers;
import org.inugami.api.monitoring.exceptions.ErrorResult;
import org.inugami.api.monitoring.exceptions.ErrorResultBuilder;
import org.inugami.api.spi.SpiLoader;
import org.inugami.monitoring.api.exceptions.ExceptionHandlerMapper;
import org.inugami.monitoring.api.exceptions.ExceptionResolver;

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
    
    private static final Set<Pattern>            KEYS_SET     = ERRORS_TYPES.keySet();
    
    //@formatter:off
    private static final ErrorCode               DEFAULT_ERROR   = newBuilder().setStatusCode(500)
                                                                            .setErrorCode("ERR-0-000")
                                                                            .setMessage("unknow error")
                                                                            .setErrorHandler((msg, error)-> Loggers.DEBUG.error(error.getMessage(),error))
                                                                            .build();
            

    //@formatter:on
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private static Map<Pattern, ErrorCode> initErrorsType() {
        final Map<Pattern, ErrorCode> errors = new LinkedHashMap<>();
        final List<ExceptionHandlerMapper> mappers = new SpiLoader().loadSpiServicesByPriority(ExceptionHandlerMapper.class);
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
        
        final ErrorResultBuilder errorBuilder = new ErrorResultBuilder();
        errorBuilder.setErrorType(errorBuilder.getErrorType());
        errorBuilder.setErrorCode(errorType.getErrorCode());
        errorBuilder.setCause(buildCause(error));
        errorBuilder.setHttpCode(errorType.getStatusCode());
        
        return errorBuilder.build();
    }
    
    private String buildCause(final Exception error) {
        return error.getCause() == null ? null : error.getCause().getMessage();
    }
    
}
