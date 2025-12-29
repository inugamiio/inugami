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
package io.inugami.monitoring.springboot.exception;

import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SpringDefaultErrorCodeResolverTest {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final String ERROR_MESSAGE = "sorry";

    @InjectMocks
    private SpringDefaultErrorCodeResolver resolver;


    // =================================================================================================================
    // RESOLVE
    // =================================================================================================================
    @Test
    void resolve_nominal() {
        assertThat(resolver.resolve(null)).isNull();
        assertText(resolver.resolve(new UncheckedException(DefaultErrorCode.buildUndefineError())),
                   """
                           {
                             "statusCode" : 500,
                             "errorCode" : "err-undefine",
                             "errorType" : "technical",
                             "exploitationError" : false,
                             "rollbackRequire" : false,
                             "retryable" : false
                           }
                           """);
        assertText(resolver.resolve(new IOException(ERROR_MESSAGE)),
                   """
                           {
                             "statusCode" : 0,
                             "category" : "other",
                             "errorCode" : "ERR-0000",
                             "errorType" : "technical",
                             "message" : "sorry",
                             "exploitationError" : false,
                             "rollbackRequire" : false,
                             "retryable" : false
                           }
                           """);

        assertText(resolver.resolve(new ErrJpaException(ERROR_MESSAGE)),
                   """
                           {
                             "statusCode" : 0,
                             "category" : "database",
                             "errorCode" : "ERR-0000",
                             "errorType" : "technical",
                             "message" : "sorry",
                             "exploitationError" : false,
                             "rollbackRequire" : false,
                             "retryable" : false
                           }
                           """);

        assertText(resolver.resolve(new ErrFeignException(ERROR_MESSAGE)),
                   """
                           {
                             "statusCode" : 0,
                             "category" : "webservice_rest",
                             "errorCode" : "ERR-0000",
                             "errorType" : "technical",
                             "message" : "sorry",
                             "exploitationError" : false,
                             "rollbackRequire" : false,
                             "retryable" : false
                           }
                           """);
        assertText(resolver.resolve(new ErrSecurityException(ERROR_MESSAGE)),
                   """
                           {
                             "statusCode" : 0,
                             "category" : "security",
                             "errorCode" : "ERR-0000",
                             "errorType" : "technical",
                             "message" : "sorry",
                             "exploitationError" : false,
                             "rollbackRequire" : false,
                             "retryable" : false
                           }
                           """);
        assertText(resolver.resolve(new ErrTimeoutException(ERROR_MESSAGE)),
                   """
                           {
                             "statusCode" : 0,
                             "category" : "connection",
                             "errorCode" : "ERR-0000",
                             "errorType" : "technical",
                             "message" : "sorry",
                             "exploitationError" : false,
                             "rollbackRequire" : false,
                             "retryable" : false
                           }
                           """);
    }

    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    private static class ErrJpaException extends Exception {
        public ErrJpaException(final String message) {
            super(message);
        }
    }

    private static class ErrFeignException extends Exception {
        public ErrFeignException(final String message) {
            super(message);
        }
    }

    private static class ErrSecurityException extends Exception {
        public ErrSecurityException(final String message) {
            super(message);
        }
    }

    private static class ErrTimeoutException extends Exception {
        public ErrTimeoutException(final String message) {
            super(message);
        }
    }
}