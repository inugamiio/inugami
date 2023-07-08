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
package io.inugami.api.monitoring.exceptions;

import io.inugami.api.exceptions.ErrorCode;
import lombok.*;

import java.io.Serializable;

/**
 * ErrorResult
 *
 * @author patrick_guillerm
 * @since 28 déc. 2018
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@Getter
public class ErrorResult implements Serializable {

    private static final long serialVersionUID = 6750655178043958203L;

    /* package */ static final String DEFAULT_ERROR_TYPE = "technical";

    private final           int       httpCode;
    @EqualsAndHashCode.Include
    private final           String    errorCode;
    private final           String    errorType;
    private final           String    message;
    private final           String    stack;
    private final           String    cause;
    private final           String    fallBack;
    private final           boolean   exploitationError;
    private final transient ErrorCode currentErrorCode;
    private final transient Throwable exception;
}
