package io.inugami.monitoring.springboot.exception;


import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCodeResolver;
import io.inugami.framework.interfaces.exceptions.ExceptionWithErrorCode;
import io.inugami.framework.interfaces.spi.SpiPriority;

import java.util.Map;
import java.util.regex.Pattern;

@SpiPriority(SpiPriority.LOWER_PRIORITY)
public class SpringDefaultErrorCodeResolver implements ErrorCodeResolver {

    public static final  String               ERR_0000          = "ERR-0000";
    public static final  String               CATEGORY_OTHER    = "other";
    public static final  String               CONNECTION        = "connection";
    public static final  String               DATABASE          = "database";
    public static final  String               SECURITY          = "security";
    private static final Map<Pattern, String> CATEGORY_MATCHERS = Map.ofEntries(
            Map.entry(Pattern.compile(".*hibernate.*", Pattern.CASE_INSENSITIVE), DATABASE),
            Map.entry(Pattern.compile(".*jpa.*", Pattern.CASE_INSENSITIVE), DATABASE),
            Map.entry(Pattern.compile(".*feign.*", Pattern.CASE_INSENSITIVE), "webservice_rest"),
            Map.entry(Pattern.compile(".*security.*", Pattern.CASE_INSENSITIVE), SECURITY),
            Map.entry(Pattern.compile(".*cors.*", Pattern.CASE_INSENSITIVE), SECURITY),
            Map.entry(Pattern.compile(".*certificate.*", Pattern.CASE_INSENSITIVE), "connection_security"),
            Map.entry(Pattern.compile(".*socket.*", Pattern.CASE_INSENSITIVE), CONNECTION),
            Map.entry(Pattern.compile(".*connect.*", Pattern.CASE_INSENSITIVE), CONNECTION),
            Map.entry(Pattern.compile(".*timeout.*", Pattern.CASE_INSENSITIVE), CONNECTION)
    );
    public static final  String               UNDEFINED_ERROR   = "undefined error";

    @Override
    public ErrorCode resolve(final Throwable exception) {
        ErrorCode result = null;
        if (exception instanceof ExceptionWithErrorCode) {
            result = ((ExceptionWithErrorCode) exception).getErrorCode();
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
