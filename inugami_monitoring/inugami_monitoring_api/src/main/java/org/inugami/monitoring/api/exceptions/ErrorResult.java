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
package org.inugami.monitoring.api.exceptions;

import java.io.Serializable;

/**
 * ErrorResult
 * 
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
public class ErrorResult implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long         serialVersionUID   = 6750655178043958203L;
    
    /* package */ static final String DEFAULT_ERROR_TYPE = "technical";
    
    private final int                 httpCode;
    
    private final String              errorCode;
    
    private final String              errorType;
    
    private final String              message;
    
    private final String              stack;
    
    private final String              cause;
    
    private final String              fallBack;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ErrorResult(int httpCode, String errorCode, String errorType, String message, String stack, String fallBack,
                       String cause) {
        super();
        this.httpCode = httpCode;
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.message = message;
        this.stack = stack;
        this.fallBack = fallBack;
        this.cause = cause;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ErrorResult [httpCode=");
        builder.append(httpCode);
        builder.append(", errorCode=");
        builder.append(errorCode);
        builder.append(", message=");
        builder.append(message);
        builder.append(", stack=");
        builder.append(stack);
        builder.append(", fallBack=");
        builder.append(fallBack);
        builder.append("]");
        return builder.toString();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;
        
        if (!result && obj != null && obj instanceof ErrorResult) {
            final ErrorResult other = (ErrorResult) obj;
            result = errorCode == null ? other.getErrorCode() == null : errorCode.equals(other.getErrorCode());
        }
        
        return result;
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
    
    public String getStack() {
        return stack;
    }
    
    public String getFallBack() {
        return fallBack;
    }
    
    public String getErrorType() {
        return errorType == null ? DEFAULT_ERROR_TYPE : errorType;
    }
    
    public String getCause() {
        return cause;
    }
    
}
