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
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCodeResolver;
import io.inugami.framework.interfaces.exceptions.ExceptionWithErrorCode;
import io.inugami.framework.interfaces.spi.SpiPriority;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

@SpiPriority(SpiPriority.LOWER_PRIORITY)
public class SpringDefaultErrorCodeResolver implements ErrorCodeResolver {
    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final  String               ERR_0000          = "ERR-0000";
    public static final  String               CATEGORY_OTHER    = "other";
    public static final  String               CONNECTION        = "connection";
    public static final  String               DATABASE          = "database";
    public static final  String               SECURITY          = "security";
    public static final  String               UNDEFINED_ERROR   = "undefined error";
    private static final Map<Pattern, String> CATEGORY_MATCHERS = initCategoryMatchers();

    private static Map<Pattern, String> initCategoryMatchers() {
        Map<Pattern, String> result = new LinkedHashMap<>();
        result.put(Pattern.compile(".*hibernate.*", Pattern.CASE_INSENSITIVE), DATABASE);
        result.put(Pattern.compile(".*jpa.*", Pattern.CASE_INSENSITIVE), DATABASE);
        result.put(Pattern.compile(".*feign.*", Pattern.CASE_INSENSITIVE), "webservice_rest");
        result.put(Pattern.compile(".*security.*", Pattern.CASE_INSENSITIVE), SECURITY);
        result.put(Pattern.compile(".*cors.*", Pattern.CASE_INSENSITIVE), SECURITY);
        result.put(Pattern.compile(".*certificate.*", Pattern.CASE_INSENSITIVE), "connection_security");
        result.put(Pattern.compile(".*socket.*", Pattern.CASE_INSENSITIVE), CONNECTION);
        result.put(Pattern.compile(".*connect.*", Pattern.CASE_INSENSITIVE), CONNECTION);
        result.put(Pattern.compile(".*timeout.*", Pattern.CASE_INSENSITIVE), CONNECTION);
        return result;
    }

    // =================================================================================================================
    // RESOLVE
    // =================================================================================================================
    @Override
    public ErrorCode resolve(final Throwable exception) {
        ErrorCode result = null;
        if (exception == null) {
            return result;
        }
        
        if (exception instanceof ExceptionWithErrorCode exceptionWithErrorCode) {
            result = exceptionWithErrorCode.getErrorCode();
        } else {
            final String errorMessage = exception.getMessage() == null ? UNDEFINED_ERROR : exception.getMessage();
            result = DefaultErrorCode.builder()
                                     .errorCode(ERR_0000)
                                     .message(errorMessage)
                                     .category(resolveCategory(exception.getClass().getName(), errorMessage))
                                     .errorTypeTechnical()
                                     .build();
        }
        return result;
    }

    private String resolveCategory(final String exceptionName, final String errorMessage) {
        for (Map.Entry<Pattern, String> entry : CATEGORY_MATCHERS.entrySet()) {
            if (entry.getKey().matcher(exceptionName).matches() || entry.getKey().matcher(errorMessage).matches()) {
                return entry.getValue();
            }
        }
        return CATEGORY_OTHER;
    }
}
