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

import feign.FeignException;
import io.inugami.framework.api.listeners.DefaultApplicationLifecycleSPI;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.inugami.commons.test.UnitTestHelper.assertText;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FeignErrorCodeResolverTest {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    @BeforeEach
    void init() {
        DefaultApplicationLifecycleSPI.unregister(FeignErrorCodeResolver.class);
    }

    @AfterEach
    void clean() {
        DefaultApplicationLifecycleSPI.unregister(FeignErrorCodeResolver.class);
    }

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    @Test
    void resolve_nominal() {
        final var resolver = errorCodeResolver();
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
        assertText(resolver.resolve(new MyFeignException(404, "sorry")),
                   """
                           {
                              "statusCode" : 404,
                              "errorCode" : "undefined",
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
    FeignErrorCodeResolver errorCodeResolver() {
        FeignErrorCodeResolver result = new FeignErrorCodeResolver();
        result.onContextRefreshed(null);
        return result;
    }

    private static class MyFeignException extends FeignException {
        protected MyFeignException(final int status, final String message) {
            super(status, message);
        }
    }
}