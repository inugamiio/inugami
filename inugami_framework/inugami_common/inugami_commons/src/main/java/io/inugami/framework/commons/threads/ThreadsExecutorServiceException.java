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
package io.inugami.framework.commons.threads;

import io.inugami.framework.interfaces.exceptions.TechnicalException;

import java.util.List;
import java.util.Optional;

/**
 * ThreadsExecutorServiceException
 *
 * @author patrick_guillerm
 * @since 29 sept. 2017
 */
@SuppressWarnings({"java:S1135"})
public class ThreadsExecutorServiceException extends TechnicalException {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final long            serialVersionUID = 188777289226292350L;
    private final        List<Exception> errors;

    // =================================================================================================================
    // CONSTRUCTORS
    // =================================================================================================================
    public ThreadsExecutorServiceException() {
        super();
        errors = null;
    }

    public ThreadsExecutorServiceException(final String message, final Object... values) {
        super(message, values);
        errors = null;
    }

    public ThreadsExecutorServiceException(final String message, final Throwable cause) {
        super(message, cause);
        errors = null;
    }

    public ThreadsExecutorServiceException(final String message) {
        super(message);
        errors = null;
    }

    public ThreadsExecutorServiceException(final Throwable cause, final String message, final Object... values) {
        super(cause, message, values);
        errors = null;
    }

    public ThreadsExecutorServiceException(final Throwable cause) {
        super(cause);
        errors = null;
    }

    public ThreadsExecutorServiceException(final List<Exception> errors) {
        super("multi eror");
        // TODO: build correct message....
        this.errors = errors;
    }

    public Optional<List<Exception>> getErrors() {
        return Optional.ofNullable(errors);
    }

}
