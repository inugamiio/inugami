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

/**
 * ErrorResult
 * 
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
public class ErrorResultBuilder {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private int    httpCode  = 500;
    
    private String errorCode;
    
    private String errorType = ErrorResult.DEFAULT_ERROR_TYPE;
    
    private String message;
    
    private String stack;
    
    private String fallBack;
    
    private String cause;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ErrorResult build() {
        return new ErrorResult(httpCode, errorCode, errorType, message, stack, fallBack, cause);
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ErrorResultBuilder [httpCode=");
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
    
    // =========================================================================
    // GETTER & SETTERS
    // =========================================================================
    public int getHttpCode() {
        return httpCode;
    }
    
    public ErrorResultBuilder setHttpCode(int httpCode) {
        this.httpCode = httpCode;
        return this;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public ErrorResultBuilder setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }
    
    public String getMessage() {
        return message;
    }
    
    public ErrorResultBuilder setMessage(String message) {
        this.message = message;
        return this;
    }
    
    public String getStack() {
        return stack;
    }
    
    public ErrorResultBuilder setStack(String stack) {
        this.stack = stack;
        return this;
    }
    
    public String getFallBack() {
        return fallBack;
    }
    
    public ErrorResultBuilder setFallBack(String fallBack) {
        this.fallBack = fallBack;
        return this;
    }
    
    public String getErrorType() {
        return errorType;
    }
    
    public ErrorResultBuilder setErrorType(String errorType) {
        this.errorType = errorType;
        return this;
    }
    
    public String getCause() {
        return cause;
    }
    
    public ErrorResultBuilder setCause(String cause) {
        this.cause = cause;
        return this;
    }
    
}
