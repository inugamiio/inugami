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
package io.inugami.framework.api.marshalling;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import org.junit.jupiter.api.Test;

import static io.inugami.framework.api.tools.unit.test.UnitTestHelper.assertText;

class ThrowableSerializerTest {
    @Test
    void throwable_serialize() throws JsonProcessingException {

        assertText(new RuntimeException("sorry"),
                   """
                           {
                             "message" : "sorry"
                           }
                           """);
        assertText(new UncheckedException(DefaultErrorCode.buildUndefineError()),
                   """
                           {
                             "message" : "",
                             "errorCode" : {
                               "statusCode" : 500,
                               "errorCode" : "err-undefine",
                               "errorType" : "technical",
                               "exploitationError" : false,
                               "rollbackRequire" : false,
                               "retryable" : false
                             }
                           }
                           """);

        assertText(new UncheckedException("sorry", new UncheckedException("root cause")),
                   """
                           {
                             "message" : "sorry",
                             "errorCode" : {
                               "statusCode" : 500,
                               "errorCode" : "err-undefine",
                               "errorType" : "technical",
                               "exploitationError" : false,
                               "rollbackRequire" : false,
                               "retryable" : false
                             },
                             "cause" : {
                               "message" : "root cause",
                               "errorCode" : {
                                 "statusCode" : 500,
                                 "errorCode" : "err-undefine",
                                 "errorType" : "technical",
                                 "exploitationError" : false,
                                 "rollbackRequire" : false,
                                 "retryable" : false
                               }
                             }
                           }
                           """);

        assertText(new RuntimeException("sorry", new UncheckedException(DefaultErrorCode.buildUndefineError())),
                   """
                           {
                             "message" : "sorry",
                             "cause" : {
                               "message" : null,
                               "errorCode" : {
                                 "statusCode" : 500,
                                 "errorCode" : "err-undefine",
                                 "errorType" : "technical",
                                 "exploitationError" : false,
                                 "rollbackRequire" : false,
                                 "retryable" : false
                               }
                             }
                           }
                           """);
    }


}