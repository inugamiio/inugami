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
package org.inugami.api.exceptions;

import java.io.Serializable;
import java.util.function.BiConsumer;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * ErrorType
 * 
 * @author patrickguillerm
 * @since 12 déc. 2017
 */
public class ErrorType implements Serializable {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long                             serialVersionUID = -837079078323167586L;
    
    private final int                                     httpCode;
    
    private final String                                  errorCode;
    
    private final String                                  message;
    
    private final String                                  errorType;
    
    @JsonIgnore
    private final transient BiConsumer<String, Exception> errorHandler;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ErrorType(final int httpCode, final String errorCode, final String message) {
        this(httpCode, errorCode, message, (msg, error) -> {
        }, null);
    }
    
    public ErrorType(final int httpCode, final String errorCode, final String message, final String errorType) {
        this(httpCode, errorCode, message, (msg, error) -> {
        }, null);
    }
    
    public ErrorType(final int httpCode, final String errorCode, final String message,
                     final BiConsumer<String, Exception> errorHandler) {
        this(httpCode, errorCode, message, errorHandler, null);
    }
    
    public ErrorType(final int httpCode, final String errorCode, final String message,
                     final BiConsumer<String, Exception> errorHandler, final String errorType) {
        super();
        this.httpCode = httpCode;
        this.errorCode = errorCode;
        this.message = message;
        this.errorHandler = errorHandler;
        this.errorType = "technical";
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ErrorType [httpCode=");
        builder.append(httpCode);
        builder.append(", errorCode=");
        builder.append(errorCode);
        builder.append(", message=");
        builder.append(message);
        builder.append(", errorType=");
        builder.append(errorType);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public int getHttpCode() {
        return httpCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getMessage() {
        return message;
    }
    
    public BiConsumer<String, Exception> getErrorHandler() {
        return errorHandler;
    }
    
    public String getErrorType() {
        return errorType;
    }
    
}
